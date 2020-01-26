/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.11.19, 13:21
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

package net.prematic.libraries.plugin.description;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentContext;
import net.prematic.libraries.plugin.description.dependency.Dependency;
import net.prematic.libraries.plugin.description.dependency.DependencyAdapter;
import net.prematic.libraries.plugin.description.dependency.PluginDependency;
import net.prematic.libraries.plugin.description.mainclass.MainClass;
import net.prematic.libraries.plugin.description.mainclass.MainClassAdapter;
import net.prematic.libraries.plugin.manager.PluginManager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class DefaultPluginDescription implements PluginDescription{

    public static final Map<String, BiFunction<PluginManager,Document, Dependency>> DEPENDENCY_FACTORIES = new LinkedHashMap<>();

    static {
        DEPENDENCY_FACTORIES.put("plugin",new PluginDependency.Factory());
    }

    private final String name;
    private final String namespace;
    private final String category;
    private final String description;
    private final String author;
    private final String website;
    private final UUID id;
    private final PluginVersion version;
    private final MainClass main;
    private final Document properties;
    private final Collection<Dependency> dependencies;
    private final Collection<String> providers;

    private PluginVersion latestVersion;

    public DefaultPluginDescription(String name,String namespace, String category, String description, String author
            , String website, UUID id, PluginVersion version, MainClass main, Document properties
            , Collection<Dependency> dependencies, Collection<String> providers) {
        this.name = name;
        this.namespace = namespace;
        this.category = category;
        this.description = description;
        this.author = author;
        this.website = website;
        this.id = id;
        this.version = version;
        this.main = main;
        this.properties = properties;
        this.dependencies = dependencies;
        this.providers = providers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public PluginVersion getVersion() {
        return version;
    }

    @Override
    public PluginVersion getLatestVersion() {
        return latestVersion;
    }

    @Override
    public void setLatestVersion(PluginVersion version) {
        this.latestVersion = version;
    }

    @Override
    public MainClass getMain() {
        return main;
    }

    @Override
    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public Collection<String> getProviders() {
        return providers;
    }

    @Override
    public Document getProperties() {
        return properties;
    }

    public static PluginDescription create(PluginManager manager, Document document){
        DocumentContext context = Document.factory().newContext();
        context.registerAdapter(Dependency.class,new DependencyAdapter(manager,DEPENDENCY_FACTORIES));
        context.registerAdapter(MainClass.class,new MainClassAdapter());
        document.setContext(context);
        return document.getAsObject(DefaultPluginDescription.class);
    }
}
