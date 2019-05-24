/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.04.19 16:09
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

import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.driver.Driver;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.utility.Iterators;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.jar.JarFile;

public class DefaultPluginManager<R> implements PluginManager<R> {

    private final Collection<Plugin> plugins;

    private final Map<Class<?>,Driver> drivers;

    public DefaultPluginManager() {
        this.plugins = ConcurrentHashMap.newKeySet();
    }

    @Override
    public Plugin getPlugin(String name) {
        return Iterators.findOne(this.plugins, plugin -> plugin.getName().equalsIgnoreCase(name));
    }

    @Override
    public Plugin getPlugin(UUID id) {
        return null;
    }

    @Override
    public <D extends Driver> D getDriver(Class<D> driverClass) {
        return (D) this.drivers.get(driverClass);
    }

    @Override
    public PluginDescription detectPluginDescription(File file) {
        try {
            return loadPluginDescription(new JarFile(file));
        } catch (IOException exception) {
            throw new IllegalArgumentException(file.getAbsolutePath()+" is not a jar file.");
        }
    }

    @Override
    public Collection<PluginDescription> detectPluginDescriptions(File directory) {
        Collection<PluginDescription> descriptions = new HashSet();
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isFile() && file.getName().endsWith(".jar")){
                        try {
                            descriptions.add(loadPluginDescription(new JarFile(file)));
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
    public PluginLoader<R> createPluginLoader(File file) {
        return null;
    }

    @Override
    public Collection<Plugin<R>> getPlugins() {
        return null;
    }

    @Override
    public void registerLifecycleStateListener(LifecycleState state, Consumer<Plugin> listener) {

    }

    @Override
    public void executeLifecycleSStateListener(LifecycleState state, Plugin plugin) {

    }

    @Override
    public int loadPlugins(File folder) {
        return 0;
    }

    @Override
    public int loadPlugins(PluginContext context) {
        return 0;
    }

    @Override
    public void shutdownPlugins() {

    }

    @Override
    public void shutdown() {

    }

    private PluginDescription loadPluginDescription(JarFile jarFile){
        return null;
    }
}
