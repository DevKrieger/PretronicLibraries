/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 30.11.20, 21:14
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

package net.pretronic.libraries.utility.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GenericWrappedList<V,VO extends V> implements List<V> {

    private final List reference;

    public GenericWrappedList(List<VO> reference) {
        this.reference = reference;
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
    public boolean contains(Object o) {
        return this.reference.contains(o);
    }

    @Override
    public Iterator<V> iterator() {
        return this.reference.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.reference.toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) this.reference.toArray(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(V v) {
        return this.reference.add(v);
    }

    @Override
    public boolean remove(Object o) {
        return this.reference.remove(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(Collection<?> c) {
        return this.reference.containsAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends V> c) {
        return this.reference.addAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(int index, Collection<? extends V> c) {
        return this.reference.addAll(index,c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection<?> c) {
        return this.reference.removeAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(Collection<?> c) {
        return this.reference.retainAll(c);
    }

    @Override
    public void clear() {
        this.reference.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(int index) {
        return (V) this.reference.get(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V set(int index, V element) {
        return (V) this.reference.set(index,element);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, V element) {
        this.reference.add(index, element);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(int index) {
        return (V) this.reference.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.reference.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.reference.lastIndexOf(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator<V> listIterator() {
        return this.reference.listIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator<V> listIterator(int index) {
        return this.reference.listIterator(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        return this.reference.subList(fromIndex, toIndex);
    }
}
