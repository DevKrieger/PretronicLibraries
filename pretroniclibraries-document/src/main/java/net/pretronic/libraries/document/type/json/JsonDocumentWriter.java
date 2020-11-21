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

package net.pretronic.libraries.document.type.json;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.*;
import net.pretronic.libraries.document.io.DocumentWriter;
import net.pretronic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * The @{@link JsonDocumentWriter} writs the document structure into the json (https://www.json.org/json-en.html) file format.
 */
public class JsonDocumentWriter implements DocumentWriter {

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
            if(document.isArray()){
                writeArrayValue(output,document.toArray(),pretty ? 0 : Integer.MIN_VALUE);
            }else{
                writeObjectValue(output,document,pretty ? 0 : Integer.MIN_VALUE);
            }
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    private int writeObjectValue(Writer output, Document document, int indent) throws IOException {
        boolean attributes = document.hasAttributes();
        if(document.isEmpty() && !attributes) output.write("{}");
        else{
            output.write("{");
            writeNewLine(output, ++indent);

            if(attributes) writeAttributeData(output,document.getAttributes(),indent);

            indent = writeObjectEntries(output,document,indent,!attributes, true,true,true);
            writeNewLine(output, --indent);
            output.write("}");
        }
        return indent;
    }

    private int writeArrayValue(Writer output, ArrayEntry document, int indent) throws IOException {
        boolean attributes = document.hasAttributes();
        if(document.isEmpty() && !attributes) output.write("[]");
        else{
            if(attributes) indent = writeAttributes(output,document,indent);

            indent = writeArrayData(output, document, indent);

            if(attributes) {
                writeNewLine(output, --indent);
                output.write('}');
            }
        }
        return indent;
    }

    private int writeArrayData(Writer output, Document document, int indent) throws IOException {
        boolean multipleLines = !document.toArray().isPrimitiveArray() || document.size() > 4;
        output.write("[");
        if(multipleLines) writeNewLine(output, ++indent);


        indent = writeObjectEntries(output,document,indent,true,false,multipleLines,false);

        if(multipleLines) writeNewLine(output, --indent);
        output.write("]");
        return indent;
    }

    private int writeObjectEntries(Writer output, DocumentNode document, int indent, boolean first, boolean key, boolean multipleLines, boolean skipNull) throws IOException {
        for (DocumentEntry entry : document) {
            if(skipNull && entry.isPrimitive() && entry.toPrimitive().isNull()) continue;
            if(first) first = false;
            else{
                output.write(',');
                if(multipleLines) writeNewLine(output, indent);
            }

            if(key) writeKey(output,entry.getKey(),isPretty(indent));

            if(entry.isPrimitive()) indent = writePrimitiveValue(output,entry.toPrimitive(),indent);
            else if(entry.isArray()) indent = writeArrayValue(output, entry.toArray(), indent);
            else if(entry.isObject()) indent = writeObjectValue(output, entry.toDocument(), indent);
        }
        return indent;
    }

    private int writePrimitiveValue(Writer output, PrimitiveEntry entry, int indent) throws IOException {
        if(entry.hasAttributes()) indent = writeAttributes(output,entry,indent);

        writePrimitiveData(output, entry);

        if(entry.hasAttributes()) {
            writeNewLine(output, --indent);
            output.write('}');
        }
        return indent;
    }

    private void writePrimitiveData(Writer output, PrimitiveEntry entry) throws IOException {
        if(entry.isNull()){
            output.write("null");
        }else if(entry.getAsObject() instanceof String || entry.getAsObject() instanceof Character) {
            output.write('"');
            output.write(escapeString(entry.getAsString()));
            output.write('"');
        }else output.write(entry.getAsString());
    }

    private String escapeString(String input){
        return input
                .replace("\\","\\\\")
                .replace("\"","\\\"")
                .replace("\t","\\t")
                .replace("\n","\\n");
    }

    private int writeAttributes(Writer output, DocumentEntry entry, int indent) throws IOException {
        output.write('{');
        writeNewLine(output,++indent);

        indent = writeAttributeData(output,entry.getAttributes(),indent);
        output.write(',');

        writeNewLine(output,indent);
        writeKey(output,"_value",isPretty(indent));
        return indent;
    }

    private int writeAttributeData(Writer output, DocumentAttributes attributes, int indent0) throws IOException {
        int indent = indent0;
        writeKey(output,"_attributes",isPretty(indent));
        output.write("{");
        writeNewLine(output, ++indent);

        indent = writeObjectEntries(output,attributes,indent,true,true,true,true);

        writeNewLine(output, --indent);
        output.write("}");
        return indent;
    }

    private void writeKey(Writer output, String key, boolean pretty) throws IOException {
        if(key != null){
            output.write('"');
            output.write(key);
            output.write("\":");
            if(pretty) output.write(' ');
        }
    }

    private void writeNewLine(Writer output, int indent)  throws IOException{
        if(indent >= 0){
            output.write('\n');
            for(int i = 0;i < indent;i++) output.write("  ");
        }
    }

    private boolean isPretty(int ident){
        return ident >= 0;
    }

}
