/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 30.03.19 17:34
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

package net.prematic.libraries.plugin;

import net.prematic.libraries.plugin.description.PluginDescription;
import net.prematic.libraries.plugin.loader.PluginLoader;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.security.SecurityPermission;

public abstract class Plugin<R> implements ObjectOwner {

    private static final SecurityPermission INIT_PERMISSION = new SecurityPermission("PrematicPluginInitialize");

    private PluginDescription description;
    private PluginLoader<R> loader;
    private R runtime;

    @Override
    public String getName(){
        return this.description.getName();
    }

    public PluginDescription getDescription(){
        return this.description;
    }

    public PluginLoader getLoader(){
        return this.loader;
    }

    public R getRuntime(){
        return this.runtime;
    }

    /*
    public File getLocalStorageFolder(){

    }
     */

    public void initialize(PluginDescription description, PluginLoader<R> loader,R runtime){
        if(this.loader != null) throw new IllegalArgumentException("This plugin instance is already initialized.");

        //Check runtime permission
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(INIT_PERMISSION);

        this.description = description;
        this.loader = loader;
        this.runtime = runtime;
    }

}
