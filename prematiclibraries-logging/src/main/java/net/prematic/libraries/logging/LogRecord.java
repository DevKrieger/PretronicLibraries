/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 26.03.19 19:08
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

import net.prematic.libraries.logging.level.DebugLevel;
import net.prematic.libraries.logging.level.LogLevel;

/**
 * The LogRecord object is for processing log messages in the Prematic logging framework
 *
 * <p>This object is final and should only be used for the logging process an no longer by the application.
 *
 * <p>Note: everything in this class can be null.</p>
 */
public final class LogRecord {

    private final long timeStamp;
    private final MessageInfo info;
    private final LogLevel logLevel;
    private final DebugLevel debugLevel;
    private final String message;
    private final Throwable thrown;
    private final Thread thread;

    public LogRecord(long timeStamp,MessageInfo info, LogLevel logLevel, DebugLevel debugLevel, String message, Throwable thrown, Thread thread) {
        this.timeStamp = timeStamp;
        this.info = info;
        this.logLevel = logLevel;
        this.debugLevel = debugLevel;
        this.message = message;
        this.thrown = thrown;
        this.thread = thread;
    }

    /**
     * Get the logging time in milliseconds.
     *
     * @return logging time in milliseconds
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Get the info
     *
     * @return The info object
     */
    public MessageInfo getInfo() {
        return info;
    }

    /**
     * Get the log level of this record.
     *
     * @return The log level
     */
    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Get the debug level of this record.
     *
     * <p>Only used with the debug log level</p>
     *
     * @return The debug level
     */
    public DebugLevel getDebugLevel() {
        return debugLevel;
    }

    /**
     * Get the raw log message (Without format).
     *
     * @return The raw log message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the exception of this record.
     *
     * <p>Only used by errors.</p>
     *
     * @return
     */
    public Throwable getThrown() {
        return thrown;
    }

    /**
     * Get the thread, from which the log message or error comes.
     *
     * @return The thread
     */
    public Thread getThread() {
        return thread;
    }

    @Override
    public String toString() {
        return timeStamp+"/"+thread.getName()+"/"+logLevel+"/"+message;
    }
}
