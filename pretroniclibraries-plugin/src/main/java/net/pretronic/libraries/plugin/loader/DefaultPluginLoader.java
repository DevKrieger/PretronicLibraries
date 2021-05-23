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

package net.pretronic.libraries.plugin.loader;

import net.pretronic.libraries.document.type.DocumentFileType;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.plugin.Plugin;
import net.pretronic.libraries.plugin.RuntimeEnvironment;
import net.pretronic.libraries.plugin.description.DefaultPluginDescription;
import net.pretronic.libraries.plugin.description.PluginDescription;
import net.pretronic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.pretronic.libraries.plugin.exception.PluginLoadException;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import net.pretronic.libraries.plugin.loader.classloader.PluginClassLoader;
import net.pretronic.libraries.plugin.manager.PluginManager;
import net.pretronic.libraries.utility.interfaces.InjectorAdapter;
import net.pretronic.libraries.utility.interfaces.InjectorAdapterAble;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;

public class DefaultPluginLoader implements PluginLoader, InjectorAdapterAble {

    private final PluginManager pluginManager;
    private final RuntimeEnvironment<?> environment;
    private final PretronicLogger logger;
    private final PluginClassLoader classLoader;
    private final String descriptionName;

    private final File location;
    private final PluginDescription description;
    private final boolean lifecycleLogging;

    private final LifecycleState defaultState;
    private InjectorAdapter injector;

    private Plugin<?> instance;
    private boolean enabled;

    public DefaultPluginLoader(PluginManager pluginManager, RuntimeEnvironment<?> environment, PretronicLogger logger
            , PluginClassLoader classLoader, String descriptionName, File location, boolean lifecycleLogging) {
        this(pluginManager,environment,logger,classLoader,descriptionName,location,null,lifecycleLogging);
    }

    public DefaultPluginLoader(PluginManager pluginManager, RuntimeEnvironment<?> environment, PretronicLogger logger
            , PluginClassLoader classLoader,  File location, PluginDescription description,boolean lifecycleLogging) {
        this(pluginManager,environment,logger,classLoader,null,location,description,lifecycleLogging);
    }

    public DefaultPluginLoader(PluginManager pluginManager, RuntimeEnvironment<?> environment, PretronicLogger logger
            , PluginClassLoader classLoader, String descriptionName, File location, PluginDescription description,boolean lifecycleLogging) {
        this.pluginManager = pluginManager;
        this.environment = environment;
        this.logger = logger;
        this.classLoader = classLoader;
        this.descriptionName = descriptionName;
        this.location = location;
        this.lifecycleLogging = lifecycleLogging;

        this.description = description!=null?description:loadDescription();

        this.defaultState = new LifecycleState(this.description,this);
    }

