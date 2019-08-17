/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.04.19 12:58
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

package net.prematic.libraries.plugin.description;

import net.prematic.libraries.document.DocumentEntry;

import java.util.Map;

public interface PluginMainClass {

    String getMainClass(String instanceName);



    static PluginMainClass readFromDocumentEntry(DocumentEntry entry){
        if(entry == null) return null;
        return entry.isPrimitive()?new SimpleMainClass(entry.toPrimitive().getAsString()):new MultiMainClass(entry.toDocument().getAsMap(String.class,String.class));
    }

    class MultiMainClass implements PluginMainClass {

        private final Map<String,String> classes;

        public MultiMainClass(Map<String,String> classes) {
            this.classes = classes;
        }

        @Override
        public String getMainClass(String instanceName) {
            String clazz =  this.classes.get(instanceName);
            return clazz != null?clazz:this.classes.get("default");
        }
    }

    class SimpleMainClass implements PluginMainClass {

        private final String clazz;

        public SimpleMainClass(String clazz) {
            this.clazz = clazz;
        }

        @Override
        public String getMainClass(String instanceName) {
            return clazz;
        }
    }



}
