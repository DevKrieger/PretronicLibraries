/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 02.04.19 08:13
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
 * Every task has a state, it shows in which step the task is and what he will do as next.
 *
 */
public enum TaskState {

    /**
     *  This task is running.
     */
    RUNNING(),

    /**
     * The task is stopped an can be started.
     */
    STOPPED(),

    /**
     * The task is successfully finished (Some tasks can be restarted).
     */
    COMPLETED(),

    /**
     * There was an problem during the execution, the task stopped.
     */
    FAILED(),

    /**
     * The task stopped because he was stopped by somebody (Or the program is stopping).
     */
    INTERRUPTED(),

    /**
     * The task is destroyed and unregistered, it can not be used anymore.
     */
    DESTROYED();

}
