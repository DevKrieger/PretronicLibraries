package net.prematic.libraries.utility.map;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 16:26
 *
 */

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class StringSplitMap<V> implements Map<String,V> {

    private LinkedHashMap<String,Map<String,V>> maps;

    public StringSplitMap() {
        this.maps = new LinkedHashMap<>();
    }

    @Override
    public int size() {
        int count = 0;
        for(Map map : this.maps.values()) count += map.size();
        return count;
    }
    @Override
    public boolean isEmpty() {
        return this.maps.isEmpty();
    }
    @Override
    public boolean containsKey(Object key) {
        Map map = getMap(key.toString());
        return (map != null?map.containsKey(key):false);
    }
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Can't search via value");
    }
    @Override
    public V get(Object key) {
        Map map = getMap(key.toString());
        return (map != null?(V)map.get(key):null);
    }
    @Override
    public V put(String key, V value) {
        Map<String,V> map = getMapAndCreate(key);
        map.put(key,value);
        return value;
    }
    @Override
    public V remove(Object key) {
        Map map = getMap(key.toString());
        if(map != null) return (V)map.remove(key);
        return null;
    }
    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        //for(Set entry : map.entrySet())
    }
    @Override
    public void clear() {
        this.maps.clear();
    }
    @Override
    public Set<String> keySet() {
        /*
        Set<String> sets = new LinkedHashSet<>();
        for(Set<String> set : this.maps.keySet()) sets.add(set)
        return sets;
         */
        return null;
    }
    @Override
    public Collection<V> values() {
        return null;
    }
    @Override
    public Set<Entry<String, V>> entrySet() {
        return null;
    }
    @Override
    public boolean equals(Object o) {
        return false;
    }
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return null;
    }
    @Override
    public void forEach(BiConsumer<? super String, ? super V> action) {

    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super V, ? extends V> function) {

    }

    @Override
    public V putIfAbsent(String key, V value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(String key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public V replace(String key, V value) {
        return null;
    }

    @Override
    public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(String key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }
    public Map<String,V> getMapAndCreate(String key){
        Map<String,V> map = getMap(key);
        if(map == null){
            map = new LinkedHashMap<>();
            this.maps.put(key.substring(0,1).toLowerCase(),map);
        }
        return map;
    }
    public Map<String,V> getMap(String key){
        return this.maps.get(key.substring(0,1).toLowerCase());
    }
}
