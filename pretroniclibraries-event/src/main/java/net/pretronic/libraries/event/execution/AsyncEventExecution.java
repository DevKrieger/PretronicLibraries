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

import java.util.Iterator;
import java.util.concurrent.Executor;

public class AsyncEventExecution implements EventExecution{

    private final EventOrigin origin;
    private final Iterator<EventExecutor> executors;
    private final Executor executor;
    private final Runnable callback;
    private final Object[] events;
    private boolean waitingForCompletion;

    public AsyncEventExecution(EventOrigin origin,Iterator<EventExecutor> executors, Executor executor, Object[] events,Runnable callback) {
        this.origin = origin;
        this.executors = executors;
        this.executor = executor;
        this.callback = callback;
        this.events = events;
        this.waitingForCompletion = false;
        executor.execute(this::executeNext);
    }

    @Override
    public EventOrigin getOrigin() {
        return origin;
    }

    private void executeNext(){
        if(executors.hasNext()){
            EventExecutor executor = executors.next();
            if(executor.getExecutionType() == ExecutionType.BLOCKING){
                executor.execute(this,events);
                executeNext();
            }else if(executor.getExecutionType() == ExecutionType.NON_BLOCKING){
                waitingForCompletion = true;
                executor.execute(this,events);
            }else if(executor.getExecutionType() == ExecutionType.ASYNC){
                this.executor.execute(() -> executor.execute(this,events));
                executeNext();
            }
        }else{
            this.callback.run();
        }
    }

    public void complete(){
        if(waitingForCompletion){
            executeNext();
            waitingForCompletion = false;
        }else {
            throw new IllegalArgumentException("Event execution is not non blocking");
        }
    }
}
