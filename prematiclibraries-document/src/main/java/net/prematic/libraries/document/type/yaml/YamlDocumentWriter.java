/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 18:47
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

import net.prematic.libraries.document.ArrayEntry;
import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.PrimitiveEntry;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class YamlDocumentWriter implements DocumentWriter {

    @Override
    public byte[] write(Document document) {
        return new byte[0];
    }

    @Override
    public byte[] write(Document document, Charset charset) {
        return new byte[0];
    }

    @Override
    public void write(Writer output, Document document, boolean pretty) {
        try {
            writeObjectValue(output,document,-1);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    public int writeObjectValue(Writer output,Document document, int indent) throws IOException{
        boolean first = true;

        if(document.getKey() != null){
            indent++;
            if(indent != 0) writeNewLine(output, indent);
        }

        for(DocumentEntry entry : document){
            if(first) first = false;
            else writeNewLine(output, indent);
            writeKey(output,entry.getKey());
            if(entry.isPrimitive()) writePrimitiveValue(output, entry.toPrimitive());
            else if(entry.isArray()) indent = writeArrayValue(output, entry.toArray(),indent);
            else if(entry.isObject()) indent = writeObjectValue(output, entry.toDocument(),indent);
        }

        if(document.getKey() != null) indent--;
        return indent;
    }

    public int writeArrayValue(Writer output, ArrayEntry array, int indent) throws IOException {
        if(array.isEmpty()) output.write("[]");
        else{
            boolean multiLine = !array.isPrimitiveArray() || array.size()>=8;
            if(multiLine){
                indent++;
                writeNewLine(output, indent);
            }else output.write("[");
            boolean first = true;
            for(DocumentEntry entry : array){
                if(first){
                    first = false;
                    if(multiLine) output.write("- ");
                }else{
                    if(multiLine){
                        writeNewLine(output, indent);
                        output.write("- ");
                    }else output.write(",");
                }
                writeEntry(output,entry,indent);
            }
            if(!multiLine) output.write("]");
        }
        return indent;
    }

    public void writeEntry(Writer output, DocumentEntry entry, int indent) throws IOException{
        if(entry.isPrimitive()) writePrimitiveValue(output, entry.toPrimitive());
        else if(entry.isArray()) indent = writeArrayValue(output, entry.toArray(),indent);
        else if(entry.isObject()) indent = writeObjectValue(output, entry.toDocument(),indent);
    }

    public void writePrimitiveValue(Writer output, PrimitiveEntry entry) throws IOException {
        if(entry.getAsObject() instanceof String){
            output.write('\'');
            output.write(entry.getAsString());
            output.write('\'');
        }else if(entry.getAsObject() instanceof String || entry.getAsObject() instanceof Character){
            output.write('\'');
            output.write(entry.getAsString());
            output.write('\'');
        }else output.write(entry.getAsString());
    }

    public void writeKey(Writer output, String key) throws IOException {
        if(key != null){
            output.write(key);
            output.write(": ");
        }
    }

    public void writeNewLine(Writer output, int indent)  throws IOException {
        if(indent >= 0){
            output.write("\n");
            for(int i = 0;i<indent;i++) output.write("  ");
        }
    }
}
