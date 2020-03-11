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

package net.pretronic.libraries.plugin;

import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.plugin.description.PluginDescription;
import net.pretronic.libraries.plugin.loader.PluginLoader;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.io.File;
import java.security.SecurityPermission;

public abstract class Plugin<R> implements ObjectOwner {

    private static final SecurityPermission INIT_PERMISSION = new SecurityPermission("PretronicPluginInitialize");

    private PluginDescription description;
    private PluginLoader loader;
    private PretronicLogger logger;
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

    public PretronicLogger getLogger(){
        return logger;
    }

    public R getRuntime(){
        return this.runtime;
    }

    public File getWorkingDirectory(){
        return new File(loader.getLocation().getParentFile(),getName().toLowerCase()+"/");
    }

    public void initialize(PluginDescription description, PluginLoader loader, PretronicLogger logger, R runtime){
        if(this.loader != null) throw new IllegalArgumentException("This plugin instance is already initialized.");

        //Check runtime permission
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(INIT_PERMISSION);

        this.description = description;
        this.loader = loader;
        this.logger = logger;
        this.runtime = runtime;
    }

}
