/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.08.19, 18:38
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

import net.prematic.libraries.event.executor.ConsumerEventExecutor;
import net.prematic.libraries.event.executor.EventExecutor;
import net.prematic.libraries.event.executor.MethodEventExecutor;
import net.prematic.libraries.utility.GeneralUtil;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class DefaultEventManager implements EventManager{

    private final Executor executor;
    private final Map<Class<?>, List<EventExecutor>> executors;

    public DefaultEventManager() {
        this(GeneralUtil.getDefaultExecutorService());
    }

    public DefaultEventManager(Executor executor) {
        this.executor = executor;

        this.executors = new HashMap<>();
    }

    @Override
    public void subscribe(ObjectOwner owner, Object listener) {
        Objects.requireNonNull(owner,"Owner can't be null.");
        Objects.requireNonNull(listener,"Listener can't be null.");

        //Search every listener method in the listener object.
        for(Method method : listener.getClass().getDeclaredMethods()){
            try{
                Listener info = method.getAnnotation(Listener.class);
                if(info != null && method.getParameterTypes().length == 1){
                    List<EventExecutor> executors = this.executors.computeIfAbsent(method.getParameterTypes()[0], k -> new ArrayList<>());
                    executors.add(new MethodEventExecutor(owner,info.priority(),listener,method));
                    sort(executors);
                }
            }catch (Exception exception){
                throw new IllegalArgumentException("Could not register listener "+listener,exception);
            }
        }
    }

    @Override
    public <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler, byte priority) {
        Objects.requireNonNull(owner,"Owner can't be null.");
        Objects.requireNonNull(eventClass,"Event type can't be null.");
        Objects.requireNonNull(handler,"Handler can't be null.");

        List<EventExecutor> executors = this.executors.computeIfAbsent(eventClass, k -> new ArrayList<>());
        executors.add(new ConsumerEventExecutor<>(owner,priority,handler));
        sort(executors);
    }

    @Override
    public void unsubscribe(Object listener) {
        executors.forEach((event, executors) -> Iterators.removeSilent(executors,
                executor -> executor instanceof MethodEventExecutor
                        && ((MethodEventExecutor) executor).getListener().equals(listener)));
    }

    @Override
    public void unsubscribe(Consumer<?> handler) {
        executors.forEach((event, executors) -> Iterators.removeSilent(executors,
                executor -> executor instanceof ConsumerEventExecutor
                        && ((ConsumerEventExecutor) executor).getConsumer().equals(handler)));
    }

    @Override
    public void unsubscribe(ObjectOwner owner) {
        executors.forEach((event, executors) -> Iterators.removeSilent(executors, executor -> executor.getOwner().equals(owner)));
    }

    @Override
    public void unsubscribeAll(Class<?> eventClass) {
        this.executors.remove(eventClass);
    }

    @Override
    public <T> T callEvent(T event) {
        List<EventExecutor> executors = this.executors.get(event.getClass());
        executors.forEach(executor -> executor.execute(event));
        return event;
    }

    @Override
    public <T> void callEventAsync(T event, Consumer<T> callback) {
        if(callback != null) executor.execute(()-> callback.accept(callEvent(event)));
        else executor.execute(()-> callEvent(event));
    }

    @Override
    public <T> CompletableFuture<T> callEventAsync(T event) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executor.execute(()->{
            try{
                future.complete(callEvent(event));
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    private void sort(List<EventExecutor> executors){
        //Sort all listeners by the priority.
        executors.sort((o1, o2) -> o1.getPriority() >= o2.getPriority()?0:-1);
    }
}
