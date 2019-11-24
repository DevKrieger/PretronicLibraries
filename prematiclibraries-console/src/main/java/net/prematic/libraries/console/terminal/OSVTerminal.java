/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 13.04.19 14:49
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

package net.prematic.libraries.console.terminal;

import java.util.Locale;

class OSVTerminal implements Terminal{

    public static final Boolean AVAILABLE = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("osv");

    public static Object STTY;

    public OSVTerminal() {
        try {
            if (STTY == null) STTY = Class.forName("com.cloudius.util.Stty").getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}
    }

    @Override
    public int getWidth() {
        return UnknownTerminal.DEFAULT_WIDTH;
    }

    @Override
    public int getHeight() {
        return UnknownTerminal.DEFAULT_HEIGHT;
    }

    @Override
    public boolean isAvailable() {
        return AVAILABLE;
    }

    @Override
    public boolean isAnsiSupported() {
        return true;
    }

    @Override
    public void initialise() {
        if(STTY != null) {
            try{
                Class.forName("com.cloudius.util.Stty").getMethod("jlineMode").invoke(STTY);
            }catch (Exception ignored){}
        }
    }

    @Override
    public void reset() {
        if(STTY != null) {
            try{
                Class.forName("com.cloudius.util.Stty").getMethod("reset").invoke(STTY);
            }catch (Exception ignored){}
        }
        System.out.println();
    }
}
