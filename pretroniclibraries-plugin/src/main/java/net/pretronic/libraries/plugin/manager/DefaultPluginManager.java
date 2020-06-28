/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package net.pretronic.libraries.plugin.manager;

import com.sun.org.apache.bcel.internal.generic.BALOAD;
import net.pretronic.libraries.document.type.DocumentFileType;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.PretronicLoggerFactory;
import net.pretronic.libraries.plugin.Plugin;
import net.pretronic.libraries.plugin.RuntimeEnvironment;
import net.pretronic.libraries.plugin.description.DefaultPluginDescription;
import net.pretronic.libraries.plugin.description.PluginDescription;
import net.pretronic.libraries.plugin.description.dependency.Dependency;
import net.pretronic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.pretronic.libraries.plugin.exception.PluginLoadException;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import net.pretronic.libraries.plugin.loader.DefaultPluginLoader;
import net.pretronic.libraries.plugin.loader.PluginLoader;
import net.pretronic.libraries.plugin.loader.classloader.URLPluginClassLoader;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.Validate;
import net.pretronic.libraries.utility.annonations.Internal;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import net.pretronic.libraries.utility.interfaces.OwnerUnregisterAble;
import net.pretronic.libraries.utility.io.archive.ZipArchive;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class DefaultPluginManager implements PluginManager {

    private final PretronicLogger logger;
    private final RuntimeEnvironment<?> environment;
    private final String descriptionName;

    private final Map<String, BiConsumer<Plugin<?>,LifecycleState>> stateListeners;

    private final Collection<PluginLoader> loaders;
    private final Collection<Plugin<?>> plugins;
    private final Collection<ServiceEntry> services;

    public DefaultPluginManager(RuntimeEnvironment<?> environment) {
        this(PretronicLoggerFactory.getLogger(PluginManager.class),environment);
    }

    public DefaultPluginManager(PretronicLogger logger, RuntimeEnvironment<?> environment){
        this(logger,environment,"manifest.json");
    }

    public DefaultPluginManager(PretronicLogger logger, RuntimeEnvironment<?> environment, String descriptionName) {
        this.logger = logger;
        this.environment = environment;
        this.descriptionName = descriptionName;

        this.stateListeners = new LinkedHashMap<>();
        this.loaders = new ArrayList<>();
        this.plugins = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    @Override
    public PretronicLogger getLogger() {
        return logger;
    }

    @Override
    public Collection<Plugin<?>> getPlugins() {
        return plugins;
    }

    @Override
    public Plugin<?> getPlugin(String name) {
        Validate.notNull(name);
        return Iterators.findOne(this.plugins, plugin -> plugin.getName().equalsIgnoreCase(name));
    }

    @Override
    public Plugin<?> getPlugin(UUID id) {
        Validate.notNull(id);
        return Iterators.findOne(this.plugins, plugin -> plugin.getDescription().getId().equals(id));
    }

    @Override
    public boolean isPluginEnabled(String name) {
        Validate.notNull(name);
        Plugin<?> plugin = getPlugin(name);
        return plugin != null && plugin.getLoader().isEnabled();
    }


    @Override
    public Collection<Class<?>> getAvailableServices() {
        Collection<Class<?>> classes = new HashSet<>();
        services.forEach(serviceEntry -> classes.add(serviceEntry.serviceClass));
        return classes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getServices(Class<T> serviceClass) {
        Validate.notNull(serviceClass);
        List<T> services =  Iterators.map(this.services, entry -> (T) entry.service, entry -> entry.serviceClass.equals(serviceClass));
        if(services.isEmpty()) throw new UnsupportedOperationException("Service is not available.");
        return services;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(Class<T> serviceClass) {
        Validate.notNull(serviceClass);
        T result = getServiceOrDefault(serviceClass,null);
        if(result == null) throw new UnsupportedOperationException("Service is not available.");
        return result;
    }

    @Override
    public <T> T getServiceOrDefault(Class<T> serviceClass) {
        Validate.notNull(serviceClass);
        return getServiceOrDefault(serviceClass,null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getServiceOrDefault(Class<T> serviceClass, Supplier<T> consumer) {
        Validate.notNull(serviceClass);
        List<ServiceEntry> services = Iterators.filter(this.services, entry -> entry.serviceClass.equals(serviceClass));
        services.sort((o1, o2) -> {
            if(o1.priority < o2.priority) return -1;
            else if(o1.priority > o2.priority) return 1;
            return 0;
        });
        if(services.size() > 0) return  (T) services.get(0).service;
        else if(consumer != null) return consumer.get();
        return null;
    }

    @Override
    public <T> void registerService(ObjectOwner owner, Class<T> serviceClass, T service, byte priority) {
        Validate.notNull(owner,serviceClass,service,priority);
        this.services.add(new ServiceEntry(owner,serviceClass,service,priority));
    }

    @Override
    public <T> boolean isServiceAvailable(Class<T> serviceClass) {
        Validate.notNull(serviceClass);
        for (ServiceEntry entry : this.services) if(entry.serviceClass.equals(serviceClass)) return true;
        return false;
    }

    @Override
    public void unregisterService(Object service) {
        Validate.notNull(service);
        Iterators.removeOne(this.services, entry -> entry.service.equals(service));
    }

    @Override
    public void unregisterServices(Class<?> serviceClass) {
        Validate.notNull(serviceClass);
        Iterators.removeOne(this.services, entry -> entry.serviceClass.equals(serviceClass));
    }

    @Override
    public void unregisterServices(ObjectOwner owner) {
        Validate.notNull(owner);
        Iterators.removeOne(this.services, entry -> entry.owner.equals(owner));
        for (ServiceEntry service : this.services) {
            if(service instanceof OwnerUnregisterAble){
                ((OwnerUnregisterAble) service).unregister(owner);
            }
        }
    }

    @Override
    public Collection<PluginLoader> getLoaders() {
        return loaders;
    }

    @Override
    public PluginLoader createPluginLoader(String name) {
        throw new UnsupportedOperationException("@Todo implement searching");
    }

    @Override
    public PluginLoader createPluginLoader(File location) {
        return createPluginLoader(location,loadPluginDescription(location));
    }

    @Override
    public PluginLoader createPluginLoader(File location,PluginDescription description) {
        PluginLoader loader = Iterators.findOne(this.loaders, loader1 -> loader1.getLocation().equals(location));
        if(loader != null) return loader;
        loader = new DefaultPluginLoader(this,environment,null//@Todo add prefixed logger
                ,URLPluginClassLoader.of(this,location),location,description,true);
        if(logger.isDebugging()) logger.debug("Created plugin loader for {} v{}",loader.getDescription().getName(),loader.getDescription().getVersion().getName());
        this.loaders.add(loader);
        return loader;
    }

    @Override
    public PluginDescription detectPluginDescription(File location) {
        return loadPluginDescription(location);
    }

    @Override
    public Collection<PluginDescription> detectPluginDescriptions(File directory) {
        Collection<PluginDescription> descriptions = new ArrayList<>();
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isFile() && file.getName().endsWith(".jar")){
                        try {
                            descriptions.add(loadPluginDescription(file));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
        return descriptions;
    }

    @Override
    public void setLifecycleStateListener(String state, BiConsumer<Plugin<?>,LifecycleState> listener) {
        this.stateListeners.put(state,listener);
    }

    @Internal
    @Override
    public void executeLifecycleStateListener(String state, LifecycleState stateEvent, Plugin plugin) {
        if(state.equals(LifecycleState.CONSTRUCTION)) this.plugins.add(plugin);
        else if(state.equals(LifecycleState.UNLOAD)) this.plugins.remove(plugin);

        BiConsumer<Plugin<?>,LifecycleState> listener = this.stateListeners.get(state);
        if(listener != null) listener.accept(plugin,stateEvent);
    }

    @Override
    public Collection<Plugin<?>> enablePlugins(File directory) {
        List<PluginLoader> loaders = findLoaders(directory);
        List<Plugin<?>> plugins = new ArrayList<>();

        loaders.sort((o1, o2) -> {
            for (Dependency dependency : o1.getDescription().getDependencies()) {
                if(dependency.isDepended(o2.getDescription())) return 1;
            }
            return 0;
        });

        loaders.forEach(loader -> plugins.add(loader.construct()));
        loaders.forEach(PluginLoader::initialize);
        loaders.forEach(PluginLoader::load);
        loaders.forEach(PluginLoader::bootstrap);

        loaders.clear();

        return plugins;
    }

    @Override
    public void disablePlugins() {
        loaders.forEach(loader -> {
            if(loader.isInstanceAvailable()) loader.shutdown();
        });
        loaders.forEach(loader -> {
            if(loader.isInstanceAvailable()) loader.unload();
        });
    }

    @Override
    public void shutdown() {
        disablePlugins();
    }

    private List<PluginLoader> findLoaders(File directory){
        List<PluginLoader> loaders = new ArrayList<>();
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isFile() && file.getName().endsWith(".jar")){
                        try {
                            loaders.add(createPluginLoader(file));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        }
        return loaders;
    }

    private PluginDescription loadPluginDescription(File jarFile){
        ZipArchive archive = new ZipArchive(jarFile);

        try{
            InputStream input = archive.getStream(descriptionName);
            if(input == null) throw new InvalidPluginDescriptionException("No plugin description found for "+jarFile.getAbsolutePath());
            return DefaultPluginDescription.create(this,DocumentFileType.JSON.getReader().read(input));
        }catch (Exception exception){
            throw new PluginLoadException("Could not load plugin description for "+jarFile.getAbsolutePath(),exception);
        }
    }

    @Override
    public void provideLoader(PluginLoader loader) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    private static class ServiceEntry {

        private final ObjectOwner owner;
        private final Class<?> serviceClass;
        private final Object service;
        private final byte priority;

        private ServiceEntry(ObjectOwner owner, Class<?> serviceClass, Object service,byte priority) {
            this.owner = owner;
            this.serviceClass = serviceClass;
            this.service = service;
            this.priority = priority;
        }
    }
}
