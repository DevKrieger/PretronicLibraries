/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 16:28
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

import net.prematic.libraries.document.ArrayEntry;
import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.PrimitiveEntry;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class JsonDocumentWriter implements DocumentWriter {

    @Override
    public byte[] write(Document document) {
        return write(document,false).getBytes();
    }

    @Override
    public byte[] write(Document document, Charset charset) {
        return write(document,false).getBytes();
    }

    @Override
    public void write(Writer output, Document document, boolean pretty) {
        try {
            writeObjectValue(output,document,pretty?0:Integer.MIN_VALUE);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    private int writeObjectValue(Writer output, Document document, int indent) throws IOException{
        if(document.isEmpty()) output.write("{}");
        else{
            output.write('{');
            indent++;
            writeNewLine(output, indent);
            indent = writeObjectEntries(output,document,indent,true,true);
            indent--;
            writeNewLine(output, indent);
            output.write('}');
        }
        return indent;
    }

    private int writeArrayValue(Writer output, ArrayEntry array, int indent) throws IOException {
        if(array.isEmpty()) output.write("[]");
        else{
            boolean multiLine = indent>=0 && !array.isPrimitiveArray();
            output.write('[');
            if(multiLine){
                indent++;
                writeNewLine(output, indent);
            }
            indent = writeObjectEntries(output,array,indent,false,multiLine);
            if(multiLine){
                indent--;
                writeNewLine(output, indent);
            }
            output.write(']');
        }
        return indent;
    }

    private void writePrimitiveValue(Writer output, PrimitiveEntry entry) throws IOException {
        if(entry.getAsObject() instanceof String){
            output.write('"');
            output.write(entry.getAsString());
            output.write('"');
        }else if(entry.getAsObject() instanceof String || entry.getAsObject() instanceof Character){
            output.write('"');
            output.write(entry.getAsString());
            output.write('"');
        }else output.write(entry.getAsString());
    }

    private int writeObjectEntries(Writer output, Document document, int indent, boolean key,boolean multiLine) throws IOException{
        boolean first = true;
        for(DocumentEntry entry : document){
            if(first) first = false;
            else{
                output.write(',');
                if(multiLine) writeNewLine(output, indent);
            }
            if(key) writeKey(output,entry.getKey());
            if(entry.isPrimitive()) writePrimitiveValue(output, entry.toPrimitive());
            else if(entry.isArray()) indent = writeArrayValue(output, entry.toArray(),indent);
            else if(entry.isObject()) indent = writeObjectValue(output, entry.toDocument(),indent);
        }
        return indent;
    }

    private void writeKey(Writer output, String key) throws IOException {
        if(key != null){
            output.write('"');
            output.write(key);
            output.write("\": ");
        }
    }

    private void writeNewLine(Writer output, int indent)  throws IOException{
        if(indent >= 0){
            output.write("\n");
            for(int i = 0;i<indent;i++) output.write("  ");
        }
    }
}
