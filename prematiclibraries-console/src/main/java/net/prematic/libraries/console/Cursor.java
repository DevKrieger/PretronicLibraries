/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.04.19 18:35
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

package net.prematic.libraries.console;

public class Cursor {

    public final PConsole console;
    public int top, left;

    public Cursor(PConsole console) {
        this.console = console;
        this.top = 0;
        this.left = 0;
    }

    public int getTop(){
        return this.top;
    }

    public int getLeft(){
        return this.left;
    }

    public void moveUp(){
        moveUp(1);
    }

    public void moveUp(int count){
        this.console.printAnsiSequence(count+"A");
    }

    public void moveDown(){
        moveDown(1);
    }

    public void moveDown(int count){
        this.console.printAnsiSequence(count+"B");
    }

    public void moveRight(){
        moveRight(1);
    }

    public void moveRight(int count){
        this.console.printAnsiSequence(count+"C");
    }

    public void moveLeft(){
        moveLeft(1);
    }

    public void moveLeft(int count){
        this.console.printAnsiSequence(count+"D");
    }

    public void moveToNextLine(){
        this.console.printAnsiSequence("E");
    }

    public void moveToScreenEnd(){
        this.console.printAnsiSequence("H");
    }

    public void moveToScreenStart(){
        this.console.printAnsiSequence("D");
    }


    public void moveToStart(){
        reset();
    }

    public void changePosition(int top, int left){
        setInternalPosition(top,left);
        console.printAnsiSequence(top+";"+left+"R");
    }

    public void setInternalPosition(int top, int left){
        this.top = top;
        this.left = left;
    }

    public void reset(){
        changePosition(0,0);
    }


}
