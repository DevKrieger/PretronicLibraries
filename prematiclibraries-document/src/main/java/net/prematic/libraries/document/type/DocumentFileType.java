/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.06.19 22:46
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

package net.prematic.libraries.document.type;

import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.document.type.binary.BinaryDocumentReader;
import net.prematic.libraries.document.type.binary.BinaryDocumentWriter;
import net.prematic.libraries.document.type.json.JsonDocumentReader;
import net.prematic.libraries.document.type.json.JsonDocumentWriter;
import net.prematic.libraries.document.type.xml.XmlDocumentReader;
import net.prematic.libraries.document.type.xml.XmlDocumentWriter;
import net.prematic.libraries.document.type.yaml.YamlDocumentReader;
import net.prematic.libraries.document.type.yaml.YamlDocumentWriter;

public class DocumentFileType {

    public static final DocumentFileType JSON = new DocumentFileType("JSON","json",new JsonDocumentWriter(),new JsonDocumentReader());

    public static final DocumentFileType YAML = new DocumentFileType("YAML","yml",new YamlDocumentWriter(),new YamlDocumentReader());

    public static final DocumentFileType BINARY = new DocumentFileType("BINARY","bin",new BinaryDocumentWriter(),new BinaryDocumentReader());

    public static final DocumentFileType XML = new DocumentFileType("XML", "xml", new XmlDocumentWriter(), new XmlDocumentReader());

    private final String name, ending;
    private final DocumentWriter writer;
    private final DocumentReader reader;

    public DocumentFileType(String name, String ending, DocumentWriter writer, DocumentReader reader) {
        this.name = name;
        this.ending = ending;
        this.writer = writer;
        this.reader = reader;
    }

    public String getName() {
        return name;
    }

    public String getEnding() {
        return ending;
    }

    public DocumentWriter getWriter() {
        return writer;
    }

    public DocumentReader getReader() {
        return reader;
    }
}
