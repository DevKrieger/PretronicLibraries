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

package net.pretronic.libraries.document.io;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.utility.io.IORuntimeException;

import java.io.*;
import java.nio.charset.Charset;

/**
 * The {@link DocumentReader} provides different methods for writing a document to a specified file format.
 */
public interface DocumentWriter {

    byte[] write(Document document);

    byte[] write(Document document, Charset charset);

    default String write(Document document, boolean pretty){
        StringWriter writer = new StringWriter();
        write(writer,document,pretty);
        return writer.toString();
    }

    default void write(File location, Document document){
        write(location, document,true);
    }

    default void write(File location, Document document, boolean pretty){
        try {
            if(!location.exists() && !location.createNewFile()){
                throw new IllegalArgumentException("Could not create "+location.getName());
            }
            write(new FileOutputStream(location),document,pretty);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    default void write(File location, Charset charset, Document document, boolean pretty){
        try {
            write(new FileOutputStream(location),charset,document,pretty);
        } catch (FileNotFoundException exception) {
            throw new IORuntimeException(exception);
        }
    }

    default void write(OutputStream output, Document document, Charset charset){
        write(output, document,true);
    }

    default void write(OutputStream output, Document document, boolean pretty){
        try {
            Writer out = new OutputStreamWriter(output);
            write(out, document, pretty);
            out.flush();
            out.close();
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    default void write(OutputStream output,Charset charset, Document document, boolean pretty){
        try {
            Writer out = new OutputStreamWriter(output,charset);
            write(out, document, pretty);
            out.flush();
            out.close();
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    void write(Writer output, Document document, boolean pretty);
}
