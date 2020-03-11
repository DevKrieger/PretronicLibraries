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

package net.pretronic.libraries.concurrent.simple;

import net.pretronic.libraries.concurrent.AbstractTask;
import net.pretronic.libraries.concurrent.Task;
import net.pretronic.libraries.concurrent.TaskScheduler;
import net.pretronic.libraries.concurrent.TaskState;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.Collections;

/**
 * A single task implementation of the task. A singleton task can only have one runnable and executed once.
 *
 * <p>This task type will automatically unregistered the task on stop.</p>
 */
public class SingletonFinalTask extends AbstractTask {

    private final Runnable runnable;

    protected SingletonFinalTask(TaskScheduler scheduler, ObjectOwner owner, int id, String name, long delay, long period, boolean async, Runnable runnable) {
        super(scheduler, owner, id, name, delay, period, async);
        this.runnable = runnable;
    }

    @Override
    public Collection<Runnable> getRunnables() {
        return Collections.singletonList(runnable);
    }

    @Override
    public Task append(Runnable runnable) {
        throw new UnsupportedOperationException("A singleton task does not support multiple runnables.");
    }

    @Override
    public Task append(Runnable... runnable) {
        throw new UnsupportedOperationException("A singleton task does not support multiple runnables.");
    }

    @Override
    public Task remove(Runnable runnable) {
        throw new UnsupportedOperationException("A singleton task does not support multiple runnables.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("A singleton task does not support multiple runnables.");
    }

    @Override
    protected void stopInternal(Throwable thrown) {
        super.stopInternal(thrown);
        this.state = TaskState.DESTROYED;
        this.listeners.clear();
        this.scheduler.unregister(this);
    }

    @Override
    public void call() {
        runnable.run();
    }
}
