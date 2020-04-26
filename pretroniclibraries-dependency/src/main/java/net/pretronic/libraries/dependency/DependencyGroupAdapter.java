/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.04.20, 16:03
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

package net.pretronic.libraries.dependency;

import net.pretronic.libraries.document.DocumentContext;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.adapter.DocumentAdapterInitializeAble;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.TypeReference;

public class DependencyGroupAdapter implements DocumentAdapter<DependencyGroup>, DocumentAdapterInitializeAble {

    private DocumentContext context;
    private final DependencyManager dependencyManager;

    public DependencyGroupAdapter(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    @Override
    public DependencyGroup read(DocumentBase base, TypeReference<DependencyGroup> type) {
        if(base.isObject()) return dependencyManager.load(base.toDocument());
        throw new IllegalArgumentException("Entry is not an object");
    }

    @Override
    public DocumentEntry write(String key, DependencyGroup object) {
        return context.serialize(key,object);
    }

    @Override
    public void initialize(DocumentContext context) {
        this.context = context;
    }
}
