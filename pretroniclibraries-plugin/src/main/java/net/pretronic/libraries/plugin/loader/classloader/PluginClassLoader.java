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

package net.pretronic.libraries.plugin.loader.classloader;

import net.pretronic.libraries.utility.io.IORuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public interface PluginClassLoader {

    Collection<Class<?>> getLoadedClasses();

    Class<?> getLoadedClass(String name) throws ClassNotFoundException;

    boolean isClassLoaded(String name);

    Class<?> loadClass(String name) throws ClassNotFoundException;


    URL getResource(String name);

    InputStream getResourceAsStream(String name);

    Enumeration<URL> getResources(String name) throws IOException;

    ClassLoader asJVMLoader();

    default boolean hasResource(String name){
        return getResourceAsStream(name) != null;
    }

    default List<String> getResourceFiles(String path){
        List<String> filenames = new ArrayList<>();

        try (InputStream in = getResourceAsStream(path);BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }

        return filenames;
    }
}
