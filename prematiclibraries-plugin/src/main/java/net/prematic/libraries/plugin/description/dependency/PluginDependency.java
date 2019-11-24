/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.11.19, 15:07
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

package net.prematic.libraries.plugin.description.dependency;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.exception.InvalidPluginDescriptionException;
import net.prematic.libraries.plugin.manager.PluginManager;

import java.util.function.BiFunction;

public class PluginDependency implements Dependency{

    private final PluginManager pluginManager;
    private final String name;

    public PluginDependency(PluginManager pluginManager, String name) {
        this.pluginManager = pluginManager;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "plugin";
    }

    @Override
    public boolean isAvailable() {
        return pluginManager.isPluginEnabled(name);
    }

    @Override
    public void write(Document document) {
        document.write(document.getString(name));
    }

    @Override
    public boolean isDepended(PluginDescription description) {
        return description.getName().equalsIgnoreCase(name);
    }

    public static class Factory implements BiFunction<PluginManager,Document, Dependency> {

        @Override
        public Dependency apply(PluginManager manager, Document document) {
            String name = document.getString("name");
            if(name == null) throw new InvalidPluginDescriptionException("Plugin dependency name can't be null");
            return new PluginDependency(manager,name);
        }
    }
}
