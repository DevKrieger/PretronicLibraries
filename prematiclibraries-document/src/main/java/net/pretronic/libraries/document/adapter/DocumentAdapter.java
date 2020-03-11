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

package net.pretronic.libraries.document.adapter;

import net.pretronic.libraries.document.DocumentContext;
import net.pretronic.libraries.document.DocumentRegistry;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.TypeReference;

/**
 * {@link DocumentAdapter}s are used for mapping special object from java to document and back.
 * Adapters are registered in a {@link DocumentContext} or
 * in the global {@link DocumentRegistry}.
 *
 * <p>Usually you only need mapper in special cases, when the object variables not correspond with the document structure.
 * Examples are: {@link java.util.List}, {@link java.util.Map} or {@link java.util.UUID}.
 * See all default examples in the package {@link net.pretronic.libraries.document.adapter.defaults}
 * </p>
 *
 * @param <T> The type of the object to map
 */
public interface DocumentAdapter<T> {

    /**
     * Transform a document entry in to a java object.
     *
     * @param base The document base part (Can be an primitive, node etc.)
     * @param type The type reference, which contains information about the type to read
     * @return The transformed object
     */
    T read(DocumentBase base, TypeReference<T> type);

    /**
     * Transform a java object into a document entry.
     *
     * @param key The key of the entry (Provided by the internal api)
     * @param object The object to transform
     * @return The object as document
     */
    DocumentEntry write(String key, T object);

}
