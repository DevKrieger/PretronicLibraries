/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.02.20, 14:50
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

package net.prematic.libraries.caching.synchronisation;

import net.prematic.libraries.caching.ArrayCache;
import net.prematic.libraries.caching.CacheQuery;
import net.prematic.libraries.document.Document;
import net.prematic.libraries.utility.Validate;
import net.prematic.synchronisation.SynchronisationCaller;
import net.prematic.synchronisation.Synchronizable;

import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ArraySynchronizableCache<O,I> extends ArrayCache<O> implements SynchronizableCache<O,I> {

    private SynchronisationCaller<I> caller;

    private CacheQuery<O> identifierQuery;
    private BiConsumer<O, Document> deleteListener;
    private BiConsumer<O, Document> updateListener;
    private BiFunction<I, Document, O> createHandler;

    public ArraySynchronizableCache() {
    }

    public ArraySynchronizableCache(int maxSize) {
        super(maxSize);
    }

    public ArraySynchronizableCache(int maxSize, int buffer) {
        super(maxSize, buffer);
    }

    public ArraySynchronizableCache(ExecutorService executor) {
        super(executor);
    }

    public ArraySynchronizableCache(ExecutorService executor, int maxSize) {
        super(executor, maxSize);
    }

    public ArraySynchronizableCache(ExecutorService executor, int maxSize, int buffer) {
        super(executor, maxSize, buffer);
    }

    @Override
    public SynchronisationCaller<I> getCaller() {
        if(caller == null) throw new IllegalArgumentException("Caller is not initialized");
        return caller;
    }

    @Override
    public void onDelete(I identifier, Document data) {
        O result = remove(identifierQuery,identifier);
        if(result != null){
            if(updateListener != null) deleteListener.accept(result,data);
        }
    }

    @Override
    public void onCreate(I identifier, Document data) {
        if(createHandler != null){
            O object = createHandler.apply(identifier, data);
            if(object != null) insert(object);
        }
    }

    @Override
    public void onUpdate(I identifier, Document data) {
        O object = get(identifierQuery,identifier);
        if(object != null){
            if(object instanceof Synchronizable){
                ((Synchronizable) object).onUpdate(data);
            }
            if(updateListener != null) updateListener.accept(object,data);
        }
    }

    @Override
    public void init(SynchronisationCaller<I> caller) {
        if(this.caller != null) throw new IllegalArgumentException("Caller is already initialized");
        this.caller = caller;
    }

    @Override
    public void setIdentifierQuery(CacheQuery<O> query) {
        Validate.notNull(query);
        this.identifierQuery = query;
    }

    @Override
    public void setDeleteListener(BiConsumer<O, Document> listener) {
        Validate.notNull(listener);
        this.deleteListener = listener;
    }

    @Override
    public void setUpdateListener(BiConsumer<O, Document> listener) {
        Validate.notNull(listener);
        this.updateListener = listener;
    }

    @Override
    public void setCreateHandler(BiFunction<I, Document, O> handler) {
        Validate.notNull(handler);
        this.createHandler = handler;
    }


}
