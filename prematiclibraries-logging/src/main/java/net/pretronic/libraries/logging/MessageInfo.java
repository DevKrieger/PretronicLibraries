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

package net.pretronic.libraries.logging;

/**
 * The message info object contains information about a log, it defines the message.
 */
public class MessageInfo {

    private final int id;
    private final String description;
    private final String helpUrl;
    private final LoggableService service;

    public MessageInfo(int id) {
        this(id,(LoggableService)null);
    }

    public MessageInfo(int id, String description) {
        this(id,description,null,null);
    }

    public MessageInfo(LoggableService service) {
        this(-1,null,null,service);
    }

    public MessageInfo(int id, LoggableService service) {
        this(id,null,null,service);
    }


    public MessageInfo(int id, String description, String helpUrl, LoggableService service) {
        this.id = id;
        this.description = description;
        this.helpUrl = helpUrl;
        this.service = service;
    }

    /**
     * Get the id of the log message.
     *
     * <p>This is useful for identifying log messages and helps the administrator to find messages or errors.</p>
     *
     * @return The if of the log message.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the description of the message. This is manly for errors and WARNs, for giving
     * information why this error happened and what they have to do to fix it.
     *
     * @return The description of the message.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the help url of the message.
     *
     * <p>This is useful for errors and WARNs</p>
     *
     * @return The help url of the message.
     */
    public String getHelpUrl() {
        return helpUrl;
    }

    /**
     * Get the service which created this log message.
     *
     * <p>This is for identify the log creator in a big program and helps finding errors in the project.</p>
     *
     * @return The service which created this log.
     */
    public LoggableService getService() {
        return service;
    }
}

