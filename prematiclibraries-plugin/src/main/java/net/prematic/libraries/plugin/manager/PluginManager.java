package net.prematic.libraries.plugin.manager;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 19.03.19 12:54
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

import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.plugin.service.ServiceRegistry;
import net.prematic.libraries.utility.annonations.Internal;
import net.prematic.libraries.utility.interfaces.ShutdownAble;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface PluginManager extends ServiceRegistry,ShutdownAble {

    PrematicLogger getLogger();

    Collection<Plugin> getPlugins();

    Plugin getPlugin(String name);

    Plugin getPlugin(UUID id);

    boolean isPluginEnabled(String name);


    Collection<PluginLoader> getLoaders();

    PluginLoader createPluginLoader(File location);

    PluginLoader createPluginLoader(File location, PluginDescription description);


    PluginDescription detectPluginDescription(File location);

    Collection<PluginDescription> detectPluginDescriptions(File directory);


    void setLifecycleStateListener(String state, BiConsumer<Plugin,LifecycleState> listener);

    @Internal
    void executeLifecycleStateListener(String state,LifecycleState stateEvent, Plugin plugin);


    Collection<Plugin> enablePlugins(File directory);


    void disablePlugins();

}
