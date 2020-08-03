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
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.parser.StringParser;

import java.math.BigDecimal;

public interface ParserState {

    ParserState DOCUMENT_START = new DocumentStart();
    ParserState DOCUMENT_PRE_KEY = new DocumentPreKey();
    ParserState DOCUMENT_KEY = new DocumentKey();
    ParserState DOCUMENT_KEY_ENDING = new DocumentKeyEnding();
    ParserState DOCUMENT_PRE_VALUE = new DocumentPreValue();
    ParserState DOCUMENT_VALUE_STRING1 = new DocumentString('"');
    ParserState DOCUMENT_VALUE_STRING2 = new DocumentString('\'');
    ParserState DOCUMENT_VALUE_KEYWORD_TRUE = new DocumentKeyword(new char[]{'t','r','u','e'},true);
    ParserState DOCUMENT_VALUE_KEYWORD_FALSE = new DocumentKeyword(new char[]{'f','a','l','s','e'},false);
    ParserState DOCUMENT_VALUE_KEYWORD_NULL = new DocumentKeyword(new char[]{'n','u','l','l'},null);
    ParserState DOCUMENT_VALUE_OBJECT = new DocumentObject();
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
            else if(current == '['){
                sequence.setCurrentState(DOCUMENT_PRE_VALUE);
                sequence.setArray(true);
            }else if(!isIgnoredChar(current)) parser.throwException("Invalid Document start (A json document has to start with {)");
        }
    }

    class DocumentPreKey implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == '"'){
                sequence.setCurrentState(DOCUMENT_KEY);
                sequence.markNext(parser);
            }else if(current == '}' && !sequence.isArray()){
                sequence.setCurrentState(DOCUMENT_FINISHED);
            }else if(current == ']' && sequence.isArray()){
                sequence.setCurrentState(DOCUMENT_FINISHED);
            }else if(!isIgnoredChar(current)) parser.throwException("Invalid key start (\" required)");
        }
    }

    class DocumentKey implements ParserState {

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if(current == '"'){
                sequence.setCurrentKey(parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()));
                sequence.setCurrentState(DOCUMENT_KEY_ENDING);
            }else if(parser.isLineFinished()) parser.throwException("Key can't be on multiple lines.");
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
            if(current == '"'){
                sequence.setCurrentState(DOCUMENT_VALUE_STRING1);
                sequence.markNext(parser);
            }else if(current == '\''){
                sequence.setCurrentState(DOCUMENT_VALUE_STRING2);
                sequence.markNext(parser);
            }else if(current == 't' || current == 'T'){
                sequence.setCurrentState(DOCUMENT_VALUE_KEYWORD_TRUE);
                sequence.setCharacterMark(1);
            }else if(current == 'f' || current == 'F'){
                sequence.setCurrentState(DOCUMENT_VALUE_KEYWORD_FALSE);
                sequence.setCharacterMark(1);
            }else if(current == 'n' || current == 'N'){
                sequence.setCurrentState(DOCUMENT_VALUE_KEYWORD_NULL);
                sequence.setCharacterMark(1);
            }else if(current == '['){
                sequence.setNextSequence(new JsonSequence(sequence.getCurrentKey(),true,DOCUMENT_PRE_VALUE));
                sequence.setCurrentState(DOCUMENT_VALUE_OBJECT);
            }else if(current == '{'){
                sequence.setNextSequence(new JsonSequence(sequence.getCurrentKey(),false,DOCUMENT_PRE_KEY));
                sequence.setCurrentState(DOCUMENT_VALUE_OBJECT);
            }else if(current == '-' || current == '+' || Character.isDigit(current)){
                sequence.setCurrentState(new DocumentNumber(parser.charIndex()));
            }else if(current == ']' && sequence.isArray()){
                sequence.setCurrentState(DOCUMENT_FINISHED);
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

        public DocumentString(char end) {
            this.end = end;
        }

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            parser.previousChar();
            if(current == end && parser.currentChar() != '\\'){
                String value = parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()+1).replace("\n","\\n");
                DocumentEntry primitive = Document.factory().newPrimitiveEntry(sequence.getCurrentKey(),value);
                sequence.pushEntry(primitive);
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }
            parser.skipChar();
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

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            ParserState state = sequence.getNextSequence().getCurrentState();
            if(state.equals(DOCUMENT_FINISHED)){
                sequence.pushEntry(sequence.getNextSequence().getSequenceEntry());
                parser.previousChar();
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }else state.parse(sequence.getNextSequence(),parser,current);
        }
    }

    class DocumentKeyword implements ParserState {

        private char[] equals;
        private Object value;

        public DocumentKeyword(char[] equals,Object value) {
            this.equals = equals;
            this.value = value;
        }

        @Override
        public void parse(JsonSequence sequence, StringParser parser, char current) {
            if((current == ',' || current == '}' || current == ']') && sequence.getCharacterMark() == equals.length){
                parser.previousChar();
                DocumentEntry entry = Document.factory().newPrimitiveEntry(sequence.getCurrentKey(),value);
                sequence.pushEntry(entry);
                sequence.setCurrentState(DOCUMENT_NEXT_PAIR);
            }else if(sequence.getCharacterMark() < equals.length && Character.toLowerCase(current) == equals[sequence.getCharacterMark()]){
                sequence.setCharacterMark(sequence.getCharacterMark()+1);
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
