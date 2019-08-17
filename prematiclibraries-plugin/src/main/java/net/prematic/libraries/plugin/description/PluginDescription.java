package net.prematic.libraries.plugin.description;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 19.03.19 11:00
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
import net.prematic.libraries.document.Document;
import net.prematic.libraries.plugin.description.dependency.PluginDependency;
import net.prematic.libraries.plugin.description.dependency.PluginLibrary;

import java.util.Collection;
import java.util.UUID;

public interface PluginDescription extends Document {

    String getName();

    String getCategory();

    String getDescription();

    String getAuthor();

    String getWebsite();

    UUID getId();

    PluginVersion getVersion();

    PluginMainClass getMainClass();

    Collection<PluginDependency> getDependencies();

    Collection<String> getRequiredDrivers();

    Collection<PluginLibrary> getLibraries();//Beta
}
