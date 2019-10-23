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

package net.prematic.libraries.document.type.ini;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class INIDocumentWriter implements DocumentWriter {

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
            writeObject(output,document,"");
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private void writeObject(Writer output, Document document, String base) throws IOException {
        if(!base.isEmpty()){
            output.write("\n");
            output.write("[");
            output.write(base);
            output.write("]");
            output.write("\n");
        }
        int index = 0;
        for (DocumentEntry entry : document) {
            if(entry.isPrimitive()){
                if(document.isArray()){
                    output.write(base+"."+index);
                    index++;
                }else output.write(entry.getKey());
                output.write("=");
                output.write(entry.toPrimitive().getAsString());
            }else{
                writeObject(output, entry.toDocument(), base+(base.isEmpty()?"":".")+entry.getKey());
            }
            output.write("\n");
        }
    }
}
