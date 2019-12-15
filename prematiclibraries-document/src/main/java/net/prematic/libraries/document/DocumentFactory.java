/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 21:56
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

package net.prematic.libraries.document;

import net.prematic.libraries.document.entry.*;

import java.util.List;

public interface DocumentFactory {

    DocumentContext newContext();


    Document newDocument();

    Document newDocument(String key);

    Document newDocument(String key, List<DocumentEntry> entries);


    PrimitiveEntry newPrimitiveEntry(String key, Object object);

    ArrayEntry newArrayEntry(String key);

    ArrayEntry newArrayEntry(String key, List<DocumentEntry> entries);


    DocumentAttributes newAttributes();

    DocumentAttributes newAttributes(List<DocumentEntry> entries);
}
