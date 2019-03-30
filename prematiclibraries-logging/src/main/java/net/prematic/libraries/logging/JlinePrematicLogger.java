/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 27.03.19 12:42
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

import jline.console.ConsoleReader;
import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.format.SimpleLogFormatter;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This logger writes all log message over the jline console reader.
 */
public class JlinePrematicLogger extends AbstractPrematicLogger {

    private final ConsoleReader reader;
    private final BlockingQueue<Runnable> queue;
    private final AsyncQueuePrinter printer;

    public JlinePrematicLogger(ConsoleReader reader) {
        this(reader,"Unknown");
    }

    public JlinePrematicLogger(ConsoleReader reader, String name) {
        this(reader,name,new SimpleLogFormatter());
    }

    public JlinePrematicLogger(ConsoleReader reader,String name, LogFormatter formatter) {
        super(name, formatter);

        this.reader =  reader;
        this.queue = new LinkedBlockingQueue<>();
        this.printer = new AsyncQueuePrinter(this.queue);
        this.printer.start();
    }

    public void formatAndWrite(MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable throwable, Thread thread){
        if(!canLog(logLevel)) return;
        long timeStamp = System.currentTimeMillis();
        this.queue.add(() -> {
            try{
                LogRecord record = new LogRecord(timeStamp,info, logLevel, debugLevel, message, throwable, thread);
                String result = formatter.format(this,record);
                reader.print(result);
                reader.drawLine();
                reader.flush();
                for(LogHandler handler : handlers) handler.handleLog(record,result);
            }catch (Exception exception){}
        });
    }

    @Override
    public void shutdown() {
        this.printer.interrupt();
        for(LogHandler handler : handlers) handler.shutdown();
    }

}
