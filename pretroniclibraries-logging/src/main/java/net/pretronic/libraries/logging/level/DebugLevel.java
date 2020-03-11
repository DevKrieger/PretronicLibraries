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
 * The debug level is additional to the log level and is for separating different debug messages.
 */
public final class DebugLevel {

    /**
     * For necessary small debugs.
     */
    public static final DebugLevel SIMPLE = new DebugLevel("SIMPLE",0);

    /**
     * The normal default debug level.
     */
    public static final DebugLevel NORMAL = new DebugLevel("SIMPLE",10000);

    /**
     * The height debug level is for debugging often occurring messages
     * like connection which are disconnecting or connections or (http) requests.
     */
    public static final DebugLevel HEIGHT = new DebugLevel("HEIGHT",20000);

    /**
     * The extended level is for extra an not very necessary debugs like packet transfer.
     */
    public static final DebugLevel EXTEND = new DebugLevel("EXTEND",30000);

    /**
     * Everything is enabled.
     */
    public static final DebugLevel ALL = new DebugLevel("ALL",Integer.MAX_VALUE);

    private final String name;
    private final int height;

    public DebugLevel(String name, int height) {
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
     * Check if another debug level can log (is higher or same).
     *
     * <p>In loggers the debug log level must also be enabled.</p>
     *
     * @param level The level which should be checked.
     * @return If this level is log able or not.
     */
    public boolean canLog(DebugLevel level){
        return this.height >=level.height;
    }


}
