package net.prematic.libraries.utility.owner;

import java.util.concurrent.ExecutorService;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.01.19 14:15
 *
 */

public interface ObjectOwner {

    String getName();

    ExecutorService getExecutor();

}
