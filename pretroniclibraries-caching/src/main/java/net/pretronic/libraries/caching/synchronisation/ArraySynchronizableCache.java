/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.caching.synchronisation;

import net.pretronic.libraries.caching.ArrayCache;
import net.pretronic.libraries.caching.CacheQuery;
import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.utility.Validate;
import net.pretronic.libraries.synchronisation.SynchronisationCaller;
import net.pretronic.libraries.synchronisation.Synchronizable;

import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The @{@link ArraySynchronizableCache} is based on the @{@link ArrayCache} and implements
 * the {@link SynchronizableCache} which provides additional functionalities for synchronising different
 * caches in a cluster network.
 *
 * @param <O> The object to cache.
 * @param <I> The main identifier which is used for the synchronisation
 */
public class ArraySynchronizableCache<O,I> extends ArrayCache<O> implements SynchronizableCache<O,I> {

    private SynchronisationCaller<I> caller;

    private CacheQuery<O> identifierQuery;
    private BiConsumer<O, Document> deleteListener;
    private BiConsumer<O, Document> updateListener;
    private BiFunction<I, Document, O> createHandler;

    private boolean clearOnDisconnect;
    private boolean skipOnDisconnect;

    private boolean connected;

    public ArraySynchronizableCache() {}

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
        if(identifierQuery == null) throw new IllegalArgumentException("Identifier query is not set");
        O result = remove(identifierQuery,identifier);
        if(result != null && updateListener != null){
            deleteListener.accept(result,data);
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
        if(identifierQuery == null) throw new IllegalArgumentException("Identifier query is not set");
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
        this.connected = caller.isConnected();
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

    @Override
    public void setClearOnDisconnect(boolean enabled) {
        this.clearOnDisconnect = enabled;
    }

    @Override
    public void setSkipOnDisconnect(boolean enabled) {
        this.skipOnDisconnect = enabled;
    }

    @Override
    public void onConnect() {
        this.connected = true;
        if(clearOnDisconnect) clear();
    }

    @Override
    public void onDisconnect() {
        this.connected = false;
        if(clearOnDisconnect) clear();
    }

    @Override
    public O get(CacheQuery<O> query, Object... identifiers) {
        if(!connected && skipOnDisconnect){
            System.out.println("[Debug] SKIP CACHE, NOT CONNECTED");
            query.validate(identifiers);
            return query.load(identifiers);
        }
        return super.get(query, identifiers);
    }

    @Override
    public O get(Predicate<O> query, Supplier<O> loader) {
        if(!connected && skipOnDisconnect){
            System.out.println("[Debug] SKIP CACHE, NOT CONNECTED");
            return loader != null ? loader.get() : null;
        }
        return super.get(query, loader);
    }
}
