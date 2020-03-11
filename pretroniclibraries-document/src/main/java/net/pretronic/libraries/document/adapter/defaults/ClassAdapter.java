/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.document.adapter.defaults;

import net.pretronic.libraries.document.DocumentRegistry;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.ReflectException;
import net.pretronic.libraries.utility.reflect.TypeReference;

public class ClassAdapter implements DocumentAdapter<Class> {

    @Override
    public Class read(DocumentBase entry, TypeReference<Class> type) {
        if(entry.isPrimitive()) {
            try {
                return Class.forName(entry.toPrimitive().getAsString());
            } catch (ClassNotFoundException exception) {
                throw new ReflectException(exception);
            }
        }
        throw new IllegalArgumentException("Can't convert a object to a class.");
    }

    @Override
    public DocumentEntry write(String key, Class object) {
        return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.getName());
    }
}
