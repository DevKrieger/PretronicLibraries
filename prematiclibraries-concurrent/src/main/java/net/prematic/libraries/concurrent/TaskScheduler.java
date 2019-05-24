package net.prematic.libraries.concurrent;

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

/**
 * Every task is simple from a task scheduler, this scheduler creates or runs tasks.
 */
public interface TaskScheduler {

    /**
     * Get all registered tasks.
     *
     * @return All tasks in a collection
     */
    Collection<Task> getTasks();

    /**
     * Get all registered tasks with a specified name.
     *
     * @return All tasks with the same name in a collection
     */
    Collection<Task> getTasks(String name);

    /**
     * Get all tasks which are in a specified state.
     *
     * @param state The state
     * @return All task in this state.
     */
    Collection<Task> getTasks(TaskState state);

    /**
     * Get a task by his id.
     *
     * @param id The task id
     * @return The task
     */
    Task getTask(int id);

    Builder createTask(ObjectOwner owner);

    /**
     * Execute (OnCall) the task.
     *
     * @param task The task which should be executed.
     */
    void executeTask(Task task);

    /**
     * Stop a task by id.
     *
     * @param id The id of the task
     */
    void stop(int id);

    /**
     * Stop the task.
     *
     * @param task Task ask which should be stopped.
     */
    void stop(Task task);

    /**
     * Stop all task from a owner (module).
     *
     * @param owner The task owner
     */
    void stop(ObjectOwner owner);

    /**
     * Stop all running tasks.
     */
    void stopAll();

    /**
     * Unregister a task by id, the task can no longer be used.
     *
     * @param id The id of the task.
     */
    void unregister(int id);

    /**
     * Unregister a task.
     *
     * @param task The task which should be stopped and unregistered.
     */
    void unregister(Task task);

    /**
     * Unregister and stop all tasks from this owner.
     *
     * @param owner The task owner
     */
    void unregister(ObjectOwner owner);

    /**
     * Unregister and destroy all tasks.
     */
    void unregisterAll();

    /**
     * Stop this scheduler and all tasks.
     */
    void shutdown();

    interface Builder {

        Builder name(String name);

        Builder async();

        Builder sync();

        Builder delay(long time, TimeUnit unit);

        Builder interval(long time, TimeUnit unit);

        Task create();

        Task execute(Runnable runnable);
    }

}
