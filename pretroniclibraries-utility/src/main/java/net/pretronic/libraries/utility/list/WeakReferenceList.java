/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 01.05.20, 10:07
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

import net.pretronic.libraries.utility.Validate;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * The {@link WeakReference} list creates a weak reference to an object. If the garbage collector removes the object
 * it is automatically removed from the list.
 *
 * @param <E> The list type
 */
public class WeakReferenceList<E> implements List<E> {

    private WeakNode<E> first;
    private WeakNode<E> last;

    @Override
    public int size() {
        if(first == null) return 0;
        else{
            int count = 0;
            WeakNode<E> position = first;
            while (position != null){
                if(position.get() == null){
                    connect(position.previous,position.next);
                }else count++;
                position = position.next;
            }
            return count;
        }
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public boolean contains(Object item) {
        return indexOf(item) > 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new WeakIterator(first);
    }

    @Override
    public Object[] toArray() {
        return toDirectReferenceList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] type) {
        return toDirectReferenceList().toArray(type);
    }

    public List<E> toDirectReferenceList(){
        List<E> result = new ArrayList<>();
        WeakNode<E> position = first;
        while (position != null){
            E object = position.get();
            if(object == null) connect(position.previous,position.next);
            else result.add(object);
            position = position.next;
        }
        return result;
    }

    @Override
    public boolean add(E object) {
        Validate.notNull(object,"A week reference list does not support null objects");
        WeakNode<E> last = this.last;
        this.last = new WeakNode<>(object);
        if(last != null){
            last.next = this.last;
            this.last.previous = last;
        }else{
            this.first = this.last;
        }
        return true;
    }

    @Override
    public boolean remove(Object object) {
        Validate.notNull(object);
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        for (Object object : objects){
            if(!contains(object)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) add(element);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E element : c) add(index,element);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        for (Object object : objects) remove(object);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        WeakNode<E> position = first;
        while (position != null){
            if(position.get() == null || !objects.contains(position.get())){
                connect(position.previous,position.next);
            }
            position = position.next;
        }
        return true;
    }

    @Override
    public void clear() {
        this.first = null;
        this.last = null;
    }

    @Override
    public E get(int index) {
        WeakNode<E> result = findIndex(index);
        return result != null ? result.get() : null;
    }

    @Override
    public E set(int index, E element) {
        WeakNode<E> position = findIndex(index);
        if(position == null) return null;
        WeakNode<E> new0 = new WeakNode<>(element);
        connect(position.previous,new0);
        connect(new0,position.next);
        return position.get();
    }

    @Override
    public void add(int index, E element) {
        set(index,element);
    }

    @Override
    public E remove(int index) {
        WeakNode<E> node = findIndex(index);
        if(node == null) return null;
        connect(node.previous,node.next);
        return node.get();
    }

    @Override
    public int indexOf(Object object) {
        Validate.notNull(object);
        int index = 0;
        WeakNode<E> position = this.first;
        while (position != null){
            E result = position.get();
            if(result == null){
                connect(position.previous,position.next);
            }else if(result.equals(object)){
                return index;
            }else{
                index++;
            }
            position = position.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object object) {
        Validate.notNull(object);
        int best = -1;
        int index = 0;
        WeakNode<E> position = this.first;
        while (position != null){
            E result = position.get();
            if(result == null){
                connect(position.previous,position.next);
            }else if(result.equals(object)){
                best = index;
            }else{
                index++;
            }
            position = position.next;
        }
        return best;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new WeakIterator(first);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        WeakNode<E> position = findIndex(index);
        if(position == null) throw new IndexOutOfBoundsException();
        return new WeakIterator(position);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return toDirectReferenceList().subList(fromIndex, toIndex);
    }

    private WeakNode<E> findIndex(int index0){
        int index = 0;
        WeakNode<E> position = first;
        while (position != null){
            if(position.get() == null){
                connect(position.previous,position.next);
            }else if(index == index0){
                return position;
            }else{
                index++;
            }
            position = position.next;
        }
        return null;
    }

    private void connect(WeakNode<E> previous,WeakNode<E> next){
        if(previous == null && next == null){
            first = null;
            last = null;
        }else if(previous == null){
            first = next;
            next.previous = null;
        }else if(next == null){
            last = previous;
            last.next = null;
        }else{
            previous.next = next;
            next.previous = previous;
        }
    }

    private static class WeakNode<E> extends WeakReference<E> {

        public WeakNode<E> next;
        public WeakNode<E> previous;

        public WeakNode(E referent) {
            super(referent);
        }
    }

    private class WeakIterator implements ListIterator<E> {

        private WeakNode<E> position;
        private int index;
        private E next;
        private E previous;

        public WeakIterator(WeakNode<E> position) {
            this.position = position;
            findNext();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            E next = this.next;
            if(next == null) throw new IndexOutOfBoundsException();
            findNext();
            return next;
        }

        @Override
        public boolean hasPrevious() {
            return previous != null;
        }

        @Override
        public E previous() {
            E result =  previous;
            if(previous == null) throw new IndexOutOfBoundsException();
            findPrevious();
            return result;
        }

        @Override
        public int nextIndex() {
            return index++;
        }

        @Override
        public int previousIndex() {
            return index-1;
        }

        @Override
        public void remove() {
            connect(position.previous,position.next);
        }

        @Override
        public void set(E element) {
            WeakNode<E> new0 = new WeakNode<>(element);
            connect(position.previous,new0);
            connect(new0,position.next);
            position = new0;
        }

        @Override
        public void add(E element) {
            set(element);
        }

        private void findNext(){
            while (position != null){
                index = 0;
                E next = position.get();
                if(next == null){
                    connect(position.previous,position.next);
                }else {
                    this.previous = this.next;
                    this.next = next;
                    position = position.next;
                    index++;
                    return;
                }
                position = position.next;
            }
            next = null;
        }
        private void findPrevious(){
            while (position != null){
                index = 0;
                E next = position.get();
                if(next == null){
                    connect(position.previous,position.next);
                }else {
                    this.previous = next;
                    this.next = this.previous;
                    position = position.previous;
                    index++;
                    return;
                }
                position = position.next;
            }
            next = null;
        }
    }
}
