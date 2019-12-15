/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 15.12.19, 17:41
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

package net.prematic.libraries.document.type.xml;

import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.parser.StringParser;

import java.util.ArrayList;
import java.util.List;

public class XmlSequence {

    private final String key;
    private final List<DocumentEntry> entries;

    private ParserState currentState;
    private String currentKey;
    private int characterMark;

    public XmlSequence(String key, ParserState currentState) {
        this.key = key;
        this.currentState = currentState;

        this.entries = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public List<DocumentEntry> getEntries() {
        return entries;
    }

    public void pushEntry(DocumentEntry entry){
        this.entries.add(entry);
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

    public void mark(StringParser parser){
        this.characterMark = parser.charIndex();
    }
}
