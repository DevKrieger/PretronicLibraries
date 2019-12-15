/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.09.19, 22:26
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

package net.prematic.libraries.document;

import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.adapter.DocumentAdapterFactory;
import net.prematic.libraries.document.adapter.defaults.ClassMappingAdapter;
import net.prematic.libraries.document.adapter.defaults.HierarchyAdapterFactory;
import net.prematic.libraries.document.entry.DocumentBase;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public interface DocumentContext {

    Collection<DocumentContext> getContexts();

    Map<Type,DocumentAdapter<?>> getAdapters();

    Collection<DocumentAdapterFactory> getFactories();

    <T> DocumentAdapter<T> findAdapter(TypeReference<T> reference);


    <T> void registerAdapter(Class<T> type, DocumentAdapter<T> adapter);

    default <T> void registerMappingAdapter(Class<T> type,Class<? extends T> mappedClass){
        registerAdapter(type,new ClassMappingAdapter<T>(mappedClass));
    }

    default <T> void registerHierarchyAdapter(Class<T> type, DocumentAdapter<T> adapter){
        registerFactory(HierarchyAdapterFactory.newFactory(adapter,type));
    }

    void registerFactory(DocumentAdapterFactory factory);

    void addContext(DocumentContext context);

    void removeContext(DocumentContext context);


    DocumentEntry serialize(Object value);

    DocumentEntry serialize(String key, Object value);


    <T> T deserialize(DocumentBase entry, Class<T> clazz);

    <T> T deserialize(DocumentBase entry, Type type);

    <T> T deserialize(DocumentBase entry, TypeReference<?> type);


    void loadConfigurationClass(Class<?> configurationClass);

    void loadConfigurationClass(Class<?> clazz, Document data);

    void loadConfigurationClass(Class<?> clazz, Document data, File source, DocumentFileType type);


    static DocumentContext getDefaultContext(){
        return DocumentRegistry.getDefaultContext();
    }

    static DocumentContext newContext(){
        return DocumentRegistry.getFactory().newContext();
    }

    static DocumentContext newPreparedContext(){
        DocumentContext context = newContext();
        context.addContext(getDefaultContext());
        return context;
    }
}
