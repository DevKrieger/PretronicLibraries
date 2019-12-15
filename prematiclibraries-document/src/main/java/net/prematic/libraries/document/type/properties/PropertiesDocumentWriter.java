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

package net.prematic.libraries.document.type.properties;

import net.prematic.libraries.document.entry.ArrayEntry;
import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.io.DocumentWriter;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class PropertiesDocumentWriter implements DocumentWriter {

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
            write(output,document,"");
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private void write(Writer output,Document document, String baseString) throws IOException {
        int i = 0;
        for (DocumentEntry entry : document) {
            if(entry.isPrimitive()){
                if(document instanceof ArrayEntry){
                    output.write(baseString+i);
                    i++;
                }else{
                    output.write(baseString);
                    output.write(entry.getKey());
                }
                output.write("=");
                output.write(entry.toPrimitive().getAsString());
            }else{
                write(output,entry.toDocument(),baseString+entry.getKey()+".");
            }
            output.write("\n");
        }
    }
}
