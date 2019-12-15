/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 14.12.19, 19:39
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
import net.prematic.libraries.document.DocumentContext;
import net.prematic.libraries.document.entry.*;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.annonations.Internal;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractDocumentNode implements DocumentNode {

    private transient DocumentContext context;
    protected transient List<DocumentEntry> entries;

    public AbstractDocumentNode() {
        this(new ArrayList<>());
    }

    public AbstractDocumentNode(List<DocumentEntry> entries) {
        this.entries = entries;
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
    public void setEntries(List<DocumentEntry> entries) {
        this.entries = entries;
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
    public boolean containsMany(String... keys) {
        for(String key : keys) if(!contains(key)) return false;
        return true;
    }

    @Override
    public void addEntry(DocumentEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void removeEntry(DocumentEntry entry) {
        this.entries.remove(entry);
    }

    @Override
    public DocumentNode clear() {
        this.entries.clear();
        return this;
    }

    @Override
    public Stream<DocumentEntry> stream() {
        return this.entries.stream();
    }

    @Override
    public DocumentNode sort(Comparator<DocumentEntry> sorter) {
        this.entries.sort(sorter);
        return this;
    }

    @Override
    public Iterator<DocumentEntry> iterator() {
        return this.entries.iterator();
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
        throw new UnsupportedOperationException("This entry is not a array.");
    }

    @Override
    public DocumentAttributes toAttributes() {
        throw new UnsupportedOperationException("This entry is not a array.");
    }

    @Override
    public DocumentNode toNode() {
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
        return false;
    }

    @Override
    public boolean isAttributes() {
        return false;
    }

    @Override
    public boolean isNode() {
        return true;
    }


    @Internal
    protected DocumentEntry findLocalEntry(String key){
        return Iterators.findOne(this.entries, entry -> key.equals(entry.getKey()));
    }

}
