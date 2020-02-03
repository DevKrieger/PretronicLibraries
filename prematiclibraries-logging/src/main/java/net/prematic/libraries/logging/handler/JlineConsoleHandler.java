package net.prematic.libraries.logging.handler;

import jline.console.ConsoleReader;
import net.prematic.libraries.logging.LogRecord;

import java.io.IOException;

public class JlineConsoleHandler implements LogHandler{

    private final ConsoleReader reader;

    public JlineConsoleHandler(ConsoleReader reader) {
        this.reader = reader;
    }

    @Override
    public String getName() {
        return "Jline Console";
    }

    @Override
    public void handleLog(LogRecord record, String formattedMessage) throws IOException {
        reader.print(formattedMessage);
        reader.drawLine();
        reader.flush();
    }

    @Override
    public void shutdown() {
        //Unused
    }
}
