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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.entry.DocumentNode;
import net.prematic.libraries.document.entry.PrimitiveEntry;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.parser.StringParser;

/*
@Todo implement attributes
 */
public class PropertiesDocumentReader implements DocumentReader {

    @Override
    public Document read(StringParser parser) {
        Document root = Document.newDocument("root");
        while(parser.hasNextChar()){
            char current = parser.nextChar();
            if(!isIgnoredCharacter(current)){
                if(current == '#' || current == '!') parser.skipLine();
                else{
                    String key = readNextKey(parser);
                    readPrimitive(parser,root,key);
                }
            }
        }
        return root;
    }

    private void readPrimitive(StringParser parser, Document root,String key){
        int keyIndex = key.lastIndexOf(".");
        String entryKey = keyIndex == -1 ? key : key.substring(keyIndex+1);
        PrimitiveEntry entry = prepareEntry(parser,root,key,entryKey);
        if(!parser.isLineFinished()){
            entry.setValue(readValue(parser));
        }
    }

    private PrimitiveEntry prepareEntry(StringParser parser,Document root, String key, String entryKey){
        DocumentNode current = root;
        String[] parts = key.split("\\.");
        for (int i = 0; i < parts.length-1; i++) {
            String part = parts[i];
            if(part.equals("_attributes")){
                current = current.toDocument().getAttributes();
                break;
            }else{
                DocumentEntry temp = current.getEntry(part);
                if(entryKey.equals("_value")){
                    PrimitiveEntry entry = Document.factory().newPrimitiveEntry(part,null);
                    if(temp != null){
                        current.removeEntry(temp);
                        if(temp.hasAttributes()) entry.setAttributes(temp.getAttributes());
                    }
                    current.addEntry(entry);
                    return entry;
                }else if(temp == null){
                    temp = Document.newDocument(part);
                    current.addEntry(temp);
                }
                else if(temp.isPrimitive()) parser.throwException("Invalid object");
                current = temp.toDocument();
            }
        }
        PrimitiveEntry entry = Document.factory().newPrimitiveEntry(entryKey,null);
        System.out.println(current);
        current.addEntry(entry);
        return entry;
    }

    private String readNextKey(StringParser parser){
        int start = parser.charIndex();
        int startLine = parser.lineIndex();
        while (parser.hasNextChar()){
            char input = parser.nextChar();
            if(input == '=' || input == ':' || parser.isLineFinished()) break;
        }
        if(startLine != parser.lineIndex()) parser.throwException("Invalid key");
        String key = parser.getOnLine(start,parser.charIndex());
        parser.skipChar();
        return key;
    }

    private String readValue(StringParser parser){
        String value = parser.currentUntilNextLine();
        if(value.charAt(value.length()-1) == '\\'){
            value = value.substring(0,value.length()-1)+"\n";
            parser.skipSpaces();
            value += readValue(parser);
        }
        return value;
    }

    private boolean isIgnoredCharacter(char input){
        return input == ' ' || input == '\t' || input == '\n' || input == '\r';
    }

}
