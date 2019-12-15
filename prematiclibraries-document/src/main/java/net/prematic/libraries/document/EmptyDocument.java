/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 05.07.19 11:39
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

public class EmptyDocument implements Document {

    private static final Document EMPTY = new EmptyDocument();

    private EmptyDocument(){}

    @Override
    public DocumentContext getContext() {
        return DocumentContext.getDefaultContext();
    }

    @Override
    public void setContext(DocumentContext context) {
        //Unused
    }

    @Override
    public <T> T getAsObject(Class<T> classOf) {
        return null;
    }

    @Override
    public <T> T getAsObject(Type type) {
        return null;
    }

    @Override
    public <T> T getAsObject(TypeReference<T> reference) {
        return null;
    }

    @Override
    public Array getAsArray() {
        return null;
    }

    @Override
    public <A> A getAsArray(A type) {
        return null;
    }

    @Override
    public <V> Collection<V> getAsCollection(Class<V> valueClass) {
        return null;
    }

    @Override
    public <V> List<V> getAsList(Class<V> valueClass) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> getAsMap(Class<K> keyClass, Class<V> valueClass) {
        return null;
    }

    @Override
    public void setEntries(List<DocumentEntry> entries) {
        //Unused in empty set
    }

    @Override
    public DocumentEntry getEntry(String key) {
        return null;
    }

    @Override
    public DocumentEntry getEntry(int index) {
        return null;
    }

    @Override
    public DocumentEntry getFirst() {
        return null;
    }

    @Override
    public DocumentEntry getLast() {
        return null;
    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public char getCharacter(String key) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key) {
        return false;
    }

    @Override
    public Number getNumber(String key) {
        return 0;
    }

    @Override
    public byte getByte(String key) {
        return 0;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public long getLong(String key) {
        return 0;
    }

    @Override
    public float getFloat(String key) {
        return 0;
    }

    @Override
    public short getShort(String key) {
        return 0;
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }

    @Override
    public Document getDocument(String key) {
        return null;
    }

    @Override
    public Array getArray(String key) {
        return null;
    }

    @Override
    public <A> A getArray(String key, A type) {
        return null;
    }

    @Override
    public <V> Collection<V> getCollection(String key, Class<V> valueClass) {
        return null;
    }

    @Override
    public <V> List<V> getList(String key, Class<V> valueClass) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> getMap(String key, Class<K> keyClass, Class<V> valueClass) {
        return null;
    }

    @Override
    public <T> T getObject(String key, Class<T> classOf) {
        return null;
    }

    @Override
    public <T> T getObject(String key, Type type) {
        return null;
    }

    @Override
    public <T> T getObject(String key, TypeReference<T> reference) {
        return null;
    }

    @Override
    public List<DocumentEntry> entries() {
        return Collections.emptyList();
    }

    @Override
    public Set<String> keys() {
        return Collections.emptySet();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public boolean containsOne(String... keys) {
        return false;
    }

    @Override
    public boolean containsMany(String... keys) {
        return false;
    }


    @Override
    public boolean isObject(String key) {
        return false;
    }

    @Override
    public boolean isPrimitive(String key) {
        return false;
    }

    @Override
    public boolean isArray(String key) {
        return false;
    }

    @Override
    public Document set(String key, Object value) {
        throw new IllegalArgumentException("It is not possible to add an entry to a empty document.");
    }

    @Override
    public Document remove(String key) {
        return this;
    }

    @Override
    public Document clear() {
        return this;
    }

    @Override
    public Stream<DocumentEntry> stream(String key) {
        return null;
    }

    @Override
    public Iterable<DocumentEntry> iterate(String key) {
        return null;
    }

    @Override
    public Document sort(String key, Comparator<DocumentEntry> sorter) {
        return this;
    }

    @Override
    public void addEntry(DocumentEntry entry) {
        //Unused
    }

    @Override
    public void removeEntry(DocumentEntry entry) {
        //Unused
    }

    @Override
    public Stream<DocumentEntry> stream() {
        return entries().stream();
    }

    @Override
    public Document sort(Comparator<DocumentEntry> sorter) {
        return this;
    }

    @Override
    public Document copy(String key) {
        return this;
    }

    @Override
    public String write(String type,boolean pretty) {
        return DocumentRegistry.getType(type).getWriter().write(this,pretty);
    }

    @Override
    public void write(File location, boolean pretty) {
        DocumentRegistry.getTypeByEnding(location.getName().substring(0,location.getName().lastIndexOf("."))).getWriter().write(location,this,pretty);
    }

    @Override
    public void write(String type, File location, boolean pretty) {
        DocumentRegistry.getType(type).getWriter().write(location,this,pretty);
    }

    @Override
    public void write(String type, OutputStream output, boolean pretty) {
        DocumentRegistry.getType(type).getWriter().write(output,this,pretty);
    }

    @Override
    public void write(String type, Writer output, boolean pretty) {
        DocumentRegistry.getType(type).getWriter().write(output,this,pretty);
    }

    @Override
    public Iterator<DocumentEntry> iterator() {
        return entries().iterator();
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }

    @Override
    public DocumentAttributes getAttributes() {
        return EmptyDocumentAttribute.newAttribute();
    }

    @Override
    public void setAttributes(DocumentAttributes attributes) {

    }

    @Override
    public boolean hasAttributes() {
        return false;
    }

    @Override
    public PrimitiveEntry toPrimitive() {
        return null;
    }

    @Override
    public ArrayEntry toArray() {
        return null;
    }

    @Override
    public Document toDocument() {
        return this;
    }

    @Override
    public DocumentAttributes toAttributes() {
        return null;
    }

    @Override
    public DocumentNode toNode() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isAttributes() {
        return false;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    public static Document newDocument(){
        return EMPTY;
    }
}
