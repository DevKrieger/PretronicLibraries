package net.prematic.libraries.logging.handler;

import net.prematic.libraries.logging.SimplePrematicLogger;

import java.util.logging.LogRecord;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public abstract class LogHandler {

    private String name;
    private SimplePrematicLogger logger;

    public LogHandler(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public SimplePrematicLogger getLogger() {
        return logger;
    }
    public void setLogger(SimplePrematicLogger logger) {
        this.logger = logger;
    }
    public abstract void onInit(SimplePrematicLogger logger);
    public abstract void log(LogRecord record, String formatedmessage);
    public abstract void shutdown();
}
