package net.prematic.libraries.caching.object;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 19:52
 *
 */

public interface CacheObjectLoader<O> {

    O load(Object identifier);

}
