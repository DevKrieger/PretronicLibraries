/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 22:02
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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.parser.StringParser;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import static net.prematic.libraries.document.type.Characters.*;

public class JsonDocumentReader implements DocumentReader{

    @Override
    public Document read(byte[] content) {
        return read(new String(content));
    }

    @Override
    public Document read(byte[] content, Charset charset) {
        return read(new String(content,charset));
    }

    @Override
    public Document read(String content) {
        return read(new StringParser(content));
    }

    @Override
    public Document read(File location) {
        return read(new StringParser(location));
    }

    @Override
    public Document read(File location, Charset charset) {
        return read(new StringParser(location,charset));
    }

    @Override
    public Document read(InputStream input) {
        return read(new StringParser(input));
    }

    @Override
    public Document read(InputStream input, Charset charset) {
        return read(new StringParser(input,charset));
    }

    @Override
    public Document read(StringParser parser) {
        DocumentEntry entry = next(parser,null);
        if(entry.isObject()) parser.throwException("Invalid json document (No object)");
        return entry.toDocument();
    }

    public DocumentEntry next(StringParser parser, String key) {
        while(parser.hasNext()){
            char input = parser.nextChar();
            if(!isIgnoredChar(input)){
                if(input == BRACE_OPEN) return readJsonObject(parser, key);
                else if(input == SQUARE_BRACKET_OPEN) return readArray(parser, key);
                else if(input == QUOT_1 || input == QUOT_2) return DocumentRegistry.getFactory().newPrimitiveEntry(key,readNextString(parser,input));
                else if(input == 't' || input == 'T') return DocumentRegistry.getFactory().newPrimitiveEntry(key,readNextBoolean(parser,true));
                else if(input == 'f' || input == 'F') return DocumentRegistry.getFactory().newPrimitiveEntry(key,readNextBoolean(parser,false));
                else if(input == 'n' || input == 'N'){
                    checkNextNull(parser);
                    return DocumentRegistry.getFactory().newPrimitiveEntry(key,null);
                }
                else if(Character.isDigit(input)) return DocumentRegistry.getFactory().newPrimitiveEntry(key,readNextNumber(parser));
                else parser.throwException(ERROR_INVALID_CHARACTER);
            }
        }
        parser.throwException("Nothing found.");
        return null;
    }

    public Document readJsonObject(StringParser parser, String key) {
        Document document = DocumentRegistry.getFactory().newDocument(key);
        char input;
        while(parser.hasNext() && (input = parser.nextChar()) != BRACE_CLOSE){
            if(!isIgnoredChar(input) && input != ','){
                if(input == QUOT_1 || input == QUOT_2){
                    String key3 = readNextString(parser,input);
                    document.entries().add(next(parser,key3));
                }else parser.throwException(ERROR_INVALID_CHARACTER);
            }
        }
        return document;
    }

    public Document readArray(StringParser parser, String key) {
        Document document = DocumentRegistry.getFactory().newArrayEntry(key);
        char input;
        int index = 0;
        while(parser.hasNext() && (input = parser.nextChar()) != SQUARE_BRACKET_CLOSE){
            if(!isIgnoredChar(input) && input != ','){
                parser.previousChar();
                document.entries().add(next(parser,"array-item-"+index));
            }
        }
        return document;
    }

    public String readNextString(StringParser parser, char endChar) {
        try{
            return parser.nextUntil(endChar, BACK_SLASH);
        }finally {
            parser.nextChar();
        }
    }

    public boolean readNextBoolean(StringParser parser, boolean check) {
        char[] checkSum = check?BOOLEAN_TRUE:BOOLEAN_FALSE;
        int i = 1;//
        while(parser.hasNext()){
            char input = Character.toLowerCase(parser.nextChar());
            if(checkSum.length <= i){
                if(!isIgnoredChar(input)){
                    if(input == COMMA) return check;
                    else if(input == BRACE_CLOSE  || input == SQUARE_BRACKET_CLOSE){
                        parser.previousChars(2);
                        return check;
                    }else parser.throwException(ERROR_INVALID_CHARACTER);
                }
            }else if(input != checkSum[i]) return false;
            else i++;
        }
        parser.throwException(ERROR_INVALID_CHARACTER);
        return false;
    }

    public void checkNextNull(StringParser parser) {
        int i = 1;
        while(parser.hasNext()){
            char input = Character.toLowerCase(parser.nextChar());
            if(NULL.length <= i){
                if(!isIgnoredChar(input)){
                    if(input == COMMA) return;
                    else if(input == BRACE_CLOSE  || input == SQUARE_BRACKET_CLOSE){
                        parser.previousChars(2);
                        return;
                    }else parser.throwException(ERROR_INVALID_CHARACTER);
                }
            }else if(input != NULL[i]); //throw exception
            else i++;
        }
        parser.throwException(ERROR_INVALID_CHARACTER);
    }

    public Number readNextNumber(StringParser parser) {
        parser.previousChar();
        boolean negative = false;
        boolean decimal = false;
        double decimalPlace = 1.0;
        double value = 0D;

        while(parser.hasNext()){
            char input = parser.nextChar();
            if(input == COMMA) break;
            else if(input == BRACE_CLOSE || input == SQUARE_BRACKET_CLOSE){
                parser.previousChars(2);
                break;
            }else if(input == NEGATIVE){
                if(value == 0) negative = true;
                else parser.throwException(ERROR_INVALID_CHARACTER);
            }else if(input == POSITIVE){
                if(value == 0) negative = false;
                else parser.throwException(ERROR_INVALID_CHARACTER);
            }else if(input == DOT){
                if(decimal) parser.throwException(ERROR_INVALID_CHARACTER);
                else decimal = true;
            }else if(Character.isDigit(input)){
                if(decimal){
                    decimalPlace *= 10;
                    value += (input-48)/decimalPlace;
                }else value = value*10 + (input-48);
            }else if(!isIgnoredChar(input)) parser.throwException(ERROR_INVALID_CHARACTER);
        }
        if(negative) value = -value;
        if(decimal) return value;
        else{
            return value;
        }
    }

    public static boolean isIgnoredChar(char c){
        return c == SPACE || c == BREAK || c == BACK || c == TAB || c == COLON ;
    }

}
