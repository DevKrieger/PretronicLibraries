package net.prematic.libraries.multistorage;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.18 21:01
 *
 */

import net.prematic.libraries.tasking.TaskScheduler;

public interface Storage {

    boolean connect();

    void disconnect();

    boolean isConnected();

    boolean isIgnoreCase();

    TaskScheduler getScheduler();

    default void reconnect() {
        disconnect();
        connect();
    }
}