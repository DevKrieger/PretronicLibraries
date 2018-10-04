package net.prematic.libraries.tasking.intern;

import net.prematic.libraries.tasking.Task;
import net.prematic.libraries.tasking.TaskOwner;
import net.prematic.libraries.tasking.TaskScheduler;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 13:15
 *
 */

public class PrematicTaskScheduler implements TaskScheduler {

    private final AtomicInteger nexttaskid;
    private final Map<Integer, Task> tasks;

    public PrematicTaskScheduler() {
        this.nexttaskid = new AtomicInteger();
        this.tasks = new LinkedHashMap<>();
    }
    public Collection<Task> getTasks() {
        return this.tasks.values();
    }

    @Override
    public Task runTaskAsync(TaskOwner owner, Runnable runnable) {
        return runTaskAsynchronously(owner,runnable);
    }

    @Override
    public Task runTaskAsynchronously(TaskOwner owner, Runnable runnable) {
        return runTaskAsynchronouslyLater(owner,runnable,0L,TimeUnit.SECONDS);
    }

    @Override
    public Task runTaskAsynchronouslyLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit) {
        return runTaskLater(owner,runnable,delay,unit,true);
    }

    @Override
    public Task scheduleAsynchronously(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit) {
        return schedule(owner,runnable,period,unit,true);
    }

    @Override
    public Task scheduleAsynchronously(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit) {
        return schedule(owner,runnable,delay,period,unit,true);
    }

    @Override
    public Task runTaskLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit) {
        return runTaskLater(owner,runnable,delay,unit,false);
    }

    @Override
    public Task runTaskLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit, boolean async) {
        return schedule(owner,runnable,delay,0L,unit,async);
    }

    @Override
    public Task schedule(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit) {
        return schedule(owner,runnable,period,unit,false);
    }

    @Override
    public Task schedule(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit, boolean async) {
        return schedule(owner,runnable,0L,period,unit,async);
    }

    @Override
    public Task schedule(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit) {
        return schedule(owner,runnable,delay,period,unit,false);
    }

    @Override
    public Task schedule(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit, boolean async) {
        PrematicTask task = new PrematicTask(this.nexttaskid.getAndIncrement(),unit.toMillis(delay)
                ,unit.toMillis(period),this,owner,runnable,async);
        owner.getExecutor().execute(task);
        return task;
    }
    public void cancelTask(int id) {
        if(this.tasks.containsKey(id)) cancelTask(this.tasks.get(id));
    }
    public void cancelTask(Task task) {
        this.tasks.remove(task.getID());
        task.setRunning(false);
    }
    public void cancelTask(TaskOwner owner) {
        for(Task task : new LinkedHashSet<>(this.tasks.values()))if(task.getOwner().equals(owner)) cancelTask(task);
    }
    public void cancelALL(){
        for(Task task : new LinkedHashSet<>(this.tasks.values())) cancelTask(task);
        this.tasks.clear();
    }
}
