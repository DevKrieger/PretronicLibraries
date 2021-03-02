/*
 * (C) Copyright 2021 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.02.21, 10:09
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

package net.pretronic.libraries.event.execution;

import net.pretronic.libraries.event.executor.EventExecutor;
import net.pretronic.libraries.event.network.EventOrigin;
import net.pretronic.libraries.utility.SystemUtil;

import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

public class SyncEventExecution implements EventExecution{

    private final EventOrigin origin;
    private volatile boolean waitingForCompletion;
    private final BiConsumer<Throwable,Object> exceptionHandler;

    public SyncEventExecution(EventOrigin origin,Iterator<EventExecutor> executors, Executor executor, Object[] events,BiConsumer<Throwable,Object> exceptionHandler) {
        this.origin = origin;
        this.waitingForCompletion = false;
        this.exceptionHandler = exceptionHandler;
        execute(executors,executor,events);
    }

    @Override
    public EventOrigin getOrigin() {
        return origin;
    }

    private void execute(Iterator<EventExecutor> executors, Executor executor0, Object[] events){
        while(executors.hasNext()){
            EventExecutor executor = executors.next();
            if(executor.getExecutionType() == ExecutionType.BLOCKING){
                executor.execute(this,events);
            }else if(executor.getExecutionType() == ExecutionType.NON_BLOCKING){
                this.waitingForCompletion = true;
                executor.execute(this,events);
                SystemUtil.sleepAsLong(() -> waitingForCompletion);
            }else if(executor.getExecutionType() == ExecutionType.ASYNC){
                executor0.execute(() -> executor.execute(this,events));
            }
        }
    }

    public void complete(){
        if(waitingForCompletion){
            waitingForCompletion = false;
        }else {
            throw new IllegalArgumentException("Event execution is not non blocking");
        }
    }

    @Override
    public void throwException(Throwable exception, Object location) {
        if(exceptionHandler != null) exceptionHandler.accept(exception,location);
        else{
            System.out.println("Could not execute subscription "+location);
            exception.printStackTrace();
        }
    }
}
