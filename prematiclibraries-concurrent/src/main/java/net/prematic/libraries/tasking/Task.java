package net.prematic.libraries.tasking;

import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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

public interface Task extends Runnable{

    int getID();

    String getName();

    TaskState getState();

    long getDelay();

    long getPeriod();

    ObjectOwner getOwner();

    TaskScheduler getScheduler();

    Collection<Runnable> getRunnables();

    boolean isRunning();

    boolean isAsync();

    Task append(Runnable runnable);

    Task append(Runnable... runnable);

    Task remove(Runnable runnable);

    Task setDelay(long delay, TimeUnit unit);

    Task setPeriod(long period, TimeUnit unit);

    Task addListener(Consumer<TaskFuture> listener);

    void clear();

    void call();

    void start();

    void stop();

    void destroy();


}
