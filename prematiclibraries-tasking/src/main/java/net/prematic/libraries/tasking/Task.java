package net.prematic.libraries.tasking;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 12:53
 *
 */

import net.prematic.libraries.utility.owner.ObjectOwner;

public interface Task extends Runnable{

    int getID();

    boolean isRunning();

    ObjectOwner getOwner();

    TaskScheduler getScheduler();

    Runnable getRunnable();

    boolean isAsync();

    void setRunning(boolean running);

    void cancel();


}
