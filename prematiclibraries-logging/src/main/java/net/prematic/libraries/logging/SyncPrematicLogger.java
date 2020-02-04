/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 27.03.19 12:37
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

package net.prematic.libraries.logging;

import net.prematic.libraries.logging.format.DefaultLogFormatter;
import net.prematic.libraries.logging.format.FormatHelper;
import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.util.Collection;

/**
 * This is a simple async implementation of the prematic logging framework.
 */
public class SyncPrematicLogger extends AbstractPrematicLogger {

    public SyncPrematicLogger(){
        this("Unknown");
    }

    public SyncPrematicLogger(String name){
        this(name,new DefaultLogFormatter());
    }

    public SyncPrematicLogger(String name, LogFormatter formatter){
        this(name,formatter,null);
    }

    public SyncPrematicLogger(String name, LogFormatter formatter, Collection<LogHandler> handlers) {
        super(name, formatter,handlers);
        info("Starting Prematic sync logging service");
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
