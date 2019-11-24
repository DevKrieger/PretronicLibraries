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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;

public interface PluginClassLoader {

    Collection<Class<?>> getLoadedClasses();

    Class<?> getLoadedClass(String name) throws ClassNotFoundException;

    boolean isClassLoaded(String name);

    Class<?> loadClass(String name) throws ClassNotFoundException;

    URL getResource(String name);

    InputStream getResourceAsStream(String name);

    Enumeration<URL> getResources(String name) throws IOException;

    ClassLoader asJVMLoader();

}
