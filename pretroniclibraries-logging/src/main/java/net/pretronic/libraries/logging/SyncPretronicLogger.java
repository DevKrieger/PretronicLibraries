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
import net.pretronic.libraries.logging.format.FormatHelper;
import net.pretronic.libraries.logging.format.LogFormatter;
import net.pretronic.libraries.logging.handler.LogHandler;
import net.pretronic.libraries.logging.level.DebugLevel;
import net.pretronic.libraries.logging.level.LogLevel;

import java.util.Collection;

/**
 * This is a simple async implementation of the pretronic logging framework.
 */
public class SyncPretronicLogger extends AbstractPretronicLogger {

    public SyncPretronicLogger(){
        this("Unknown");
    }

    public SyncPretronicLogger(String name){
        this(name,new DefaultLogFormatter());
    }

    public SyncPretronicLogger(String name, LogFormatter formatter){
        this(name,formatter,null);
    }

    public SyncPretronicLogger(String name, LogFormatter formatter, Collection<LogHandler> handlers) {
        super(name, formatter,handlers);
        info("Starting Pretronic sync logging service");
    }

    public void formatAndWrite(MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable throwable, Thread thread){
        if(!canLog(logLevel)) return;
        long timeStamp = System.currentTimeMillis();
        LogRecord record = new LogRecord(timeStamp,info, logLevel, debugLevel, message, throwable, thread);
        try {
            String result = formatter.format(this,record);
            for(LogHandler handler : handlers) handler.handleLog(record,result);
        }catch (Exception exception) {
            System.out.println("[Logger-Exception] An error in logging service occurred:");
            StringBuilder builder = new StringBuilder();
            FormatHelper.buildStackTrace(builder,Thread.currentThread(),exception,"[Logger-Exception]");
            System.out.println(builder.toString());
        }
    }

    @Override
    public void shutdown() {
        for(LogHandler handler : handlers) handler.shutdown();
    }

}
