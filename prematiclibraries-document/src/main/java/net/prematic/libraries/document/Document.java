/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.06.19 21:42
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

package net.prematic.libraries.document;

import net.prematic.libraries.utility.parser.StringParser;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;

public interface Document extends  Iterable<DocumentEntry>,DocumentEntry {

    //Current entry

    <T> T getAsObject(Class<T> classOf);

    <T> T getAsObject(Type type);

    <T> T getAsObject(TypeReference<T> reference);

    Array getAsArray();

    <A> A getAsArray(A type);

    <V> Collection<V> getAsCollection(Class<V> valueClass);

    <V> List<V> getAsList(Class<V> valueClass);

    <K,V> Map<K,V> getAsMap(Class<K> keyClass, Class<V> valueClass);



    //Sub entries

    DocumentEntry getEntry(String key);

    DocumentEntry getEntry(int index);

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
    
    Document getDocument(String key);

    Array getArray(String key);

    <A> A getArray(String key,A type);

    <V> Collection<V> getCollection(String key, Class<V> valueClass);

    <V> List<V> getList(String key, Class<V> valueClass);

    <K,V> Map<K,V> getMap(String key, Class<K> keyClass, Class<V> valueClass);

    <T> T getObject(String key, Class<T> classOf);

    <T> T getObject(String key, Type type);

    <T> T getObject(String key, TypeReference<T> reference);

    //entry info

    List<DocumentEntry> entries();

    Set<String> keys();

    int size();

    boolean contains(String key);

    boolean containsOne(String... keys);

    boolean containsAll(String... keys);

    boolean isObject(String key);

    boolean isPrimitive(String key);

    boolean isArray(String key);


    //Update entry

    Document set(String key, Object value);

    Document remove(String key);

    Document clear();

    Stream<DocumentEntry> stream(String key);

    Iterable<DocumentEntry> iterate(String key);

    Document sort(String key, Comparator<DocumentEntry> sorter);


    //Update current entry

    Stream<DocumentEntry> stream();

    Document sort(Comparator<DocumentEntry> sorter);

    Document copy(String key);

    //Save entry

    String write(String type,boolean pretty);

    void write(File location, boolean pretty);

    void write(String type, File location, boolean pretty);

    void write(String type, OutputStream output, boolean pretty);

    void write(String type, Writer output, boolean pretty);


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

    default Document getDocument(String key, Document defaultValue){
        return contains(key)?getDocument(key):defaultValue;
    }

    default <V> Collection<V> getCollection(String key, Class<V> valueClass, Collection<V> defaultValue){
        return contains(key)?getCollection(key, valueClass):defaultValue;
    }

    default <K,V> Map<K,V> getMap(String key, Class<K> keyClass, Class<V> valueClass, Map<K, V> defaultValue){
        return contains(key)?getMap(key,keyClass, valueClass):defaultValue;
    }

    default Document add(String key, Object value){
        if(value != null && !contains(key)) set(key, value);
        return this;
    }

    default String write(String type){
        return write(type,true);
    }

    default void write(File location){
        write(location, true);
    }

    default void write(String type, File location){
        write(type, location,true);
    }

    default void write(String type, OutputStream output){
        write(type, output,true);
    }

    default void write(String type, Writer output){
        write(type, output,true);
    }

    //Static content

    static DocumentFactory factory(){
        return DocumentRegistry.getFactory();
    }

    static Document newDocument(){
        return DocumentRegistry.getFactory().newDocument();
    }

    static Document newDocument(Object object){
        return DocumentRegistry.serialize(object).toDocument();
    }

    static Document read(File location){
        return DocumentRegistry.getTypeByEnding(location.getName().substring(0,location.getName().lastIndexOf("."))).getReader().read(location);
    }

    static Document read(File location, Charset charset){
        return DocumentRegistry.getTypeByEnding(location.getName().substring(0,location.getName().lastIndexOf("."))).getReader().read(location, charset);
    }

    static Document read(String type, String content){
        return DocumentRegistry.getType(type).getReader().read(content);
    }

    static Document read(String type, File location){
        return DocumentRegistry.getType(type).getReader().read(location);
    }

    static Document read(String type, File location, Charset charset){
        return DocumentRegistry.getType(type).getReader().read(location,charset);
    }

    static Document read(String type, InputStream input){
        return DocumentRegistry.getType(type).getReader().read(input);
    }

    static Document read(String type, InputStream input, Charset charset){
        return DocumentRegistry.getType(type).getReader().read(input, charset);
    }

    static Document read(String type, StringParser parser){
        return DocumentRegistry.getType(type).getReader().read(parser);
    }
}