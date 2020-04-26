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
import net.pretronic.libraries.utility.parser.StringParser;

public interface ParserState {

    ParserState DOCUMENT_START = new DocumentStart();

    ParserState FIRST_TAG = new FirstTag();
    ParserState SPECIAL_TAG = new SpecialTag();
    ParserState SPECIAL_END_TAG = new SpecialEndTag();

    ParserState TAG_IN = new TagIn();
    ParserState TAG_ATTRIBUTE_KEY = new TagAttributeKey();
    ParserState TAG_ATTRIBUTE_KEY_END = new TagAttributeKeyEnd();
    ParserState TAG_ATTRIBUTE_VALUE_PRE = new TagAttributeValuePre();
    ParserState TAG_ATTRIBUTE_VALUE = new TagAttributeValue();

    ParserState TAG_FINISHING = new TagFinishing();
    ParserState TAG_FINISH = new TagFinish();

    ParserState NEXT = new Next();
    ParserState NEXT_TAG_PRE = new NextTagPre();
    ParserState NEXT_TAG = new NextTag();

    ParserState TEXT = new Text();

    ParserState SPECIAL = new Special();

    ParserState CDAT_PRE = new CommentCDataPre();
    ParserState CDAT = new CommentCData();
    ParserState CDAT_ENDING = new CommentCDataEnding();

    ParserState COMMENT_PRE2 = new CommentPre2();
    ParserState COMMENT = new Comment();
    ParserState COMMENT_FINISHING = new CommentFinishing();
    ParserState COMMENT_FINISHING2 = new CommentFinishing2();

    ParserState TAG_NAME = new TagName();


    void parse(XmlSequence sequence, StringParser parser, char current);

    static boolean isNotIgnoredChar(char c){
        return c != ' ' && c != '\n' && c != '\t' && c != '\r';
    }

