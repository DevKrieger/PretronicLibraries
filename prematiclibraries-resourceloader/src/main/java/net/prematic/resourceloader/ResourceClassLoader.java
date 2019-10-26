/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.10.19, 21:00
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

package net.prematic.resourceloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This is a simple class loader based on the java url class loader.
 */
public class ResourceClassLoader extends URLClassLoader {

    public ResourceClassLoader(File resource) {
        super(new URL[]{toUrl(resource)});
    }

    private static URL toUrl(File resource){
        try {
            return resource.toURI().toURL();
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Could not create url from file ("+exception.getMessage()+")");
        }
    }
}
