package net.prematic.libraries.utility.owner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.01.19 14:15
 *
 */

public class SystemObjectOwner implements ObjectOwner {

    public static SystemObjectOwner DEFAULT = new SystemObjectOwner();
    public static ExecutorService EXECUTOR;

    private final String name;

    public SystemObjectOwner(){
        this("System");
    }
    public SystemObjectOwner(String name) {
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
