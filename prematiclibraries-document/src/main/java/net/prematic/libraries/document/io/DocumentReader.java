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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.utility.parser.StringParser;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

public interface DocumentReader {

    Document read(byte[] content);

    Document read(byte[] content, Charset charset);

    Document read(String content);

    Document read(File location);

    Document read(File location, Charset charset);

    Document read(InputStream input);

    Document read(InputStream input, Charset charset);

    Document read(StringParser parser);

}
