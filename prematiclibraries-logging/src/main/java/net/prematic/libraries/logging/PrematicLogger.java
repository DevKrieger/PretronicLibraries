package net.prematic.libraries.logging;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.util.Collection;

/**
 * The PrematicLogger class is the main object of the Prematic logging framework, it is the implementation for all Prematic loggers.
 */
public interface PrematicLogger {

    /**
     * Get the name of the logger.
     *
     * <p>This is for identifying the logger in a project with more than one loggers.</p>
     *
     * @return The name of the logger
     */
    String getName();

    /**
     * Get the level of this logger.
     *
     * @return The level of this logger
     */
    LogLevel getLogLevel();

    /**
     * Get the debug level of this logger.
     *
     * @return The debug level of this logger
     */
    DebugLevel getDebugLevel();

    /**
     * Get the message formatter of this logger.
     *
     * @return The formatter of this logger
     */
    LogFormatter getFormatter();

    /**
     * Get all additional log handlers of this loggers.
     *
     * @return All log handlers
     */
    Collection<LogHandler> getHandlers();

    /**
     * Check if another level can log on this logger.
     *
     * @param level The level which should be checked
     * @return If the level is log able
     */
    default boolean canLog(LogLevel level){
        return getLogLevel().canLog(level);
    }

    /**
     * Check if another debug level can debug on this logger.
     *
     * <p>Debug log level must be enabled.</p>
     *
     * @param level The debug leven which should bec checked
     * @return If the level can debug
     */
    default boolean canDebug(DebugLevel level){
        return isDebugging() && getDebugLevel().canLog(level);
    }

    /**
     * Check if the debug mode is enabled on this logger.
     *
     * @return If it is enabled
     */
    default boolean isDebugging(){
        return canLog(LogLevel.DEBUG);
    }

    /**
     * Set a log leven to this logger.
     *
     * @param level The new log level
     */
    void setLevel(LogLevel level);

    /**
     * Set a debug level to this logger.
     *
     * @param level The new debug level
     */
    void setDebugLevel(DebugLevel level);

    /**
     * Set a new message formatter to this logger.
     *
     * @param formatter The new formatter
     */
    void setFormatter(LogFormatter formatter);

    /**
     * Register a new handler on this logger.
     *
     * @param handler A log handler
     */
    void registerHandler(LogHandler handler);

    /**
     * Unregister a register log handler from this logger.
     *
     * @param handler The log handler
     */
    void unregisterHandler(LogHandler handler);


    //Info

    default void info(String message){
        info(null,message);
    }

    default void info(Object object){
        info(null,object);
    }

    default void info(String message,Object... objects){
        info(null,message,objects);
    }

    default void info(MessageInfo info,String message){
        log(info,LogLevel.INFO,message);
    }

    default void info(MessageInfo info,Object object){
        log(info,LogLevel.INFO,object);
    }

    default void info(MessageInfo info,String message, Object... objects){
        log(info,LogLevel.INFO,message,objects);
    }

    //WARN

    default void warn(String message){
        warn(null,message);
    }

    default void warn(Object object){
        warn(null,object);
    }

    default void warn(String message,Object... objects){
        warn(null,message,objects);
    }

    default void warn(MessageInfo info,String message){
        log(info,LogLevel.WARN,message);
    }

    default void warn(MessageInfo info,Object object){
        log(info,LogLevel.WARN,object);
    }

    default void warn(MessageInfo info,String message, Object... objects){
        log(info,LogLevel.WARN,message,objects);
    }

    //Error

    default void error(String message){
        error(null,message);
    }

    default void error(Object object){
        error(null,object);
    }

    default void error(String message, Object... objects){
        error((MessageInfo) null,message,objects);
    }

    default void error(Throwable throwable){
        error(throwable instanceof InfoAbleException?((InfoAbleException) throwable).getInfo():null,throwable);
    }

    default void error(MessageInfo info,String message){
        log(info,LogLevel.ERROR,message);
    }

    default void error(MessageInfo info,Object object){
        log(info,LogLevel.ERROR,object);
    }

    default void error(MessageInfo info,String message, Object... objects){
        log(info,LogLevel.ERROR,message,objects);
    }

    void error(MessageInfo info,Throwable throwable);

    default void error(String extraMessage, Throwable throwable){
        error(null,throwable,extraMessage);
    }

    default void error(Throwable throwable,String extraMessage,Object... objects){
        error(null,throwable,extraMessage,objects);
    }

    void error(MessageInfo info, Throwable throwable,String extraMessage);

    void error(MessageInfo info, Throwable throwable,String extraMessage,Object... objects);


    //Debugging

    default void debug(String message){
        debug(DebugLevel.NORMAL,message);
    }

    default void debug(Object object){
        debug(DebugLevel.NORMAL,object);
    }

    default void debug(String message, Object... objects){
        debug(DebugLevel.NORMAL,message,objects);
    }

    default void debug(DebugLevel level, String message){
        debug(null,level,message);
    }

    default void debug(DebugLevel level, Object object){
        debug(null,level,object);
    }

    default void debug(DebugLevel level, String message, Object... objects){
        debug(null,level,message,objects);
    }

    default void debug(MessageInfo info, String message){
        debug(info,DebugLevel.NORMAL,message);
    }

    default void debug(MessageInfo info, Object object){
        debug(info,DebugLevel.NORMAL,object);
    }

    default void debug(MessageInfo info, String message,Object... objects){
        debug(info,DebugLevel.NORMAL,message,objects);
    }

    void debug(MessageInfo info, DebugLevel level, String message);

    void debug(MessageInfo info, DebugLevel level, Object object);

    void debug(MessageInfo info, DebugLevel level, String message, Object... objects);


    //Final logging

    default void log(LogLevel level,String message){
        log(null,level,message);
    }

    default void log(LogLevel level,Object object){
        log(null,level,object);
    }

    default void log(LogLevel level,String message,Object... objects){
        log(null,level,message,objects);
    }

    default void log(MessageInfo info,LogLevel level,String message){
        log(info, level, message,Thread.currentThread());
    }

    default void log(MessageInfo info,LogLevel level,Object object){
        log(info, level, object.toString());
    }

    default void log(MessageInfo info,LogLevel level,String message, Object... objects){
        log(info,level,FormatHelper.format(message, objects));
    }

    void log(MessageInfo info, LogLevel level, String message, Thread thread);

    /**
     * Shutdown this logger.
     */
    void shutdown();

}
