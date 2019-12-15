/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 16:45
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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentContext;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.adapter.DocumentAdapterInitializeAble;
import net.prematic.libraries.document.entry.DocumentBase;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.reflect.Primitives;
import net.prematic.libraries.utility.reflect.ReflectException;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapAdapter implements DocumentAdapter<Map>, DocumentAdapterInitializeAble {

    private DocumentContext context;

    @SuppressWarnings("unchecked")
    @Override
    public Map read(DocumentBase entry, TypeReference<Map> type) {
        if(entry.isPrimitive()) throw new IllegalArgumentException("Object is a primitive type.");

        Type keyType = type.getArgument(0);
        Type valueType = type.getArgument(1);

        Map instance;

        try{
            if(!type.getRawClass().isInterface()) instance = (Map<?,?>) type.getRawClass().getDeclaredConstructor().newInstance();
            else instance = new HashMap<>();
        }catch (Exception exception){
            throw new ReflectException(exception);
        }
        if(keyType == String.class){
            entry.toDocument().forEach(entry1 -> instance.put(entry1.getKey(),context.deserialize(entry1,valueType)));
        }else{
            entry.toDocument().forEach(entry12 -> instance.put(context.deserialize(DocumentRegistry.getFactory().newPrimitiveEntry(null, entry12.getKey()),keyType)
                    ,context.deserialize(entry12,valueType)));
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DocumentEntry write(String key, Map object) {
        Document document = DocumentRegistry.getFactory().newDocument(key);
        Set<Map.Entry> entrySet = object.entrySet();
        for(Map.Entry entry : entrySet){
            String itemKey;
            if(Primitives.isPrimitive(entry.getKey())) itemKey = entry.getKey().toString();
            else itemKey = entry.getKey().toString();
            document.entries().add(context.serialize(itemKey,entry.getValue()));
        }
        return document;
    }

    @Override
    public void initialize(DocumentContext context) {
        this.context = context;
    }
}
