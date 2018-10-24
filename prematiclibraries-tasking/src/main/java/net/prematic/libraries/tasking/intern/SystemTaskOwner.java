package net.prematic.libraries.tasking.intern;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 13:30
 *
 */

import net.prematic.libraries.tasking.TaskOwner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemTaskOwner implements TaskOwner {

    public static SystemTaskOwner DEFAULT = new SystemTaskOwner();
    private static ExecutorService EXECUTOR;
    private String name;

    public SystemTaskOwner(){
        this("System");
    }
    public SystemTaskOwner(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public ExecutorService getExecutor() {
        if(EXECUTOR == null) EXECUTOR = Executors.newCachedThreadPool();
        return EXECUTOR;
    }
    public static void shutdown(){
        if(EXECUTOR != null) EXECUTOR.shutdown();
    }
}
