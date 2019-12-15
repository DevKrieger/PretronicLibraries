/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 15.12.19, 16:08
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

package net.prematic.libraries.document.type.json;

import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.entry.DocumentAttributes;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.Iterators;

import java.util.ArrayList;
import java.util.List;

public class JsonSequence {

    private final String key;
    private final List<DocumentEntry> entries;
    private final boolean array;

    private ParserState currentState;
    private String currentKey;

    public JsonSequence(String key,boolean array,ParserState currentState) {
        this.key = key;
        this.array = array;
        this.currentState = currentState;

        this.entries = new ArrayList<>();
    }

    public List<DocumentEntry> getEntries() {
        return entries;
    }

    public DocumentEntry getSequenceEntry(){
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
            DocumentEntry result = createDocument();
            result.setAttributes(attributes);
            return result;
        }
        return createDocument();
    }

    private Document createDocument(){
        return array ? Document.factory().newArrayEntry(key,entries) : Document.factory().newDocument(key,entries);
    }

    public void pushEntry(DocumentEntry entry){
        this.entries.add(entry);
    }

    public boolean isArray(){
        return array;
    }

    public ParserState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ParserState currentState) {
        this.currentState = currentState;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }
}
