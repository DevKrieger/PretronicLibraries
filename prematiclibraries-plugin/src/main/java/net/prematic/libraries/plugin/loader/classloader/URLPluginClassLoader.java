/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 24.11.19, 18:45
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

package net.prematic.libraries.plugin.loader.classloader;

import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.plugin.manager.PluginManager;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.Validate;
import net.prematic.libraries.utility.io.FileUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class URLPluginClassLoader extends URLClassLoader implements PluginClassLoader{

    private final PluginManager pluginManager;
    private final Collection<Class<?>> loadedClasses;

    public URLPluginClassLoader(PluginManager pluginManager,URL[] urls, ClassLoader parent) {
        super(urls, parent);
        Validate.notNull(pluginManager);
        this.pluginManager = pluginManager;
        this.loadedClasses = ConcurrentHashMap.newKeySet();
    }

    public URLPluginClassLoader(PluginManager pluginManager,URL[] urls) {
        super(urls);
        Validate.notNull(pluginManager);
        this.pluginManager = pluginManager;
        this.loadedClasses = ConcurrentHashMap.newKeySet();
    }

    @Override
    public Collection<Class<?>> getLoadedClasses() {
        return loadedClasses;
    }

    @Override
    public Class<?> getLoadedClass(String name) throws ClassNotFoundException{
        Class<?> class0 = getLoadedClass0(name);
        if(class0 == null) throw new ClassNotFoundException();
        else return class0;
    }

    public Class<?> getLoadedClass0(String name) {
        return Iterators.findOne(this.loadedClasses, clazz -> clazz.getName().equals(name));
    }

    @Override
    public boolean isClassLoaded(String name) {
        return getLoadedClass0(name) != null;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result = getLoadedClass(name);
        if(result == null){
            if(this.pluginManager != null){
                for(PluginLoader loader : this.pluginManager.getLoaders()){
                    result = loader.getClassLoader().getLoadedClass(name);
                    if(result != null) break;
                }
            }
            if(result == null){
                result = super.loadClass(name,resolve);
                this.loadedClasses.add(result);
                if(this.pluginManager.getLogger().canDebug(DebugLevel.HEIGHT)){
                    this.pluginManager.getLogger().debug(DebugLevel.HEIGHT,"Loaded class {}",name);
                }
            }
        }
        if(result == null) throw new ClassNotFoundException(name);
        return result;
    }

    @Override
    public ClassLoader asJVMLoader() {
        return this;
    }

    public static URLPluginClassLoader of(PluginManager pluginManager, File file){
        return new URLPluginClassLoader(pluginManager,new URL[]{FileUtil.fileToUrl(file)});
    }

    public static URLPluginClassLoader of(PluginManager pluginManager,File file,ClassLoader parent){
        return new URLPluginClassLoader(pluginManager,new URL[]{FileUtil.fileToUrl(file)},parent);
    }
}
