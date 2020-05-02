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

package net.pretronic.libraries.plugin.manager;

import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.plugin.Plugin;
import net.pretronic.libraries.plugin.description.PluginDescription;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import net.pretronic.libraries.plugin.loader.PluginLoader;
import net.pretronic.libraries.plugin.service.ServiceRegistry;
import net.pretronic.libraries.utility.annonations.Internal;
import net.pretronic.libraries.utility.interfaces.ShutdownAble;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface PluginManager extends ServiceRegistry,ShutdownAble {

    PretronicLogger getLogger();

    Collection<Plugin<?>> getPlugins();

    Plugin<?> getPlugin(String name);

    Plugin<?> getPlugin(UUID id);

    boolean isPluginEnabled(String name);


    Collection<PluginLoader> getLoaders();

    PluginLoader createPluginLoader(String name);

    PluginLoader createPluginLoader(File location);

    PluginLoader createPluginLoader(File location, PluginDescription description);


    PluginDescription detectPluginDescription(File location);

    Collection<PluginDescription> detectPluginDescriptions(File directory);


    void setLifecycleStateListener(String state, BiConsumer<Plugin<?>, LifecycleState> listener);

    @Internal
    void executeLifecycleStateListener(String state,LifecycleState stateEvent, Plugin<?> plugin);


    Collection<Plugin<?>> enablePlugins(File directory);


    void disablePlugins();

    @SuppressWarnings("May be blocked by some maangers")
    void provideLoader(PluginLoader loader);

}
