package net.prematic.libraries.logging;

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

public interface PrematicLogger {

    String getName();

    LogLevel getLevel();

    void setLevel(LogLevel level);

    void log(String message);

    void log(Object object);

    void log(LogLevel level,String message);

    void log(LogLevel level,Object object);

    void warn(String message);

    void warn(Object object);

    void error(String message);

    void error(Object object);

    void error(Throwable throwable);

    void debug(String message);

    void debug(Object object);

    void shutdown();

}
