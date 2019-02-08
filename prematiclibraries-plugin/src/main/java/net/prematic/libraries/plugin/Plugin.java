package net.prematic.libraries.plugin;

import net.prematic.libraries.utility.owner.ObjectOwner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public abstract class Plugin implements ObjectOwner {

    private PluginDescription description;
    private PluginManager pluginManager;
    private PluginClassloader loader;
    private ExecutorService executor;
    private boolean enabled;

    public Plugin(){
        this.enabled = false;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public PluginDescription getDescription() {
        return this.description;
    }

    public PluginClassloader getLoader() {
        return this.loader;
    }

    @Override
    public String getName() {
        return this.description.getName();
    }

    public ExecutorService getExecutor() {
        if(this.executor == null) this.executor = Executors.newCachedThreadPool();
        return this.executor;
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    protected void init(PluginDescription description, PluginManager pluginManager, PluginClassloader loader){
        this.description = description;
        this.pluginManager = pluginManager;
        this.loader = loader;
        this.enabled = false;
    }

    protected abstract void bootstrap();

    protected abstract void shutdown();
}
