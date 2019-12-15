/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 10:17
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
import net.prematic.libraries.utility.reflect.ReflectException;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class CollectionAdapter implements DocumentAdapter<Collection>, DocumentAdapterInitializeAble {

    public static Map<Class<? extends Collection>,Class<? extends Collection>> MAPPED_CLASS = new LinkedHashMap<>();

    static {
        MAPPED_CLASS.put(Collection.class, HashSet.class);
        MAPPED_CLASS.put(Set.class, HashSet.class);
        MAPPED_CLASS.put(List.class, ArrayList.class);
        MAPPED_CLASS.put(Queue.class, ArrayBlockingQueue.class);
    }

    private DocumentContext context;

    @Override
    public Collection read(DocumentBase entry, TypeReference<Collection> type) {
        if(entry.isPrimitive()) throw new IllegalArgumentException("Object is a primitive type.");
        Collection<?> instance;

        try{
            if(!type.getRawClass().isInterface()) instance = (Collection<?>) type.getRawClass().getDeclaredConstructor().newInstance();
            else instance = MAPPED_CLASS.get(type.getRawClass()).getDeclaredConstructor().newInstance();
        }catch (Exception exception){
            throw new ReflectException(exception);
        }

        Type itemType = type.getArgument(0);
        entry.toDocument().forEach(entry1 -> instance.add(context.deserialize(entry1,(Type)itemType)));
        return instance;
    }

    @Override
    public DocumentEntry write(String key, Collection object) {
        Document document = DocumentRegistry.getFactory().newArrayEntry(key);
        int i = 0;
        for(Object item : object){
            document.entries().add(context.serialize("list-item-"+i,item));
            i++;
        }
        return document;
    }

    @Override
    public void initialize(DocumentContext context) {
        this.context = context;
    }
}
