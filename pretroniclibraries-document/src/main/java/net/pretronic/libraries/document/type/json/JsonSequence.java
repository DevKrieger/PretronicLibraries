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

package net.pretronic.libraries.document.type.json;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentAttributes;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.parser.StringParser;

import java.util.ArrayList;
import java.util.List;

public class JsonSequence {

    private final String key;
    private final List<DocumentEntry> entries;
    private boolean array;

    private ParserState currentState;
    private String currentKey;

    private int characterMark;
    private int lineMark;
    private JsonSequence nextSequence;

    protected JsonSequence(String key,boolean array,ParserState currentState) {
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

    public void setArray(boolean array) {
        this.array = array;
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

    public int getCharacterMark() {
        return characterMark;
    }

    public int getLineMark() {
        return lineMark;
    }

    public void setCharacterMark(int characterMark) {
        this.characterMark = characterMark;
    }

    public void mark(StringParser parser){
        this.characterMark = parser.charIndex();
        this.lineMark = parser.lineIndex();
    }

    public void markNext(StringParser parser){
        parser.skipChar();
        mark(parser);
        parser.previousChar();
    }

    public JsonSequence getNextSequence() {
        return nextSequence;
    }

    public void setNextSequence(JsonSequence nextSequence) {
        this.nextSequence = nextSequence;
    }
}
