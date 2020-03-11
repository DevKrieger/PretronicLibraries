/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.logging;

import net.pretronic.libraries.logging.format.DefaultLogFormatter;
import net.pretronic.libraries.logging.format.LogFormatter;
import net.pretronic.libraries.logging.handler.ConsoleHandler;
import net.pretronic.libraries.logging.handler.LogHandler;
import net.pretronic.libraries.logging.level.DebugLevel;
import net.pretronic.libraries.logging.level.LogLevel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Deprecated
public class SimplePretronicLogger implements PretronicLogger {

    private final PretronicLogger original;

    public SimplePretronicLogger(String name){
        this(name,new DefaultLogFormatter());
    }

    public SimplePretronicLogger(String name, LogFormatter formatter){
        this(name,formatter,System.out,System.err);
    }

    public SimplePretronicLogger(String name, LogFormatter formatter, OutputStream outputStream){
        this(name,formatter,outputStream, outputStream);
    }

    public SimplePretronicLogger(String name, LogFormatter formatter, OutputStream outputStream, OutputStream errorStream){
        this(name,formatter,outputStream,errorStream, StandardCharsets.UTF_8);
    }

    public SimplePretronicLogger(String name, LogFormatter formatter, OutputStream outputStream, OutputStream errorStream, Charset charset) {
        original = new AsyncPretronicLogger(name,formatter);
        original.addHandler(new ConsoleHandler(outputStream,errorStream,charset));
    }

    @Override
    public String getName() {
        return original.getName();
    }

    @Override
    public LogLevel getLogLevel() {
        return original.getLogLevel();
    }

    @Override
    public DebugLevel getDebugLevel() {
        return original.getDebugLevel();
    }

    @Override
    public LogFormatter getFormatter() {
        return original.getFormatter();
    }

    @Override
    public Collection<LogHandler> getHandlers() {
        return original.getHandlers();
    }

    @Override
    public void addHandler(LogHandler handler) {
        original.addHandler(handler);
    }

    @Override
    public void removeHandler(LogHandler handler) {
        original.removeHandler(handler);
    }

    @Override
    public void setLevel(LogLevel level) {
        original.setLevel(level);
    }

    @Override
    public void setDebugLevel(DebugLevel level) {
        original.setDebugLevel(level);
    }

    @Override
    public void setFormatter(LogFormatter formatter) {
        original.setFormatter(formatter);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable) {
        original.error(info,throwable);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable, String extraMessage) {
        original.error(info,throwable,extraMessage);
    }

    @Override
    public void error(MessageInfo info, Throwable throwable, String extraMessage, Object... objects) {
        original.error(info,throwable,extraMessage,objects);
    }

    @Override
    public void debug(DebugLevel level, MessageInfo info, String message) {
        original.debug(level,info,message);
    }

    @Override
    public void debug(DebugLevel level, MessageInfo info, Object object) {
        original.debug(level,info,object);
    }

    @Override
    public void debug(DebugLevel level, MessageInfo info, String message, Object... objects) {
        original.debug(level,info,message,objects);
    }

    @Override
    public void log(MessageInfo info, LogLevel level, String message, Thread thread) {
        original.log(info,level,message,thread);
    }

    @Override
    public void shutdown() {
        original.shutdown();
    }
}
