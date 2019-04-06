/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.19 11:01
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

package net.prematic.libraries.tasking.simple;

import net.prematic.libraries.tasking.AbstractTask;
import net.prematic.libraries.tasking.Task;
import net.prematic.libraries.tasking.TaskScheduler;
import net.prematic.libraries.tasking.TaskState;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.Collections;

public class SingletonFinalTask extends AbstractTask {

    private final Runnable runnable;

    public SingletonFinalTask(TaskScheduler scheduler, ObjectOwner owner, int id, String name, long delay, long period, boolean async, Runnable runnable) {
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
