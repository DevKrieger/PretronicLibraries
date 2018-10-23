package net.prematic.libraries.caching.object;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.10.18 14:08
 *
 */

public interface CacheObjectExpireDeletion<O> {

    public boolean delete(O object);

}
