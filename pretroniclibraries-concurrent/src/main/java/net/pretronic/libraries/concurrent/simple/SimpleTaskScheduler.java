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

package net.pretronic.libraries.concurrent.simple;

import net.pretronic.libraries.concurrent.Task;
import net.pretronic.libraries.concurrent.TaskScheduler;
import net.pretronic.libraries.concurrent.TaskState;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple implementation of the task scheduler.
 */
public class SimpleTaskScheduler implements TaskScheduler {

    private final AtomicInteger taskIdManager;
    private final Collection<Task> tasks;
    private final ExecutorService executor;

    public SimpleTaskScheduler() {
        this(Executors.newCachedThreadPool());
    }

    public SimpleTaskScheduler(ExecutorService executor) {
        this.executor = executor;
        this.taskIdManager = new AtomicInteger(1);
        tasks = ConcurrentHashMap.newKeySet();
    }

    @Override
    public Collection<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public Collection<Task> getTasks(String name) {
        return Iterators.filter(this.tasks, task -> task.getName().equalsIgnoreCase(name));
    }

    @Override
    public Collection<Task> getTasks(TaskState state) {
        return Iterators.filter(tasks, task -> task.getState() == state);
    }

    @Override
    public Task getTask(int id) {
        return Iterators.findOne(tasks, task -> task.getID() == id);
    }

    @Override
    public Builder createTask(ObjectOwner owner) {
        return new SimpleTaskBuilder(taskIdManager.getAndIncrement(),owner);
    }

    @Override
    public void executeTask(Task task) {
        this.executor.execute(task);
    }

    @Override
    public void stop(int id) {
        Task task = getTask(id);
        if(task != null) stop(task);
    }

    @Override
    public void stop(Task task) {
        task.stop();
    }

    @Override
    public void stop(ObjectOwner owner) {
        Iterators.forEach(tasks,Task::stop, task -> task.getOwner().equals(owner));
    }

    @Override
    public void stopAll() {
        tasks.forEach(Task::stop);
    }

    @Override
    public void unregister(int id) {
        Task task = getTask(id);
        if(task != null) unregister(task);
    }

    @Override
    public void unregister(Task task) {
        if(task.getState() == TaskState.DESTROYED) this.tasks.remove(task);
        else task.destroy();
    }

    @Override
    public void unregister(ObjectOwner owner) {
        Iterators.forEach(tasks, Task::destroy, task -> task.getOwner().equals(owner));
    }

    @Override
    public void unregisterAll() {
        tasks.forEach(Task::destroy);
    }

    @Override
    public void shutdown() {
        unregisterAll();
        this.executor.shutdown();
    }

    private class SimpleTaskBuilder implements TaskScheduler.Builder {

        private final int id;
        private final ObjectOwner owner;

        private String name;
        private long interval;
        private long delay;
        private boolean async;

        SimpleTaskBuilder(int id, ObjectOwner owner) {
            this.id = id;
            this.owner = owner;
        }

        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder async() {
            this.async = true;
            return this;
        }

        @Override
        public Builder sync() {
            this.async = false;
            return this;
        }

        @Override
        public Builder delay(long time, TimeUnit unit) {
            this.delay = unit.toMillis(time);
            return this;
        }

        @Override
        public Builder interval(long time, TimeUnit unit) {
            this.interval = unit.toMillis(time);
            return this;
        }

        @Override
        public Task create() {
            MultipleTask task =  new MultipleTask(SimpleTaskScheduler.this,this.owner,this.id,this.name,this.delay,this.interval,this.async);
            tasks.add(task);
            return task;
        }

        @Override
        public Task execute(Runnable runnable) {
            Task task = new SingletonFinalTask(SimpleTaskScheduler.this,this.owner,this.id,this.name,this.delay,this.interval,this.async,runnable);
            tasks.add(task);
            task.start();
            return task;
        }
    }
}
