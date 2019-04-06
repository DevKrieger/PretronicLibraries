package net.prematic.libraries.tasking;

import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public interface TaskScheduler {

    Collection<Task> getTasks();

    Collection<Task> getTasks(TaskState state);

    Task getTask(int id);

    Task createTask(ObjectOwner owner, String name);

    Task createLaterTask(ObjectOwner owner, String name,long delay,TimeUnit unit);

    Task createScheduleTask(ObjectOwner owner, String name,long period,TimeUnit unit);

    Task createTask(ObjectOwner owner, String name,long delay, long period,TimeUnit unit);


    Task createAsynchronouslyTask(ObjectOwner owner, String name);

    Task createAsynchronouslyLaterTask(ObjectOwner owner, String name,long delay,TimeUnit unit);

    Task createAsynchronouslyScheduleTask(ObjectOwner owner, String name,long period,TimeUnit unit);

    Task createAsynchronouslyTask(ObjectOwner owner, String name,long delay, long period,TimeUnit unit);


    Task createTask(ObjectOwner owner, String name,long delay, long period,TimeUnit unit, boolean async);



    Task runTaskLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit);

    Task scheduleTask(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit);

    Task runTask(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit);


    Task runAsynchronouslyTask(ObjectOwner owner, Runnable runnable);

    Task runAsynchronouslyTaskLater(ObjectOwner owner, Runnable runnable, Long delay, TimeUnit unit);

    Task scheduleAsynchronouslyTask(ObjectOwner owner, Runnable runnable, Long period, TimeUnit unit);

    Task runAsynchronouslyTask(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit);


    Task runTask(ObjectOwner owner, Runnable runnable, Long delay, Long period, TimeUnit unit, boolean async);


    void executeTask(Task task);

    void stop(int id);

    void stop(Task task);

    void stop(ObjectOwner owner);

    void stopAll();

    void unregister(int id);

    void unregister(Task task);

    void unregister(ObjectOwner owner);

    void unregisterAll();

    void shutdown();

}
