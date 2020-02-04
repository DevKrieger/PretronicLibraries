/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.12.19, 18:30
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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.entry.*;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.utility.GeneralUtil;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class YamlDocumentWriter implements DocumentWriter {

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
            writeObjectValue(output,document,0,true);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public void writeObjectValue(Writer output,Document document, int indent,boolean first0) throws IOException {
        boolean first = first0;
        if(document.hasAttributes()){
            if(indent != 0)  writeNewLine(output,indent);
            writeAttributes(output,document.getAttributes(),indent);
            first = false;
        }
        writeObjectEntries(output,document,indent,first);
    }

    private void writeObjectEntries(Writer output, DocumentNode node, int indent, boolean first) throws IOException {
        for (DocumentEntry entry : node) {
            if(entry.isPrimitive()){
                if(first) first = false;
                else writeNewLine(output, indent);
                writeKey(output,entry.getKey());
                writePrimitiveValue(output,entry.toPrimitive(),indent+1);
            }else if(entry.isArray()){
                if(first) first = false;
                else writeNewLine(output, indent);
                writeKey(output,entry.getKey());
                writeArrayValue(output,entry.toArray(),indent+1);
            }else if(entry.isObject()){
                if(!entry.toDocument().isEmpty()){
                    if(first) first = false;
                    else writeNewLine(output, indent);
                    writeKey(output,entry.getKey());
                    writeObjectValue(output,entry.toDocument(),indent+1,false);
                }
            }
        }
    }

    public void writeArrayValue(Writer output, ArrayEntry array, int indent) throws IOException {
        if(array.hasAttributes()){
            writeNewLine(output, indent);
            writeAttributes(output,array.getAttributes(),indent);
            writeNewLine(output,indent);
            writeKey(output,"_value");
        }
        if(array.isEmpty() || (array.isPrimitiveArray() && array.size() <= 4)){
            writeSimpleArray(output, array);
        }else{
            writeAdvancedArray(output, array, indent);
        }
    }

    public void writeSimpleArray(Writer output, ArrayEntry array) throws IOException{
        output.write("[");
        boolean first = true;
        for (DocumentEntry entry : array) {
            if(first) first = false;
            else output.write(",");
            writePrimitiveData(output,entry.toPrimitive());
        }
        output.write("]");
    }

    public void writeAdvancedArray(Writer output, ArrayEntry array,int indent) throws IOException {
        for (DocumentEntry entry : array) {
            writeNewLine(output,indent);
            output.write("- ");
            if(entry.isPrimitive()){
                writePrimitiveValue(output,entry.toPrimitive(),indent+1);
            }else if(entry.isArray()){
                writeArrayValue(output,entry.toArray(),indent+1);
            }else if(entry.isObject()){
                writeObjectValue(output,entry.toDocument(),indent+1,true);
            }
        }
    }

    private void writeAttributes(Writer output, DocumentAttributes attributes, int indent) throws IOException {
        writeKey(output,"_attributes");
        writeObjectEntries(output,attributes,indent+1,false);
    }

    private void writePrimitiveValue(Writer output, PrimitiveEntry entry, int indent) throws IOException {
        if(entry.hasAttributes()){
            writeNewLine(output, indent);
            writeAttributes(output,entry.getAttributes(),indent);
            writeNewLine(output,indent);
            writeKey(output,"_value");
        }
        writePrimitiveData(output, entry);
    }

    private void writePrimitiveData(Writer output, PrimitiveEntry entry) throws IOException {
        if(!entry.isNull()){
            String data = entry.getAsString();
            if(entry.getAsObject() instanceof String || entry.getAsObject() instanceof Character){
                if(!(data.equalsIgnoreCase("false") || data.equalsIgnoreCase("true") || GeneralUtil.isNumber(data))){
                    output.write('\'');
                    output.write(entry.getAsString());
                    output.write('\'');
                    return;
                }
            }
            output.write(data);
        }else output.write("null");
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
