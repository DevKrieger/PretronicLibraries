/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Philipp Elvin Friedhoff
 * @since 23.08.19, 21:30
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

package net.prematic.libraries.utility;

import java.util.regex.Pattern;

public final class Validate {

    public static void isTrue(boolean expression, String message, Object... objects) {
        if(!expression) throw new IllegalArgumentException(String.format(message, objects));
    }

    public static void isTrue(boolean expression, String message) {
        if(!expression) throw new IllegalArgumentException(message);
    }

    public static void checkMatches(CharSequence value, Pattern pattern, String message, Object... objects) {
        if(value == null || pattern == null || !pattern.matcher(value).matches()) throw new IllegalArgumentException(String.format(message, objects));
    }

    public static void checkMatches(CharSequence value, Pattern pattern, String message) {
        if(value == null || pattern == null || !pattern.matcher(value).matches()) throw new IllegalArgumentException(message);
    }
}