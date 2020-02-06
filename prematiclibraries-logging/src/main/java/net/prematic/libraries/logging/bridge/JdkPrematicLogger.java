/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.09.19, 20:16
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package net.prematic.libraries.logging.bridge;

import net.prematic.libraries.logging.MessageInfo;
import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.logging.format.FormatHelper;
import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JdkPrematicLogger implements PrematicLogger {

    private final Logger logger;
    private final Collection<LogHandler> handlers;

    private DebugLevel debugLevel;
    private Handler translateHandler;

    public JdkPrematicLogger(Logger logger) {
        this.logger = logger;
        this.debugLevel = DebugLevel.NORMAL;
        this.handlers = new ArrayList<>();
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public LogLevel getLogLevel() {
        return translateLevel(logger.getLevel());
    }

    @Override
    public DebugLevel getDebugLevel() {
        return debugLevel;
    }

    @Override
    public LogFormatter getFormatter() {
        throw new UnsupportedOperationException("This logger is managed by another logging service.");
    }

    @Override
    public Collection<LogHandler> getHandlers() {
        return handlers;
    }

    @Override
    public void addHandler(LogHandler handler) {
        this.handlers.add(handler);
        if(translateHandler == null){
            this.translateHandler = new TranslateHandler();
            this.logger.addHandler(this.translateHandler);
        }
    }

    @Override
    public void removeHandler(LogHandler handler) {
        this.handlers.remove(handler);
    }

    @Override
    public void setLevel(LogLevel level) {
        logger.setLevel(translateLevel(level));
    }

    @Override
    public void setDebugLevel(DebugLevel level) {
        this.debugLevel = level;
    }

    @Override
    public void setFormatter(LogFormatter formatter) {
        throw new UnsupportedOperationException("This logger is managed by another logging service.");
    }

    @Override
    public void error(MessageInfo info, Throwable throwable) {
        error(info,throwable,null);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable, String extraMessage) {
        log(info,LogLevel.DEBUG,extraMessage,throwable);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable, String extraMessage, Object... objects) {
        error(info,throwable,FormatHelper.format(extraMessage,objects));
    }

    @Override
    public void debug(DebugLevel level, MessageInfo info, String message) {
        if(this.debugLevel.canLog(level)) log(info,LogLevel.DEBUG,message);
    }

    @Override
    public void debug(DebugLevel level, MessageInfo info, Object object) {
        debug(level,info,object.toString());
    }

    @Override
    public void debug(DebugLevel level, MessageInfo info, String message, Object... objects) {
        debug(level,info, FormatHelper.format(message,objects));
    }

    @Override
    public void log(MessageInfo info, LogLevel level, String message, Thread thread) {
       log(info,level,message,thread,null);
    }

    public void log(MessageInfo info, LogLevel level, String message, Thread thread,Throwable throwable){
        LogRecord record = new LogRecord(translateLevel(level),message);
        record.setLoggerName(getName());
        record.setThreadID((int) thread.getId());
        record.setThrown(throwable);
        logger.log(record);
    }

    private Level translateLevel(LogLevel level){
        if(level.equals(LogLevel.ALL)) return Level.ALL;
        else if(level.equals(LogLevel.WARN)) return Level.WARNING;
        else if(level.equals(LogLevel.OFF)) return Level.OFF;
        else if(level.equals(LogLevel.ERROR)) return Level.SEVERE;
        else if(level.equals(LogLevel.DEBUG)) return Level.FINE;
        else return Level.INFO;
    }

    private LogLevel translateLevel(Level level){
        if(level == Level.ALL) return LogLevel.ALL;
        else if(level == Level.WARNING) return LogLevel.WARN;
        else if(level == Level.OFF) return LogLevel.OFF;
        else if(level == Level.SEVERE) return LogLevel.ERROR;
        else if(level == Level.FINE) return LogLevel.DEBUG;
        else return LogLevel.INFO;
    }

    @Override
    public void shutdown() {
        //Unused
    }

    private class TranslateHandler extends Handler {

        @Override
        public void publish(LogRecord record) {
            net.prematic.libraries.logging.LogRecord translatedRecord = new net.prematic.libraries.logging.LogRecord(
                    record.getMillis(),null,translateLevel(record.getLevel()),debugLevel
                    ,record.getMessage(),record.getThrown(),findThread(record.getThreadID()));
            handlers.forEach(handler -> {
                try {
                    handler.handleLog(translatedRecord,record.getMessage());
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }

        @Override
        public void flush() {
            //Unused
        }

        @Override
        public void close() throws SecurityException {
            //Nothing to close
        }

        private Thread findThread(int id){
            for (Thread thread : Thread.getAllStackTraces().keySet()) if(thread.getId() == id) return thread;
            return null;
        }
    }
}
