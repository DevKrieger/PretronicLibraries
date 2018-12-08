package net.prematic.libraries.sql;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.1018 21:10
 *
 */

import net.prematic.libraries.multistorage.Storage;
import net.prematic.libraries.tasking.TaskScheduler;
import net.prematic.libraries.tasking.intern.PrematicTaskScheduler;

import java.sql.Connection;

public abstract class SQL implements Storage {

    private TaskScheduler scheduler;
    private boolean ignoreCase, supportNoCase, optionsOnEnd;

    public SQL() {
        this.ignoreCase = true;
        this.supportNoCase = true;
        this.optionsOnEnd = false;
        this.scheduler = new PrematicTaskScheduler();
    }

    public SQL(boolean ignoreCase, boolean supportNoCase, boolean optionsOnEnd) {
        this.ignoreCase = ignoreCase;
        this.supportNoCase = supportNoCase;
        this.optionsOnEnd = optionsOnEnd;
    }

    @Override
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public boolean isOptionsOnEnd() {
        return optionsOnEnd;
    }

    public boolean supportNoCase() {
        return supportNoCase;
    }

    @Override
    public TaskScheduler getScheduler() {
        return scheduler;
    }

    public SQL setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public SQL setSupportNoCase(boolean supportNoCase) {
        this.supportNoCase = supportNoCase;
        return this;
    }

    public SQL setOptionsOnEnd(boolean optionsOnEnd) {
        this.optionsOnEnd = optionsOnEnd;
        return this;
    }

    public abstract Connection getConnection();
    public abstract void loadDriver();
}
