/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 14.12.19, 16:53
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

package net.prematic.libraries.document.entry;

public interface PrimitiveEntry extends DocumentEntry{

    Object getAsObject();

    String getAsString();

    char getAsCharacter();

    boolean getAsBoolean();

    Number getAsNumber();

    byte getAsByte();

    int getAsInt();

    long getAsLong();

    float getAsFloat();

    short getAsShort();

    double getAsDouble();

    boolean isNull();

    void setValue(Object object);


    default PrimitiveEntry copy(){
        return copy(getKey());
    }

    PrimitiveEntry copy(String key);

}
