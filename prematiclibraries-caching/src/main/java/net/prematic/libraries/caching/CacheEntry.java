package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 16:21
 *
 */

public class CacheEntry<O> {

    private long entered;
    private O object;

    public CacheEntry(long entered, O object) {
        this.entered = entered;
        this.object = object;
    }

    public long getEntered() {
        return entered;
    }

    public O getObject() {
        return object;
    }
}
