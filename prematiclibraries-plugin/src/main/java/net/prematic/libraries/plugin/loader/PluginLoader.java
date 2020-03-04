package net.prematic.libraries.plugin.loader;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 19.03.19 14:05
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
import net.prematic.libraries.plugin.lifecycle.LifecycleState;
import net.prematic.libraries.plugin.loader.classloader.PluginClassLoader;
import net.prematic.libraries.plugin.manager.PluginManager;

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

    void shutdown();

    void unload();


}
