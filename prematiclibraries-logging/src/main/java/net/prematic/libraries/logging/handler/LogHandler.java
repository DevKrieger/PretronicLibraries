package net.prematic.libraries.logging.handler;

import net.prematic.libraries.logging.PrematicLogger;

import java.util.logging.LogRecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public abstract class LogHandler {

    private String name;
    private PrematicLogger logger;

    public LogHandler(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public PrematicLogger getLogger() {
        return logger;
    }
    public void setLogger(PrematicLogger logger) {
        this.logger = logger;
    }
    public abstract void onInit(PrematicLogger logger);
    public abstract void log(LogRecord record, String formatedmessage);
    public abstract void shutdown();
}
