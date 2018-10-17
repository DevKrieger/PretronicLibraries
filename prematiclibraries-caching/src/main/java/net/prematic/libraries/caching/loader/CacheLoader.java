package net.prematic.libraries.caching.loader;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.10.18 19:48
 *
 */

public interface CacheLoader<Identifier1,Identifier2,Object> {

    public CacheLoadResult<Identifier1,Identifier2,Object> loadByIdentifier1(Identifier1 i);

    public CacheLoadResult<Identifier1,Identifier2,Object> loadByIdentifier2(Identifier2 i2);

}
