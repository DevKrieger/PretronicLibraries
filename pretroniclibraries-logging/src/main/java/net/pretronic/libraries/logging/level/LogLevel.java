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

package net.pretronic.libraries.logging.level;

/**
 * The log levels controls if a message can be logged or not, you are able to create custom levels.
 */
public final class LogLevel {

    /**
     * Is used when the logger output is disabled (Use this only for emergency information).
     */
    public final static LogLevel OFF = new LogLevel("OFF",Integer.MIN_VALUE);

    /**
     * Is used for exceptions in the program.
     */
    public final static LogLevel ERROR = new LogLevel("ERROR",0);

    /**
     * Is used for WARN or not necessary errors.
     */
    public final static LogLevel WARN = new LogLevel("WARN",10000);

    /**
     * Is used for normal information output an is the default level.
     */
    public final static LogLevel INFO = new LogLevel("INFO",20000);

    /**
     * Is used for developer debugs
     */
    public final static LogLevel DEBUG = new LogLevel("DEBUG",30000);

    /**
     * Everything is enabled (You can use this for very height unnecessary messages).
     */
    public final static LogLevel ALL = new LogLevel("ALL",Integer.MAX_VALUE);

    private final String name;
    private final int height;

    public LogLevel(String name, int height) {
        this.name = name;
        this.height = height;
    }

    /**
     * Get the name of the level.
     *
     * @return The name of the level.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the height of the level, is used for compering and checking different levels.
     *
     * @return The height of the level.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Check if another level can log (is higher or same).
     *
     * @param level The level which should be checked.
     * @return If this level is log able or not.
     */
    public boolean canLog(LogLevel level){
        return this.height >=level.height;
    }

}
