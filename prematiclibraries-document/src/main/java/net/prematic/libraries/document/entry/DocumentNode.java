/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 14.12.19, 19:54
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

import net.prematic.libraries.document.DocumentContext;
import net.prematic.libraries.utility.annonations.Internal;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface DocumentNode extends DocumentBase,Iterable<DocumentEntry>{

    DocumentContext getContext();

    void setContext(DocumentContext context);


    <T> T getAsObject(Class<T> classOf);

    <T> T getAsObject(Type type);

    <T> T getAsObject(TypeReference<T> reference);


    //Sub entries

    List<DocumentEntry> entries();

    void setEntries(List<DocumentEntry> entries);

    Set<String> keys();

    int size();


    DocumentEntry getEntry(String key);

    DocumentEntry getEntry(int index);

    default DocumentEntry getEntry(String[] keys){
        return getEntry(keys,0);
    }

    DocumentEntry getEntry(String[] keys, int offset);

    DocumentEntry getFirst();

    DocumentEntry getLast();


    String getString(String key);

    char getCharacter(String key);

    boolean getBoolean(String key);

    Number getNumber(String key);

    byte getByte(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    short getShort(String key);

    double getDouble(String key);


    boolean contains(String key);

    boolean containsOne(String... keys);

    boolean containsMany(String... keys);


    //Update entry

    DocumentNode set(String key, Object value);

    default DocumentNode rename(String source,String destination){
        DocumentEntry entry = getEntry(source);
        if(entry != null){
            set(destination,entry);
            remove(source);
        }
        return this;
    }


    @Internal
    void addEntry(DocumentEntry entry);

    @Internal
    void removeEntry(DocumentEntry entry);


    DocumentNode remove(String key);

    DocumentNode clear();


    //Update current entry

    Stream<DocumentEntry> stream();

    DocumentNode sort(Comparator<DocumentEntry> sorter);


    DocumentNode copy();


    //Extra implementation

    default boolean isEmpty(){
        return size() <= 0;
    }

    default String getString(String key, String defaultValue){
        return contains(key)?getString(key):defaultValue;
    }

    default char getCharacter(String key, char defaultValue){
        return contains(key)?getCharacter(key):defaultValue;
    }

    default boolean getBoolean(String key, boolean defaultValue){
        return contains(key)?getBoolean(key):defaultValue;
    }

    default byte getByte(String key, byte defaultValue){
        return contains(key)?getByte(key):defaultValue;
    }

    default int getInt(String key, int defaultValue){
        return contains(key)?getInt(key):defaultValue;
    }

    default long getLong(String key, long defaultValue){
        return contains(key)?getLong(key):defaultValue;
    }

    default float getFloat(String key, float defaultValue){
        return contains(key)?getFloat(key):defaultValue;
    }

    default short getShort(String key, short defaultValue){
        return contains(key)?getShort(key):defaultValue;
    }

    default double getDouble(String key, double defaultValue){
        return contains(key)?getDouble(key):defaultValue;
    }


    default DocumentNode add(String key, Object value){
        if(value != null && !contains(key)) set(key, value);
        return this;
    }

}
