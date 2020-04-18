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

package net.pretronic.libraries.document.entry;

import net.pretronic.libraries.document.Document;

/**
 * The {@link DocumentBase} is the lowest interface and contains basic methods for transforming in different entries.
 */
public interface DocumentBase {

    /**
     * @return This entry as @{@link PrimitiveEntry}
     */
    PrimitiveEntry toPrimitive();

    /**
     * @return This entry as @{@link ArrayEntry}
     */
    ArrayEntry toArray();

    /**
     * @return This entry as @{@link Document}
     */
    Document toDocument();

    /**
     * @return This entry as @{@link DocumentAttributes}
     */
    DocumentAttributes toAttributes();

    /**
     * @return This entry as @{@link DocumentNode}
     */
    DocumentNode toNode();


    /**
     * @return true if this entry is a primitive
     */
    boolean isPrimitive();

    /**
     * @return true if this entry is an array
     */
    boolean isArray();

    /**
     * @return true if this entry is an object (document)
     */
    boolean isObject();

    /**
     * @return true if this entry is an attribute collection
     */
    boolean isAttributes();

    /**
     * @return true if this entry is a node
     */
    boolean isNode();


    /**
     * Copy this base with the included data.
     *
     * @return The new entry
     */
    DocumentBase copy();

}
