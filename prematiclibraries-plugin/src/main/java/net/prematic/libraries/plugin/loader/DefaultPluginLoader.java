package net.prematic.libraries.plugin.loader;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 19.03.19 08:05
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

import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.RuntimeEnvironment;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.driver.DriverPlugin;
import net.prematic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.prematic.libraries.plugin.exception.PluginLoadException;
import net.prematic.libraries.plugin.lifecycle.Lifecycle;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.manager.PluginManager;
import net.prematic.libraries.utility.FileUtil;
import net.prematic.libraries.utility.Iterators;

import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPluginLoader<R> extends URLClassLoader implements PluginLoader<R>{

    private final static Set<DefaultPluginLoader> LOADERS = ConcurrentHashMap.newKeySet();

    private final RuntimeEnvironment<R> environment;
    private final PluginDescription description;
    private final Collection<Class<?>> loadedClasses;
    private final LifecycleState<R> state;

    private Plugin instance;

    public DefaultPluginLoader(RuntimeEnvironment<R> environment, PluginDescription description) {
        super(new URL[] {FileUtil.toUrl(description.getPluginLocation())});
        this.environment = environment;
        this.description = description;
        this.loadedClasses = ConcurrentHashMap.newKeySet();
        this.state = new LifecycleState<>(this.description,this,this.environment);
    }

    @Override
    public PluginManager getPluginManager() {
        return null;
    }

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
    public boolean isDriver() {
        return this.instance instanceof DriverPlugin;
    }

    @Override
    public void executeLifeCycleState(String state) {
        executeLifeCycleState(state,this.state);
    }

    @Override
    public void executeLifeCycleState(String state, LifecycleState stateEvent) {
        if(!isInstanceAvailable()) throw new PluginLoadException("Plugin is not constructed.");
        for(Method method : instance.getClass().getDeclaredMethods()){
            Lifecycle cycle = method.getAnnotation(Lifecycle.class);
            if(cycle!= null && !Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 1 && cycle.state().equals(state)
                    && (cycle.environment().equals("null") || cycle.environment().equalsIgnoreCase(environment.getName()))){
                if(stateEvent.getClass().isAssignableFrom(method.getParameterTypes()[0])){
                    try{method.invoke(instance,(method.getParameterTypes()[0]).cast(stateEvent));
                    }catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception)
                    {
                        exception.printStackTrace();
                    }
                }

            }
        }
    }

    @Override
    public Plugin enable() {
        Plugin plugin = construct();
        initialize();
        load();

        if(plugin instanceof DriverPlugin){
            ((DriverPlugin) plugin).buildDriver();
        }

        bootstrap();
        return plugin;
    }

    @Override
    public void disable() {
        shutdown();
        unload();
    }

    @Override
    public Plugin construct() {
        if(isInstanceAvailable()) throw new PluginLoadException("Plugin is already constructed.");
        LOADERS.add(this);
        try{
            Class<? extends Plugin> mainClass = loadMainClass();
            if(mainClass == null) throw new PluginLoadException("No main class for plugin "+description.getName()+" v"+description.getVersion().getName()+" found.");
            this.instance = mainClass.getDeclaredConstructor().newInstance();
            executeLifeCycleState(LifecycleState.CONSTRUCTION);
            return this.instance;
        }catch (Exception exception){
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
    }

    @Override
    public void install() {
        executeLifeCycleState(LifecycleState.INSTALLATION);
    }

    @Override
    public void bootstrap() {
        executeLifeCycleState(LifecycleState.BOOTSTRAP);
    }

    @Override
    public void shutdown() {
        executeLifeCycleState(LifecycleState.SHUTDOWN);
    }

    @Override
    public void uninstall() {
        executeLifeCycleState(LifecycleState.UNINSTALLATION);
    }

    @Override
    public void unload() {
        executeLifeCycleState(LifecycleState.UNLOAD);
        LOADERS.remove(this);
    }

    private Class<? extends Plugin> loadMainClass() throws Exception{
        Class<?> clazz = loadClass(description.getMainClass().getMainClass(this.environment.getName()));
        if(clazz.isAssignableFrom(Plugin.class)) return (Class<Plugin>) clazz;
        throw new InvalidPluginDescriptionException("No main class found.");
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class result = getLoadedClass(name);
        if(result == null){
            for(DefaultPluginLoader loader : LOADERS){
                result = loader.getLoadedClass(name);
                if(result != null) break;
            }
            if(result == null){
                result = super.loadClass(name,resolve);
                this.loadedClasses.add(result);
            }
        }
        if(result == null) throw new ClassNotFoundException(name);
        return result;
    }
}
