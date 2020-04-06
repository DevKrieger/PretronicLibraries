/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.20, 16:00
 * @web %web%
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

package net.pretronic.libraries.document;

import net.pretronic.libraries.document.type.DocumentFileType;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class Test {

    public static void main(String[] args) {
        Document data = Document.newDocument();
        data.set("test",10);
        data.set("hallo","test");
        data.set("hallo2","test");
        data.set("hallo3","test");


        byte[] result = DocumentFileType.BINARY.getWriter().write(data, StandardCharsets.UTF_8);
        System.out.println(new String(result));
        Document in = DocumentFileType.BINARY.getReader().read(new ByteArrayInputStream(result),StandardCharsets.UTF_8);
        System.out.println(DocumentFileType.JSON.getWriter().write(in,true));

    }
}
