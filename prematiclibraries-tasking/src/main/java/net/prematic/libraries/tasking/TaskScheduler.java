package net.prematic.libraries.tasking;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 12:54
 *
 */

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface TaskScheduler {

    public Collection<Task> getTasks();

    public Task runTaskAsync(TaskOwner owner, Runnable runnable);

    public Task runTaskAsynchronously(TaskOwner owner, Runnable runnable);

    public Task runTaskAsynchronouslyLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit);

    public Task scheduleAsynchronously(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit);

    public Task scheduleAsynchronously(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit);

    public Task runTaskLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit);

    public Task runTaskLater(TaskOwner owner, Runnable runnable, Long delay, TimeUnit unit, boolean async);

    public Task schedule(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit);

    public Task schedule(TaskOwner owner, Runnable runnable, Long period, TimeUnit unit, boolean async);

    public Task schedule(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit);

    public Task schedule(TaskOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit, boolean async);

    public void cancelTask(int id);

    public void cancelTask(Task task);

    public void cancelTask(TaskOwner owner);

    public void cancelALL();

}
