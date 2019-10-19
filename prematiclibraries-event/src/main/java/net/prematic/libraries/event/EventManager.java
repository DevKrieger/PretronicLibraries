/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.19 21:01
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

package net.prematic.libraries.event;

import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface EventManager {

    void subscribe(ObjectOwner owner, Object listener);

    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler){
        subscribe(owner, eventClass, handler,EventPriority.NORMAL);
    }

    <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler, byte priority);

    void unsubscribe(Object listener);

    void unsubscribe(Consumer<?> handler);

    void unsubscribe(ObjectOwner owner);

    void unsubscribeAll(Class<?> eventClass);


    default <T> T callEvent(T event){
        return callEvent((Class<T>) event.getClass(),event);
    }

    default <T> void callEventAsync(T event, Consumer<T> callback){
        callEventAsync((Class<T>) event.getClass(),event,callback);
    }

    default <T> CompletableFuture<T> callEventAsync(T event){
        return callEventAsync((Class<T>) event.getClass(),event);
    }


    <T,E extends T> E callEvent(Class<T> executionClass,E event);

    <T,E extends T> void callEventAsync(Class<T> executionClass,E event, Consumer<T> callback);

    <T,E extends T>  CompletableFuture<T> callEventAsync(Class<T> executionClass,E event);
}
