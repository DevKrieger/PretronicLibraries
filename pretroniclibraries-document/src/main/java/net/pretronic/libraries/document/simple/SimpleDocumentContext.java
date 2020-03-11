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

package net.pretronic.libraries.document.simple;

import net.pretronic.libraries.document.DocumentContext;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.adapter.DocumentAdapterFactory;
import net.pretronic.libraries.document.adapter.DocumentAdapterInitializeAble;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.utils.SerialisationUtil;
import net.pretronic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleDocumentContext implements DocumentContext {

    private final Map<Type, DocumentAdapter<?>> adapters;
    private final Collection<DocumentAdapterFactory> factories;
    private final Collection<DocumentContext> contexts;

    public SimpleDocumentContext() {
        this.adapters = new LinkedHashMap<>();
        this.factories = new ArrayList<>();
        this.contexts = new ArrayList<>();
    }

    @Override
    public Collection<DocumentContext> getContexts() {
        return contexts;
    }

    @Override
    public Map<Type, DocumentAdapter<?>> getAdapters() {
        return adapters;
    }

    @Override
    public Collection<DocumentAdapterFactory> getFactories() {
        return factories;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> DocumentAdapter<T> findAdapter(TypeReference<T> reference) {
        DocumentAdapter<?> adapter = adapters.get(reference.getRawType());
        if(adapter != null) return (DocumentAdapter<T>) adapter;
        for(DocumentAdapterFactory factory : factories){
            adapter = factory.create(reference);
            if(adapter != null){
                if(adapter instanceof DocumentAdapterInitializeAble) ((DocumentAdapterInitializeAble) adapter).initialize(this);
                adapters.put(reference.getRawType(),adapter);
                return (DocumentAdapter<T>) adapter;
            }
        }
        for(DocumentContext context : contexts){
            adapter = context.findAdapter(reference);
            if(adapter != null) return (DocumentAdapter<T>) adapter;
        }
        return null;
    }

    @Override
    public <T> void registerAdapter(Class<T> type, DocumentAdapter<T> adapter) {
        if(adapter instanceof DocumentAdapterInitializeAble) ((DocumentAdapterInitializeAble) adapter).initialize(this);
        this.adapters.put(type,adapter);
    }

    @Override
    public void registerFactory(DocumentAdapterFactory factory) {
        this.factories.add(factory);
    }

    @Override
    public void addContext(DocumentContext context) {
        this.contexts.add(context);
    }

    @Override
    public void removeContext(DocumentContext context) {
        this.contexts.remove(context);
    }

    @Override
    public DocumentEntry serialize(Object value) {
        return SerialisationUtil.serialize(this,value);
    }

    @Override
    public DocumentEntry serialize(String key, Object value) {
        return SerialisationUtil.serialize(this,key,value);
    }

    @Override
    public <T> T deserialize(DocumentBase entry, Class<T> clazz) {
        return SerialisationUtil.deserialize(this,entry,clazz);
    }

    @Override
    public <T> T deserialize(DocumentBase entry, Type type) {
        return SerialisationUtil.deserialize(this,entry,type);
    }

    @Override
    public <T> T deserialize(DocumentBase entry, TypeReference<?> type) {
        return SerialisationUtil.deserialize(this,entry,type);
    }
}
