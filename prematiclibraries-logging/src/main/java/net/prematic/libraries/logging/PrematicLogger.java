package net.prematic.libraries.logging;

import net.prematic.libraries.logging.handler.LogHandler;
import jline.console.ConsoleReader;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public class PrematicLogger extends Logger {

    private String messageformat;
    private final Thread worker;
    private final DateFormat dateformat;
    private final LoggingFormatter formatter;
    private final ConsoleReader reader;
    private final BlockingQueue<Runnable> queue;
    private boolean debugging;
    private List<LogHandler> handlers;

    public PrematicLogger(LogHandler... handlers) {
        this("[[time]] [level]: [message]",new SimpleDateFormat("HH:mm:ss"),false,Level.ALL,handlers);
    }
    public PrematicLogger(Boolean debugging, LogHandler... handlers) {
        this("[[time]] [level]: [message]",new SimpleDateFormat("HH:mm:ss"), debugging,Level.ALL,handlers);
    }
    public PrematicLogger(SimpleDateFormat dateformat, Boolean debugging, LogHandler... handlers) {
        this("[[time]] [level]: [message]",dateformat, debugging,Level.ALL, handlers);
    }
    public PrematicLogger(String messageformat,SimpleDateFormat dateformat, Boolean debugging, LogHandler... handlers) {
        this(messageformat,dateformat, debugging,Level.ALL, handlers);
    }
    public PrematicLogger(String messageformat,SimpleDateFormat dateformat, Boolean debugging,Level level, LogHandler... handlers) {
        super("PrematicLogger", null);
        try {
            Field field = Charset.class.getDeclaredField("defaultCharset");
            field.setAccessible(true);
            field.set(null, Charset.forName("UTF-8"));
            setLevel(level);
            this.messageformat = messageformat;
            this.dateformat = dateformat;
            this.handlers = new LinkedList<>(Arrays.asList(handlers));
            this.queue = new LinkedBlockingQueue<>();
            this.worker = new Printer();
            this.formatter = new LoggingFormatter();
            this.reader = new ConsoleReader(System.in, System.out);
            this.reader.setExpandEvents(false);
            this.debugging = debugging;

            FileHandler filehandler = new FileHandler();
            filehandler.setEncoding(StandardCharsets.UTF_8.name());
            filehandler.setFormatter(new LoggingFormatter());
            addHandler(filehandler);

            LoggingHandler logginghandler = new LoggingHandler();
            logginghandler.setFormatter(formatter);
            logginghandler.setEncoding(StandardCharsets.UTF_8.name());
            logginghandler.setLevel(Level.INFO);
            addHandler(logginghandler);

            System.setOut(new PrintStream(new LoggingOutputStream(Level.INFO), true, StandardCharsets.UTF_8.name()));
            System.setErr(new PrintStream(new LoggingOutputStream(Level.SEVERE), true, StandardCharsets.UTF_8.name()));
        }catch (Exception exception) {
            throw new RuntimeException("Could not init prematic logging service ("+exception.getMessage()+")");
        }
    }
    public String getMessageFormat() {
        return this.messageformat;
    }
    public List<LogHandler> getLogHandlers() {
        return this.handlers;
    }
    public ConsoleReader getReader() {
        return this.reader;
    }
    public DateFormat getDateFormat() {
        return this.dateformat;
    }
    public LoggingFormatter getFormatter() {
        return this.formatter;
    }
    public Boolean isDebugging(){
        return this.debugging;
    }
    public void setDebugging(Boolean debugging){
        this.debugging = debugging;
    }
    public void registerHadnler(LogHandler handler){
        handler.setLogger(this);
        handler.onInit(this);
        this.handlers.add(handler);
    }
    public void debug(String message) {
        if(this.debugging) log(Level.WARNING, "[DEBUG] "+message);
    }
    public void shutdown() {
        for(Handler handler : getHandlers()) handler.close();
        for(LogHandler handler : this.handlers) handler.shutdown();
        try {
            this.reader.killLine();
        }catch (IOException e) {}
    }
    private class LoggingOutputStream extends ByteArrayOutputStream {

        private final Level level;

        public LoggingOutputStream(Level level){
            this.level = level;
        }
        public void flush() throws IOException {
            String contents = toString(StandardCharsets.UTF_8.name());
            super.reset();
            if(contents.equalsIgnoreCase("<notext>")) logp(level, "", "","");
            else if(!contents.isEmpty() && !contents.equals(System.getProperty("line.separator"))) logp(level, "", "", contents);
        }
    }
    private class LoggingHandler extends Handler {
        @Override
        public void publish(LogRecord record) {
            if(isLoggable(record)){
                queue.offer(()->{
                    try {
                        String message = ConsoleReader.RESET_LINE+getFormatter().format(record);
                        reader.print(message);
                        reader.drawLine();
                        reader.flush();
                        for(LogHandler handler : handlers) handler.log(record,message);
                    } catch (Exception exception) {}
                });
            }
        }
        @Override
        public void flush() {}
        @Override
        public void close() throws SecurityException { }
    }
    private class LoggingFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();
            if(record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                builder.append(writer).append("\n");
            }
            StringBuilder message = new StringBuilder(ConsoleReader.RESET_LINE)
                    .append(messageformat
                            .replace("[time]",dateformat.format(System.currentTimeMillis()))
                            .replace("[level]",record.getLevel().getName())
                            .replace("[message]",record.getMessage())
                            .replace("[user]",System.getProperty("user.name")))
                    .append("\n").append(builder.substring(0));
            return message.substring(0);
        }
    }
    private class Printer extends Thread {
        public Printer(){
            setPriority(Thread.MIN_PRIORITY);
            setDaemon(true);
            start();
        }
        @Override
        public void run() {
            while(!isInterrupted()) {
                try {
                    Runnable runnable = queue.take();
                    runnable.run();
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}