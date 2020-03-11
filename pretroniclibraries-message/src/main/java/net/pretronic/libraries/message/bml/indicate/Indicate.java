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

package net.pretronic.libraries.message.bml.indicate;

public class Indicate {

    private final IndicateType type;
    private final char prefix;
    private final char start;
    private final char end;

    public Indicate(IndicateType type, char start, char end) {
        this(type,start,end,(char)255);
    }

    public Indicate(IndicateType type, char start, char end, char prefix) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.prefix = prefix;
    }

    public IndicateType getType() {
        return type;
    }

    public char getPrefix() {
        return prefix;
    }

    public char getStart() {
        return start;
    }

    public char getEnd() {
        return end;
    }

    public char getParameterSeparator(){
        return ' ';
    }

    public boolean hasPrefix(){
        return prefix == 255;
    }

    public boolean isParametrized(){
        return false;
    }

    public boolean areSubIndicatesAllowed(){
        return false;
    }

}
