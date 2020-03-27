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

package net.pretronic.libraries.document.type.xml;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentAttributes;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.parser.StringParser;

import java.util.ArrayList;
import java.util.List;

public class XmlSequence {

    private final List<DocumentEntry> entries;
    private String key;
    private DocumentAttributes attributes;

    private ParserState currentState;
    private int characterMark;
    private int lineMark;

    private String tempKey;
    private XmlSequence nextSequence;

    protected XmlSequence(ParserState currentState) {
        this.currentState = currentState;
        this.entries =  new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<DocumentEntry> getEntries() {
        return entries;
    }

    public void pushEntry(DocumentEntry entry){
        this.entries.add(entry);
    }

    public void pushAttribute(String value){
        if(attributes == null) attributes = Document.factory().newAttributes();
        attributes.add(tempKey,value);
    }

    public ParserState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ParserState currentState) {
        this.currentState = currentState;
    }

    public String getTempKey() {
        return tempKey;
    }

    public void setTempKey(String tempKey) {
        this.tempKey = tempKey;
    }

    public XmlSequence getNextSequence() {
        return nextSequence;
    }

    public void setNextSequence(XmlSequence nextSequence) {
        this.nextSequence = nextSequence;
    }

    public int getCharacterMark() {
        return characterMark;
    }

    public int getLineMark() {
        return lineMark;
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

    public DocumentEntry getSequenceEntry(){
        DocumentEntry result;
        if(entries.size() == 1 && entries.get(0).getKey().equals("_text")){
            result = entries.get(0);
            result.setKey(key);
        }else{
            if(isArray()) result = Document.factory().newArrayEntry(key,entries);
            else result = Document.factory().newDocument(key,entries);
        }
        if(attributes != null) result.setAttributes(attributes);
        return result;
    }

    private boolean isArray(){
        if(entries.size() <= 1) return false;
        String first = entries.get(0).getKey();
        for (int i = 1; i < entries.size(); i++) {
            DocumentEntry entry = entries.get(i);
            if(!(entry.getKey().equalsIgnoreCase(first) || entry.getKey().startsWith("array-index"))) return false;
        }
        return true;
    }
}
