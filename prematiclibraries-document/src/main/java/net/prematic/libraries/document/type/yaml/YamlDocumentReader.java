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

import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.GeneralUtil;
import net.prematic.libraries.utility.parser.StringParser;

import static net.prematic.libraries.document.type.Characters.BACK;
import static net.prematic.libraries.document.type.Characters.SPACE;

public class YamlDocumentReader implements DocumentReader {

    @Override
    public Document read(StringParser parser) {
        return nextObject(parser, null);
    }

    private DocumentEntry next(StringParser parser, String key) {
        while(parser.hasNext()) {
            if(parser.charIndex() == 0){
                return nextObject(parser,key);
            }else{
                return nextPrimitive(parser,key);
            }
        }
        return null;
    }

    private Document nextObject(StringParser parser, String key) {
        System.out.println(key+" -> Object");
        Document document = DocumentRegistry.getFactory().newDocument(key);
        int spaceCount = 0;
        int finalCount = -1;
        while(parser.hasNext()) {
            System.out.println("Index: "+parser.lineIndex()+" | "+parser.charIndex());
            char input = parser.nextChar();
            if(input == SPACE) spaceCount++;
            else{
                System.out.println("Place: "+finalCount+" | "+spaceCount);
                if(finalCount == -1) {
                    finalCount = spaceCount;
                    parser.previousChar();
                }else if(finalCount != spaceCount) return document;
                else parser.previousChars(2);
                String entryKey = readKey(parser);
                System.out.println(parser.lineIndex()+" | "+parser.charIndex());
                document.entries().add(next(parser,entryKey));
                spaceCount = 0;
            }
        }
        return document;
    }

    private DocumentEntry nextPrimitive(StringParser parser, String key) {
        System.out.println(key+" -> Primitive");
        String primitive = parser.currentUntilNextLine();
        System.out.println(parser.lineIndex()+" | "+parser.charIndex());
        parser.skipChar();
        parser.skipSpaces();
        if(primitive.equalsIgnoreCase("true")) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, true);
        } else if(primitive.equals("false")) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, false);
        } else if(GeneralUtil.isNaturalNumber(primitive)) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, Long.valueOf(primitive));
        } else if(GeneralUtil.isNumber(primitive)) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, Double.valueOf(primitive));
        } else {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, primitive);
        }
    }

    private String readKey(StringParser parser) {
        String key = parser.nextUntil(':');
        parser.skipChar();
        return key;
    }

    private boolean isIgnoredChar(char c) {
        return c == SPACE || c == BACK;
    }
}
