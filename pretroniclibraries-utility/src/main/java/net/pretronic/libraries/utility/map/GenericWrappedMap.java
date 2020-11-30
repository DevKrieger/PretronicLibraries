/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 30.11.20, 21:06
 * @web %web%
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

package net.pretronic.libraries.utility.map;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GenericWrappedMap<K,V,KO extends K,VO extends V> implements Map<K,V> {

    private final Map reference;

    public GenericWrappedMap(Map<KO,VO> reference) {
        this.reference = reference;
    }

    @SuppressWarnings("unchecked")
    public Map<KO,VO> getReference() {
        return reference;
    }

    @Override
    public int size() {
        return this.reference.size();
    }

    @Override
    public boolean isEmpty() {
        return this.reference.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        return (V)this.reference.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        return (V) this.reference.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        return (V) this.reference.remove(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.reference.putAll(m);
    }

    @Override
    public void clear() {
        this.reference.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keySet() {
        return this.reference.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        return this.reference.values();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.reference.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || this.reference.equals(o)) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericWrappedMap<?, ?, ?, ?> that = (GenericWrappedMap<?, ?, ?, ?>) o;
        return Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return this.reference.hashCode();
    }
}
