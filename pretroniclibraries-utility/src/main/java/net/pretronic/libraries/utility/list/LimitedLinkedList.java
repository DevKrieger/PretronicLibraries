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

package net.pretronic.libraries.utility.list;

import java.util.Collection;
import java.util.LinkedList;

public class LimitedLinkedList<T> extends LinkedList<T> {

    //Todo finish extra methods

    private final int maxsize;

    public LimitedLinkedList(int maxsize) {
        this.maxsize = maxsize;
    }

    public LimitedLinkedList(Collection<T> collection, int maxsize) {
        super(collection);
        this.maxsize = maxsize;
    }

    public int getMaxSize() {
        return this.maxsize;
    }

    public boolean canEnter() {
        return super.size() < this.maxsize;
    }

    @Override
    public boolean add(T object) {
        if(canEnter()) return super.add(object);
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
    }

    @Override
    public void addFirst(T object) {
        if(canEnter()) super.addFirst(object);
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
    }

    @Override
    public void addLast(T object) {
        if(canEnter()) super.addLast(object);
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
    }

    @Override
    public boolean offer(T o) {
        if(canEnter()) return super.offer(o);
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
    }

    @Override
    public boolean offerFirst(T object) {
        if(canEnter()) return super.offerFirst(object);
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
    }

    @Override
    public boolean offerLast(T object) {
        if(canEnter()) return super.offerLast(object);
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for(T object : collection) if(!this.add(object)) return false;
        else throw new IllegalArgumentException("Reached limit of "+this.maxsize+" in limited list");
        return true;
    }
}
