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

package net.pretronic.libraries.plugin.loader;

import net.pretronic.libraries.plugin.Plugin;
import net.pretronic.libraries.plugin.description.PluginDescription;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import net.pretronic.libraries.plugin.loader.classloader.PluginClassLoader;
import net.pretronic.libraries.plugin.manager.PluginManager;

import java.io.File;

public interface PluginLoader {

    File getLocation();

    PluginManager getPluginManager();

    PluginDescription getDescription();

    PluginClassLoader getClassLoader();


    boolean isInstanceAvailable();

    boolean isMainClassAvailable();

    boolean isEnabled();

    void executeLifeCycleState(String state);

    void executeLifeCycleState(String state, LifecycleState stateEvent);


    Plugin getInstance();

    Plugin enable();//Complete loading process

    void disable();


    Plugin construct();

    void initialize();

    void load();

    void bootstrap();

    void reload();//Configuration reload (internal plugin reload)

    void shutdown();

    void unload();


}
