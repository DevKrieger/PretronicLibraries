/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.19 10:56
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

package net.prematic.libraries.concurrent.simple;

import net.prematic.libraries.concurrent.Task;
import net.prematic.libraries.concurrent.TaskScheduler;
import net.prematic.libraries.concurrent.TaskState;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The simple implementation of the prematic task scheduler.
 */
public class SimpleTaskScheduler implements TaskScheduler {

    private final AtomicInteger taskIdManager;
    private final Map<Integer,Task> tasks;
    private final ExecutorService executor;

    public SimpleTaskScheduler() {
        this(Executors.newCachedThreadPool());
    }

    public SimpleTaskScheduler(ExecutorService executor) {
        this.executor = executor;
        this.taskIdManager = new AtomicInteger(1);
        tasks = new ConcurrentHashMap<>();
    }

    @Override
    public Collection<Task> getTasks() {
        return this.tasks.values();
    }

    @Override
    public Collection<Task> getTasks(String name) {
        return Iterators.filter(this.tasks.values(), task -> task.getName().equalsIgnoreCase(name));
    }

    @Override
    public Collection<Task> getTasks(TaskState state) {
        return Iterators.filter(tasks.values(), task -> task.getState() == state);
    }

    @Override
    public Task getTask(int id) {
        return this.tasks.get(id);
    }

    @Override
    public Builder createTask(ObjectOwner owner) {
        return new SimpleTaskBuilder(this,taskIdManager.getAndIncrement(),owner);
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
        Iterators.forEach(tasks.values(),Task::stop, task -> task.getOwner().equals(owner));
    }

    @Override
    public void stopAll() {
        tasks.values().forEach(Task::stop);
    }

    @Override
    public void unregister(int id) {
        Task task = getTask(id);
        if(task != null) unregister(task);
    }

    @Override
    public void unregister(Task task) {
        if(task.getState() == TaskState.DESTROYED) this.tasks.remove(task.getID());
        else task.destroy();
    }

    @Override
    public void unregister(ObjectOwner owner) {
        Iterators.forEach(tasks.values(), Task::destroy, task -> task.getOwner().equals(owner));
    }

    @Override
    public void unregisterAll() {
        tasks.values().forEach(Task::destroy);
    }

    @Override
    public void shutdown() {
        unregisterAll();
        this.executor.shutdown();
    }

    public class SimpleTaskBuilder implements TaskScheduler.Builder {

        private final int id;
        private final ObjectOwner owner;
        private final TaskScheduler scheduler;

        private String name;
        private long interval, delay;
        private boolean async;

        SimpleTaskBuilder(TaskScheduler scheduler,int id, ObjectOwner owner) {
            this.id = id;
            this.owner = owner;
            this.scheduler = scheduler;
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
            return new MultipleTask(this.scheduler,this.owner,this.id,this.name,this.delay,this.interval,this.async);
        }

        @Override
        public Task execute(Runnable runnable) {
            Task task = new SingletonFinalTask(this.scheduler,this.owner,this.id,this.name,this.delay,this.interval,this.async,runnable);
            task.start();
            return task;
        }
    }
}
