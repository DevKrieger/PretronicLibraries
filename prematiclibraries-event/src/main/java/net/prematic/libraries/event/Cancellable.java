package net.prematic.libraries.event;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 09.12.18 00:05
 *
 */

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}