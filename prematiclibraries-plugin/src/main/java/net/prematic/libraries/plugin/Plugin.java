package net.prematic.libraries.plugin;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:10
 *
 */

import net.prematic.libraries.command.owner.CommandOwner;
import net.prematic.libraries.event.ObjectOwner;
import net.prematic.libraries.tasking.TaskOwner;
import net.prematic.libraries.tasking.intern.SystemTaskOwner;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Plugin implements CommandOwner, TaskOwner, ObjectOwner {

    private PluginDescription description;
    private PluginManager pluginmanager;
    private PluginClassloader loader;
    private ExecutorService executor;
    private Boolean enabled;

    public Plugin(){
        this.enabled = false;
    }
    public PluginManager getPluginManager() {
        return this.pluginmanager;
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
    @Override
    public ExecutorService getExecutor() {
        if(this.executor == null) this.executor = Executors.newCachedThreadPool();
        return this.executor;
    }
    public Boolean isEnabled(){
        return this.enabled;
    }
    public void init(PluginDescription description, PluginManager pluginmanager, PluginClassloader loader){
        this.description = description;
        this.pluginmanager = pluginmanager;
        this.loader = loader;
        this.enabled = false;
    }
    protected abstract void bootstrap();
    protected abstract void shutdown();
}
