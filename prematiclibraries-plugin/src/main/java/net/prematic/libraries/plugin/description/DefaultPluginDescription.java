/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.08.19, 19:15
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
import net.prematic.libraries.document.WrappedDocument;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

public class DefaultPluginDescription extends WrappedDocument implements PluginDescription {

    private final File location;
    private final String name, category, description, author, website;
    private final UUID id;
    private final PluginVersion version;
    private final PluginMainClass mainClass;

    private final Collection<String> dependencies, softDependencies;

    public DefaultPluginDescription(File location, Document original) {
        super(original);
        this.location = location;

        this.name = original.getString("name");
        this.category = original.getString("category","");
        this.description = original.getString("description","");
        this.author = original.getString("author","");
        this.website = original.getString("website","");
        this.id = original.getObject("id",UUID.class);
        this.mainClass = PluginMainClass.readFromDocumentEntry(original.getEntry("mainClass"));

        PluginVersion version = original.getObject("version",PluginVersion.class);
        this.version = version!=null?version:new PluginVersion("Unknown",0,0);

        this.dependencies = original.getObject("dependencies",new TypeReference<Collection<String>>(){});
        this.softDependencies = original.getObject("softDependencies",new TypeReference<Collection<String>>(){});
    }

    @Override
    public File getLocation() {
        return this.location;
    }

    @Override
    public String getName() {
        return name;
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
    public PluginMainClass getMainClass() {
        return mainClass;
    }

    @Override
    public Collection<String> getDependencies() {
        return dependencies;
    }

    @Override
    public Collection<String> getSoftDependencies() {
        return softDependencies;
    }

    @Override
    public Collection<String> getRequiredServices() {
        return null;
    }

    @Override
    public Collection<String> getProvidedServices() {
        return null;
    }
}
