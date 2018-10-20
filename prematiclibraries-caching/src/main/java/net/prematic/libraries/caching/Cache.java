package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.10.18 19:40
 *
 */


import net.prematic.libraries.caching.defaults.PrematicCache;
import net.prematic.libraries.caching.object.CacheObjectLoader;
import net.prematic.libraries.caching.object.CacheObjectQuery;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Cache<O> {

    public List<O> getAll();

    public O get(String identifierName, Object identifier);

    public int size();

    public PrematicCache<O> setMaxSize(int maxSize);

    public PrematicCache<O> setExpire(long expireTime, TimeUnit unit);

    public void insert(O object);

    public O remove(String identifierName, Object identifier);

    public void registerQuery(String name, CacheObjectQuery<O> query);

    public void registerLoader(String name, CacheObjectLoader<O> loader);

    public void shutdown();

}
