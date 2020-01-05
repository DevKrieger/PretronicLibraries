/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 22:47
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

import net.prematic.libraries.document.entry.ArrayEntry;
import net.prematic.libraries.document.entry.DocumentAttributes;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.entry.PrimitiveEntry;

import java.util.List;

/**
 * The {@link DocumentFactory} creates new instances of the different entry types.
 * A factory is registered in the {@link DocumentRegistry}, only one factory can exist per application.
 *
 * <p>Default implementation: {@link net.prematic.libraries.document.simple.SimpleDocumentFactory}</p>>
 */
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
