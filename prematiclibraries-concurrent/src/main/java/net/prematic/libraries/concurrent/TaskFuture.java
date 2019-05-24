/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 01.04.19 19:22
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

package net.prematic.libraries.concurrent;

/**
 * A task future is created for every listener, it contains information about the task state.
 *
 * The task future is over given in a consumer which is registered at a task.
 */
public interface TaskFuture {

    /**
     * Get the current task state.
     *
     * @return The current state.
     */
    TaskState getState();

    /**
     * Get an problem (Usually only included when the task failed).
     *
     * <p>Note: If no problem is available, the thrown will be null.</p>
     * @return the current problem.
     */
    Throwable getThrowable();

    /**
     * Check if the task is starting (TaskState.RUNNING);
     *
     * @return if the task starts.
     */
    boolean isStarting();

    /**
     * Check if the task is completed (TaskState.COMPLETED);
     *
     * @return if the task is completed
     */
    boolean isCompleted();

    /**
     * Check if the task failed(TaskState.FAILED);
     *
     * @return if the task failed
     */
    boolean isFailed();

    /**
     * Check if the task is interrupted (TaskState.INTERRUPTED);
     *
     * @return if the task is interrupted
     */
    boolean isInterrupted();
}
