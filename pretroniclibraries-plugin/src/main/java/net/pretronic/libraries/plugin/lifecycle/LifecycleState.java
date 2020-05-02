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

package net.pretronic.libraries.plugin.lifecycle;

import net.pretronic.libraries.plugin.description.PluginDescription;
import net.pretronic.libraries.plugin.loader.PluginLoader;

public class LifecycleState {

    public static final String CONSTRUCTION = "CONSTRUCTION";

    public static final String INITIALISATION = "INITIALISATION";

    public static final String LOAD = "LOAD";

    public static final String BOOTSTRAP = "BOOTSTRAP";

    public static final String RELOAD = "RELOAD";

    public static final String SHUTDOWN = "SHUTDOWN";

    public static final String UNLOAD = "UNLOAD";

    private final PluginDescription description;
    private final PluginLoader loader;

    public LifecycleState(PluginDescription description, PluginLoader loader) {
        this.description = description;
        this.loader = loader;
    }

    public PluginDescription getDescription() {
        return description;
    }

    public PluginLoader getLoader() {
        return loader;
    }

    public <T> T getService(Class<T> serviceClass){
        return loader.getPluginManager().getService(serviceClass);
    }


}
