/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.concurrent;

import net.pretronic.libraries.concurrent.simple.TaskDestroyedException;
import net.pretronic.libraries.utility.Validate;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This is an abstract implementation of the task interface.
 */
public abstract class AbstractTask implements Task{

    protected final TaskScheduler scheduler;
    protected final ObjectOwner owner;
    protected final int id;
    protected final String name;
    protected final boolean async;

    protected long delay, period;
    protected TaskState state;
    protected Thread runningThread;

    protected Collection<Consumer<TaskFuture>> listeners;

    protected AbstractTask(TaskScheduler scheduler, ObjectOwner owner, int id, String name, long delay, long period, boolean async) {
        Validate.notNull(owner,scheduler);
        this.scheduler = scheduler;
        this.owner = owner;
        this.id = id;
        this.name = name!=null?name:"Task-"+id;
        this.delay = delay;
        this.period = period;
        this.async = async;
        this.listeners = ConcurrentHashMap.newKeySet();
        this.state = TaskState.STOPPED;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TaskState getState() {
        return this.state;
    }

    @Override
    public long getDelay() {
        return this.delay;
    }

    @Override
    public long getInterval() {
        return this.period;
    }

    @Override
    public ObjectOwner getOwner() {
        return this.owner;
    }

    @Override
    public TaskScheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public boolean isAsync() {
        return this.async;
    }

    @Override
    public boolean isRunning() {
        return state == TaskState.RUNNING;
    }

    @Override
    public Task setDelay(long delay, TimeUnit unit) {
        TaskDestroyedException.validate(this);
        this.delay = unit.toMillis(delay);
        return this;
    }

    @Override
    public Task setInterval(long period, TimeUnit unit) {
        TaskDestroyedException.validate(this);
        this.period = unit.toMillis(period);
        return this;
    }

    @Override
    public Task addListener(Consumer<TaskFuture> listener) {
        TaskDestroyedException.validate(this);
        this.listeners.add(listener);
        return this;
    }

    @Override
    public void start() {
        TaskDestroyedException.validate(this);
        if(state != TaskState.STOPPED) throw new IllegalArgumentException("Task "+name+" is already running.");
        scheduler.executeTask(this);
    }

    @SuppressWarnings("Use the start method to start a task (This is only for internal starting).")
    @Override
    public void run() {
        if(state == TaskState.RUNNING) throw new IllegalArgumentException("Task "+name+" is already running.");
        this.runningThread = Thread.currentThread();
        if(runningThread.getName().equals("main")) throw new IllegalArgumentException("It is not possible to run a task in the main thread.");
        state = TaskState.RUNNING;

        invokeListeners(null);

        if(delayTask()) return;

        while(state == TaskState.RUNNING && !Thread.interrupted()){
            execute();
            if(repeatTask()) return;
        }
        if(state != TaskState.STOPPED) stopInternal(null);
    }

    private void execute() {
        try{
            if(this.async) call();
            else{
                synchronized(this) {
                    call();
                }
            }
            state = TaskState.COMPLETED;
            invokeListeners(null);
        }catch (Throwable thrown){
            state = TaskState.FAILED;
            if(!invokeListeners(thrown)) thrown.printStackTrace();
        }
    }

    private boolean delayTask() {
        if(this.delay > 0){
            try {
                Thread.sleep(this.delay);
            }catch(InterruptedException exception) {
                Thread.currentThread().interrupt();
                if(state == TaskState.STOPPED) return true;
                state = TaskState.INTERRUPTED;
                stopInternal(exception);
                return true;
            }
        }
        return false;
    }

    private boolean repeatTask() {
        if(this.period > 0L){
            state = TaskState.RUNNING;
            try {
                Thread.sleep(this.period);
                if(state != TaskState.RUNNING) return true;
            }catch(InterruptedException exception) {
                Thread.currentThread().interrupt();
                state = TaskState.INTERRUPTED;
                stopInternal(exception);
                return true;
            }
        }else stopInternal(null);
        return false;
    }

    @Override
    public void stop() {
        if(state == TaskState.STOPPED || state == TaskState.DESTROYED) throw new IllegalArgumentException("Task "+name+" is already stopped.");
        state = TaskState.STOPPED;
        if(this.runningThread != null) runningThread.interrupt();
    }

    @Override
    public void destroy() {
        TaskDestroyedException.validate(this);
        stop();
        this.state = TaskState.DESTROYED;
        this.listeners.clear();
        this.scheduler.unregister(this);
    }

    protected boolean invokeListeners(Throwable thrown){
        if(listeners.isEmpty()) return false;
        TaskFuture future = new DefaultTaskFuture(this.state,thrown);
        listeners.forEach(listener -> listener.accept(future));
        return true;
    }

    protected void stopInternal(Throwable thrown){
        if(state != TaskState.COMPLETED) invokeListeners(thrown);
        state = TaskState.STOPPED;
        runningThread = null;
    }

    private static class DefaultTaskFuture implements TaskFuture {

        private final TaskState state;
        private final Throwable thrown;

        public DefaultTaskFuture(TaskState state, Throwable thrown) {
            this.state = state;
            this.thrown = thrown;
        }

        @Override
        public TaskState getState() {
            return state;
        }

        @Override
        public boolean isStarting() {
            return state == TaskState.RUNNING;
        }

        @Override
        public boolean isCompleted() {
            return state == TaskState.COMPLETED;
        }

        @Override
        public boolean isFailed() {
            return state == TaskState.FAILED;
        }

        @Override
        public boolean isInterrupted() {
            return state == TaskState.INTERRUPTED || state == TaskState.STOPPED || state == TaskState.DESTROYED;
        }

        @Override
        public Throwable getThrowable() {
            return thrown;
        }
    }
}
