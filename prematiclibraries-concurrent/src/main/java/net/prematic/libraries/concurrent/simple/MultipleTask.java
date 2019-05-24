/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.19 11:00
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

package net.prematic.libraries.concurrent.simple;

import net.prematic.libraries.concurrent.AbstractTask;
import net.prematic.libraries.concurrent.Task;
import net.prematic.libraries.concurrent.TaskScheduler;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A default task implementation for attaching more then one runnable.
 */
public class MultipleTask extends AbstractTask {

    private final Collection<Runnable> runnables;

    public MultipleTask(TaskScheduler scheduler, ObjectOwner owner, long id, String name, long delay, long period, boolean async) {
        super(scheduler, owner, id, name, delay, period, async);
        runnables = ConcurrentHashMap.newKeySet();
    }

    @Override
    public Collection<Runnable> getRunnables() {
        return null;
    }

    @Override
    public Task append(Runnable runnable) {
        TaskDestroyedException.validate(this);
        if(runnables.contains(runnable)) throw new IllegalArgumentException("this runnable is already registered");
        this.runnables.add(runnable);
        return this;
    }

    @Override
    public Task append(Runnable... runnables) {
        TaskDestroyedException.validate(this);
        for(Runnable runnable : runnables) append(runnable);
        return this;
    }

    @Override
    public Task remove(Runnable runnable) {
        TaskDestroyedException.validate(this);
        this.runnables.remove(this);
        return this;
    }

    @Override
    public void clear() {
        TaskDestroyedException.validate(this);
        this.runnables.clear();
    }

    @Override
    public void call() {
        runnables.forEach(Runnable::run);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.runnables.clear();
    }
}
