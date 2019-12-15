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

import net.prematic.libraries.utility.parser.StringParser;

public interface ParserState {

    ParserState DOCUMENT_START = new DocumentStart();

    ParserState FIRST_TAG = new FirstTag();
    ParserState SPECIAL_TAG = new SpecialTag();
    ParserState PRE_TAG = new SpecialTag();

    ParserState TAG_NAME = new TagName();


    void parse(XmlSequence sequence, StringParser parser, char current);

    static boolean isNotIgnoredChar(char c){
        return c != ' ' && c != '\n' && c != '\t' && c != '\r';
    }

    class DocumentStart implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '<'){
                sequence.setCurrentState(FIRST_TAG);
            }else if(isNotIgnoredChar(current)) parser.throwException("Invalid Character");
        }
    }

    class FirstTag implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ':' || current == '?') sequence.setCurrentState(SPECIAL_TAG);
            else if(isNotIgnoredChar(current)) parser.throwException("Invalid Character");
        }
    }

    class SpecialTag implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {

        }
    }

    class PreTag implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == '/'){

            }else if(Character.isLetter(current)){
                sequence.mark(parser);
                sequence.setCurrentState(TAG_NAME);
            }
        }
    }

    class TagName implements ParserState{

        @Override
        public void parse(XmlSequence sequence, StringParser parser, char current) {
            if(current == ' ' || current == '>'){
                sequence.setCurrentKey(parser.getOnLine(sequence.getCharacterMark(),parser.charIndex()));
            //    sequence.setCurrentState(current == ' ' ? : TAG_VALUE);
            }
        }
    }


}
