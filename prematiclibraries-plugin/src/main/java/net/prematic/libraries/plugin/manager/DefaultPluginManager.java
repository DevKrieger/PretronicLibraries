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

import net.prematic.libraries.jarsignature.JarVerifier;
import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.RuntimeEnvironment;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.driver.Driver;
import net.prematic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.prematic.libraries.plugin.exception.NoDriverException;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.loader.DefaultPluginLoader;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.io.FileUtil;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DefaultPluginManager<R> implements PluginManager<R> {

    private File pluginInstallationFolder;
    private final RuntimeEnvironment<R> environment;
    private final Collection<PluginLoader<R>> loaders;

    private final Map<Class<?>,Driver> drivers;

    public DefaultPluginManager(RuntimeEnvironment<R> environment) {
        this.environment = environment;
        this.loaders = new LinkedHashSet<>();
        this.drivers = new LinkedHashMap<>();
    }

    @Override
    public Plugin getPlugin(String name) {
        return null;
        //return Iterators.findOne(this.loaders, plugin -> plugin.getName().equalsIgnoreCase(name));
    }

    @Override
    public Plugin getPlugin(UUID id) {
        return null;
    }

    @Override
    public <D extends Driver> D getDriver(Class<D> driverClass) {
        Driver driver =  this.drivers.get(driverClass);
        if(driver == null) throw new NoDriverException("No driver for class "+driverClass+" found.");
        return (D) driver;
    }

    @Override
    public Collection<PluginLoader<R>> getLoaders() {
        return this.loaders;
    }

    @Override
    public Collection<Plugin<R>> getPlugins() {
        return null;
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
        Collection<PluginDescription> descriptions = new HashSet<>();
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
        PluginLoader<R> loader = Iterators.findOne(this.loaders, loader1 -> loader1.getLocation().equals(file));
        if(loader != null) return loader;
        loader = new DefaultPluginLoader<>(this,this.environment,file);
        this.loaders.add(loader);
        return loader;
    }

    @Override
    public void registerLifecycleStateListener(LifecycleState state, Consumer<Plugin> listener) {

    }

    @Override
    public void executeLifecycleStateListener(LifecycleState state, Plugin plugin) {

    }



    @Override
    public Plugin<R> loadPlugin(File location) {
        return null;
    }


    @Override
    public Collection<Plugin<R>> loadPlugins(File folder) {
        return null;
    }


    @Override
    public void shutdownPlugins() {

    }

    @Override
    public void shutdown() {

    }

    public PluginDescription loadPluginDescription(File file){
        try {
            return loadPluginDescription(new JarFile(file));
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    public PluginDescription loadPluginDescription(JarFile jarFile){
        JarEntry entry = jarFile.getJarEntry("plugin.yml");
        if(entry == null) throw new InvalidPluginDescriptionException("No plugin.json found.");
        return null;
    }
}
