package net.prematic.libraries.utility.map;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 23.11.18 20:41
 *
 */

import java.util.LinkedHashMap;

public class StringCaseIgnoreLinkedMap<O> extends LinkedHashMap<String,O> {

    @Override
    public O get(Object key) {
        if(!(key instanceof String)) throw new IllegalArgumentException("StringCaseIgnoreLinkedMap supports only String as key");
        return super.get(((String) key).toLowerCase());
    }
    @Override
    public O put(String key, O value) {
        return super.put(key.toLowerCase(), value);
    }
}