/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.10.19 14:48
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

package net.prematic.libraries.document.type.properties;

import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.parser.StringParser;

/*
@Todo implement attributes
 */
public class PropertiesDocumentReader implements DocumentReader {

    @Override
    public Document read(StringParser parser) {
        Document document = Document.newDocument();
        while(parser.hasNextChar()){
            char input = parser.nextChar();
            if(!isIgnoredCharacter(input)){
                if(input == '#' || input == '!') parser.skipLine();
                else{
                    String key = readNextKey(parser);
                    String value = readValue(parser);
                    document.entries().add(DocumentRegistry.getFactory().newPrimitiveEntry(key,value));
                }
            }
        }
        return document;
    }

    private String readNextKey(StringParser parser){
        int start = parser.charIndex()-1;
        int end = start;
        boolean pending = false;
        while(parser.hasNextChar()){
            char input = parser.nextChar();
            if(input == ' ' || input == '\n' || input == '\r' || input == '\t' || input ==  '=' || input == ':' ) pending = true;
            else if(parser.isLineFinished()) break;
            else if(pending){
                parser.previousChar();
                break;
            }else end++;
        }
        return parser.get(parser.lineIndex(),start,end+1);
    }

    private String readValue(StringParser parser){
        String value = parser.currentUntilNextLine();
        if(value.charAt(value.length()-1) == '\\'){
            value = value.substring(0,value.length()-1);
            parser.skipSpaces();
            value += " "+readValue(parser);
        }
        return value;
    }

    private boolean isIgnoredCharacter(char input){
        return input == ' ' || input == '\t' || input == '\n' || input == '\r';
    }

}
