package net.prematic.libraries.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public class PluginClassloader extends URLClassLoader {

    public static final Set<PluginClassloader> loaders = new CopyOnWriteArraySet<>();
    private final Map<String,Class<?>> classes;

    public PluginClassloader(URL[] urls) {
        super(urls);
        this.classes = new LinkedHashMap<>();
        loaders.add(this);
    }
    public Map<String, Class<?>> getClasses() {
        return this.classes;
    }
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve,true);
    }
    private Class<?> loadClass(String name,boolean resolve, boolean checkOther) throws ClassNotFoundException {
        Class result = classes.get(name);
        if(result == null && checkOther) for(PluginClassloader loader : loaders) result = loader.getClass(name);
        if(result == null){
            try {
                return super.loadClass(name,resolve);
            }catch(ClassNotFoundException exception){}
        }
        if(result == null) throw new ClassNotFoundException(name);
        return result;
    }
    public Class<?> getClass(String name){
        return this.classes.get(name);
    }
}
