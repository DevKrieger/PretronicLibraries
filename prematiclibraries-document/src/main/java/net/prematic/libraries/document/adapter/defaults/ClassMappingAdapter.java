/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.07.19 22:35
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

package net.prematic.libraries.document.adapter.defaults;

import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.utility.reflect.TypeReference;

public class ClassMappingAdapter<T> implements DocumentAdapter<T> {

    private final Class<? extends T> mappedClass;

    public ClassMappingAdapter(Class<? extends T> mappedClass) {
        this.mappedClass = mappedClass;
    }

    @Override
    public T read(DocumentEntry entry, TypeReference<T> type) {
        return DocumentRegistry.deserialize(entry,mappedClass);
    }

    @Override
    public DocumentEntry write(String key, T object) {
        return DocumentRegistry.serializeObject(key,mappedClass,object);
    }
}
