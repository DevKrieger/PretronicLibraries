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

package net.pretronic.libraries.document;

import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.entry.DocumentNode;
import net.pretronic.libraries.document.type.DocumentFileType;
import net.pretronic.libraries.document.utils.ConfigurationUtil;
import net.pretronic.libraries.utility.annonations.Internal;
import net.pretronic.libraries.utility.map.Pair;
import net.pretronic.libraries.utility.parser.StringParser;
import net.pretronic.libraries.utility.reflect.TypeReference;

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

    /**
     * Get the current document as array.
     *
     * @return The document transformed to an array
     */
    Array getAsArray();

    /**
     * Get the current document as array.
     *
     * @param type The type of the arrray
     * @param <A> The type of the arrray
     * @return The document transformed to an array
     */
    <A> A getAsArray(A type);

    <V> Collection<V> getAsCollection(Class<V> valueClass);

    <V> List<V> getAsList(Class<V> valueClass);

    <K,V> Map<K,V> getAsMap(Class<K> keyClass, Class<V> valueClass);


    //Sub entries

    /**
     * Get a nested document.
     *
     * @param key The key of the document
     * @return The sub document or null
     */
    Document getDocument(String key);

    Array getArray(String key);

    <A> A getArray(String key,A type);

    <V> Collection<V> getCollection(String key, Class<V> valueClass);

    <V> List<V> getList(String key, Class<V> valueClass);

    <K,V> Map<K,V> getMap(String key, Class<K> keyClass, Class<V> valueClass);

    /**
     * Get an serialize a entry to a java object.
     *
     * @param key The key of the nested object
     * @param classOf The class of the object for serializing
     * @param <T> The type of the object
     * @return THe transformed object
     */
    <T> T getObject(String key, Class<T> classOf);

    <T> T getObject(String key, Type type);

    <T> T getObject(String key, TypeReference<T> reference);


    //Update entry

    /**
     * Set a object to this document.
     *
     * @param key The key of the entry
     * @param value The object
     * @return The current document
     */
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

    /**
     * Sort the entries of a nested entry with a @{@link Comparator}
     *
     * @param key The key of the sub entry
     * @param sorter The comparator to sort
     * @return The current document
     */
    Document sort(String key, Comparator<DocumentEntry> sorter);

    @Internal
    void addEntry(DocumentEntry entry);

    @Internal
    void removeEntry(DocumentEntry entry);


    /**
     * Remove a entry from this document.
     *
     * @param key The key of the entry
     * @return The current document
     */
    Document remove(String key);


    /**
     * Remove all entries in this document.
     *
     * @return The current document
     */
    Document clear();


    //Update current entry


    /**
     * Get the entries of this object in a stream.
     *
     * @return The created stream
     */
    Stream<DocumentEntry> stream();

    /**
     * Sor the entries of the current document with a @{@link Comparator}
     *
     * @return The current document
     */
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

    /**
     * Add an object to the document if the key is not already present.
     *
     * @param key The key of the entry
     * @param value The object
     * @return The current document
     */
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

    /**
     * Get the default document factory.
     *
     * @return The document factory
     */
    static DocumentFactory factory(){
        return DocumentRegistry.getFactory();
    }

    /**
     * Create a new document from the default factory.
     *
     * @return The new created document
     */
    static Document newDocument(){
        return DocumentRegistry.getFactory().newDocument();
    }

    static Document newDocument(String key) {
        return factory().newDocument(key);
    }

    static Document newDocument(Object object){
        return DocumentRegistry.getDefaultContext().serialize(object).toDocument();
    }

    /**
     * Create a new empty document.
     *
     * @return The new created document
     */
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

    /**
     * Find an existing file with an undefined type.
     *
     * @param location The location of the file
     * @param baseName The base name of the file (Before the dot)
     * @return The existing file and type
     */
    static Pair<File, DocumentFileType> findExistingType(File location, String baseName){
        for (DocumentFileType type : DocumentRegistry.getTypes()) {
            File file = new File(location,baseName+"."+type.getEnding());
            if(file.exists()) return new Pair<>(file,type);
        }
        return null;
    }

    /**
     * Load a static configuration class
     *
     * @param clazz The configuration class
     * @param document The document
     */
    static void loadConfigurationClass(Class<?> clazz, Document document){
        ConfigurationUtil.loadConfigurationClass(clazz, document);
    }

    /**
     * Load a static configuration class
     *
     * @param clazz The configuration class
     * @param document The document
     * @param appendMissing True if missing entries should be added to the document
     */
    static void loadConfigurationClass(Class<?> clazz, Document document, boolean appendMissing){
        ConfigurationUtil.loadConfigurationClass(clazz, document,appendMissing);
    }
}
