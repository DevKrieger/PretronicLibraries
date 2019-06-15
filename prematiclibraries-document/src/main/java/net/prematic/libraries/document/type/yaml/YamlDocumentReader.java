/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 19:27
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

package net.prematic.libraries.document.type.yaml;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.parser.StringParser;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public class YamlDocumentReader implements DocumentReader {

    @Override
    public Document read(byte[] content) {
        return null;
    }

    @Override
    public Document read(byte[] content, Charset charset) {
        return null;
    }

    @Override
    public Document read(String content) {
        return null;
    }

    @Override
    public Document read(File location) {
        return null;
    }

    @Override
    public Document read(File location, Charset charset) {
        return null;
    }

    @Override
    public Document read(InputStream input) {
        return null;
    }

    @Override
    public Document read(InputStream input, Charset charset) {
        return null;
    }

    @Override
    public Document read(StringParser parser) {
        return null;
    }

    /*
    public static void main(String[] args){
        StringParser parser = new StringParser(new File("test.yml"));

        System.out.println(parser);
    }

    public DocumentEntry next(StringParser parser,String key, int objectIndex){
        while(parser.hasNext()) {
            char input = parser.nextChar();
            if(input == BREAK) return readYamlObject(parser, key);
            else if(input == SQUARE_BRACKET_OPEN);//array
            else if(input == QUOT_1 || input == QUOT_2) return DocumentRegistry.getFactory().newPrimitiveEntry(key,readNextString(parser,input));
            else if(input == 't' || input == 'T');//boolean true
            else if(input == 'f' || input == 'F');//boolean true
            else if(input == 'n' || input == 'N'){
                //checkNextNull(parser);
                return DocumentRegistry.getFactory().newPrimitiveEntry(key,null);
            }
            else if(Character.isDigit(input));//number return DocumentRegistry.getFactory().newPrimitiveEntry(key,readNextNumber(parser));
            else if(Character.isDigit(input));
            else if(!isIgnoredChar(input)) parser.throwException(ERROR_INVALID_CHARACTER);
        }
        return null;
    }

    public Document readYamlObject(StringParser parser,String key){
        Document document = DocumentRegistry.getFactory().newDocument(key);
        int objectIndex = -1, currentIndex = 0;
        while(parser.hasNext()){
            char input = parser.nextChar();
            if(input == ' '){
                currentIndex++;
            }else{
                if(objectIndex == -1){
                    objectIndex = currentIndex;
                }
                else if(objectIndex != currentIndex){
                    parser.previousChars(currentIndex);
                    break;
                }
                String entryKey = readNextKey(parser);
                document.entries().add(next(parser,entryKey));
            }
            if(!isIgnoredChar(input)){
                String entryKey = readNextKey(parser);
                document.entries().add(next(parser,entryKey));
            }
        }
        return document;
    }

    public String readNextKey(StringParser parser){
        return parser.nextUntil(':');
    }

    public String readNextString(StringParser parser, char endChar){
        return parser.nextUntil(endChar);
    }

    public static boolean isIgnoredChar(char c){
        return c == SPACE || c == BREAK || c == BACK || c == TAB;
    }
     */
}
