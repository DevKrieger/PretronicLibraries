/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.event;

import net.pretronic.libraries.event.execution.EventExecution;
import net.pretronic.libraries.event.execution.ExecutionType;
import net.pretronic.libraries.event.executor.EventExecutor;
import net.pretronic.libraries.event.network.EventOrigin;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface EventBus {

    void subscribe(ObjectOwner owner, Object listener);

    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler){
        subscribe(owner, eventClass, handler,EventPriority.NORMAL);
    }

    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler, byte priority){
        subscribe(owner, eventClass, handler,ExecutionType.BLOCKING, priority);
    }

    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler, ExecutionType type){
        subscribe(owner, eventClass, handler, type,EventPriority.NORMAL);
    }

    <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler, ExecutionType type, byte priority);


    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, BiConsumer<T,EventExecution> handler){
        subscribe(owner, eventClass, handler,EventPriority.NORMAL);
    }

    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, BiConsumer<T, EventExecution> handler, byte priority){
        subscribe(owner, eventClass, handler,ExecutionType.BLOCKING, priority);
    }

    default <T> void subscribe(ObjectOwner owner, Class<T> eventClass, BiConsumer<T, EventExecution> handler, ExecutionType type){
        subscribe(owner, eventClass, handler, type,EventPriority.NORMAL);
    }

    <T> void subscribe(ObjectOwner owner, Class<T> eventClass, BiConsumer<T, EventExecution> handler,ExecutionType type, byte priority);


    void unsubscribe(Object listener);

    void unsubscribe(Consumer<?> handler);

    void unsubscribe(BiConsumer<?,EventOrigin> handler);

    void unsubscribe(ObjectOwner owner);

    void unsubscribeAll(Class<?> eventClass);

    void addExecutor(Class<?> eventClass, EventExecutor executor);


    @SuppressWarnings("unchecked")
    default <T> T callEvent(T event){
        return callEvent((Class<T>) event.getClass(),event);
    }

    default <T,E extends T> E callEvent(Class<T> executionClass,E event){
        callEvents(executionClass, event);
        return event;
    }

    default <T> void callEvents(Class<T> executionClass,Object... events){
        callEvents(null,executionClass,events);
    }

    <T> void callEvents(EventOrigin origin,Class<T> executionClass, Object... events);


    @SuppressWarnings("unchecked")
    default <T> void callEventAsync(T event, Consumer<T> callback){
        callEventAsync((Class<T>) event.getClass(),event,callback);
    }

    default <T,E extends T> void callEventAsync(Class<T> executionClass,E event, Consumer<T> callback){
        callEventsAsync(executionClass,()-> callback.accept(event),event);
    }

    default <T> void callEventsAsync(Class<T> executionClass, Runnable callback,Object... events){
        callEventsAsync(null,executionClass,callback,events);
    }

    <T> void callEventsAsync(EventOrigin origin,Class<T> executionClass, Runnable callback,Object... events);


    @SuppressWarnings("unchecked")
    default <T> CompletableFuture<T> callEventAsync(T event){
        return callEventAsync((Class<T>) event.getClass(),event);
    }

    default <T,E extends T> CompletableFuture<T> callEventAsync(Class<T> executionClass,E event){
        CompletableFuture<T> future = new CompletableFuture<>();
        callEventsAsync(executionClass,()->{
            try{
                future.complete(event);
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        },event);
        return future;
    }


    default <T> CompletableFuture<Void> callEventsAsync(Class<T> executionClass,Object... events){
        CompletableFuture<Void> future = new CompletableFuture<>();
        callEventsAsync(executionClass,()->{
            try{
                future.complete(null);
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        },events);
        return future;
    }

    default <T> CompletableFuture<Void> callEventsAsync(EventOrigin origin,Class<T> executionClass,Object... events){
        CompletableFuture<Void> future = new CompletableFuture<>();
        callEventsAsync(origin,executionClass,()->{
            try{
                future.complete(null);
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        },events);
        return future;
    }

    Class<?> getMappedClass(Class<?> original);

    void registerMappedClass(Class<?> original, Class<?> mapped);
}
