package net.prematic.libraries.tasking;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 12:53
 *
 */

public interface Task extends Runnable{

    public int getID();

    public boolean isRunning();

    public TaskOwner getOwner();

    public TaskScheduler getScheduler();

    public Runnable getRunnable();

    public boolean isAsync();

    public void setRunning(boolean running);

    public void cancel();


}
