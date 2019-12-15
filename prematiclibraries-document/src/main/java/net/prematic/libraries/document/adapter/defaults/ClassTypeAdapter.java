/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 19:32
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

import net.prematic.libraries.document.DocumentContext;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.adapter.DocumentAdapterInitializeAble;
import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.entry.DocumentBase;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.reflect.TypeReference;

public class ClassTypeAdapter<T> implements DocumentAdapter<T>, DocumentAdapterInitializeAble {

    public static final String ADAPTER_NAME = "adapterSerialisationClass";

    public Class<? extends T> classNotFoundAdapter;

    private DocumentContext context;

    public ClassTypeAdapter() {}

    public ClassTypeAdapter(Class<? extends T> classNotFoundAdapter) {
        this.classNotFoundAdapter = classNotFoundAdapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(DocumentBase entry, TypeReference<T> type) {
        if(entry.isPrimitive()) throw new IllegalArgumentException("Entry is not a object.");
        String clazz = entry.toDocument().getString(ADAPTER_NAME);
        if(clazz != null){
            try {
                return (T) context.deserialize(entry,Class.forName(clazz));
            } catch (ClassNotFoundException ignored) {}
        }
        return context.deserialize(entry,classNotFoundAdapter);
    }

    @Override
    public DocumentEntry write(String key, T object) {
        Document document = context.serialize(object).toDocument();
        document.entries().add(DocumentRegistry.getFactory().newPrimitiveEntry(ADAPTER_NAME,object.getClass().getName()));
        return document;
    }

    @Override
    public void initialize(DocumentContext context) {
        this.context = context;
    }
}
