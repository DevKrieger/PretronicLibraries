/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.08.19, 18:59
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

package net.prematic.libraries.document.context;

import net.prematic.libraries.document.DocumentFactory;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.utility.annonations.Experimental;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

@Experimental
public interface DocumentSerializeContext {

    Map<Type,DocumentAdapter<?>> getAdapters();

    Collection<DocumentFactory> getFactories();

    <T> void addAdapter(Class<T> type, DocumentAdapter<T> adapter);

    <T> void addMappingAdapter(Class<T> type, DocumentAdapter<? extends T> adapter);

    <T> void addHierarchyAdapter(Class<T> type, DocumentAdapter<T> adapter);

    void addFactory(DocumentFactory factory);
}
