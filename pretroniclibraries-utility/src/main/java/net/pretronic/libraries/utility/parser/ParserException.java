/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
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

package net.pretronic.libraries.utility.parser;

public class ParserException extends RuntimeException {

    private final StringParser parser;
    private final int line;
    private final int index;

    public ParserException(StringParser parser, int line, int index, String message) {
        super(message);
        this.parser = parser;
        this.line = line;
        this.index = index;
    }

    public StringParser getParser() {
        return parser;
    }

    public int getLine() {
        return line;
    }

    public int getIndex() {
        return index;
    }

    public ParserException(StringParser parser, int line, int index, String message, Throwable cause) {
        super(message, cause);
        this.parser = parser;
        this.line = line;
        this.index = index;
    }
}
