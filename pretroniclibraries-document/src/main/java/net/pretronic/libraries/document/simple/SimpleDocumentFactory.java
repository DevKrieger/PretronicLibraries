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

package net.pretronic.libraries.document.simple;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.DocumentContext;
import net.pretronic.libraries.document.DocumentFactory;
import net.pretronic.libraries.document.entry.ArrayEntry;
import net.pretronic.libraries.document.entry.DocumentAttributes;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.entry.PrimitiveEntry;

import java.util.List;

public class SimpleDocumentFactory implements DocumentFactory {

    @Override
    public DocumentContext newContext() {
        return new SimpleDocumentContext();
    }

    @Override
    public Document newDocument() {
        return newDocument(null);
    }

    @Override
    public Document newDocument(String key) {
        return new SimpleDocument(key);
    }

    @Override
    public Document newDocument(String key, List<DocumentEntry> entries) {
        return new SimpleDocument(key,entries);
    }

    @Override
    public PrimitiveEntry newPrimitiveEntry(String key, Object object) {
        return new SimplePrimitiveEntry(key,object);
    }

    @Override
    public ArrayEntry newArrayEntry(String key) {
        return new SimpleArrayEntry(key);
    }

    @Override
    public ArrayEntry newArrayEntry(String key, List<DocumentEntry> entries) {
        return new SimpleArrayEntry(key,entries);
    }

    @Override
    public DocumentAttributes newAttributes() {
        return new SimpleDocumentAttribute();
    }

    @Override
    public DocumentAttributes newAttributes(List<DocumentEntry> entries) {
        return new SimpleDocumentAttribute(entries);
    }
}
