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

package net.pretronic.libraries.document.type.yaml;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.parser.StringParser;

public class YamlParser {

    private ParserState state;
    private YamlSequence sequence;

    private String tempKey;
    private int tempIndent;
    private int characterMark;
    private int lineMark;

    public YamlParser() {
        this.state = ParserState.DOCUMENT_START;
        this.sequence = new YamlSequence("root",0,null,false);
    }

    public ParserState getState() {
        return state;
    }

    public void setState(ParserState state) {
        this.state = state;
    }

    public YamlSequence getSequence() {
        return sequence;
    }

    public void setSequence(YamlSequence sequence) {
        this.sequence = sequence;
    }

    public String getTempKey() {
        return tempKey;
    }

    public void setTempKey(String tempKey) {
        this.tempKey = tempKey;
    }

    public int getTempIndent() {
        return tempIndent;
    }

    public void setTempIndent(int tempIndent) {
        this.tempIndent = tempIndent;
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

    public Document parse(StringParser parser){
        while (parser.hasNextChar()) state.parse(this,parser,parser.nextChar());
        while (sequence.getParent() != null){
            DocumentEntry entry = sequence.getEntry();
            sequence = sequence.getParent();
            sequence.pushEntry(entry);
        }
        return sequence.getEntry().toDocument();
    }
}
