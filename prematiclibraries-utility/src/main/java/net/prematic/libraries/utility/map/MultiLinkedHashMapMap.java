package net.prematic.libraries.utility.map;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

public class MultiLinkedHashMapMap<K,V> {

    private Map<Integer,Map<K,V>> maps;

    public MultiLinkedHashMapMap() {
        this.maps = new LinkedHashMap<>();
    }
    public Map<Integer, Map<K, V>> getMaps() {
        return this.maps;
    }
    public V get(K key){
        for(Integer mapId : maps.keySet()){
            V value = get(mapId,key);
            if(value != null) return value;
        }
        return null;
    }
    public V get(int mapId, K key){
        Map<K,V> map = this.maps.get(mapId);
        if(map != null) map.get(key);
        return null;
    }
    public Boolean containsKey(int mapId,K key){
        Map<K,V> map = this.maps.get(mapId);
        if(map != null) return map.containsKey(key);
        return false;
    }
    public Boolean containsKey(K key){
        for(Integer mapId : maps.keySet()) if(containsKey(mapId,key)) return true;
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
