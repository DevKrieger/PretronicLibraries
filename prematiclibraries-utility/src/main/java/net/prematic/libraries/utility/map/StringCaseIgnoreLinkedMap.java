package net.prematic.libraries.utility.map;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 23.11.18 20:41
 *
 */

import java.util.LinkedHashMap;

public class StringCaseIgnoreLinkedMap<Object> extends LinkedHashMap<String, Object> {

    @Override
    public Object get(java.lang.Object key) {
        if(!(key instanceof String)) throw new IllegalArgumentException("StringCaseIgnoreLinkedMap supports only String as key");
        return super.get(((String) key).toLowerCase());
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key.toLowerCase(), value);
    }
}