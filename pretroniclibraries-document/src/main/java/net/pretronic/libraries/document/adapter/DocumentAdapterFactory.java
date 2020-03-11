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

import net.pretronic.libraries.document.adapter.defaults.HierarchyAdapterFactory;
import net.pretronic.libraries.utility.reflect.TypeReference;

/**
 * {@link DocumentAdapter} are restricted to a type. With factories you are able to dynamically
 * create adapters for different object types.
 *
 * <p>If no adapter was found for a type, all factory will be asked for a possible adapter.
 * Example: {@link HierarchyAdapterFactory}</p>
 */
public interface DocumentAdapterFactory {

    /**
     * Find and create an adapter for a type
     *
     * @param type The object type
     * @param <T> The new adapter, which corresponds to the reference type
     * @return The new adapter, if an adapter is available or null
     */
    <T> DocumentAdapter<T> create(TypeReference<T> type);

}
