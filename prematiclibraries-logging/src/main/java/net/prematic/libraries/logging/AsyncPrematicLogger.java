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
import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is a simple async implementation of the prematic logging framework.
 */
public class AsyncPrematicLogger extends AbstractPrematicLogger {

    private final BlockingQueue<LogRecord> queue;
    private final AsyncQueuePrinter printer;

    public AsyncPrematicLogger(){
        this("Unknown");
    }

    public AsyncPrematicLogger(String name){
        this(name,new DefaultLogFormatter());
    }

    public AsyncPrematicLogger(String name, LogFormatter formatter){
        this(name,formatter,null);
    }

    public AsyncPrematicLogger(String name, LogFormatter formatter, Collection<LogHandler> handlers) {
        super(name, formatter,handlers);
        this.queue = new LinkedBlockingQueue<>();
        this.printer = new AsyncQueuePrinter(this,this.queue);
        this.printer.start();
        info("Starting Prematic async logging service");
    }

    public void formatAndWrite(MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable throwable, Thread thread){
        if(!canLog(logLevel)) return;
        long timeStamp = System.currentTimeMillis();
        LogRecord record = new LogRecord(timeStamp,info, logLevel, debugLevel, message, throwable, thread);
        queue.offer(record);
    }

    @Override
    public void shutdown() {
        this.printer.interrupt();
        for(LogHandler handler : handlers) handler.shutdown();
    }

}
