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

import net.prematic.libraries.plugin.Plugin;
import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.driver.Driver;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.utility.interfaces.ShutdownAble;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface PluginManager<R> extends ShutdownAble {

    Plugin getPlugin(String name);

    Plugin getPlugin(UUID id);

    Collection<Plugin<R>> getPlugins();

    <D extends Driver> D getDriver(Class<D> driverClass);

    PluginDescription detectPluginDescription(File file);

    Collection<PluginDescription> detectPluginDescriptions(File folder);

    PluginLoader<R> createPluginLoader(File file);

    void registerLifecycleStateListener(LifecycleState state, Consumer<Plugin> listener);

    void executeLifecycleSStateListener(LifecycleState state, Plugin plugin);

    int loadPlugins(File folder);

    int loadPlugins(PluginContext context);

    void shutdownPlugins();

}
