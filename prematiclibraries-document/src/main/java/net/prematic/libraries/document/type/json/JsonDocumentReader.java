/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 14.12.19, 13:06
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
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.parser.StringParser;

public class JsonDocumentReader implements DocumentReader {

    @Override
    public Document read(StringParser parser) {
        JsonSequence sequence = new JsonSequence("root",false,ParserState.DOCUMENT_START);
        while (parser.hasNextChar()){
            sequence.getCurrentState().parse(sequence,parser,parser.nextChar());
        }
        return sequence.getSequenceEntry().toDocument();
    }
}
