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
    public Task runTaskAsync(TaskOwner owner, Runnable runnable) {
        return runTaskAsynchronously(owner,runnable);
    }
    public Task runTaskAsynchronously(TaskOwner owner, Runnable runnable) {
        return runTaskLater(owner,runnable,0L,TimeUnit.MILLISECONDS);
    }
    public Task runTaskLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit) {
        return schedule(owner,runnable,delay,0L,unit);
    }
    public Task schedule(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit) {
        return schedule(owner,runnable,0L,period,unit);
    }
    public Task schedule(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit) {
        PrematicTask task = new PrematicTask(this.nexttaskid.getAndIncrement(),unit.toMillis(delay)
                ,unit.toMillis(period),this,owner,runnable);
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
        for(Task task : new LinkedHashSet<>(this.tasks.values()))
            if(task.getOwner().equals(owner)){
                this.tasks.remove(task.getID());
                task.setRunning(false);
            }
    }
    public void cancelALL(){
        for(Task task : new LinkedHashSet<Task>(this.tasks.values())) task.cancel();
        this.tasks.clear();
    }
}
