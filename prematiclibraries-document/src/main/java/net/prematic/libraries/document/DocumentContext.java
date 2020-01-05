/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 22:35
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
import net.prematic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * The {@link DocumentContext} is assigned to a {@link Document} and provides information for
 * the serialisation and deserialization. {@link DocumentAdapter}s and {@link DocumentAdapterFactory}s
 * are registered to a context. It is also possible to assign sub contexts.
 */
public interface DocumentContext {

    /**
     * Get all assigned sub contexts.
     *
     * @return The contexts in a collection
     */
    Collection<DocumentContext> getContexts();

    /**
     * Get all adapters and the object type as key.
     *
     * @return The adapters in a map
     */
    Map<Type,DocumentAdapter<?>> getAdapters();

    /**
     * Get all adapter factories .
     *
     * @return ALl factories in a collection
     */
    Collection<DocumentAdapterFactory> getFactories();

    /**
     * Find an adapter, which fits to a {@link TypeReference}
     *
     * @param reference The reference with the object information.
     * @param <T> The object type
     * @return The adapter or null
     */
    <T> DocumentAdapter<T> findAdapter(TypeReference<T> reference);


    /**
     * Register a new adapter to this context.
     *
     * @param type The class of the object
     * @param adapter The adapter for transforming the object
     * @param <T> The object type
     */
    <T> void registerAdapter(Class<T> type, DocumentAdapter<T> adapter);

    /**
     * Register a simple mapping adapter.
     *
     * <p>It is not possible to directly read interfaces with {@link Document#getObject(String, Type)}.
     * But ig you register a new mapping adapter, the interface class will be replaced with the mapped class.</p>
     *
     * @param type The class to map
     * @param mappedClass The mapped class
     * @param <T> The type
     */
    default <T> void registerMappingAdapter(Class<T> type,Class<? extends T> mappedClass){
        registerAdapter(type,new ClassMappingAdapter<>(mappedClass));
    }

    /**
     * Register a new hierarchy adapter.
     *
     * <p>Example: {@link Collection} -> {@link java.util.List} / {@link java.util.HashSet}</p>
     * <p>If you register a hierarchy adapter for the Collection interface, the registered
     * adapter is also made available to the subclasses.</p>
     *
     * @param type The super class type
     * @param adapter The adapter, which should also be used for the children
     * @param <T> The super class type
     */
    default <T> void registerHierarchyAdapter(Class<T> type, DocumentAdapter<T> adapter){
        registerFactory(HierarchyAdapterFactory.newFactory(adapter,type));
    }

    /**
     * Register a new adapter factory.
     *
     * @param factory The factory
     */
    void registerFactory(DocumentAdapterFactory factory);

    /**
     * Add a sub context.
     *
     * @param context The context to add
     */
    void addContext(DocumentContext context);

    /**
     * Remove a sub context.
     *
     * @param context The context to add
     */
    void removeContext(DocumentContext context);


    DocumentEntry serialize(Object value);

    DocumentEntry serialize(String key, Object value);


    <T> T deserialize(DocumentBase entry, Class<T> clazz);

    <T> T deserialize(DocumentBase entry, Type type);

    <T> T deserialize(DocumentBase entry, TypeReference<?> type);

    /**
     * Get the global context (Assigned in the {@link DocumentRegistry}
     *
     * @return The global context
     */
    static DocumentContext getDefaultContext(){
        return DocumentRegistry.getDefaultContext();
    }

    /**
     * Create a new context.
     *
     * @return The new context
     */
    static DocumentContext newContext(){
        return DocumentRegistry.getFactory().newContext();
    }

    /**
     * Create a new context, which already contains all adapters from the global context-
     *
     * @return The new context
     */
    static DocumentContext newPreparedContext(){
        DocumentContext context = newContext();
        context.addContext(getDefaultContext());
        return context;
    }
}
