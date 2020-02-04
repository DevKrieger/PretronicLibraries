package net.prematic.libraries.logging.handler;

import net.prematic.libraries.logging.LogRecord;
import net.prematic.libraries.logging.level.LogLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConsoleHandler implements LogHandler {

    private final OutputStream outputStream;
    private final OutputStream errorStream;
    private final Charset charset;

    public ConsoleHandler() {
        this(System.out,System.out);
    }

    public ConsoleHandler(OutputStream outputStream, OutputStream errorStream) {
        this(outputStream,errorStream,StandardCharsets.UTF_8);
    }

    public ConsoleHandler(OutputStream outputStream, OutputStream errorStream, Charset charset) {
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.charset = charset;
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void handleLog(LogRecord record, String formattedMessage) throws IOException {
        OutputStream output;
        if(record.getLogLevel() == LogLevel.ERROR) output = errorStream;
        else output = outputStream;
        output.write(formattedMessage.getBytes(charset));
    }

    @Override
    public void shutdown() {
        //Unused
    }
}
