package net.prematic.libraries.caching.loader;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.10.18 19:48
 *
 */

public class CacheLoadResult<Identifier1,Identifier2,Object> {

    private Identifier1 identifier1;
    private Identifier2 identifier2;
    private Object object;

    public CacheLoadResult(Identifier1 identifier1, Identifier2 identifier2, Object object) {
        this.identifier1 = identifier1;
        this.identifier2 = identifier2;
        this.object = object;
    }
    public Identifier1 getIdentifier1() {
        return this.identifier1;
    }
    public Identifier2 getIdentifier2() {
        return this.identifier2;
    }
    public Object getObject() {
        return this.object;
    }
}
