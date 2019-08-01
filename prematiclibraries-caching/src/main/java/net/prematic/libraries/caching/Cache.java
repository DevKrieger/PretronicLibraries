/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 12.06.19 13:26
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

package net.prematic.libraries.caching;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The Prematic cache library provides a bridge between your application and database. The Prematic cache is not a
 * key value store, you are able to access a object with a query from every side.
 *
 * <p>You are able to register a CacheQuery. With this query you are able to search a object. If the object
 * is not cached, the integrated loader will load the object from your storage.</p>
 *
 * @param <O> The object to cache.
 */
public interface Cache<O> {

    /**
     * Get all local cached objects.
     *
     * @return All objects in a collection
     */
    Collection<O> getCachedObjects();

    /**
     * Get the size of your cache (Without buffer).
     *
     * @return The cache size.
     */
    int size();

    /**
     * Get a object with a registered query.
     *
     * @param queryName The name of the query
     * @param identifiers The identifiers for your object
     * @return The object (Can be null)
     */
    O get(String queryName,Object... identifiers);

    /**
     * Get a object with a direct query.
     *
     * <p>We recommend to use a registered query (It is faster).</p>
     *
     * @param query The search query
     * @return The object (Can be null)
     */
    O get(Predicate<O> query);

    /**
     * Get a object with a direct query and loader.
     *
     * <p>We recommend to use a registered query (It is faster).</p>
     *
     * @param query The search query
     * @param loader The loader when the object is null
     * @return The object (Can be null)
     */
    O get(Predicate<O> query, Supplier<O> loader);

    CompletableFuture<O> getAsync(String queryName, Object... identifiers);

    CompletableFuture<O> getAsync(Predicate<O> query);

    CompletableFuture<O> getAsync(Predicate<O> query, Supplier<O> loader);


    /**
     * Insert a object in the cache.
     *
     * @param object The object to insert
     */
    void insert(O object);

    void insertAsync(O object);


    /**
     * Remove a object with a registered query.
     *
     * @param queryName The name of the query
     * @param identifiers The identifiers for your object
     * @return The removed object (Can be null)
     */
    O remove(String queryName,Object... identifiers);

    /**
     * Remove a object with a direct query.
     *
     * <p>We recommend to use a registered query (It is faster).</p>
     *
     * @param query The search query
     * @return The object (Can be null)
     */
    O remove(Predicate<O> query);

    /**
     * Remove a defined object.
     *
     * @param cachedObject The cached object.
     * @return If the remove was successfully
     */
    boolean remove(O cachedObject);

    CompletableFuture<O> removeAsync(String queryName,Object... identifiers);

    CompletableFuture<O> removeAsync(Predicate<O> query);

    CompletableFuture<Boolean> removeAsync(O cachedObject);


    /**
     * Set the maximum size of a Cache
     *
     * <p>If the maximum size is reached, the oldest object will be automatically removed</p>
     *
     * @param maxSize The size
     * @return The current cache
     */
    Cache<O> setMaxSize(int maxSize);

    /**
     * Set the refresh time.
     *
     * <p>A refresh time is the maximum lifecycle of an object.</p>
     *
     * @param refreshTime The time
     * @param unit The unit of the time
     * @return The current cache
     */
    Cache<O> setRefresh(long refreshTime, TimeUnit unit);

    /**
     * Set the expire time.
     *
     * <p>If an object is longer then the expire time is not used, it will be removed</p>
     *
     * @param expireTime The time
     * @param unit The unit of the time
     * @return The current cache
     */
    Cache<O> setExpire(long expireTime, TimeUnit unit);

    /**
     * Set the expire time after an access.
     *
     * <p>If an object is longer then the expire time is not used, it will be removed</p>
     *
     * @param expireTime The time
     * @param unit The unit of the time
     * @return The current cache
     */
    Cache<O> setExpireAfterAccess(long expireTime, TimeUnit unit);

    /**
     * Set a remove listener.
     *
     * <p>This listener is on every remove called.</p>
     *
     * @param onRemove The remove listener.
     * @return The current cache
     */
    Cache<O> setRemoveListener(Consumer<O> onRemove);

    /**
     * Register a static search query and object loader.
     *
     * @param name The name of the query
     * @param query The query
     * @return The current cache
     */
    Cache<O> registerQuery(String name, CacheQuery<O> query);

    /**
     * Unregister a registered query.
     *
     * @param name The query name
     * @return The current cache
     */
    Cache<O> unregisterQuery(String name);

    /**
     * Clear this cache.
     */
    void clear();

    /**
     * Shut this cache down (Clear and stop all tasks).
     */
    void shutdown();

}
