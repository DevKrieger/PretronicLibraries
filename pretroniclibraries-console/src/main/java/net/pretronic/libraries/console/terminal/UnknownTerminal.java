/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.console.terminal;

public class UnknownTerminal implements Terminal{

    public static final int DEFAULT_WIDTH = 80;

    public static final int DEFAULT_HEIGHT = 30;

    private boolean available;
    private boolean ansiSupported;
    private int width;
    private int height;

    public UnknownTerminal() {
        this(false,false);
    }

    public UnknownTerminal(boolean available, boolean ansiSupported) {
        this(available,ansiSupported,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public UnknownTerminal(boolean available, boolean ansiSupported, int width, int height) {
        this.available = available;
        this.ansiSupported = ansiSupported;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean isAnsiSupported() {
        return ansiSupported;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setAnsiSupported(boolean ansiSupported) {
        this.ansiSupported = ansiSupported;
    }

    @Override
    public void initialise() {
        //Unused
    }

    @Override
    public void reset() {
        //Unused
    }

}
