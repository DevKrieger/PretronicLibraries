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
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The multiple task implementation is for attaching more then one runnable.
 */
public class MultipleTask extends AbstractTask {

    private final Collection<Runnable> runnables;

    protected MultipleTask(TaskScheduler scheduler, ObjectOwner owner, int id, String name, long delay, long period, boolean async) {
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