    class DocumentStart implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '<') sequence.setCurrentState(FIRST_TAG);
            else if(isNotIgnoredChar(current)) parser.throwException("Invalid Character");
        }
    }

    class FirstTag implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ':' || current == '?' || current == '!') sequence.setCurrentState(SPECIAL_TAG);
            else if(Character.isLetter(current)){
                sequence.setNextSequence(new XmlSequence(TAG_NAME));
                sequence.getNextSequence().mark(parser);
                sequence.setCurrentState(NEXT_TAG);
            }else if(isNotIgnoredChar(current)) parser.throwException("Invalid Character");
        }
    }

    class SpecialTag implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '>') sequence.setCurrentState(SPECIAL_END_TAG);
        }
    }

    class SpecialEndTag implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '<'){
                sequence.setCurrentState(NEXT_TAG_PRE);
                sequence.markNext(parser);
            }else if(isNotIgnoredChar(current)) parser.throwException("Invalid character at document start");
        }
    }

    class TagName implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ' ' || current == '\t'  || current == '\r'  || current == '>'){
                if(parser.charIndex()-sequence.getCharacterMark() <= 1) parser.throwException("Invalid space");
                else{
                    sequence.setKey(parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()));
                    if(current == '>') sequence.setCurrentState(NEXT);
                    else sequence.setCurrentState(TAG_IN);
                }
            }else if(current == '/'){
                sequence.setKey(parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()));
                sequence.setCurrentState(TAG_FINISHING);
                parser.skipChar();
            }
        }
    }

    class TagIn implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '>') sequence.setCurrentState(NEXT);
            else if(Character.isLetter(current) || Character.isDigit(current)){
                sequence.mark(parser);
                sequence.setCurrentState(TAG_ATTRIBUTE_KEY);
            }
        }
    }

    class TagAttributeKey implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ' ' || current == '='){
                sequence.setTempKey(parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()));
                sequence.setCurrentState(current == ' ' ? TAG_ATTRIBUTE_KEY_END : TAG_ATTRIBUTE_VALUE_PRE);
            }
        }
    }

    class TagAttributeKeyEnd implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '='){
                sequence.markNext(parser);
                sequence.setCurrentState(TAG_ATTRIBUTE_VALUE_PRE);
            }else if(isNotIgnoredChar(current)) parser.throwException("= required");
        }
    }

    class TagAttributeValuePre implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '"'){
                sequence.markNext(parser);
                sequence.setCurrentState(TAG_ATTRIBUTE_VALUE);
            }else if(isNotIgnoredChar(current)) parser.throwException("Invalid character");
        }
    }

    class TagAttributeValue implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '"'){
                sequence.pushAttribute(parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()));
                sequence.setCurrentState(TAG_IN);
            }
        }
    }

    class Next implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '<'){
                sequence.setCurrentState(NEXT_TAG_PRE);
                sequence.mark(parser);
            }else if(isNotIgnoredChar(current)){
                sequence.mark(parser);
                sequence.setCurrentState(TEXT);
            }
        }
    }

    class NextTagPre implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '/') sequence.setCurrentState(TAG_FINISHING);
            else if(current == '!') sequence.setCurrentState(SPECIAL);
            else if(Character.isLetter(current)){
                sequence.setNextSequence(new XmlSequence(TAG_NAME));
                sequence.getNextSequence().mark(parser);
                sequence.setCurrentState(NEXT_TAG);
            }else if(isNotIgnoredChar(current)) sequence.setCurrentState(TEXT);
        }
    }

    class NextTag implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            ParserState state = sequence.getNextSequence().getCurrentState();
            if(state.equals(TAG_FINISH)){
                sequence.pushEntry(sequence.getNextSequence().getSequenceEntry());
                sequence.setCurrentState(NEXT);
                parser.previousChar();
            }else state.parse(sequence.getNextSequence(), parser, current);
        }
    }


    class Text implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '<'){
                sequence.pushEntry(Document.factory().newPrimitiveEntry("_text"
                        ,parser.get(sequence.getLineMark(),sequence.getCharacterMark(),parser.lineIndex(),parser.charIndex())));
                sequence.setCurrentState(NEXT_TAG_PRE);
            }
        }
    }

    class Special implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '-') sequence.setCurrentState(COMMENT_PRE2);
            else if(current == '[') sequence.setCurrentState(CDAT_PRE);
            else if(isNotIgnoredChar(current)) sequence.setCurrentState(TEXT);
        }
    }

    class CommentCDataPre implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == 'C'){
                parser.skipChars(5);
                sequence.setCurrentState(CDAT);
                sequence.markNext(parser);
            }
        }
    }

    class CommentCData implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ']') sequence.setCurrentState(CDAT_ENDING);
        }
    }

    class CommentCDataEnding implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ']'){
                parser.previousChars(2);
                sequence.pushEntry(Document.factory().newPrimitiveEntry("_text"
                        ,parser.get(sequence.getLineMark(),sequence.getCharacterMark(),parser.lineIndex(),parser.charIndex())));
                parser.skipChars(3);
                sequence.setCurrentState(ParserState.NEXT);
            }else sequence.setCurrentState(CDAT);
        }
    }


    class CommentPre2 implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '-') sequence.setCurrentState(COMMENT);
            else if(isNotIgnoredChar(current)) parser.throwException("Invalid character");
        }
    }

    class Comment implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '-') sequence.setCurrentState(COMMENT_FINISHING);
        }
    }

    class CommentFinishing implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '-') sequence.setCurrentState(COMMENT_FINISHING2);
            else sequence.setCurrentState(COMMENT);
        }
    }

    class CommentFinishing2 implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '>') sequence.setCurrentState(NEXT);
            else sequence.setCurrentState(COMMENT);
        }
    }

    class TagFinishing implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '>') sequence.setCurrentState(TAG_FINISH);
        }
    }

    class TagFinish implements ParserState {

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {/*Unused*/}

    }

}
