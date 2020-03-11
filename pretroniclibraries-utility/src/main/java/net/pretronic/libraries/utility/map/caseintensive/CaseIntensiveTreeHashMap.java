/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.utility.map.caseintensive;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CaseIntensiveTreeHashMap<V> extends TreeMap<String,V> implements CaseIntensiveMap<V> {

    @Override
    public V get(Object key) {
        if(key instanceof String) return super.get(((String) key).toLowerCase());
        else throw KEY_NO_STRING_EXCEPTION;
    }

    @Override
    public boolean containsKey(Object key) {
        if(key instanceof String) return super.containsKey(((String) key).toLowerCase());
        else throw KEY_NO_STRING_EXCEPTION;
    }

    @Override
    public V put(String key, V value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        m.forEach((BiConsumer<String, V>) (s, v) -> put(s.toLowerCase(),v));
    }

    @Override
    public V remove(Object key) {
        if(key instanceof String) return super.remove(((String) key).toLowerCase());
        else throw KEY_NO_STRING_EXCEPTION;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        if(key instanceof String) return super.getOrDefault(((String) key).toLowerCase(), defaultValue);
        else throw KEY_NO_STRING_EXCEPTION;
    }

    @Override
    public V putIfAbsent(String key, V value) {
        return super.putIfAbsent(key.toLowerCase(), value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        if(key instanceof String) return super.remove(((String) key).toLowerCase(),value);
        else throw KEY_NO_STRING_EXCEPTION;
    }

    @Override
    public boolean replace(String key, V oldValue, V newValue) {
        return super.replace(key.toLowerCase(), oldValue, newValue);
    }

    @Override
    public V replace(String key, V value) {
        return super.replace(key.toLowerCase(), value);
    }

    @Override
    public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
        return super.computeIfAbsent(key.toLowerCase(), mappingFunction);
    }

    @Override
    public V computeIfPresent(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return super.computeIfPresent(key.toLowerCase(), remappingFunction);
    }

    @Override
    public V compute(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return super.compute(key.toLowerCase(), remappingFunction);
    }

    @Override
    public V merge(String key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return super.merge(key.toLowerCase(), value, remappingFunction);
    }
}
