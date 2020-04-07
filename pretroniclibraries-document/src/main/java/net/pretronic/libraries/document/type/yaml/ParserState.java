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
import net.pretronic.libraries.utility.GeneralUtil;
import net.pretronic.libraries.utility.parser.StringParser;

public interface ParserState {

    ParserState DOCUMENT_START = new DocumentStart();
    ParserState DOCUMENT_KEY = new DocumentKey();

    ParserState DOCUMENT_VALUE_PRE = new DocumentValuePre();
    ParserState DOCUMENT_VALUE_UNDEFINED = new DocumentValueUndefined();
    ParserState DOCUMENT_VALUE_DEFINED_TEXT1 = new DocumentValueDefinedText('"');
    ParserState DOCUMENT_VALUE_DEFINED_TEXT2 = new DocumentValueDefinedText('\'');
    ParserState DOCUMENT_VALUE_TEXT = new DocumentValueText();

    ParserState DOCUMENT_ARRAY_IN = new DocumentArrayIn();
    ParserState DOCUMENT_ARRAY_VALUE = new DocumentArrayValue();
    ParserState DOCUMENT_ARRAY_VALUE_ENDING = new DocumentArrayValueEnding();

    ParserState DOCUMENT_ARRAY_ADVANCED = new DocumentArrayAdvanced();
    ParserState DOCUMENT_ARRAY_ADVANCED_KEY = new DocumentArrayAdvancedKey();
    ParserState DOCUMENT_ARRAY_ADVANCED_VALUE = new DocumentArrayAdvancedValue();
    ParserState DOCUMENT_ARRAY_ADVANCED_NEXT = new DocumentArrayAdvancedNext();
    ParserState DOCUMENT_ARRAY_ADVANCED_SUB_KEY = new DocumentArrayAdvancedSubKey();

    ParserState DOCUMENT_NEXT = new DocumentNext();
    ParserState DOCUMENT_NEXT_SAME = new DocumentNextSame();

    void parse(YamlParser yaml, StringParser parser, char current);


