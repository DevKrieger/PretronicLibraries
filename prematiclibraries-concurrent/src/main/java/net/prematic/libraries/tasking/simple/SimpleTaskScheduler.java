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

package net.prematic.libraries.tasking.simple;

import net.prematic.libraries.tasking.Task;
import net.prematic.libraries.tasking.TaskScheduler;
import net.prematic.libraries.tasking.TaskState;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleTaskScheduler implements TaskScheduler {

    private final static long ZERO = 0;
    private final static TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;

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
    public Collection<Task> getTasks(TaskState state) {
        return Iterators.iterateAcceptedReturn(tasks.values(), task -> task.getState() == state);
    }

    @Override
    public Task getTask(int id) {
        return this.tasks.get(id);
    }

    @Override
    public Task createTask(ObjectOwner owner, String name) {
        return createTask(owner, name,ZERO,ZERO,DEFAULT_UNIT);
    }

    @Override
    public Task createLaterTask(ObjectOwner owner, String name, long delay, TimeUnit unit) {
        return createTask(owner, name,delay,ZERO,unit);
    }

    @Override
    public Task createScheduleTask(ObjectOwner owner, String name, long period, TimeUnit unit) {
        return createTask(owner, name,ZERO,period,unit);
    }

    @Override
    public Task createTask(ObjectOwner owner, String name, long delay, long period, TimeUnit unit) {
        return createTask(owner, name, delay, period, unit,false);
    }

    @Override
    public Task createAsynchronouslyTask(ObjectOwner owner, String name) {
        return createAsynchronouslyTask(owner, name,ZERO,ZERO,DEFAULT_UNIT);
    }

    @Override
    public Task createAsynchronouslyLaterTask(ObjectOwner owner, String name, long delay, TimeUnit unit) {
        return createAsynchronouslyTask(owner, name,delay,ZERO,unit);
    }

    @Override
    public Task createAsynchronouslyScheduleTask(ObjectOwner owner, String name, long period, TimeUnit unit) {
        return createAsynchronouslyTask(owner, name,ZERO,period,unit);
    }

    @Override
    public Task createAsynchronouslyTask(ObjectOwner owner, String name, long delay, long period, TimeUnit unit) {
        return createTask(owner, name, delay, period, unit,true);
    }

    @Override
    public Task createTask(ObjectOwner owner, String name, long delay, long period, TimeUnit unit, boolean async) {
        Task task = new MultipleTask(this,owner,taskIdManager.getAndIncrement(),name,unit.toMillis(delay),unit.toMillis(period),async);
        this.tasks.put(task.getID(),task);
        return task;
    }

    @Override
    public Task runTaskLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit) {
        return runTask(owner,runnable,delay,ZERO,unit);
    }

    @Override
    public Task scheduleTask(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit) {
        return runTask(owner,runnable,ZERO,period,unit);
    }

    @Override
    public Task runTask(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit) {
        return runTask(owner, runnable, delay, period, unit,false);
    }

    @Override
    public Task runAsynchronouslyTask(ObjectOwner owner, Runnable runnable) {
        return runTask(owner,runnable,ZERO,ZERO,DEFAULT_UNIT);
    }

    @Override
    public Task runAsynchronouslyTaskLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit) {
        return runTask(owner,runnable,delay,ZERO,unit);
    }

    @Override
    public Task scheduleAsynchronouslyTask(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit) {
        return runTask(owner,runnable,ZERO,period,unit);
    }

    @Override
    public Task runAsynchronouslyTask(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit) {
        return runTask(owner, runnable, delay, period, unit,true);
    }

    @Override
    public Task runTask(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit, boolean async) {
        Task task = new SingletonFinalTask(this,owner,taskIdManager.getAndIncrement(),null,unit.toMillis(delay),unit.toMillis(period),async,runnable);
        this.tasks.put(task.getID(),task);
        task.start();
        return task;
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
        Iterators.iterateAcceptedForEach(tasks.values(), task -> task.getOwner().equals(owner), Task::stop);
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
        Iterators.iterateAcceptedForEach(tasks.values(), task -> task.getOwner().equals(owner), Task::destroy);
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
}
