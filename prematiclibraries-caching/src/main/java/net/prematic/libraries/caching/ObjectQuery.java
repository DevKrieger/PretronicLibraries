package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 17:15
 *
 */

public interface ObjectQuery<O> {

    public abstract boolean is(Object identifier, O object);

}
