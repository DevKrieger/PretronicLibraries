/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 22:36
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

import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.entry.DocumentNode;
import net.prematic.libraries.document.utils.ConfigurationUtil;
import net.prematic.libraries.utility.annonations.Internal;
import net.prematic.libraries.utility.parser.StringParser;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The {@link Document} is the main object of the documentation library. A {@link Document} is a
 * node and holds different types of primitives or node entries.
 *
 * The {@link Document} provides many methods for getting and transforming the data.
 * There are also methods for reading and writing documents in file or streams.
 */
public interface Document extends DocumentNode, DocumentEntry {

    Array getAsArray();

    <A> A getAsArray(A type);

    <V> Collection<V> getAsCollection(Class<V> valueClass);

    <V> List<V> getAsList(Class<V> valueClass);

    <K,V> Map<K,V> getAsMap(Class<K> keyClass, Class<V> valueClass);



    //Sub entries
    
    Document getDocument(String key);

    Array getArray(String key);

    <A> A getArray(String key,A type);

    <V> Collection<V> getCollection(String key, Class<V> valueClass);

    <V> List<V> getList(String key, Class<V> valueClass);

    <K,V> Map<K,V> getMap(String key, Class<K> keyClass, Class<V> valueClass);

    <T> T getObject(String key, Class<T> classOf);

    <T> T getObject(String key, Type type);

    <T> T getObject(String key, TypeReference<T> reference);

    //Update entry

    Document set(String key, Object value);

    boolean isArray(String key);

    boolean isObject(String key);

    boolean isPrimitive(String key);

    default Document rename(String source,String destination){
        DocumentEntry entry = getEntry(source);
        if(entry != null) entry.setKey(destination);
        return this;
    }

    Stream<DocumentEntry> stream(String key);

    Iterable<DocumentEntry> iterate(String key);

    Document sort(String key, Comparator<DocumentEntry> sorter);

    @Internal
    void addEntry(DocumentEntry entry);

    @Internal
    void removeEntry(DocumentEntry entry);


    Document remove(String key);


    Document clear();


    //Update current entry

    Stream<DocumentEntry> stream();

    Document sort(Comparator<DocumentEntry> sorter);


    default Document copy(){
        return copy(getKey());
    }

    Document copy(String key);


    //Save entry

    String write(String type,boolean pretty);

    void write(File location, boolean pretty);

    void write(String type, File location, boolean pretty);

    void write(String type, OutputStream output, boolean pretty);

    void write(String type, Writer output, boolean pretty);


    //Extra implementation


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

    static Document newDocument(String key) {
        return factory().newDocument(key);
    }

    static Document newDocument(Object object){
        return DocumentRegistry.getDefaultContext().serialize(object).toDocument();
    }

    static Document newEmptyDocument(){
        return EmptyDocument.newDocument();
    }


    static Document read(File location){
        return DocumentRegistry.getTypeByEnding(location.getName().substring(location.getName().lastIndexOf(".")+1)).getReader().read(location);
    }

    static Document read(File location, Charset charset){
        return DocumentRegistry.getTypeByEnding(location.getName().substring(location.getName().lastIndexOf(".")+1)).getReader().read(location, charset);
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

    static void loadConfigurationClass(Class<?> clazz, Document document){
        ConfigurationUtil.loadConfigurationClass(clazz, document);
    }

    static void loadConfigurationClass(Class<?> clazz, Document document, boolean appendMissing){
        ConfigurationUtil.loadConfigurationClass(clazz, document,appendMissing);
    }
}