    static boolean isSpaceChar(char c){
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    static void findParent(YamlParser yaml, StringParser parser, int indent) {
        YamlSequence sequence = yaml.getSequence();
        while (indent < sequence.getIndent()){
            DocumentEntry entry = sequence.getEntry();
            sequence = sequence.getParent();
            if(sequence == null){
                parser.throwException("Invalid indent");
                return;
            }
            sequence.pushEntry(entry);
        }
        yaml.setSequence(sequence);
    }

    static String extractText(StringParser parser, int fromLine, int toLine){
        if(fromLine == toLine) return "";
        StringBuilder builder = new StringBuilder();

        for(int i = fromLine;i<=toLine;i++){
            boolean start = true;
            for(int e = 0;e<parser.getLines()[i].length;e++){
                char current = parser.getLines()[i][e];
                if(start){
                    if(isSpaceChar(current)) continue;
                    else start = false;
                }
                builder.append(parser.getLines()[i][e]);
            }
            if(i != toLine) builder.append("\n");
        }
        return builder.toString();
    }

    class DocumentStart implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == '#') {
                parser.skipLine();
            }else if(current != ' '){
                yaml.mark(parser);
                yaml.setState(DOCUMENT_KEY);
            }
        }
    }

    class DocumentKey implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == ':'){
                yaml.setTempKey(parser.getOnLine(yaml.getCharacterMark(),parser.charIndex()).trim());
                yaml.setState(DOCUMENT_VALUE_PRE);
            }
        }
    }

    class DocumentValuePre implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(yaml.getLineMark() != parser.lineIndex() ) {
                yaml.mark(parser);
                yaml.setState(DOCUMENT_NEXT);
                parser.previousChar();
            }else if(current == '#'){
                parser.lineEnd();
                if(parser.hasNextChar()){
                    parser.nextChar();
                    yaml.mark(parser);
                    parser.previousChar();
                    yaml.setState(DOCUMENT_NEXT);
                }
                System.out.println();
            }else if(current == '|' || current == '>'){
                parser.skipLine();
                yaml.setTempIndent(0);
                yaml.mark(parser);
                yaml.setState(DOCUMENT_VALUE_TEXT);
            }else if(current == '['){
                yaml.setSequence(new YamlSequence(yaml.getTempKey(),-1,yaml.getSequence(),true));
                yaml.setState(DOCUMENT_ARRAY_IN);
            }else if(current == '"'){
                yaml.setState(DOCUMENT_VALUE_DEFINED_TEXT1);
                yaml.markNext(parser);
            }else if(current == '\''){
                yaml.setState(DOCUMENT_VALUE_DEFINED_TEXT2);
                yaml.markNext(parser);
            }else if(current == '~'){
                yaml.getSequence().pushEntry(Document.factory().newDocument(yaml.getTempKey()));
                yaml.setState(DOCUMENT_NEXT_SAME);
                yaml.markNext(parser);
            }else if(!isSpaceChar(current)){
                yaml.mark(parser);
                yaml.setState(DOCUMENT_VALUE_UNDEFINED);
                parser.previousChar();
            }
        }
    }

    class DocumentValueUndefined implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(parser.isLineFinished()){
                String text = parser.getOnLine(yaml.getCharacterMark(),parser.charIndex()+1).trim();
                yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry(yaml.getTempKey(),extractValue(text)));
                yaml.setState(DOCUMENT_NEXT_SAME);
            }else if(current == '#'){
                String text = parser.getOnLine(yaml.getCharacterMark(),parser.charIndex()).trim();
                yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry(yaml.getTempKey(),extractValue(text)));
                parser.lineEnd();
                yaml.setState(DOCUMENT_NEXT_SAME);
            }
        }

        private Object extractValue(String text) {
            Object value = text;
            if (text.equalsIgnoreCase("true") || text.equalsIgnoreCase("false")) {
                value = Boolean.parseBoolean(text);
            } else if (GeneralUtil.isNaturalNumber(text)) {
                value = Long.parseLong(text);
            } else if (GeneralUtil.isNumber(text)) {
                value = Double.parseDouble(text);
            }
            return value;
        }
    }

    class DocumentValueDefinedText implements ParserState {

        private final char endCharacter;

        public DocumentValueDefinedText(char endCharacter) {
            this.endCharacter = endCharacter;
        }

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            parser.previousChar();
            char prev = parser.currentChar();
            parser.skipChar();
            char next = 255;
            if(parser.hasNextChar()){
                parser.skipChar();
                next = parser.currentChar();
                parser.previousChar();
            }

            if(current == endCharacter && next == endCharacter){
                parser.skipChar();
                return;
            }

            if(current == endCharacter && prev != '\\'){
                String value = parser.get(yaml.getLineMark(),yaml.getCharacterMark(),parser.lineIndex(),parser.charIndex(),yaml.getSequence().getIndent());
                if(value != null){
                    value = value.replace(endCharacter+""+endCharacter,String.valueOf(endCharacter));
                    value = value.replace(String.valueOf('\\'+endCharacter),String.valueOf(endCharacter));
                }
                yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry(yaml.getTempKey(),value));
                parser.lineEnd();
                yaml.mark(parser);
                yaml.setState(yaml.getSequence().isArray() ? DOCUMENT_ARRAY_VALUE_ENDING : DOCUMENT_NEXT_SAME);
            }
        }
    }

    class DocumentValueText implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(!isSpaceChar(current)){
                if(yaml.getTempIndent() > yaml.getSequence().getIndent()){
                    parser.skipLine();
                    yaml.setTempIndent(0);
                }else{
                    String text = extractText(parser,yaml.getLineMark(),parser.lineIndex()-1);
                    yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry(yaml.getTempKey(),text));
                    findParent(yaml,parser,current);
                    yaml.mark(parser);
                    yaml.setState(DOCUMENT_KEY);
                }
            }else{
                yaml.setTempIndent(yaml.getTempIndent()+1);
            }
        }
    }

    class DocumentNext implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(yaml.getLineMark() != parser.lineIndex()){
                yaml.mark(parser);
            }
            if(!isSpaceChar(current)){
                int indent = parser.charIndex()-yaml.getCharacterMark();
                if(indent == yaml.getSequence().getIndent()){
                    if(current == '-'){
                        if(!yaml.getSequence().isArray()){
                            yaml.setSequence(new YamlSequence(yaml.getTempKey(),indent,yaml.getSequence(),true));
                        }
                        yaml.setState(DOCUMENT_ARRAY_ADVANCED);
                        return;
                    }else{//Changed null entries to document, maybe search for a better solution
                        yaml.getSequence().pushEntry(Document.factory().newDocument(yaml.getTempKey()));
                    }
                }else if(indent > yaml.getSequence().getIndent()){
                    if(current == '-'){
                        yaml.setSequence(new YamlSequence(yaml.getTempKey(),indent,yaml.getSequence(),true));
                        yaml.setState(DOCUMENT_ARRAY_ADVANCED);
                        return;
                    }else{
                        yaml.setSequence(new YamlSequence(yaml.getTempKey(),indent,yaml.getSequence(),false));
                    }
                }else{
                    findParent(yaml, parser, indent);
                }
                yaml.mark(parser);
                yaml.setState(DOCUMENT_KEY);
            }
        }
    }

    class DocumentNextSame implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == '#'){
                parser.skipLine();
                return;
            }
            if (yaml.getLineMark() != parser.lineIndex()) {
                yaml.mark(parser);
            }
            if (!isSpaceChar(current)) {
                int indent = parser.charIndex() - yaml.getCharacterMark();
                if (indent == yaml.getSequence().getIndent()) {
                    yaml.mark(parser);
                    yaml.setState(DOCUMENT_KEY);
                } else if (indent < yaml.getSequence().getIndent()) {
                    findParent(yaml, parser, indent);
                    yaml.setState(DOCUMENT_KEY);
                } else parser.throwException("Invalid indent");
            }
        }
    }

    class DocumentArrayIn implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == '"'){
                yaml.setState(DOCUMENT_VALUE_DEFINED_TEXT1);
                yaml.markNext(parser);
            }else if(current == '\''){
                yaml.setState(DOCUMENT_VALUE_DEFINED_TEXT2);
                yaml.markNext(parser);
            }else if(current == ']'){
                DocumentEntry entry = yaml.getSequence().getEntry();
                yaml.setSequence(yaml.getSequence().getParent());
                yaml.getSequence().pushEntry(entry);
                yaml.setState(DOCUMENT_NEXT_SAME);
            }else if(!isSpaceChar(current)){
                yaml.setState(DOCUMENT_ARRAY_VALUE);
                yaml.mark(parser);
            }
        }
    }

    class DocumentArrayValue implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == ',' || current == ']'){
                String value = parser.getOnLine(yaml.getCharacterMark(),parser.charIndex());
                yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry("value",value));
                if(current == ']'){
                    DocumentEntry entry = yaml.getSequence().getEntry();
                    yaml.setSequence(yaml.getSequence().getParent());
                    yaml.getSequence().pushEntry(entry);
                    yaml.setState(DOCUMENT_NEXT_SAME);
                }else yaml.setState(DOCUMENT_ARRAY_IN);
            }
        }
    }

    class DocumentArrayValueEnding implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == ','){
                yaml.setState(DOCUMENT_ARRAY_IN);
            }else if(current == ']'){
                DocumentEntry entry = yaml.getSequence().getEntry();
                yaml.setSequence(yaml.getSequence().getParent());
                yaml.getSequence().pushEntry(entry);
                yaml.setState(DOCUMENT_NEXT_SAME);
            }
        }
    }


    class DocumentArrayAdvanced implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(!parser.isLineFinished()){
                if(!isSpaceChar(current)){
                    yaml.mark(parser);
                    yaml.setState(DOCUMENT_ARRAY_ADVANCED_KEY);
                }else yaml.setTempIndent(yaml.getTempIndent()+1);
            }
        }
    }

    class DocumentArrayAdvancedKey implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == ':'){
                yaml.setTempKey(parser.getOnLine(yaml.getCharacterMark(),parser.charIndex()).trim());
                int indent = yaml.getSequence().getIndent()+yaml.getTempIndent()+1;
                if(indent != yaml.getSequence().getIndent()){
                    yaml.setSequence(new YamlSequence("value",indent,yaml.getSequence(),false));
                }
                yaml.setState(DOCUMENT_ARRAY_ADVANCED_VALUE);
                yaml.markNext(parser);
            }else if(parser.isLineFinished()){
                String value = parser.getOnLine(yaml.getCharacterMark(),parser.currentChar()).trim();
                yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry("value",value));
                yaml.setState(DOCUMENT_NEXT);
            }
        }
    }

    class DocumentArrayAdvancedValue implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(parser.isLineFinished()){
                String value = parser.getOnLine(yaml.getCharacterMark(),parser.charIndex()+1).trim();
                if(value.startsWith("'") && value.endsWith("'")) value = value.substring(1,value.length()-1);
                yaml.getSequence().pushEntry(Document.factory().newPrimitiveEntry(yaml.getTempKey(),value));
                yaml.setState(DOCUMENT_ARRAY_ADVANCED_NEXT);
            }
        }
    }

    class DocumentArrayAdvancedNext implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(yaml.getLineMark() != parser.lineIndex()){
                yaml.mark(parser);
            }
            if(!isSpaceChar(current)){
                int indent = parser.charIndex()-yaml.getCharacterMark();
                if(indent == yaml.getSequence().getIndent()){
                    yaml.setState(DOCUMENT_ARRAY_ADVANCED_SUB_KEY);
                    yaml.mark(parser);
                }else if(indent > yaml.getSequence().getIndent()){
                    parser.throwException("Yaml does not support sub objects in arrays");
                }else{
                    findParent(yaml, parser, indent);
                    if(current == '-'){
                        yaml.setState(DOCUMENT_ARRAY_ADVANCED);
                        yaml.setTempIndent(0);
                    }else{
                        yaml.setState(DOCUMENT_KEY);
                        yaml.mark(parser);
                    }
                }
            }
        }
    }

    class DocumentArrayAdvancedSubKey implements ParserState {

        @Override
        public void parse(YamlParser yaml, StringParser parser, char current) {
            if(current == ':'){
                yaml.setTempKey(parser.getOnLine(yaml.getCharacterMark(),parser.charIndex()).trim());
                yaml.setState(DOCUMENT_ARRAY_ADVANCED_VALUE);
                yaml.markNext(parser);
            }else if(parser.isLineFinished()){
                parser.throwException("Invalid key-value pair");
            }
        }
    }

}
