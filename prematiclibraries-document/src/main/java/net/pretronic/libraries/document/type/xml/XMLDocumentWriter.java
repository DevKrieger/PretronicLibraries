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

package net.pretronic.libraries.document.type.xml;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentAttributes;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.entry.PrimitiveEntry;
import net.pretronic.libraries.document.io.DocumentWriter;
import net.pretronic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class XMLDocumentWriter implements DocumentWriter {

    @Override
    public byte[] write(Document document) {
        return write(document,false).getBytes();
    }

    @Override
    public byte[] write(Document document, Charset charset) {
        return write(document,false).getBytes(charset);
    }

    @Override
    public void write(Writer output, Document document, boolean pretty) {
        try {
            writeObject(output,document,pretty ? 0 : Integer.MIN_VALUE);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private void writeObject(Writer output, Document document, int indent) throws IOException {
        output.write("<");
        output.write(document.getKey() != null ? document.getKey() : "root");
        if(document.hasAttributes()){
            writeAttributes(output,document.getAttributes());
        }
        output.write(">");
        writeEntries(output, document, indent+1);
        writeNewLine(output,indent);

        output.write("</");
        output.write(document.getKey() != null ? document.getKey() : "root");
        output.write(">");
    }

    private void writeEntries(Writer output, Document document, int indent) throws IOException {
        for (DocumentEntry entry : document) {
            writeNewLine(output, indent);
            if(entry.isObject() || entry.isArray()) writeObject(output, entry.toDocument(), indent);
            else if(entry.isPrimitive()){
                if(entry.getKey().equals("_text")) writeText(output,entry.toPrimitive());
                else writePrimitive(output,entry.toPrimitive());
            }
        }
    }

    private void writeAttributes(Writer output, DocumentAttributes attributes) throws IOException {
        for (DocumentEntry attribute : attributes) {
            output.write(" ");
            output.write(attribute.getKey());
            output.write("=\"");
            output.write(attribute.toPrimitive().getAsString());
            output.write("\"");
        }
    }

    private void writePrimitive(Writer output, PrimitiveEntry primitive) throws IOException {
        output.write("<");
        output.write(primitive.getKey());
        if(primitive.hasAttributes()){
            writeAttributes(output,primitive.getAttributes());
        }
        output.write(">");
        if(primitive.isNull()) output.write("null");
        else output.write(primitive.getAsString());
        output.write("</");
        output.write(primitive.getKey());
        output.write(">");
    }

    private void writeText(Writer output, PrimitiveEntry primitive) throws IOException {
        output.write(primitive.getAsString());
    }

    private void writeNewLine(Writer output, int indent)  throws IOException{
        if(indent >= 0){
            output.write("\n");
            for(int i = 0;i<indent;i++) output.write("  ");
        }
    }
}
