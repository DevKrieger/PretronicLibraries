/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 17:23
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

package net.prematic.libraries.document.type.yaml;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.entry.DocumentAttributes;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.Iterators;

import java.util.ArrayList;
import java.util.List;

public class YamlSequence {

    private final String key;
    private final int indent;
    private final YamlSequence parent;
    private final boolean array;
    private final List<DocumentEntry> entries;

    public YamlSequence(String key, int indent, YamlSequence parent, boolean array) {
        this.key = key;
        this.indent = indent;
        this.parent = parent;
        this.array = array;

        this.entries = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public int getIndent() {
        return indent;
    }

    public YamlSequence getParent() {
        return parent;
    }

    public boolean isArray() {
        return array;
    }

    public List<DocumentEntry> getEntries() {
        return entries;
    }

    public void pushEntry(DocumentEntry entry){
        this.entries.add(entry);
    }

    public DocumentEntry getEntry(){
        if(array){
            return Document.factory().newArrayEntry(key,entries);
        }else {
            DocumentEntry attributeEntry = Iterators.removeOne(this.entries, entry
                    -> entry.getKey() != null
                    && entry.getKey().equals("_attributes")
                    && entry.isObject());
            if(attributeEntry != null){
                DocumentAttributes attributes = Document.factory().newAttributes(attributeEntry.toDocument().entries());
                if(this.entries.size() == 1){
                    DocumentEntry value = this.entries.get(0);
                    if(value.getKey().equals("_value")){
                        value.setAttributes(attributes);
                        value.setKey(key);
                        return value;
                    }
                }
                DocumentEntry result = Document.factory().newDocument(key,entries);
                result.setAttributes(attributes);
                return result;
            }
            return Document.factory().newDocument(key,entries);
        }
    }
}
