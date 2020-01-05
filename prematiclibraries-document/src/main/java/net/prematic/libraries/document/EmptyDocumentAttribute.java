/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 22:38
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

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

/**
 * The {@link EmptyDocumentAttribute} is singleton attribute entry which is always empty and can not contain any kind of data.
 */
public class EmptyDocumentAttribute implements DocumentAttributes {

    private static final DocumentAttributes EMPTY = new EmptyDocumentAttribute();

    private EmptyDocumentAttribute(){}

    @Override
    public PrimitiveEntry getEntry(String key) {
        return null;
    }

    @Override
    public PrimitiveEntry getEntry(int index) {
        return null;
    }

    @Override
    public DocumentEntry getEntry(String[] keys, int offset) {
        return null;
    }

    @Override
    public PrimitiveEntry getFirst() {
        return null;
    }

    @Override
    public PrimitiveEntry getLast() {
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
        return null;
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
    public DocumentContext getContext() {
        return DocumentRegistry.getDefaultContext();
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
    public List<DocumentEntry> entries() {
        return Collections.emptyList();
    }

    @Override
    public void setEntries(List<DocumentEntry> entries) {

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
    public DocumentAttributes set(String key, Object value) {
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
    public DocumentAttributes remove(String key) {
        return this;
    }

    @Override
    public DocumentAttributes clear() {
        return this;
    }

    @Override
    public Stream<DocumentEntry> stream() {
        return null;
    }

    @Override
    public DocumentNode sort(Comparator<DocumentEntry> sorter) {
        return null;
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
        return null;
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
        return false;
    }

    @Override
    public boolean isAttributes() {
        return true;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public DocumentAttributes copy() {
        return EMPTY;
    }


    public static DocumentAttributes newAttribute(){
        return EMPTY;
    }

    @Override
    public Iterator<DocumentEntry> iterator() {
        return entries().iterator();
    }
}
