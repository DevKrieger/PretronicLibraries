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

import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The simple implementation of the event api.
 */
public class SimpleEventManager implements EventManager {

    private final Executor executor;
    private final Map<Class<?>,List<MethodEntry>> methods;

    public SimpleEventManager() {
        this(Executors.newCachedThreadPool());
    }

    public SimpleEventManager(Executor asyncExecutor) {
        this.executor = asyncExecutor;
        this.methods = new ConcurrentHashMap<>();
    }

    public void registerListener(ObjectOwner owner, Object listener){
        if(owner == null || listener == null) throw new NullPointerException("Owner or listener is null.");

        //Search every listener method in the listener object.
        for(Method method : listener.getClass().getDeclaredMethods()){
            try{
                Listener info = method.getAnnotation(Listener.class);
                if(info != null){
                    if(method.getParameterTypes().length == 1){
                        List<MethodEntry> methods = this.methods.computeIfAbsent(method.getParameterTypes()[0], k -> new ArrayList<>());
                        methods.add(new MethodEntry(info.priority(),owner,listener,method));
                        //Sort all listeners by the priority.
                        methods.sort((o1, o2) -> o1.getPriority() >= o2.getPriority()?0:-1);
                    }
                }
            }catch (Exception exception){
                throw new IllegalArgumentException("Could not register listener "+listener,exception);
            }
        }
    }

    public void unregisterListener(Object listener){
        for(List<MethodEntry> holders : this.methods.values()) Iterators.remove(holders, method -> method.getListener().equals(listener));
    }

    public void unregisterListener(ObjectOwner owner){
        for(List<MethodEntry> holders : this.methods.values()) Iterators.remove(holders, method -> method.getOwner().equals(owner));
    }

    public void callEventAsync(Object event){
        if(executor != null) executor.execute(() -> callEvent(event));
        else callEvent(event);
    }

    public void callEvent(Object event){
        Collection<MethodEntry> methods = this.methods.get(event.getClass());
        if(methods != null) methods.forEach(method -> method.invoke(event));
    }

    /**
     * Contains information about a listener and when this method should be executed.
     */
    public class MethodEntry {

        private final int priority;
        private final ObjectOwner owner;
        private final Object listener;
        private final Method method;

        public MethodEntry(int priority, ObjectOwner owner, Object listener, Method methode) {
            this.priority = priority;
            this.owner = owner;
            this.listener = listener;
            this.method = methode;
        }

        public ObjectOwner getOwner() {
            return this.owner;
        }

        public int getPriority() {
            return this.priority;
        }

        public Object getListener() {
            return this.listener;
        }

        public Method getMethod() {
            return this.method;
        }

        public void invoke(Object event){
            try{
                this.method.invoke(this.listener,event);
            }catch (Exception exception){
                throw new EventException("Could not execute listener "+listener,exception);
            }
        }
    }
}
