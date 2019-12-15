/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 21:39
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

package net.prematic.libraries.document.io;

import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.utility.io.InputStreamReadable;
import net.prematic.libraries.utility.parser.StringParser;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface DocumentReader extends InputStreamReadable<Document> {

    default Document read(byte[] content) {
        return read(new String(content));
    }

    default Document read(byte[] content, Charset charset) {
        return read(new String(content,charset));
    }

    default Document read(String content) {
        return read(new StringParser(content));
    }

    default Document read(File location) {
        return read(new StringParser(location));
    }

    default Document read(File location, Charset charset) {
        return read(new StringParser(location,charset));
    }

    default Document read(InputStream input) {
        return read(new StringParser(input));
    }

    default Document read(InputStream input, Charset charset) {
        return read(new StringParser(input,charset));
    }

    Document read(StringParser parser);

}
