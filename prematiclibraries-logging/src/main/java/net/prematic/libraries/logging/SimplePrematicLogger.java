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

import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.format.SimpleLogFormatter;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is a simple async implementation of the prematic logging framework.
 */
public class SimplePrematicLogger extends AbstractPrematicLogger {

    private final OutputStream outputStream, errorStream;
    private final Charset charset;
    private final BlockingQueue<Runnable> queue;
    private final AsyncQueuePrinter printer;

    public SimplePrematicLogger(){
        this("Unknown");
    }

    public SimplePrematicLogger(String name){
        this(name,new SimpleLogFormatter());
    }

    public SimplePrematicLogger(String name, LogFormatter formatter){
        this(name,formatter,System.out,System.err);
    }

    public SimplePrematicLogger(String name, LogFormatter formatter, OutputStream outputStream){
        this(name,formatter,outputStream, outputStream);
    }

    public SimplePrematicLogger(String name, LogFormatter formatter, OutputStream outputStream, OutputStream errorStream){
        this(name,formatter,outputStream,errorStream, StandardCharsets.UTF_8);
    }

    public SimplePrematicLogger(String name, LogFormatter formatter, OutputStream outputStream, OutputStream errorStream, Charset charset) {
        super(name, formatter);
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.charset = charset;
        this.queue = new LinkedBlockingQueue<>();
        this.printer = new AsyncQueuePrinter(this.queue);
        this.printer.start();
        info("Starting Prematic logging service.");
    }

    public void formatAndWrite(MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable throwable, Thread thread){
        if(!canLog(logLevel)) return;
        this.queue.offer(()->{
            try{
                long timeStamp = System.currentTimeMillis();
                LogRecord record = new LogRecord(timeStamp,info, logLevel, debugLevel, message, throwable, thread);
                String result = formatter.format(this,record);
                if(logLevel == LogLevel.ERROR) errorStream.write(result.getBytes(charset));
                else outputStream.write(result.getBytes(charset));

                for(LogHandler handler : handlers) handler.handleLog(record,result);
            }catch (Exception ignored){}
        });
    }

    @Override
    public void shutdown() {
        this.printer.interrupt();
        for(LogHandler handler : handlers) handler.shutdown();
    }

}
