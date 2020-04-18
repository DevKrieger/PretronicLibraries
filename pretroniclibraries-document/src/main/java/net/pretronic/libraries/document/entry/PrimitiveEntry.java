/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.document.entry;

/**
 * A @{@link PrimitiveEntry} represents a single line entry without nested entries.
 * This entry usually represents a java primitive or special objects like {@link String}, {@link java.util.Date}
 * or {@link java.util.UUID} which are translated by an @{@link net.pretronic.libraries.document.adapter.DocumentAdapter}
 */
public interface PrimitiveEntry extends DocumentEntry{

    /**
     *  Get the raw object without any conversion.
     *
     * @return The unconverted object
     */
    Object getAsObject();

    /**
     * Get the object as string.
     *
     * @return The converted object as string
     */
    String getAsString();

    /**
     * Get the object as character, see @{@link net.pretronic.libraries.utility.Convert#toCharacter(Object)}
     *
     * @return The converted object as character
     */
    char getAsCharacter();

    /**
     * Get the object as boolean, see @{@link net.pretronic.libraries.utility.Convert#toBoolean(Object)} (Object)}
     *
     * @return The converted object as boolean
     */
    boolean getAsBoolean();

    /**
     * Get the object as boolean.
     *
     * @return The converted object as number
     */
    Number getAsNumber();

    /**
     * Get the object as byte, see @{@link net.pretronic.libraries.utility.Convert#toByte(Object)} (Object)} (Object)}
     *
     * @return The converted object as byte
     */
    byte getAsByte();

    /**
     * Get the object as integer, see @{@link net.pretronic.libraries.utility.Convert#toInteger(Object)} (Object)} (Object)} (Object)}
     *
     * @return The converted object as integer
     */
    int getAsInt();

    /**
     * Get the object as long, see @{@link net.pretronic.libraries.utility.Convert#toLong(Object)} (Object)} (Object)} (Object)} (Object)}
     *
     * @return The converted object as long
     */
    long getAsLong();

    /**
     * Get the object as float, see @{@link net.pretronic.libraries.utility.Convert#toFloat(Object)} (Object)} (Object)} (Object)} (Object)} (Object)}
     *
     * @return The converted object as float
     */
    float getAsFloat();

    /**
     * Get the object as short, see @{@link net.pretronic.libraries.utility.Convert#toShort(Object)} (Object)} (Object)} (Object)} (Object)} (Object)} (Object)}
     *
     * @return The converted object as short
     */
    short getAsShort();

    /**
     * Get the object as double, see @{@link net.pretronic.libraries.utility.Convert#toDouble(Object)} (Object)} (Object)} (Object)} (Object)} (Object)} (Object)} (Object)}
     *
     * @return The converted object as double
     */
    double getAsDouble();

    /**
     * Check if the object is null.
     *
     * @return true if object == null
     */
    boolean isNull();

    /**
     * Set the new value
     *
     * @param object The object to set
     */
    void setValue(Object object);

    default PrimitiveEntry copy(){
        return copy(getKey());
    }

    PrimitiveEntry copy(String key);

}
