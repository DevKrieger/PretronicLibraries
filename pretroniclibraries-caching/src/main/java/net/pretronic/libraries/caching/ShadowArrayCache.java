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

package net.pretronic.libraries.caching;

import net.pretronic.libraries.utility.list.WeakReferenceList;

import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

/**
 * The @{@link ShadowArrayCache} creates a weak reference to a object if it is removed from the cache.
 *
 * @param <O>
 */
public class ShadowArrayCache<O> extends ArrayCache<O>{

    private final WeakReferenceList<O> shadowReferenceObjects;

    public ShadowArrayCache() {
        shadowReferenceObjects = new WeakReferenceList<>();
    }

    public ShadowArrayCache(int maxSize) {
        super(maxSize);
        shadowReferenceObjects = new WeakReferenceList<>();
    }

    public ShadowArrayCache(int maxSize, int buffer) {
        super(maxSize, buffer);
        shadowReferenceObjects = new WeakReferenceList<>();
    }

    public ShadowArrayCache(ExecutorService executor) {
        super(executor);
        shadowReferenceObjects = new WeakReferenceList<>();
    }

    public ShadowArrayCache(ExecutorService executor, int maxSize) {
        super(executor, maxSize);
        shadowReferenceObjects = new WeakReferenceList<>();
    }

    public ShadowArrayCache(ExecutorService executor, int maxSize, int buffer) {
        super(executor, maxSize, buffer);
        shadowReferenceObjects = new WeakReferenceList<>();
    }

    @Override
    protected void callInternalRemove(O object) {
        shadowReferenceObjects.add(object);
    }

    @Override
    protected O callInternalLoad(CacheQuery<O> query, Object[] identifiers) {
        for (O object : shadowReferenceObjects) {
            if(query.check(object,identifiers)) return object;
        }
        return null;
    }

    @Override
    protected O callInternalLoad(Predicate<O> query) {
        for (O object : shadowReferenceObjects) {
            if(query.test(object)) return object;
        }
        return null;
    }
}
