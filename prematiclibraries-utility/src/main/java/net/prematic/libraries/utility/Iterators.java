package net.prematic.libraries.utility;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.03.19 23:00
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.*;

public final class Iterators {

    //Find

    public static <U> U findOne(Iterable<U> list, Predicate<U> acceptor) {
        Iterator<U> iterator = list.iterator();
        U result;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) return result;
        return null;
    }

    public static <U> U findOneOrWhenNull(Iterable<U> list, Predicate<U> acceptor, Supplier<U> whenNull) {
        Iterator<U> iterator = list.iterator();
        U result;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) return result;
        return whenNull.get();
    }

    public static <U> void forEach(Iterable<U> list, Consumer<U> forEach){
        Iterator<U> iterator = list.iterator();
        U result;
        while(iterator.hasNext() && (result=iterator.next()) != null) forEach.accept(result);
    }

    public static <U> void forEach(Iterable<U> list, Consumer<U> forEach, Predicate<U> acceptor) {
        Iterator<U> iterator = list.iterator();
        U result;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) forEach.accept(result);
    }

    public static <U> void forEachIndexed(Iterable<U> list, BiConsumer<U, Integer> forEach) {
        Iterator<U> iterator = list.iterator();
        U result;
        int index = 0;
        while(iterator.hasNext() && (result=iterator.next()) != null) {
            forEach.accept(result, index);
            index++;
        }
    }

    public static <U> List<U> filter(Iterable<U> list, Predicate<U> acceptor){
        List<U> result = new ArrayList<>();
        forEach(list,result::add,acceptor);
        return result;
    }

    //Map

    public static <U,R> List<R> map(Iterable<U> list, Function<U,R> mapper){
        List<R> result = new ArrayList<>();
        forEach(list, value -> {
            R object = mapper.apply(value);
            if(object != null) result.add(object);
        });
        return result;
    }

    public static <U,R> List<R> map(Iterable<U> list, Function<U,R> mapper,Predicate<U> acceptor){
        List<R> result = new ArrayList<>();
        forEach(list, value ->{
            R object = mapper.apply(value);
            if(object != null) result.add(object);
        },acceptor);
        return result;
    }

    public static <U,R> R mapOne(Iterable<U> list,Predicate<U> acceptor, Function<U,R> wrapper){
        U result = findOne(list,acceptor);
        if(result != null) return wrapper.apply(result);
        return null;
    }

    public static <U, R> void map(Iterable<U> list, Collection<R> source, Function<U, R> mapper) {
        forEach(list, (value) -> {
            R object = mapper.apply(value);
            if (object != null) {
                source.add(object);
            }
        });
    }

    public static <U, R> void map(Iterable<U> list, R[] source, Function<U, R> mapper) {
        forEachIndexed(list, (value, index) -> {
            R object = mapper.apply(value);
            if (object != null) {
                source[index] = object;
            }
        });
    }

    //Remove

    public static <U> List<U> remove(Iterable<U> list, Predicate<U> acceptor){
        List<U> removed = new ArrayList<>();
        Iterator<U> iterator = list.iterator();
        U result;
        while(iterator.hasNext() && (result=iterator.next()) != null){
            if(acceptor.test(result)){
                iterator.remove();
                removed.add(result);
            }
        }
        return removed;
    }

    public static <U> U removeOne(Iterable<U> list, Predicate<U> acceptor){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null){
            if(acceptor.test(result)){
                iterator.remove();
                return result;
            }
        }
        return result;
    }
    public static <U> void removeSilent(Iterable<U> list, Predicate<U> acceptor){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) iterator.remove();
    }

}