    @Override
    public File getLocation() {
        return this.location;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public PluginDescription getDescription() {
        return this.description;
    }

    @Override
    public PluginClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public boolean isInstanceAvailable() {
        return instance != null;
    }

    @Override
    public boolean isMainClassAvailable() {
        String className = description.getMain().getMainClass(this.environment.getName());
        if(className == null) return false;
        Class<?> clazz = null;
        try {clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException ignored) {}
        return clazz != null && Plugin.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void executeLifeCycleState(String state) {
        executeLifeCycleState(state,this.defaultState);
    }

    @Override
    public void executeLifeCycleState(String state, LifecycleState stateEvent) {
        if(!isInstanceAvailable()) throw new PluginLoadException("No plugin instance available.");
        for(Method method : instance.getClass().getDeclaredMethods()){
            Lifecycle cycle = method.getAnnotation(Lifecycle.class);
            if(cycle!= null
                    && !Modifier.isStatic(method.getModifiers())
                    && method.getParameterCount() == 1
                    && cycle.state().equals(state)
                    && (cycle.environment().equals("ANY") || cycle.environment().equalsIgnoreCase(environment.getName()))
                    && stateEvent.getClass().isAssignableFrom(method.getParameterTypes()[0])){
                try{
                    method.invoke(instance,(method.getParameterTypes()[0]).cast(stateEvent));
                }catch (Exception exception) {
                    exception.printStackTrace();
                    pluginManager.getLogger().error("Could not execute lifecycle state {} for plugin {}",state,description.getName());
                }
            }
        }
        this.pluginManager.executeLifecycleStateListener(state,stateEvent,instance);
    }

    @Override
    public Plugin<?> getInstance() {
        if(!isInstanceAvailable()) throw new PluginLoadException("No plugin instance available.");
        return instance;
    }

    @Override
    public Plugin<?> enable() {
        construct();
        initialize();
        load();
        bootstrap();
        return getInstance();
    }

    @Override
    public void disable() {
        shutdown();
        unload();
    }

    @Override
    public Plugin<?> construct() {
        if(isInstanceAvailable()) throw new PluginLoadException("Plugin is already constructed.");
        try{
            Class<? extends Plugin> mainClass = loadMainClass();
            this.instance = mainClass.getDeclaredConstructor().newInstance();
            executeLifeCycleState(LifecycleState.CONSTRUCTION);
            if(lifecycleLogging && pluginManager.getLogger().isDebugging()){
                pluginManager.getLogger().debug("Created instance for {} v{}",description.getName(),description.getVersion().getName());
            }
            return this.instance;
        }catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException exception){
            throw new PluginLoadException("Could not create plugin instance for plugin "+description.getName()+" ("+exception.getMessage()+")",exception);
        }
    }

    @Override
    public void initialize() {
        if(injector != null) injector.inject(this.instance);
        Class<?> pluginClass = this.instance.getClass();
        while(!pluginClass.getSuperclass().equals(Plugin.class)) pluginClass = pluginClass.getSuperclass();
        Type type = pluginClass.getGenericSuperclass();
        if(!(type instanceof Class)) {
            ParameterizedType parameterized = (ParameterizedType)type;
            if(!(((Class<?>) parameterized.getActualTypeArguments()[0]).isAssignableFrom(this.environment.getInstance().getClass()))){
                throw new IllegalArgumentException("Invalid runtime type at plugin class ("+parameterized.getActualTypeArguments()[0]+" is not assignable by "+this.environment.getInstance().getClass()+").");
            }
        }
        this.instance.initialize(this.description,this,logger,this.environment.getInstance());
        executeLifeCycleState(LifecycleState.INITIALISATION);
    }

    @Override
    public void load() {
        executeLifeCycleState(LifecycleState.LOAD);
        if(lifecycleLogging) pluginManager.getLogger().info("Loaded plugin {} v{}",description.getName(),description.getVersion().getName());
    }

    @Override
    public void bootstrap() {
        enabled = true;
        executeLifeCycleState(LifecycleState.BOOTSTRAP);
        if(lifecycleLogging)  pluginManager.getLogger().info("Started plugin {} v{} by {}",description.getName(),description.getVersion().getName(),description.getAuthor());
    }

    @Override
    public void shutdown() {
        enabled = false;
        executeLifeCycleState(LifecycleState.SHUTDOWN);
        if(lifecycleLogging) pluginManager.getLogger().info("Stopped plugin {} v{} by {}",description.getName(),description.getVersion().getName(),description.getAuthor());
    }

    @Override
    public void unload() {
        executeLifeCycleState(LifecycleState.UNLOAD);
        instance = null;
        if(lifecycleLogging) pluginManager.getLogger().info("Unloaded plugin {} v{}",description.getName(),description.getVersion().getName());
    }

    private Class<? extends Plugin> loadMainClass() throws ClassNotFoundException{
        String className = description.getMain().getMainClass(this.environment.getName());
        if(className == null) throw new PluginLoadException("No main class for plugin "+description.getName()+" v"+description.getVersion().getName()+" found.");
        Class<?> clazz = classLoader.loadClass(className);
        if(clazz != null && Plugin.class.isAssignableFrom(clazz)) return (Class<Plugin>) clazz;
        throw new PluginLoadException("No main class for plugin "+description.getName()+" v"+description.getVersion().getName()+" found.");
    }

    public PluginDescription loadDescription(){//Paths  json / yml
        InputStream stream = classLoader.getResourceAsStream(descriptionName);
        try{
            if(stream == null) throw new InvalidPluginDescriptionException("No plugin description found");
            return DefaultPluginDescription.create(pluginManager,DocumentFileType.JSON.getReader().read(stream));
        }finally {
            try { stream.close(); } catch (IOException ignored) {}
        }
    }

    @Override
    public void setInjector(InjectorAdapter adapter) {
        this.injector = adapter;
    }
}
