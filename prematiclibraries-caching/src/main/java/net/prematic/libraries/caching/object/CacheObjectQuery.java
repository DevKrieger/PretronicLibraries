package net.prematic.libraries.caching.object;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 19:53
 *
 */

public interface CacheObjectQuery<O> {

    public abstract boolean is(Object identifier, O object);

}
