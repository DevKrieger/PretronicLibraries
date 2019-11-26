/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 26.11.19, 19:40
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

package net.prematic.libraries.utility.map.callback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConcurrentCallbackMap<K,V> extends ConcurrentHashMap<K,V> implements CallbackMap<K,V>{

    private BiConsumer<K, V> putCallback, removeCallback;

    public ConcurrentCallbackMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ConcurrentCallbackMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ConcurrentCallbackMap() {
    }

    public ConcurrentCallbackMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public V put(K key, V value) {
        try{
            return super.put(key, value);
        }finally {
            onPut(key, value);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
        m.forEach(putCallback);
    }

    @Override
    public V remove(Object key) {
        V result =  super.remove(key);
        if(result != null) onRemove((K) key,result);
        return result;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V result = super.putIfAbsent(key, value);
        if(result != value) onPut(key,value);
        return result;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean result =  super.remove(key, value);
        if(result) onRemove((K) key, (V) value);
        return result;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean result =  super.replace(key, oldValue, newValue);
        if(result){
            onRemove(key,oldValue);
            onPut(key,newValue);
        }
        return result;
    }

    @Override
    public V replace(K key, V value) {
        V result =  super.replace(key, value);
        if(result != null) onRemove(key, result);
        onPut(key, value);
        return result;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return super.computeIfAbsent(key, k -> {
            V result = mappingFunction.apply(k);
            if(result != null) onPut(k,result);
            return result;
        });
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.computeIfPresent(key, (k, v) -> {
            V result = remappingFunction.apply(k,v);
            if(v != result){
                onRemove(k,v);
                onPut(k,result);
            }
            return result;
        });
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.compute(key, (k, v) -> {
            V result = remappingFunction.apply(k,v);
            if(result != v){
                if(v != null) onRemove(k,v);
                onPut(k,v);
            }
            return result;
        });
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return super.merge(key, value, (v, v2) -> {
            V result = remappingFunction.apply(v, v2);
            onRemove(key,v);
            onPut(key,result);
            return result;
        });
    }

    @Override
    public void setPutCallback(BiConsumer<K, V> consumer) {
        this.putCallback = consumer;
    }

    @Override
    public void setRemoveCallback(BiConsumer<K, V> consumer) {
        this.removeCallback = consumer;
    }

    private void onPut(K key, V value){
        if(putCallback != null) putCallback.accept(key, value);
    }

    private void onRemove(K key, V value){
        if(removeCallback != null) removeCallback.accept(key, value);
    }
}
