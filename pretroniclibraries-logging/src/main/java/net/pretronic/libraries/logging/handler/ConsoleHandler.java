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

package net.pretronic.libraries.logging.handler;

import net.pretronic.libraries.logging.LogRecord;
import net.pretronic.libraries.logging.level.LogLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The console handlers prints messages in the console.
 *
 * <p>The log factory appends this handler automatically.</p>
 */
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
