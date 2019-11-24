/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.11.19, 13:44
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

package net.prematic.libraries.plugin.description.mainclass;

import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.Map;

public class MainClassAdapter implements DocumentAdapter<MainClass> {

    private final static Type MAP_TYPE = new TypeReference<Map<String,String>>(){}.getType();

    @Override
    public MainClass read(DocumentEntry entry, TypeReference<MainClass> type) {
        if(entry.isPrimitive()) return new SingleMainClass(entry.toPrimitive().getAsString());
        else return new MultipleMainClass(entry.toDocument().getAsObject(MAP_TYPE));
    }

    @Override
    public DocumentEntry write(String key, MainClass object) {
        return object.write(key);
    }
}
