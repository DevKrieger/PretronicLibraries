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

package net.pretronic.libraries.event.executor;

import net.pretronic.libraries.event.EventException;
import net.pretronic.libraries.event.network.EventOrigin;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.lang.reflect.Method;

public class MethodEventExecutor implements EventExecutor{

    private final ObjectOwner owner;
    private final byte priority;
    private final Object listener;
    private final Class<?> allowedClass;
    private final Method method;
    private final boolean withOrigin;
    private final boolean onlyRemote;
    private final boolean onlyLocal;

    public MethodEventExecutor(ObjectOwner owner, byte priority, Object listener,Class<?> allowedClass, Method method) {
        this(owner,priority,listener,allowedClass,method,false,false);
    }

    public MethodEventExecutor(ObjectOwner owner, byte priority, Object listener,Class<?> allowedClass, Method method,boolean onlyRemote,boolean onlyLocal) {
        this.owner = owner;
        this.priority = priority;
        this.listener = listener;
        this.allowedClass = allowedClass;
        this.method = method;

        this.withOrigin = method.getParameterCount() == 2;

        this.onlyLocal = onlyLocal;
        this.onlyRemote = onlyRemote;
    }

    @Override
    public byte getPriority() {
        return priority;
    }

    @Override
    public ObjectOwner getOwner() {
        return owner;
    }

    public Object getListener() {
        return listener;
    }

    @Override
    public void execute(EventOrigin origin,Object... events) {
        if(onlyRemote && !origin.isRemote()) return;
        if(onlyLocal && !origin.isLocal()) return;

        for (Object event : events) {
            if(allowedClass.isAssignableFrom(event.getClass())){
                try{
                    if(withOrigin) this.method.invoke(this.listener,event,origin);
                    else this.method.invoke(this.listener,event);
                }catch (Exception exception){
                    System.out.println("Could not execute subscription "+listener.getClass()+"#"+method.getName());
                    exception.printStackTrace();
                }
            }
        }
    }
}
