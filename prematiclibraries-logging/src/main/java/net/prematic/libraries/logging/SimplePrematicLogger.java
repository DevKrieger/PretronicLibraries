package net.prematic.libraries.logging;

import net.prematic.libraries.logging.format.DefaultLogFormatter;
import net.prematic.libraries.logging.format.LogFormatter;
import net.prematic.libraries.logging.handler.ConsoleHandler;
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Deprecated
public class SimplePrematicLogger implements PrematicLogger{

    private final PrematicLogger original;

    public SimplePrematicLogger(String name){
        this(name,new DefaultLogFormatter());
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
        original = new AsyncPrematicLogger(name,formatter);
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
