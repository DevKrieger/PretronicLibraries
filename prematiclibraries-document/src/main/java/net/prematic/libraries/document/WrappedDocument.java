/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.08.19, 19:18
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

import net.prematic.libraries.document.entry.*;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

public class WrappedDocument implements Document {

    private final Document original;

    public WrappedDocument(Document original) {
        this.original = original;
    }

    @Override
    public DocumentContext getContext() {
        return original.getContext();
    }

    @Override
    public void setContext(DocumentContext context) {
        original.setContext(context);
    }

    public Document getOriginal() {
        return original;
    }

    @Override
    public <T> T getAsObject(Class<T> classOf) {
        return original.getAsObject(classOf);
    }

    @Override
    public <T> T getAsObject(Type type) {
        return original.getAsObject(type);
    }

    @Override
    public <T> T getAsObject(TypeReference<T> reference) {
        return original.getAsObject(reference);
    }

    @Override
    public Array getAsArray() {
        return original.getAsArray();
    }

    @Override
    public <A> A getAsArray(A type) {
        return original.getAsArray(type);
    }

    @Override
    public <V> Collection<V> getAsCollection(Class<V> valueClass) {
        return original.getAsCollection(valueClass);
    }

    @Override
    public <V> List<V> getAsList(Class<V> valueClass) {
        return original.getAsList(valueClass);
    }

    @Override
    public <K, V> Map<K, V> getAsMap(Class<K> keyClass, Class<V> valueClass) {
        return original.getAsMap(keyClass, valueClass);
    }

    @Override
    public void setEntries(List<DocumentEntry> entries) {
        original.setEntries(entries);
    }

    @Override
    public DocumentEntry getEntry(String key) {
        return original.getEntry(key);
    }

    @Override
    public DocumentEntry getEntry(int index) {
        return original.getEntry(index);
    }

    @Override
    public DocumentEntry getFirst() {
        return original.getFirst();
    }

    @Override
    public DocumentEntry getLast() {
        return original.getLast();
    }

    @Override
    public String getString(String key) {
        return original.getString(key);
    }

    @Override
    public char getCharacter(String key) {
        return original.getCharacter(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return original.getBoolean(key);
    }

    @Override
    public Number getNumber(String key) {
        return original.getNumber(key);
    }

    @Override
    public byte getByte(String key) {
        return original.getByte(key);
    }

    @Override
    public int getInt(String key) {
        return original.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return original.getLong(key);
    }

    @Override
    public float getFloat(String key) {
        return original.getFloat(key);
    }

    @Override
    public short getShort(String key) {
        return original.getShort(key);
    }

    @Override
    public double getDouble(String key) {
        return original.getDouble(key);
    }

    @Override
    public Document getDocument(String key) {
        return original.getDocument(key);
    }

    @Override
    public Array getArray(String key) {
        return original.getArray(key);
    }

    @Override
    public <A> A getArray(String key, A type) {
        return original.getArray(key, type);
    }

    @Override
    public <V> Collection<V> getCollection(String key, Class<V> valueClass) {
        return original.getCollection(key, valueClass);
    }

    @Override
    public <V> List<V> getList(String key, Class<V> valueClass) {
        return original.getList(key, valueClass);
    }

    @Override
    public <K, V> Map<K, V> getMap(String key, Class<K> keyClass, Class<V> valueClass) {
        return original.getMap(key, keyClass, valueClass);
    }

    @Override
    public <T> T getObject(String key, Class<T> classOf) {
        return original.getObject(key, classOf);
    }

    @Override
    public <T> T getObject(String key, Type type) {
        return original.getObject(key, type);
    }

    @Override
    public <T> T getObject(String key, TypeReference<T> reference) {
        return original.getObject(key, reference);
    }

    @Override
    public List<DocumentEntry> entries() {
        return original.entries();
    }

    @Override
    public Set<String> keys() {
        return original.keys();
    }

    @Override
    public int size() {
        return original.size();
    }

    @Override
    public boolean contains(String key) {
        return original.contains(key);
    }

    @Override
    public boolean containsOne(String... keys) {
        return original.containsOne(keys);
    }

    @Override
    public boolean containsMany(String... keys) {
        return original.containsMany(keys);
    }

    @Override
    public boolean isObject(String key) {
        return original.isObject(key);
    }

    @Override
    public boolean isPrimitive(String key) {
        return original.isPrimitive(key);
    }

    @Override
    public boolean isArray(String key) {
        return original.isArray(key);
    }

    @Override
    public Document set(String key, Object value) {
        return original.set(key, value);
    }

    @Override
    public Document remove(String key) {
        return original.remove(key);
    }

    @Override
    public Document clear() {
        return original.clear();
    }

    @Override
    public Stream<DocumentEntry> stream(String key) {
        return original.stream();
    }

    @Override
    public Iterable<DocumentEntry> iterate(String key) {
        return original.iterate(key);
    }

    @Override
    public Document sort(String key, Comparator<DocumentEntry> sorter) {
        return original.sort(key, sorter);
    }

    @Override
    public void addEntry(DocumentEntry entry) {
        original.addEntry(entry);
    }

    @Override
    public void removeEntry(DocumentEntry entry) {
        original.removeEntry(entry);
    }

    @Override
    public Stream<DocumentEntry> stream() {
        return original.stream();
    }

    @Override
    public Document sort(Comparator<DocumentEntry> sorter) {
        return original.sort(sorter);
    }

    @Override
    public Document copy(String key) {
        return original.copy(key);
    }

    @Override
    public String write(String type, boolean pretty) {
        return original.write(type, pretty);
    }

    @Override
    public void write(File location, boolean pretty) {
        original.write(location, pretty);
    }

    @Override
    public void write(String type, File location, boolean pretty) {
        original.write(type, location, pretty);
    }

    @Override
    public void write(String type, OutputStream output, boolean pretty) {
        original.write(type, output, pretty);
    }

    @Override
    public void write(String type, Writer output, boolean pretty) {
        original.write(type, output, pretty);
    }

    @Override
    public Iterator<DocumentEntry> iterator() {
        return original.iterator();
    }

    @Override
    public String getKey() {
        return original.getKey();
    }

    @Override
    public void setKey(String key) {
        original.setKey(key);
    }

    @Override
    public DocumentAttributes getAttributes() {
        return original.getAttributes();
    }

    @Override
    public void setAttributes(DocumentAttributes attributes) {
        original.setAttributes(attributes);
    }

    @Override
    public boolean hasAttributes() {
        return original.hasAttributes();
    }

    @Override
    public PrimitiveEntry toPrimitive() {
        return original.toPrimitive();
    }

    @Override
    public ArrayEntry toArray() {
        return original.toArray();
    }

    @Override
    public Document toDocument() {
        return original.toDocument();
    }

    @Override
    public DocumentAttributes toAttributes() {
        return original.toAttributes();
    }

    @Override
    public DocumentNode toNode() {
        return original.toNode();
    }

    @Override
    public boolean isPrimitive() {
        return original.isPrimitive();
    }

    @Override
    public boolean isArray() {
        return original.isArray();
    }

    @Override
    public boolean isObject() {
        return original.isObject();
    }

    @Override
    public boolean isAttributes() {
        return original.isAttributes();
    }

    @Override
    public boolean isNode() {
        return original.isNode();
    }
}
