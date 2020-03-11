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

package net.pretronic.libraries.document.type;

import net.pretronic.libraries.document.DocumentRegistry;
import net.pretronic.libraries.document.io.DocumentReader;
import net.pretronic.libraries.document.io.DocumentWriter;
import net.pretronic.libraries.document.type.binary.BinaryDocumentReader;
import net.pretronic.libraries.document.type.binary.BinaryDocumentWriter;
import net.pretronic.libraries.document.type.json.JsonDocumentReader;
import net.pretronic.libraries.document.type.json.JsonDocumentWriter;
import net.pretronic.libraries.document.type.properties.PropertiesDocumentReader;
import net.pretronic.libraries.document.type.properties.PropertiesDocumentWriter;
import net.pretronic.libraries.document.type.xml.XMLDocumentWriter;
import net.pretronic.libraries.document.type.xml.XmlDocumentReader;
import net.pretronic.libraries.document.type.yaml.YamlDocumentReader;
import net.pretronic.libraries.document.type.yaml.YamlDocumentWriter;

/**
 * The document libraries supports different file types for reading and writing the data. You are able to
 * register a custom {@link DocumentFileType} in the {@link DocumentRegistry}
 */
public class DocumentFileType {

    public static final DocumentFileType JSON = new DocumentFileType("JSON","json",new JsonDocumentWriter(),new JsonDocumentReader());

    public static final DocumentFileType YAML = new DocumentFileType("YAML","yml",new YamlDocumentWriter(),new YamlDocumentReader());

    public static final DocumentFileType BINARY = new DocumentFileType("BINARY","bin",new BinaryDocumentWriter(),new BinaryDocumentReader());

    public static final DocumentFileType XML = new DocumentFileType("XML", "xml", new XMLDocumentWriter(), new XmlDocumentReader());

    public static final DocumentFileType PROPERTIES = new DocumentFileType("PROPERTIES", "properties", new PropertiesDocumentWriter(), new PropertiesDocumentReader());


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
