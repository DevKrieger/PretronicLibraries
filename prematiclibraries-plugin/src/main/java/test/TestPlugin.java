/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.08.19, 19:36
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

package test;

import net.prematic.libraries.plugin.lifecycle.Lifecycle;
import net.prematic.libraries.plugin.lifecycle.LifecycleState;

public class TestPlugin {

    //Version 1

    @Lifecycle(state=LifecycleState.CONSTRUCTION)
    public void onConstruction(LifecycleState state){
        System.out.println("Plugin wurde konstruiert");

        new Thread(()->{
            System.out.println("TEST");
        }).start();
    }

    @Lifecycle(state=LifecycleState.INITIALISATION)
    public void onInit(LifecycleState state){
        System.out.println("Plugin init");
    }

    @Lifecycle(state=LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        System.out.println("Plugin wurde fertig geladen");
    }

    @Lifecycle(state=LifecycleState.BOOTSTRAP)
    public void onTest(LifecycleState state){
        //String test = getRuntime();
        System.out.println("Plugin wurde gestarten ");
    }

    @Lifecycle(state=LifecycleState.SHUTDOWN)
    public void onShutdown(LifecycleState state){
        System.out.println("Plugin wurde heruntergefahren");
    }

    @Lifecycle(state=LifecycleState.UNLOAD)
    public void onUnload(LifecycleState state){
        System.out.println("Plugin wurde entladen");
    }



    /*

    --> Detecting plugins


    --> Enable cycle

    CONSTRUCTION -> Neue Main Instance wurde erstellt und plugin wurde registriert
    INITIALIZATION -> Plugin wurde initialisiert (Runtime Instance available)
    LOAD_COMPLETED -> Plugin wurde fertig geladen


    DriverBootstrap

    INSTALL -> Plugin neu installieren
    BOOTSTRAP -> Plugin wurde geladen und kann nun gestartet werden


    --> Runtime cycle
    RELOAD -> Reload the system (configuration etc).
    RESTART -> Restart the system (Called by user runtime)


    --> Disable cycle

    SHUTDOWN -> Shutdown the plugin and disable everything from this plugin
    UNINSTALL -> Uninstall everything
    UNLOAD -> The Plugin is completed unloaded, tasks and other thinks are disable by runtime.


    ----------------

    --> Cause
    - on Runtime start


    INITIALIZATION
      -> Runtime instance
      -> Plugin loader
      -> Plugin description
      ->


    Plugin Mysql -> register


    Driver -> return MySqlDatabaseDriver.class




    Plugin p1 -> get
    Plugin p2 -> get






    DatabaseDriver Abstract

    MySqlDatabaseDriver
    MongoDbDatabaseDriver


    PluginManager.getService(DatabaseDriver.class)

    DatabaseDriver -> MySqlDatabaseDriver












     */
}
