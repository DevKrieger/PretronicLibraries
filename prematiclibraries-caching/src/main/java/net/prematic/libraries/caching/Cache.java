package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.10.18 19:40
 *
 */


import net.prematic.libraries.caching.object.CacheObjectLoader;
import net.prematic.libraries.caching.object.CacheObjectQuery;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
