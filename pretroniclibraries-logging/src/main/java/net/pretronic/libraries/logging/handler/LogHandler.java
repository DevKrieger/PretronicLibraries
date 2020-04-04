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

import java.io.IOException;

/**
 * This object is for handling and printing records.
 *
 * <p>A log record must be register by the logger and should only be used in one logger.</p>
 */
public interface LogHandler {

    /**
     * Get the name of this logging handler.
     *
     * @return The name of this handler
     */
    String getName();

    /**
     * Handler a log record.
     *
     * @param record The log record which should be handled.
     * @param formattedMessage The formatted message of this record.
     * @throws IOException exception
     */
    void handleLog(LogRecord record, String formattedMessage) throws IOException;

    /**
     * Shut this log handler down.
     */
    void shutdown();

}
