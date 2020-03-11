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

package net.pretronic.libraries.plugin.description.mainclass;

import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.Map;

public class MainClassAdapter implements DocumentAdapter<MainClass> {

    private final static Type MAP_TYPE = new TypeReference<Map<String,String>>(){}.getType();

    @Override
    public MainClass read(DocumentBase entry, TypeReference<MainClass> type) {
        if(entry.isPrimitive()) return new SingleMainClass(entry.toPrimitive().getAsString());
        else return new MultipleMainClass(entry.toDocument().getAsObject(MAP_TYPE));
    }

    @Override
    public DocumentEntry write(String key, MainClass object) {
        return object.write(key);
    }
}
