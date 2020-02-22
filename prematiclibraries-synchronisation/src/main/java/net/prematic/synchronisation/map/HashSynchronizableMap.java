/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.02.20, 15:34
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

package net.prematic.synchronisation.map;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.utility.Validate;
import net.prematic.synchronisation.SynchronisationCaller;
import net.prematic.synchronisation.Synchronizable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class HashSynchronizableMap<K,V> extends HashMap<K,V> implements SynchronizableMap<K,V> {

    private SynchronisationCaller<K> caller;
    private BiConsumer<V, Document> deleteListener;
    private BiConsumer<V, Document> updateListener;
    private BiFunction<K, Document, V> createHandler;

    public HashSynchronizableMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public HashSynchronizableMap(int initialCapacity) {
        super(initialCapacity);
    }

    public HashSynchronizableMap() {
    }

    public HashSynchronizableMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public void setDeleteListener(BiConsumer<V, Document> listener) {
        Validate.notNull(listener);
        this.deleteListener = listener;
    }

    @Override
    public void setUpdateListener(BiConsumer<V, Document> listener) {
        Validate.notNull(listener);
        this.updateListener = listener;
    }

    @Override
    public void setCreateHandler(BiFunction<K, Document, V> handler) {
        Validate.notNull(handler);
        this.createHandler = handler;
    }

    @Override
    public SynchronisationCaller<K> getCaller() {
        return caller;
    }

    @Override
    public void onDelete(K identifier, Document data) {
        V result = remove(identifier);
        if(result != null){
            if(updateListener != null) deleteListener.accept(result,data);
        }
    }

    @Override
    public void onCreate(K identifier, Document data) {
        if(createHandler != null){
            V object = createHandler.apply(identifier, data);
            if(object != null) put(identifier,object);
        }
    }

    @Override
    public void onUpdate(K identifier, Document data) {
        V object = get(identifier);
        if(object != null){
            if(object instanceof Synchronizable){
                ((Synchronizable) object).onUpdate(data);
            }
            if(updateListener != null) updateListener.accept(object,data);
        }
    }

    @Override
    public void init(SynchronisationCaller<K> caller) {
        Validate.notNull(caller);
        this.caller = caller;
    }
}
