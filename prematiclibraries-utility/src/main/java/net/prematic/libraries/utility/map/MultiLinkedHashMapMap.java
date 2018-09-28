package net.prematic.libraries.utility.map;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 12.09.18 12:47
 *
 */

import java.util.LinkedHashMap;
import java.util.Map;

public class MultiLinkedHashMapMap<K,V> {

    private Map<Integer,Map<K,V>> maps;

    public MultiLinkedHashMapMap() {
        this.maps = new LinkedHashMap<>();
    }
    public Map<Integer, Map<K, V>> getMaps() {
        return this.maps;
    }
    public V get(K key){
        for(Integer mapid : maps.keySet()){
            V value = get(mapid,key);
            if(value != null) return value;
        }
        return null;
    }
    public V get(int mapid, K key){
        Map<K,V> map = this.maps.get(mapid);
        if(map != null) map.get(key);
        return null;
    }
    public Boolean containsKey(int mapid,K key){
        Map<K,V> map = this.maps.get(mapid);
        if(map != null) return map.containsKey(key);
        return false;
    }
    public Boolean containsKey(K key){
        for(Integer mapid : maps.keySet()) if(containsKey(mapid,key)) return true;
        return false;
    }
    public Integer put(K key,V value){
        for(Map.Entry<Integer,Map<K,V>> entry : maps.entrySet()){
           if(entry.getValue().size() < 3000){
               entry.getValue().put(key,value);
               return entry.getKey();
           }
        }
        final int mapid = maps.size();
        maps.put(mapid,new LinkedHashMap<>());
        maps.get(mapid).put(key,value);
        return mapid;
    }
}
