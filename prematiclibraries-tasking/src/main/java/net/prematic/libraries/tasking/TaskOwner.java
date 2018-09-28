package net.prematic.libraries.tasking;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 12:52
 *
 */

import java.util.concurrent.ExecutorService;

public interface TaskOwner {

    public String getName();

    public ExecutorService getExecutor();

}
