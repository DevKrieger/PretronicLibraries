/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 14:38
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

package net.prematic.libraries.document.simple;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.entry.DocumentAttributes;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SimpleDocument extends AbstractDocumentNode implements Document {

    private transient String key;
    private transient DocumentAttributes attributes;

    public SimpleDocument(String key) {
        this.key = key;
    }

    public SimpleDocument(String key,List<DocumentEntry> entries) {
        super(entries);
        this.key = key;
    }

    @Override
    public Array getAsArray() {
        return getContext().deserialize(this,new TypeReference<Object[]>());
    }

    @Override
    public <A> A getAsArray(A type) {
        return getContext().deserialize(this, (Type) type.getClass());
    }

    @Override
    public <V> Collection<V> getAsCollection(Class<V> valueClass) {
        return getContext().deserialize(this,new TypeReference<Collection<V>>());
    }

    @Override
    public <V> List<V> getAsList(Class<V> valueClass) {
        return getContext().deserialize(this,new TypeReference<List<V>>(){});
    }

    @Override
    public <K, V> Map<K, V> getAsMap(Class<K> keyClass, Class<V> valueClass) {
        return getContext().deserialize(this,new TypeReference<Map<K,V>>(){});
    }

    @Override
    public Document getDocument(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument():null;
    }

    @Override
    public Array getArray(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument().getAsArray():null;
    }

    @Override
    public <A> A getArray(String key, A type) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument().getAsArray(type):null;
    }

    @Override
    public <V> Collection<V> getCollection(String key, Class<V> valueClass) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument().getAsCollection(valueClass):null;
    }

    @Override
    public <V> List<V> getList(String key, Class<V> valueClass) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument().getAsList(valueClass):null;
    }

    @Override
    public <K, V> Map<K, V> getMap(String key, Class<K> keyClass, Class<V> valueClass) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument().getAsMap(keyClass,valueClass):null;
    }

    @Override
    public <T> T getObject(String key, Class<T> classOf) {
        DocumentEntry entry = getEntry(key);
        return entry!=null?getContext().deserialize(entry,classOf):null;
    }

    @Override
    public <T> T getObject(String key, Type type) {
        DocumentEntry entry = getEntry(key);
        return entry!=null?getContext().deserialize(entry,type):null;
    }

    @Override
    public <T> T getObject(String key, TypeReference<T> reference) {
        DocumentEntry entry = getEntry(key);
        return entry!=null?getContext().deserialize(entry,reference):null;
    }

    @Override
    public DocumentEntry getEntry(String key) {
        return getEntry(key.split("\\."),0);
    }

    @Override
    public DocumentEntry getEntry(String[] keys, int offset) {
        DocumentEntry entry = findLocalEntry(keys[offset]);
        if(offset+1 == keys.length) return entry;
        else if(entry == null) return null;
        else if(entry.isNode()) return entry.toNode().getEntry(keys,offset+1);
        else throw new IllegalArgumentException("Object is not an object");
    }

    @Override
    public Document set(String key, Object value) {
        int index = key.indexOf('.');
        if(index == -1){
            DocumentEntry entry = findLocalEntry(key);
            if(entry != null) this.entries.remove(entry);

            if(value instanceof DocumentEntry){
                if(key.equals(((DocumentEntry) value).getKey())) this.entries.add((DocumentEntry) value);
                else this.entries.add(((DocumentEntry) value).copy(key));
            }else this.entries.add(getContext().serialize(key, value));
        }else{
            String localKey = key.substring(0,index);
            DocumentEntry entry = findLocalEntry(localKey);
            if (entry == null || !entry.isObject()) {
                this.entries.remove(entry);
                entry = new SimpleDocument(localKey);
                this.entries.add(entry);
            }
            entry.toDocument().set(key.substring(index+1), value);
        }
        return this;
    }

    @Override
    public boolean isObject(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!= null && entry.isObject();
    }

    @Override
    public boolean isArray(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!= null && entry.isArray();
    }

    @Override
    public boolean isPrimitive(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!= null && entry.isPrimitive();
    }

    @Override
    public Stream<DocumentEntry> stream(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument().stream():null;
    }

    @Override
    public Iterable<DocumentEntry> iterate(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isObject()?entry.toDocument():null;
    }

    @Override
    public Document sort(String key, Comparator<DocumentEntry> sorter) {
        DocumentEntry entry = getEntry(key);
        if(entry != null && entry.isObject()) entry.toDocument().sort(sorter);
        return this;
    }

    @Override
    public Document remove(String key) {
        int index = key.lastIndexOf('.');
        if(index == -1){
            DocumentEntry entry = findLocalEntry(key);
            if(entry != null) this.entries.remove(entry);
        }else{
            DocumentEntry entry = getEntry(key.substring(0,index-1));
            if(entry != null && entry.isObject()) entry.toDocument().remove(key.substring(index+1));
        }
        return this;
    }

    @Override
    public Document clear() {
        super.clear();
        return this;
    }

    @Override
    public Document sort(Comparator<DocumentEntry> sorter) {
        super.sort(sorter);
        return this;
    }

    @Override
    public Document toDocument() {
        return this;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public DocumentAttributes getAttributes() {
        if(attributes == null) attributes = Document.factory().newAttributes();
        return attributes;
    }

    @Override
    public void setAttributes(DocumentAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean hasAttributes() {
        return attributes != null && !attributes.isEmpty();
    }

    @Override
    public Document copy(String key) {
        SimpleDocument document = new SimpleDocument(key);
        forEach(entry -> document.entries.add(entry.copy(entry.getKey())));
        document.setAttributes(getAttributes().copy());
        return document;
    }

    @Override
    public String write(String type,boolean pretty) {
        return DocumentRegistry.getType(type).getWriter().write(this,pretty);
    }

    @Override
    public void write(File location, boolean pretty) {
        DocumentRegistry.getTypeByEnding(location.getName().substring(location.getName().lastIndexOf(".")+1)).getWriter().write(location,this,pretty);
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
}
