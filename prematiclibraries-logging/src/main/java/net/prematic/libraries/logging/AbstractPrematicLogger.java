package net.prematic.libraries.logging;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 24.02.19 17:19
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

import net.prematic.libraries.logging.format.FormatHelper;
import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.format.SimpleLogFormatter;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a abstract implementation of the prematic logger interface.
 *
 *  <p>It contains an normal and error output stream.</p>
 */
public abstract class AbstractPrematicLogger implements PrematicLogger {

    protected final String name;
    protected final Collection<LogHandler> handlers;

    protected LogLevel logLevel;
    protected DebugLevel debugLevel;
    protected LogFormatter formatter;

    public AbstractPrematicLogger(){
        this("Unknown");
    }

    public AbstractPrematicLogger(String name){
        this(name,new SimpleLogFormatter());
    }

    public AbstractPrematicLogger(String name, LogFormatter formatter) {
        this.name = name;
        this.formatter = formatter;
        this.handlers = ConcurrentHashMap.newKeySet();

        this.logLevel = LogLevel.INFO;
        this.debugLevel = DebugLevel.NORMAL;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    @Override
    public DebugLevel getDebugLevel() {
        return this.debugLevel;
    }

    @Override
    public LogFormatter getFormatter() {
        return this.formatter;
    }

    @Override
    public Collection<LogHandler> getHandlers() {
        return this.handlers;
    }

    @Override
    public void setLevel(LogLevel level) {
        this.logLevel = level;
    }

    @Override
    public void setDebugLevel(DebugLevel level) {
        this.debugLevel = level;
    }

    @Override
    public void setFormatter(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void registerHandler(LogHandler handler) {
        if(!this.handlers.contains(handler)) this.handlers.add(handler);
    }

    @Override
    public void unregisterHandler(LogHandler handler) {
        this.handlers.remove(handler);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable) {
        formatAndWrite(info,LogLevel.ERROR,null,null,throwable);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable, String extraMessage) {
        formatAndWrite(info,LogLevel.ERROR,null,extraMessage,throwable);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable, String extraMessage, Object... objects) {
        formatAndWrite(info,LogLevel.ERROR,null,FormatHelper.format(extraMessage,objects),throwable);
    }

    @Override
    public void debug(MessageInfo info, DebugLevel level, String message) {
        formatAndWrite(info,LogLevel.DEBUG,level,message,null);
    }

    @Override
    public void debug(MessageInfo info, DebugLevel level, Object object) {
        formatAndWrite(info,LogLevel.DEBUG,level,object.toString(),null);
    }

    @Override
    public void debug(MessageInfo info, DebugLevel level, String message, Object... objects) {
        formatAndWrite(info,LogLevel.DEBUG,level,FormatHelper.format(message,objects),null);
    }

    @Override
    public void log(MessageInfo info, LogLevel level, String message, Thread thread){
        formatAndWrite(info,level,null,message,null,thread);
    }
    private void formatAndWrite(MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable throwable){
        formatAndWrite(info, logLevel, debugLevel, message, throwable,Thread.currentThread());
    }

    public abstract void formatAndWrite(MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable throwable, Thread thread);

    @Override
    public String toString() {
        return name+" with level "+logLevel;
    }
}
