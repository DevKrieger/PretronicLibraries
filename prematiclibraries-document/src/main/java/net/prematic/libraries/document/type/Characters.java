/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 15.06.19 14:51
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

package net.prematic.libraries.document.type;

public class Characters {

    public static final char QUOT_1 = '"';
    public static final char QUOT_2 = '\'';
    public static final char DOT = '.';
    public static final char COLON = ':';
    public static final char COMMA = ',';

    public static final char POSITIVE = '+';
    public static final char NEGATIVE = '-';

    public static final char SPACE = ' ';
    public static final char BACK = '\r';
    public static final char TAB = '\t';
    public static final char BREAK = '\n';
    public static final char BACK_SLASH = '\\';
    public static final char BRACE_OPEN = '{';
    public static final char BRACE_CLOSE = '}';
    public static final char SQUARE_BRACKET_OPEN = '[';
    public static final char SQUARE_BRACKET_CLOSE = ']';

    public static final char[] BOOLEAN_TRUE = {'t','r','u','e'};
    public static final char[] BOOLEAN_FALSE = {'f','a','l','s','e'};
    public static final char[] NULL = {'n','u','l','l'};

    public static String ERROR_INVALID_CHARACTER = "Invalid Character";

}
