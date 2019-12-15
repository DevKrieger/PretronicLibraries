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
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.type.Characters;
import net.prematic.libraries.utility.parser.StringParser;

import java.math.BigDecimal;

public interface ParserState {

    ParserState DOCUMENT_START = new DocumentStart();
    ParserState DOCUMENT_PRE_KEY = new DocumentPreKey();
    ParserState DOCUMENT_KEY_ENDING = new DocumentKeyEnding();
    ParserState DOCUMENT_PRE_VALUE = new DocumentPreValue();
    ParserState DOCUMENT_NEXT_PAIR = new DocumentNextPair();
    ParserState DOCUMENT_FINISHED = new DocumentFinished();

    void parse(JsonSequence sequence, StringParser parser, char current);

    static boolean isIgnoredChar(char c){
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    class DocumentStart implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == '{') sequence.setCurrentState(DOCUMENT_PRE_KEY);
            else if(!isIgnoredChar(current)) parser.throwException("Invalid Document start (A json document has to start with {)");
        }
    }

    class DocumentPreKey implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == '"') sequence.setCurrentState(new DocumentKey());
            else if(current == '}' && !sequence.isArray()){
                sequence.setCurrentState(DOCUMENT_FINISHED);
            }else if(current == ']' && sequence.isArray()){
                sequence.setCurrentState(DOCUMENT_FINISHED);
            }else if(!isIgnoredChar(current)) parser.throwException("Invalid key start (\" required)");
        }
    }

    class DocumentKey implements ParserState {

        private int start;

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == '"'){
                sequence.setCurrentKey(parser.getOnLine(start,parser.charIndex()));
                sequence.setCurrentState(DOCUMENT_KEY_ENDING);
            }else if(start == 0) start = parser.charIndex();
            else if(parser.isLineFinished()) parser.throwException("Key can't be on multiple lines.");
        }
    }

    class DocumentKeyEnding implements ParserState {
        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == ':') sequence.setCurrentState(DOCUMENT_PRE_VALUE);
            else if(!isIgnoredChar(current)) parser.throwException("Invalid key end (: required)");
        }
    }

    class DocumentPreValue implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == '"' || current == '\''){
                sequence.setCurrentState(new DocumentString(current,parser.charIndex()));
            }else if(current == 't' || current == 'T'){
                sequence.setCurrentState(new DocumentKeyword(Characters.BOOLEAN_TRUE,true));
            }else if(current == 'f' || current == 'F'){
                sequence.setCurrentState(new DocumentKeyword(Characters.BOOLEAN_FALSE,false));
            }else if(current == 'n' || current == 'N'){
                sequence.setCurrentState(new DocumentKeyword(Characters.NULL,null));
            }else if(current == '['){
                sequence.setCurrentState(new DocumentObject(sequence.getCurrentKey(),true));
            }else if(current == '{'){
                sequence.setCurrentState(new DocumentObject(sequence.getCurrentKey(),false));
            }else if(current == '-' || current == '+' || Character.isDigit(current)){
                sequence.setCurrentState(new DocumentNumber(parser.charIndex()));
            }else if(!isIgnoredChar(current)) parser.throwException("Invalid character");
        }
    }

    class DocumentNextPair implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == ','){
                if(sequence.isArray()) sequence.setCurrentState(DOCUMENT_PRE_VALUE);
                else sequence.setCurrentState(DOCUMENT_PRE_KEY);
            }else if(current == '}' && !sequence.isArray()){
               sequence.setCurrentState(DOCUMENT_FINISHED);
            }else if(current == ']' && sequence.isArray()){
                sequence.setCurrentState(DOCUMENT_FINISHED);
            }else if(!isIgnoredChar(current)) parser.throwException("Invalid character");
        }
    }

    class DocumentString implements ParserState {

        private final char end;
        private final int start;
        private boolean blocked;

        public DocumentString(char end, int start) {
            this.end = end;
            this.start = start;
        }

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == end && !blocked){
                String value = parser.getOnLine(start+1,parser.charIndex());
                DocumentEntry primitive = Document.factory().newPrimitiveEntry(sequence.getCurrentKey(),value);
                sequence.pushEntry(primitive);
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }else blocked = current == '\\';
        }
    }

    class DocumentNumber implements ParserState {

        private final int start;

        public DocumentNumber(int start) {
            this.start = start;
        }

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == ' ' || current == ',' || current == '}' || current == ']'){
                parser.previousChar();
                int length = parser.charIndex()-start+1;
                if(length <= 0) parser.throwException("Number can't be on multiple lines");
                BigDecimal result = null;
                try {
                     result = new BigDecimal(parser.getLines()[parser.lineIndex()],start,length);
                }catch (Exception exception){
                    parser.throwException("Invalid number");
                }
                DocumentEntry primitive = Document.factory().newPrimitiveEntry(sequence.getCurrentKey(),result);
                sequence.pushEntry(primitive);
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }
        }
    }

    class DocumentObject implements ParserState {

        private final JsonSequence sequence;

        public DocumentObject(String key, boolean array) {
            sequence = new JsonSequence(key,array,array ? DOCUMENT_PRE_VALUE : DOCUMENT_PRE_KEY);
        }

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            ParserState state = this.sequence.getCurrentState();
            if(state == DOCUMENT_FINISHED){
                sequence.pushEntry(this.sequence.getSequenceEntry());
                parser.previousChar();
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }else state.parse(this.sequence,parser,current);
        }
    }

    class DocumentKeyword implements ParserState {

        private int start;
        private char[] equals;
        private Object value;

        public DocumentKeyword(char[] equals,Object value) {
            this.start = 1;
            this.equals = equals;
            this.value = value;
        }

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if((current == ',' || current == '}' || current == ']') && start == equals.length){
                parser.previousChar();
                DocumentEntry entry = Document.factory().newPrimitiveEntry(sequence.getCurrentKey(),value);
                sequence.pushEntry(entry);
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }else if(start < equals.length && Character.toLowerCase(current) == equals[start]){
                start++;
            }else if(!isIgnoredChar(current)){
                parser.throwException("Invalid Characters");
            }
        }
    }

    class DocumentFinished implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(!isIgnoredChar(current)) parser.throwException("Invalid characters");
        }

    }

}
