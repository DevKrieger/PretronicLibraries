package net.prematic.libraries.tasking;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 12:54
 *
 */

import net.prematic.libraries.utility.owner.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface TaskScheduler {

    Collection<Task> getTasks();

    Task runTaskAsync(ObjectOwner owner, Runnable runnable);

    Task runTaskAsynchronously(ObjectOwner owner, Runnable runnable);

    Task runTaskAsynchronouslyLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit);

    Task scheduleAsynchronously(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit);

    Task scheduleAsynchronously(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit);

    Task runTaskLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit);

    Task runTaskLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit, boolean async);

    Task schedule(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit);

    Task schedule(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit, boolean async);

    Task schedule(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit);

    Task schedule(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit, boolean async);

    void cancelTask(int id);

    void cancelTask(Task task);

    void cancelTask(ObjectOwner owner);

    void cancelAll();

}
