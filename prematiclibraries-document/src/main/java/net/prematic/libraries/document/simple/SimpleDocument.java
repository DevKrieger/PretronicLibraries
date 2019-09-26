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

import net.prematic.libraries.document.*;
import net.prematic.libraries.utility.GeneralUtil;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

public class SimpleDocument implements Document {

    private final String key;
    private List<DocumentEntry> entries;
    private DocumentContext context;

    public SimpleDocument(String key) {
        this.key = key;
        this.entries = new ArrayList<>();
    }

    @Override
    public DocumentContext getContext() {
        if(context == null) return DocumentContext.getDefaultContext();
        return context;
    }

    @Override
    public void setContext(DocumentContext context) {
        this.context = context;
    }

    @Override
    public <T> T getAsObject(Class<T> classOf) {
        return getContext().deserialize(this,classOf);
    }

    @Override
    public <T> T getAsObject(Type typeOf) {
        return getContext().deserialize(this,typeOf);
    }

    @Override
    public <T> T getAsObject(TypeReference<T> reference) {
        return getContext().deserialize(this,reference);
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
    public DocumentEntry getEntry(String key) {
        String[] sequences = key.split("\\.");
        DocumentEntry last;
        if(GeneralUtil.isNaturalNumber(sequences[0])) last = getEntry(Integer.parseInt(sequences[0]));
        else last = findLocalEntry(sequences[0]);
        for(int i = 1;i<sequences.length;i++){
            if(last != null && last.isObject()) last = last.toDocument().getEntry(sequences[0]);
            else return null;
        }
        return last;
    }

    public DocumentEntry findLocalEntry(String key){
        return Iterators.findOne(this.entries, entry -> key.equals(entry.getKey()));
    }

    @Override
    public DocumentEntry getEntry(int index) {
        return this.entries.size()>index?this.entries.get(index):null;
    }

    @Override
    public DocumentEntry getFirst() {
        return this.entries.size()>0?this.entries.get(0):null;
    }

    @Override
    public DocumentEntry getLast() {
        return this.entries.size()>0?this.entries.get(this.entries.size()-1):null;
    }

    @Override
    public String getString(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsString():null;
    }

    @Override
    public char getCharacter(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsCharacter():' ';
    }

    @Override
    public boolean getBoolean(String key) {
        DocumentEntry entry = getEntry(key);
        return entry != null && entry.isPrimitive() && entry.toPrimitive().getAsBoolean();
    }

    @Override
    public Number getNumber(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsNumber():0;
    }

    @Override
    public byte getByte(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsByte():0;
    }

    @Override
    public int getInt(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsInt():0;
    }

    @Override
    public long getLong(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsLong():0;
    }

    @Override
    public float getFloat(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsFloat():0;
    }

    @Override
    public short getShort(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsShort():0;
    }

    @Override
    public double getDouble(String key) {
        DocumentEntry entry = getEntry(key);
        return entry!=null&&entry.isPrimitive()?entry.toPrimitive().getAsDouble():0;
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
    public List<DocumentEntry> entries() {
        return this.entries;
    }

    @Override
    public Set<String> keys() {
        Set<String> keys = new HashSet<>();
        forEach(entry -> keys.add(entry.getKey()));
        return keys;
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public boolean contains(String key) {
        return getEntry(key)!=null;
    }

    @Override
    public boolean containsOne(String... keys) {
        for(String key : keys) if(contains(key)) return true;
        return false;
    }

    @Override
    public boolean containsAll(String... keys) {
        for(String key : keys) if(!contains(key)) return false;
        return true;
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
    public Document set(String key, Object value) {
        int index = key.indexOf('.');
        if(index == -1){
            DocumentEntry entry = findLocalEntry(key);
            if(entry != null) this.entries.remove(entry);

            if(value instanceof DocumentEntry){
                if(key.equals(((DocumentEntry) value).getKey())) this.entries.add((DocumentEntry) value);
                else this.entries.add(((DocumentEntry) value).copy(key));
            }else this.entries.add(getContext().serialize(key,value));
        }else{
            String localKey = key.substring(0,index);
            DocumentEntry entry = findLocalEntry(localKey);
            if(entry != null && entry.isObject()) entry.toDocument().set(key.substring(index+1), value);
            else{
                this.entries.remove(entry);
                entry = new SimpleDocument(localKey);
                this.entries.add(entry);
                entry.toDocument().set(key.substring(index+1),value);
            }
        }
        return this;
    }

    public DocumentEntry addInternal(String key, Document previous, DocumentEntry entry){
        previous.set(key,entry);
        return entry;
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
        this.entries.clear();
        return this;
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
    public Stream<DocumentEntry> stream() {
        return this.entries.stream();
    }

    @Override
    public Document sort(Comparator<DocumentEntry> sorter) {
        this.entries.sort(sorter);
        return this;
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

    @Override
    public Iterator<DocumentEntry> iterator() {
        return this.entries.iterator();
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public PrimitiveEntry toPrimitive() {
        throw new UnsupportedOperationException("This entry is not a primitive.");
    }

    @Override
    public ArrayEntry toArray() {
        throw new UnsupportedOperationException("This entry is not a array.");
    }

    @Override
    public Document toDocument() {
        return this;
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
    public Document copy(String key) {
        SimpleDocument document = new SimpleDocument(key);
        forEach(entry -> document.entries.add(entry.copy(entry.getKey())));
        return document;
    }
}
