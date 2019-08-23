/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 17.08.19, 15:29
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

package net.prematic.libraries.plugin.loader;

import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.RuntimeEnvironment;
import net.prematic.libraries.plugin.description.DefaultPluginDescription;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.prematic.libraries.plugin.exception.PluginLoadException;
import net.prematic.libraries.plugin.lifecycle.Lifecycle;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.manager.PluginManager;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPluginLoader extends URLClassLoader implements PluginLoader {

    private final PluginManager pluginManager;
    private final RuntimeEnvironment environment;

    private final File location;
    private final PluginDescription description;

    private final Collection<Class<?>> loadedClasses;
    private final LifecycleState defaultState;

    private Plugin instance;
    private boolean enabled;

    public DefaultPluginLoader(PluginManager pluginManager, RuntimeEnvironment environment, File location){
        this(pluginManager,environment,location,null);
    }

    public DefaultPluginLoader(PluginManager pluginManager, RuntimeEnvironment environment, File location, PluginDescription description){
        this(pluginManager,environment,location,description,null);
    }

    public DefaultPluginLoader(PluginManager pluginManager, RuntimeEnvironment environment, File location, PluginDescription description, LifecycleState defaultState) {
        super(new URL[] {FileUtil.fileToUrl(location)});
        this.pluginManager = pluginManager;
        this.environment = environment;
        this.location = location;

        this.description = description!=null?description:loadDescription();
        this.defaultState = defaultState!=null?defaultState:new LifecycleState<>(this.description,this,this.environment);

        this.loadedClasses = ConcurrentHashMap.newKeySet();
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
    public Collection<Class<?>> getLoadedClasses() {
        return this.loadedClasses;
    }

    @Override
    public Class<?> getLoadedClass(String name) {
        return Iterators.findOne(this.loadedClasses, clazz -> clazz.getName().equals(name));
    }

    @Override
    public boolean isInstanceAvailable() {
        return instance != null;
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
                    this.pluginManager.executeLifecycleStateListener(state,stateEvent,instance);
                }catch (Exception exception) {
                    pluginManager.getLogger().error("Could not execute lifecycle state {} for plugin {}",state,description.getName());
                }
            }
        }
    }

    @Override
    public Plugin getInstance() {
        if(!isInstanceAvailable()) throw new PluginLoadException("No plugin instance available.");
        return instance;
    }

    @Override
    public Plugin enable() {
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
    public Plugin construct() {
        if(isInstanceAvailable()) throw new PluginLoadException("Plugin is already constructed.");
        try{
            Class<? extends Plugin> mainClass = loadMainClass();
            this.instance = mainClass.getDeclaredConstructor().newInstance();
            executeLifeCycleState(LifecycleState.CONSTRUCTION);
            if(pluginManager.getLogger().isDebugging()) pluginManager.getLogger().debug("Created instance for {} v{}",description.getName(),description.getVersion().getName());
            return this.instance;
        }catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException exception){
            throw new PluginLoadException("Could not create plugin instance for plugin "+description.getName()+" ("+exception.getMessage()+")",exception);
        }
    }

    @Override
    public void initialize() {
        Class<?> pluginClass = this.instance.getClass();
        while(!pluginClass.getSuperclass().equals(Plugin.class)) pluginClass = pluginClass.getSuperclass();
        Type type = pluginClass.getGenericSuperclass();
        if(!(type instanceof Class)) {
            ParameterizedType parameterized = (ParameterizedType)type;
            if(!this.environment.getInstance().getClass().isAssignableFrom((Class<?>) parameterized.getActualTypeArguments()[0])){
                throw new IllegalArgumentException("Invalid runtime type at plugin class ("+parameterized.getActualTypeArguments()[0]+" is not assignable by "+this.instance.getClass()+").");
            }
        }
        this.instance.initialize(this.description,this,this.environment.getInstance());
        executeLifeCycleState(LifecycleState.INITIALISATION);
    }

    @Override
    public void load() {
        executeLifeCycleState(LifecycleState.LOAD);
        pluginManager.getLogger().info("Loaded plugin {} v{}",description.getName(),description.getVersion().getName());
    }

    @Override
    public void bootstrap() {
        enabled = true;
        executeLifeCycleState(LifecycleState.BOOTSTRAP);
        pluginManager.getLogger().info("Started plugin {} v{} by {}",description.getName(),description.getVersion().getName(),description.getAuthor());
    }

    @Override
    public void shutdown() {
        enabled = false;
        executeLifeCycleState(LifecycleState.SHUTDOWN);
        pluginManager.getLogger().info("Stopped plugin {} v{} by {}",description.getName(),description.getVersion().getName(),description.getAuthor());
    }

    @Override
    public void unload() {
        executeLifeCycleState(LifecycleState.UNLOAD);
        instance = null;
        pluginManager.getLogger().info("Unloaded plugin {} v{}",description.getName(),description.getVersion().getName());
    }


    private Class<? extends Plugin> loadMainClass() throws ClassNotFoundException{
        String className = description.getMainClass().getMainClass(this.environment.getName());
        if(className == null) throw new PluginLoadException("No main class for plugin "+description.getName()+" v"+description.getVersion().getName()+" found.");
        Class<?> clazz = loadClass(className);
        if(clazz != null && Plugin.class.isAssignableFrom(clazz)) return (Class<Plugin>) clazz;
        throw new PluginLoadException("No main class for plugin "+description.getName()+" v"+description.getVersion().getName()+" found.");
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class result = getLoadedClass(name);
        if(result == null){
            if(this.pluginManager != null){
                for(PluginLoader loader : this.pluginManager.getLoaders()){
                    result = loader.getLoadedClass(name);
                    if(result != null) break;
                }
            }
            if(result == null){
                result = super.loadClass(name,resolve);
                this.loadedClasses.add(result);
                if(this.pluginManager.getLogger().canDebug(DebugLevel.HEIGHT)) this.pluginManager.getLogger().debug(DebugLevel.HEIGHT,"Loaded class {}",name);
            }
        }
        if(result == null) throw new ClassNotFoundException(name);
        return result;
    }

    public PluginDescription loadDescription(){
        InputStream resourceStream = getResourceAsStream(PluginDescription.getBasePath()+".json");
        try{
            if(resourceStream == null){
                resourceStream = getResourceAsStream(PluginDescription.getBasePath()+".yml");
                if(resourceStream == null) throw new InvalidPluginDescriptionException("No plugin description found.");
                else return new DefaultPluginDescription(location,DocumentFileType.YAML.getReader().read(resourceStream));
            }else return new DefaultPluginDescription(location,DocumentFileType.JSON.getReader().read(resourceStream));
        }finally {
            if(resourceStream != null) {
                try {resourceStream.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
