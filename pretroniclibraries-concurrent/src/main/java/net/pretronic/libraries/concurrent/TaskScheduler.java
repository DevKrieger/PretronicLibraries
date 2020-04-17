/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.concurrent;

import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

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
     * @param name The name of the task
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
     * Get a task by id.
     *
     * @param id The id of the tak
     * @return The task
     */
    Task getTask(int id);


    /**
     * Create a new task with a builder. See more in @{@link Builder}
     *
     * @param owner The task owner
     * @return The task builder
     */
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

    /**
     *
     */
    interface Builder {

        /**
         * The name if the task
         *
         * @param name The name
         * @return The current builder
         */
        Builder name(String name);

        /**
         * The task is executed async (Not synchronized).
         *
         * @return The current builder
         */
        Builder async();

        /**
         * The task is synchronized with the main thread
         *
         * @return The current builder
         */
        Builder sync();

        /**
         * The task will execute with a delay
         *
         * @param time The delay time
         * @param unit The time unit of the delay
         * @return The current builder
         */
        Builder delay(long time, TimeUnit unit);

        /**
         * The task will repeat
         *
         * <p>Important: The delay is only executed at the first time</p>
         *
         * @param time The interval
         * @param unit The unit of the interval
         * @return The current builder
         */
        Builder interval(long time, TimeUnit unit);

        /**
         * Create and register a task. The task must be started manually.
         *
         * <p>You can then attach runnables to the task.</p>
         *
         * @return The created task
         */
        Task create();

        /**
         * Execute the task directly with a runnable.
         *
         * <p>Important: The task can only be executed once.</p>
         *
         * @param runnable The runnable to execute
         * @return The created task
         */
        Task execute(Runnable runnable);
    }

}
