package net.prematic.libraries.multistorage;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.18 21:01
 *
 */

public interface Storage {

    boolean connect();

    void disconnect();

    boolean isConnected();

    default void reconnect() {
        disconnect();
        connect();
    }
}