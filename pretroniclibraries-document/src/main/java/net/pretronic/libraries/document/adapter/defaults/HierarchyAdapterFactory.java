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

package net.pretronic.libraries.document.adapter.defaults;

import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.adapter.DocumentAdapterFactory;
import net.pretronic.libraries.utility.reflect.TypeReference;

public class HierarchyAdapterFactory implements DocumentAdapterFactory {

    private final DocumentAdapter adapter;
    private final Class<?> rawClass;

    public HierarchyAdapterFactory(DocumentAdapter adapter, Class<?> rawClass) {
        this.adapter = adapter;
        this.rawClass = rawClass;
    }

    @Override
    public <T> DocumentAdapter<T> create(TypeReference<T> type) {
        return rawClass.isAssignableFrom(type.getRawClass())?adapter:null;
    }

    public static HierarchyAdapterFactory newFactory(DocumentAdapter adapter, Class<?> rawClass){
        return new HierarchyAdapterFactory(adapter,rawClass);
    }
}
