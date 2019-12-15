/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 19:55
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

import net.prematic.libraries.document.entry.ArrayEntry;
import net.prematic.libraries.document.entry.DocumentEntry;

import java.util.List;

public class SimpleArrayEntry extends SimpleDocument implements ArrayEntry {

    public SimpleArrayEntry(String key) {
        super(key);
    }

    public SimpleArrayEntry(String key, List<DocumentEntry> entries) {
        super(key,entries);
    }

    @Override
    public boolean isPrimitiveArray() {
        for(DocumentEntry entry : entries()) if(!entry.isPrimitive()) return false;
        return true;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public ArrayEntry toArray() {
        return this;
    }

    @Override
    public ArrayEntry copy(String key) {
        SimpleArrayEntry document = new SimpleArrayEntry(key);
        forEach(entry -> document.entries().add(entry.copy(entry.getKey())));
        return document;
    }

}
