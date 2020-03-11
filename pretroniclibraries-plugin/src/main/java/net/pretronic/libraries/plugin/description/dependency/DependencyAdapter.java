/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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

package net.pretronic.libraries.plugin.description.dependency;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.DocumentRegistry;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.pretronic.libraries.plugin.manager.PluginManager;
import net.pretronic.libraries.utility.reflect.TypeReference;

import java.util.Map;
import java.util.function.BiFunction;

public final class DependencyAdapter implements DocumentAdapter<Dependency> {

    private final PluginManager manager;
    private final Map<String, BiFunction<PluginManager,Document, Dependency>> dependencyFactories;

    public DependencyAdapter(PluginManager manager, Map<String, BiFunction<PluginManager, Document, Dependency>> dependencyFactories) {
        this.manager = manager;
        this.dependencyFactories = dependencyFactories;
    }

    @Override
    public Dependency read(DocumentBase entry, TypeReference<Dependency> type0) {
        if(!entry.isObject()) throw new IllegalArgumentException("Entry is not a document");
        Document document = entry.toDocument();
        String type = entry.toPrimitive().getAsString();
        if(type == null) throw new InvalidPluginDescriptionException("Dependency requires a type");
        BiFunction<PluginManager,Document, Dependency> factory = dependencyFactories.get(type.toLowerCase());
        if(factory == null) throw new InvalidPluginDescriptionException("Invalid dependency type");
        return factory.apply(manager,document);
    }

    @Override
    public DocumentEntry write(String key, Dependency object) {
        Document root = DocumentRegistry.getFactory().newDocument(key);
        root.set("type",object.getType());
        object.write(root);
        return root;
    }
}
