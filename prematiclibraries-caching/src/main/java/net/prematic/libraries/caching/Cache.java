package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.10.18 19:40
 *
 */

import net.prematic.libraries.caching.loader.CacheLoader;

import java.util.concurrent.TimeUnit;

public interface Cache<Identifier1,Identifier2,Object> {

    public Object getByIdentifier1(Identifier1 identifier1);

    public Object getByIdentifier2(Identifier2 identifier2);

    public void setAutoDelete(Long time, TimeUnit unit);

    public void setLoader(CacheLoader loader);

    public void insert(Identifier1 identifier1,Identifier2 identifier2,Object object);

    public void removeByIdentifier1(Identifier1 identifier1);

    public void removeByIdentifier2(Identifier2 identifier2);

    public void updateByIdentifier1(Identifier1 identifier1, Object object);

    public void updateByIdentifier2(Identifier2 identifier2, Object object);

    public void updateIdentifier1(Identifier2 identifier2, Identifier1 newIdentifier1);

    public void updateIdentifier2(Identifier1 identifier1, Identifier2 newIdentifier2);

}
