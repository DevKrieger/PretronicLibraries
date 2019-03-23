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
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Iterators {

    public static <U> U iterateOne(Iterable<U> list, Predicate<U> acceptor) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) return result;
        return null;
    }

    public static <U> void iterateForEach(Iterable<U> list, Consumer<U> forEach){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) forEach.accept(result);
    }

    public static <U> void iterateAcceptedForEach(Iterable<U> list, Predicate<U> acceptor, Consumer<U> forEach) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) forEach.accept(result);
    }

    public static <U> List<U> iterateAcceptedReturn(Iterable<U> list, Predicate<U> acceptor){
        List<U> result = new ArrayList<>();
        iterateAcceptedForEach(list,acceptor,result::add);
        return result;
    }

    public static <U> void iterateAndRemove(Iterable<U> list, Predicate<U> acceptor){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptor.test(result)) iterator.remove();
    }

}
