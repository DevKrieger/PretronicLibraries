/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 17.08.19, 15:47
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.prematic.libraries.plugin.manager;

import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.logging.PrematicLoggerFactory;
import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.RuntimeEnvironment;
import net.prematic.libraries.plugin.description.DefaultPluginDescription;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.loader.DefaultPluginLoader;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.io.archive.ZipArchive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;

public class DefaultPluginManager implements PluginManager{

    private final PrematicLogger logger;
    private final RuntimeEnvironment environment;

    private final Map<String, BiConsumer<Plugin,LifecycleState>> stateListeners;

    private final Collection<PluginLoader> loaders;
    private final Collection<Plugin> plugins;
    private final Map<Class<?>,Object> services;

    public DefaultPluginManager(RuntimeEnvironment environment) {
        this(PrematicLoggerFactory.getLogger(PluginManager.class),environment);
    }

    public DefaultPluginManager(PrematicLogger logger, RuntimeEnvironment environment) {
        this.logger = logger;
        this.environment = environment;

        this.stateListeners = new LinkedHashMap<>();
        this.loaders = new ArrayList<>();
        this.plugins = new ArrayList<>();
        this.services = new LinkedHashMap<>();
    }

    @Override
    public PrematicLogger getLogger() {
        return logger;
    }

    @Override
    public Collection<Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public Plugin getPlugin(String name) {
        return Iterators.findOne(this.plugins, plugin -> plugin.getName().equalsIgnoreCase(name));
    }

    @Override
    public Plugin getPlugin(UUID id) {
        return Iterators.findOne(this.plugins, plugin -> plugin.getDescription().getId().equals(id));
    }

    @Override
    public boolean isPluginEnabled(String name) {
        Plugin plugin = getPlugin(name);
        return plugin != null && plugin.getLoader().isEnabled();
    }


    @Override
    public <T> T getService(Class<T> serviceClass) {
        return (T) this.services.get(serviceClass);
    }

    @Override
    public <T> void registerService(Class<T> serviceClass, T service) {
        this.services.put(serviceClass,service);
    }

    @Override
    public <T> boolean isServiceAvailable(Class<T> serviceClass) {
        return getService(serviceClass) != null;
    }


    @Override
    public Collection<PluginLoader> getLoaders() {
        return loaders;
    }

    @Override
    public PluginLoader createPluginLoader(File location) {
        return createPluginLoader(location,null);
    }

    @Override
    public PluginLoader createPluginLoader(PluginDescription description) {
        return createPluginLoader(description.getLocation(),description);
    }

    private PluginLoader createPluginLoader(File location, PluginDescription description) {
        PluginLoader loader = Iterators.findOne(this.loaders, loader1 -> loader1.getLocation().equals(location));
        if(loader != null) return loader;
        loader = new DefaultPluginLoader(this,environment,location,description);
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
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        }
        return descriptions;
    }

    @Override
    public void setLifecycleStateListener(String state, BiConsumer<Plugin,LifecycleState> listener) {
        this.stateListeners.put(state,listener);
    }

    @Override
    public void executeLifecycleStateListener(String state, LifecycleState stateEvent, Plugin plugin) {
        if(state.equals(LifecycleState.CONSTRUCTION)) this.plugins.add(plugin);
        else if(state.equals(LifecycleState.UNLOAD)) this.plugins.remove(plugin);

        BiConsumer<Plugin,LifecycleState> listener = this.stateListeners.get(state);
        if(listener != null) listener.accept(plugin,stateEvent);
    }

    @Override
    public void executeLifecycleStateListener(String state, LifecycleState stateEvent, Collection<Plugin> plugins) {
        BiConsumer<Plugin,LifecycleState> listener = this.stateListeners.get(state);
        plugins.forEach(plugin -> {
            if(state.equals(LifecycleState.CONSTRUCTION)) DefaultPluginManager.this.plugins.add(plugin);
            else if(state.equals(LifecycleState.UNLOAD)) DefaultPluginManager.this.plugins.remove(plugin);

            if(listener != null) listener.accept(plugin,stateEvent);
        });
    }

    @Override
    public Collection<Plugin> enablePlugins(File directory) {
        List<PluginLoader> loaders = findLoaders(directory);
        List<Plugin> plugins = new ArrayList<>();

        //@Todo sort by dependencies

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

        InputStream input = archive.getStream(PluginDescription.getBasePath()+".yml");
        try{
            if(input == null){
                input = archive.getStream(PluginDescription.getBasePath()+".yml");
                if(input == null) throw new InvalidPluginDescriptionException("No plugin description found.");
                else return new DefaultPluginDescription(jarFile, DocumentFileType.YAML.getReader().read(input));
            }else return new DefaultPluginDescription(jarFile,DocumentFileType.JSON.getReader().read(input));
        }finally {
            if(input != null) {
                try {input.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
