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

package net.pretronic.libraries.utility;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This small convert library helps you to convert any object in a specified primitive type.
 */
public class Convert {

    /**
     * Convert a input object to a string vale.
     *
     * @param input The object to convert
     * @return The input object as string
     */
    public static String toString(Object input){
        return input != null ? input.toString() : null;
    }

    /**
     * Convert a input object to a character vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as character
     */
    public static char toCharacter(Object input){
        if(input == null) return 0;
        else if(input instanceof Character) return (char) input;
        else if(input instanceof Number) return (char) input;
        else{
            String content = input.toString();
            if(content.length() > 0) return content.charAt(0);
            else throw new IllegalArgumentException("Can not be converted to character " + input);
        }
    }

    /**
     * Convert a input object to a boolean vale.
     *
     * <p>A convert problem will return false</p>
     *
     * @param input The object to convert
     * @return The input object as boolean
     */
    public static boolean toBoolean(Object input){
        if(input == null) return false;
        else if(input instanceof Boolean) return (boolean) input;
        else if(input instanceof Integer) return (Integer) input == 1;
        else if(input instanceof Long) return (Long) input == 1;
        else if(input instanceof Double) return (Double) input == 1;
        else if(input instanceof Float) return (Float) input == 1;
        else if(input instanceof Byte) return (Byte) input == 1;
        else if(input instanceof Number) return ((Number) input).intValue() == 1;
        else{
            String textInput = input.toString().trim();
            if(textInput.equalsIgnoreCase("true")) return true;
            else if(textInput.equalsIgnoreCase("false")) return false;
            else if(textInput.equalsIgnoreCase("on")) return true;
            else if(textInput.equalsIgnoreCase("off")) return false;
            else if(textInput.equalsIgnoreCase("enabled")) return true;
            else if(textInput.equalsIgnoreCase("disabled")) return false;
        }
        throw new IllegalArgumentException("Can not be converted to character " + input);
    }

    /**
     * Convert a input object to a integer vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as integer
     */
    public static int toInteger(Object input){
        if(input == null) return 0;
        else if(input instanceof Number) return ((Number) input).intValue();
        else if(input instanceof Character) return (int)input;
        else if(input instanceof Boolean) return ((Boolean)input?1:0);
        else{
            try{
                return Integer.parseInt(input.toString());
            }catch (NumberFormatException ignored){
                throw new IllegalArgumentException("Can not be converted to integer  " + input);
            }
        }
    }

    /**
     * Convert a input object to a long vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as long
     */
    public static long toLong(Object input){
        if(input == null) return 0;
        else if(input instanceof Number) return ((Number) input).longValue();
        else if(input instanceof Character) return (int)input;
        else if(input instanceof Boolean) return ((Boolean)input?1:0);
        else{
            try{
                return Long.parseLong(input.toString());
            }catch (NumberFormatException ignored){
                throw new IllegalArgumentException("Can not be converted to long " + input);
            }
        }
    }

    /**
     * Convert a input object to a double vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as double
     */
    public static double toDouble(Object input){
        if(input == null) return 0;
        else if(input instanceof Number) return ((Number) input).doubleValue();
        else if(input instanceof Character) return (int)input;
        else if(input instanceof Boolean) return ((Boolean)input?1:0);
        else{
            try{
                return Double.parseDouble(input.toString());
            }catch (NumberFormatException ignored){
                throw new IllegalArgumentException("Can not be converted to double " + input);
            }
        }
    }

    /**
     * Convert a input object to a float vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as float
     */
    public static float toFloat(Object input){
        if(input == null) return 0;
        else if(input instanceof Number) return ((Number) input).floatValue();
        else if(input instanceof Character) return (int)input;
        else if(input instanceof Boolean) return ((Boolean)input?1:0);
        else{
            try{
                return Float.parseFloat(input.toString());
            }catch (NumberFormatException ignored){
                throw new IllegalArgumentException("Can not be converted to float " + input);
            }
        }
    }

    /**
     * Convert a input object to a short vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as short
     */
    public static short toShort(Object input){
        if(input == null) return 0;
        else if(input instanceof Number) return ((Number) input).shortValue();
        else if(input instanceof Character) return (byte)input;
        else if(input instanceof Boolean) return (short) ((Boolean)input?1:0);
        else{
            try{
                return Short.parseShort(input.toString());
            }catch (NumberFormatException ignored){
                throw new IllegalArgumentException("Can not be converted to short " + input);
            }
        }
    }

    /**
     * Convert a input object to a byte vale.
     *
     * <p>A convert problem will return 0</p>
     *
     * @param input The object to convert
     * @return The input object as byte
     */
    public static byte toByte(Object input){
        if(input == null) return 0;
        else if(input instanceof Number) return ((Number) input).byteValue();
        else if(input instanceof Character) return (byte)input;
        else if(input instanceof Boolean) return (byte) ((Boolean)input?1:0);
        else{
            try{
                return Byte.parseByte(input.toString());
            }catch (NumberFormatException ignored){
                throw new IllegalArgumentException("Can not be converted to byte " + input);
            }
        }
    }

    public static UUID toUUID(Object input) {
        if(input == null) return null;
        else if(input instanceof UUID) return (UUID) input;
        else if(input instanceof byte[]) {
            ByteBuffer byteBuffer = ByteBuffer.wrap((byte[]) input);
            return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
        } else if(input instanceof String) {
            if(((String)input).contains("-")) {
                return UUID.fromString((String) input);
            } else {
                try {
                    StringBuilder dashBuilder = new StringBuilder((String)input)
                            .insert(20, '-')
                            .insert(16, '-')
                            .insert(12, '-')
                            .insert(8, '-');
                    return UUID.fromString(dashBuilder.toString());
                } catch (IndexOutOfBoundsException | IllegalArgumentException exception) {
                    throw new IllegalArgumentException("Can not be converted to uuid " + input);
                }
            }
        }
        throw new IllegalArgumentException("Can not be converted to uuid " + input);
    }

    public static Date toDate(Object input) {
        if(input == null) return null;
        else if(input instanceof Date) return (Date) input;
        else if(input instanceof Long) return new Date((long) input);
        throw new IllegalArgumentException("Can not be converted to date " + input);
    }

    public static TimeUnit toTimeUnit(Object input) {
        if(input == null) return null;
        else if(input instanceof TimeUnit) return (TimeUnit) input;
        else{
            String rawUnit = input.toString();
            TimeUnit unit = GeneralUtil.valueOfEnumOrNull(TimeUnit.class, rawUnit.toUpperCase());
            if(unit != null) return unit;
            if(StringUtil.equalsOne(rawUnit,"day","d")) return TimeUnit.DAYS;
            else if(StringUtil.equalsOne(rawUnit,"hour","h")) return TimeUnit.HOURS;
            else if(StringUtil.equalsOne(rawUnit,"minute","m")) return TimeUnit.MINUTES;
            else if(StringUtil.equalsOne(rawUnit,"second","s")) return TimeUnit.SECONDS;
            throw new IllegalArgumentException("Can not be converted to TimeUnit " + input);
        }
    }

    public static BigDecimal toBigDecimal(Object input) {
        if(input == null) return null;
        else if(input instanceof BigDecimal) return (BigDecimal) input;
        else if(input instanceof Double) return BigDecimal.valueOf((double) input);
        else if(input instanceof Long) return BigDecimal.valueOf((long) input);
        else if(input instanceof Integer) return BigDecimal.valueOf((int) input);
        else if(input instanceof String) return new BigDecimal((String) input);
        throw new IllegalArgumentException("Can not be converted to BigDecimal " + input);
    }
}
