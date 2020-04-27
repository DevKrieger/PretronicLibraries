/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.03.20, 17:04
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

package net.pretronic.libraries.message.bml.variable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class EmptyVariableSet implements VariableSet{

    private static EmptyVariableSet EMPTY = new EmptyVariableSet();

    private EmptyVariableSet(){};

    @Override
    public Set<Variable> getVariables() {
        return this;
    }

    @Override
    public ObjectVariable get(String name) {
        return null;
    }

    @Override
    public ObjectVariable getOrCreate(String name) {
        return null;//Unused
    }

    @Override
    public String getValue(String name) {
        return null;
    }

    @Override
    public VariableSet add(String name, Object source) {
        throw new UnsupportedOperationException("It is not possible to add a variable to a empty set.");
    }

    @Override
    public VariableSet remove(String name) {
        return this;//Unused in empty set (Nothing to add)
    }

    @Override
    public int size() {
        return 0;//Always 0
    }

    @Override
    public boolean isEmpty() {
        return true;//Always empty
    }

    @Override
    public boolean contains(Object o) {
        return false;//Always 0
    }

    @Override
    public Iterator<Variable> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) Array.newInstance(ObjectVariable.class,0);
    }

    @Override
    public boolean add(Variable variable) {
        throw new UnsupportedOperationException("It is not possible to add a variable to a empty set.");
    }

    @Override
    public boolean remove(Object o) {
        return false;//Nothing to remove
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;//Nothing to check
    }

    @Override
    public boolean addAll(Collection<? extends Variable> c) {
        throw new UnsupportedOperationException("It is not possible to add a variable to a empty set.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        //Nothing to clear
    }

    public static VariableSet newEmptySet(){
        return EMPTY;
    }
}
