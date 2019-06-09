/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.06.19 16:56
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

package net.prematic.libraries.logging.bridge.slf4j;

import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.logging.SimplePrematicLogger;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * This is a bridge for hooking prematic logging service in the self4j logger.
 */
public class SLF4JBridge implements Logger {

    private final PrematicLogger logger;

    public SLF4JBridge() {
        this(new SimplePrematicLogger());
    }

    public SLF4JBridge(PrematicLogger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String s) {
        this.logger.debug(DebugLevel.SIMPLE,s);
    }

    @Override
    public void trace(String s, Object o) {
        this.logger.debug(DebugLevel.SIMPLE,s,o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        this.logger.debug(DebugLevel.SIMPLE,s,o,o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        this.logger.debug(DebugLevel.SIMPLE,s,objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        this.logger.debug(DebugLevel.SIMPLE,compareThrowable(s,throwable));
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.logger.canDebug(DebugLevel.SIMPLE);
    }

    @Override
    public void trace(Marker marker, String s) {
        this.logger.debug(DebugLevel.SIMPLE,s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        this.logger.debug(DebugLevel.SIMPLE,s,o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        this.logger.debug(DebugLevel.SIMPLE,s,o,o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        this.logger.debug(DebugLevel.SIMPLE,s,objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        this.logger.debug(DebugLevel.SIMPLE,compareThrowable(s,throwable));
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.canDebug(DebugLevel.NORMAL);
    }

    @Override
    public void debug(String s) {
        this.logger.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        this.logger.debug(s,o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        this.logger.debug(s,o,o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        this.logger.debug(s,objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        this.logger.debug(s+" ("+throwable.getMessage()+")");
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.logger.canDebug(DebugLevel.NORMAL);
    }

    @Override
    public void debug(Marker marker, String s) {
        this.logger.debug(s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        this.logger.debug(s,o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        this.logger.debug(s,o,o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        this.logger.debug(s,objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        this.logger.debug(compareThrowable(s,throwable));
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.canLog(LogLevel.INFO);
    }

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void info(String s, Object o) {
        this.logger.info(s,o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        this.logger.info(s,o,o1);
    }

    @Override
    public void info(String s, Object... objects) {
        this.logger.info(s,objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        this.logger.info(compareThrowable(s,throwable));
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.logger.canLog(LogLevel.INFO);
    }

    @Override
    public void info(Marker marker, String s) {
        this.logger.info(s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        this.logger.info(s,o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        this.logger.info(s,o,o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        this.logger.info(s,objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        this.logger.info(compareThrowable(s,throwable));
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.canLog(LogLevel.WARN);
    }

    @Override
    public void warn(String s) {
        this.logger.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        this.logger.warn(s,o);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        this.logger.warn(s,o,o1);
    }

    @Override
    public void warn(String s, Object... objects) {
        this.logger.warn(s,objects);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        this.logger.warn(compareThrowable(s,throwable));
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.logger.canLog(LogLevel.WARN);
    }

    @Override
    public void warn(Marker marker, String s) {
        this.logger.warn(s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        this.logger.warn(s,o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        this.logger.warn(s,o,o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        this.logger.warn(s,objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        this.logger.warn(s,compareThrowable(s,throwable));
    }

    @Override
    public boolean isErrorEnabled() {
       return this.logger.canLog(LogLevel.ERROR);
    }

    @Override
    public void error(String s) {
        this.logger.error(s);
    }

    @Override
    public void error(String s, Object o) {
        this.logger.error(s,o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        this.logger.error(s,o,o1);
    }

    @Override
    public void error(String s, Object... objects) {
        this.logger.error(s,objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        this.logger.error(compareThrowable(s,throwable));
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.logger.canLog(LogLevel.ERROR);
    }

    @Override
    public void error(Marker marker, String s) {
        this.logger.error(s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        this.logger.error(s,o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        this.logger.error(s,o,o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        this.logger.error(s,objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        this.logger.error(compareThrowable(s,throwable));
    }

    private String compareThrowable(String message, Throwable throwable){
        return message+" ("+throwable.getMessage()+")";
    }
}
