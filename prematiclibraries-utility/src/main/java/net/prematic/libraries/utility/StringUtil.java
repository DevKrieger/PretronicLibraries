package net.prematic.libraries.utility;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.03.19 22:39
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    public static final char[] DEFAULT_SUBSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ".toCharArray();

    public static String getRandomString(final int size,char[] subset){
        char[] chars = new char[size];
        for(int i=0;i<chars.length;i++) chars[i] = subset[GeneralUtil.RANDOM.nextInt(subset.length)];
        return new String(chars);
    }

    public static String getRandomString(int size) {
        return getRandomString(size, DEFAULT_SUBSET);
    }

    public static String reverse(String string){
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();
        for(int i = chars.length-1;i > -1;i--) builder.append(chars[i]);
        return builder.toString();
    }

    public static boolean equalsOne(String string, String... values){
        for(String value : values) if(value.equalsIgnoreCase(string)) return true;
        return false;
    }

    public static boolean equalsAll(String string, String... values){
        for(String value : values) if(!value.equalsIgnoreCase(string)) return false;
        return true;
    }

    public static String[] splitAndKeep(String input, String regex) {
        return splitAndKeep(input, regex,0);
    }

    public static String[] splitAndKeep(String input, String regex, int offset) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        int position = 0;
        while (matcher.find()) {
            result.add(input.substring(position, matcher.end()-1-offset));
            result.add(String.valueOf(input.charAt(matcher.end()-1-offset)));
            position = matcher.end()-offset;
        }
        if(position < input.length()) result.add(input.substring(position));
        return result.toArray(new String[0]);
    }
}
