package net.prematic.libraries.caching;

import net.prematic.libraries.caching.object.CacheObjectLoader;
import net.prematic.libraries.caching.object.CacheObjectQuery;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public interface Cache<O> {

    Collection<O> getObjects();

    O get(String queryName, Object identifier);

    Future<O> getAsync(String queryName, Object identifier);

    int size();

    O insert(O object);

    O remove(String queryName, Object identifier);

    O removeFirst();

    void remove(O cachedObject);

    Cache<O> setMaxSize(int maxSize);

    Cache<O> setExpire(long expireTime, TimeUnit unit);

    Cache<O> setRemoveListener(Consumer<O> onRemove);

    Cache<O> registerQuery(String name, CacheObjectQuery<O> query);

    Cache<O> registerLoader(String name, CacheObjectLoader<O> loader);

    Cache<O> unregisterQuery(String name);

    Cache<O> unregisterLoader(String name);

    void clear();

    void shutdown();

}
