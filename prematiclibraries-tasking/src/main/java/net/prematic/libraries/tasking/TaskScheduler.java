package net.prematic.libraries.tasking;

import net.prematic.libraries.utility.owner.ObjectOwner;

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
