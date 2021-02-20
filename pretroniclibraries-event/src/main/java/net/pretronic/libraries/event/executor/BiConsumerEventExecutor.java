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

import net.pretronic.libraries.event.execution.EventExecution;
import net.pretronic.libraries.event.execution.ExecutionType;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.function.BiConsumer;

public class BiConsumerEventExecutor<E> implements EventExecutor{

    private final ObjectOwner owner;
    private final byte priority;
    private final ExecutionType executionType;
    private final Class<?> allowedClass;
    private final BiConsumer<E,EventExecution> consumer;

    public BiConsumerEventExecutor(ObjectOwner owner, byte priority,ExecutionType executionType, Class<?> allowedClass, BiConsumer<E,EventExecution> consumer) {
        this.owner = owner;
        this.priority = priority;
        this.executionType = executionType;
        this.allowedClass = allowedClass;
        this.consumer = consumer;
    }

    @Override
    public byte getPriority() {
        return priority;
    }

    @Override
    public ExecutionType getExecutionType() {
        return executionType;
    }

    @Override
    public ObjectOwner getOwner() {
        return owner;
    }

    public BiConsumer<E,EventExecution> getConsumer() {
        return consumer;
    }

    @Override
    public void execute(EventExecution execution,Object... events) {
        for (Object event : events){
            if(allowedClass.isAssignableFrom(event.getClass())){
                try{
                    consumer.accept((E) event,execution);
                }catch (Exception exception){
                    System.out.println("Could not execute subscription "+consumer.getClass());
                    exception.printStackTrace();
                }
            }
        }
    }

}
