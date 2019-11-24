/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.04.19 18:43
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

import org.fusesource.jansi.internal.WindowsSupport;

import java.util.Locale;

class WindowsTerminal implements Terminal{

    public static final Boolean AVAILABLE = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win");

    public int defaultConsoleMode;

    @Override
    public int getWidth() {
        return WindowsSupport.getWindowsTerminalWidth();
    }

    @Override
    public int getHeight() {
        return WindowsSupport.getWindowsTerminalHeight();
    }

    @Override
    public boolean isAvailable() {
        return AVAILABLE;
    }

    @Override
    public boolean isAnsiSupported() {
        //Check if xterm is used.
        String term = System.getenv("TERM");
        boolean xterm = term!= null && term.equals("xterm");

        //Check if the program is running over a cygwin instance (CygWin allows to run native linux applications on windows).
        String pwd = System.getenv("PWD");
        boolean cygwin = pwd != null && pwd.startsWith("/") && term!= null && !term.equals("cygwin");

        //Ansi is only on default windows computers available.
        return isAvailable() && !xterm && !cygwin;
    }

    @Override
    public void initialise() {
        defaultConsoleMode = WindowsSupport.getConsoleMode();
        WindowsSupport.setConsoleMode(4);
    }

    @Override
    public void reset() {
        if(defaultConsoleMode > 0) WindowsSupport.setConsoleMode(defaultConsoleMode);
    }
}
