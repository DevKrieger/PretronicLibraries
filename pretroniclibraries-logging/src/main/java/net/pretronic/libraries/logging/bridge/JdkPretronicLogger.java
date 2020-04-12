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

package net.pretronic.libraries.logging.bridge;

import net.pretronic.libraries.logging.MessageInfo;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.format.FormatHelper;
import net.pretronic.libraries.logging.format.LogFormatter;
import net.pretronic.libraries.logging.handler.LogHandler;
import net.pretronic.libraries.logging.level.DebugLevel;
import net.pretronic.libraries.logging.level.LogLevel;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JdkPretronicLogger implements PretronicLogger {

    private final Logger logger;
    private final Collection<LogHandler> handlers;
    private final Map<LogLevel,Level> logLevelTranslation;

    private DebugLevel debugLevel;
    private Handler translateHandler;
    private Function<LogLevel,String> prefixProcessor;

    public JdkPretronicLogger(Logger logger) {
        this.logger = logger;
        this.debugLevel = DebugLevel.NORMAL;
        this.handlers = new ArrayList<>();
        this.logLevelTranslation = new HashMap<>();

        this.logLevelTranslation.put(LogLevel.OFF,Level.OFF);
        this.logLevelTranslation.put(LogLevel.ERROR,Level.SEVERE);
        this.logLevelTranslation.put(LogLevel.WARN,Level.WARNING);
        this.logLevelTranslation.put(LogLevel.INFO,Level.INFO);
        this.logLevelTranslation.put(LogLevel.DEBUG,Level.FINE);
        this.logLevelTranslation.put(LogLevel.ALL,Level.ALL);
    }

    public Function<LogLevel, String> getPrefixProcessor() {
        return prefixProcessor;
    }

    public void setPrefixProcessor(Function<LogLevel, String> prefixProcessor) {
        this.prefixProcessor = prefixProcessor;
    }


    public Map<LogLevel, Level> getLogLevelTranslation() {
        return logLevelTranslation;
    }

    public Logger getLogger() {
        return logger;
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
        error(info,throwable, FormatHelper.format(extraMessage,objects));
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

    public void log(MessageInfo info, LogLevel level, String message0, Thread thread,Throwable throwable){
        String message = message0;
        if(prefixProcessor != null){
            String result = prefixProcessor.apply(level);
            if(result != null)       message = result+message;
        }
        LogRecord record = new LogRecord(translateLevel(level),message);
        record.setLoggerName(getName());
        record.setThreadID((int) thread.getId());
        record.setThrown(throwable);
        logger.log(record);
    }

    private Level translateLevel(LogLevel level){
        Level result = logLevelTranslation.get(level);
        return result != null ? result : Level.INFO;
    }

    private LogLevel translateLevel(Level level){
        LogLevel result = null;
        for (Map.Entry<LogLevel, Level> entry : logLevelTranslation.entrySet()) {
            if(entry.getValue().equals(level)){
                result = entry.getKey();
                break;
            }
        }
        return result != null ? result : LogLevel.INFO;
    }

    @Override
    public void shutdown() {
        //Unused
    }

    private class TranslateHandler extends Handler {

        @Override
        public void publish(LogRecord record) {
            net.pretronic.libraries.logging.LogRecord translatedRecord = new net.pretronic.libraries.logging.LogRecord(
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
