/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.04.19 12:55
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

import net.prematic.libraries.logging.io.LoggingPrintStream;
import net.prematic.libraries.logging.level.LogLevel;
import net.prematic.libraries.plugin.RuntimeEnvironment;
import net.prematic.libraries.plugin.manager.DefaultPluginManager;
import net.prematic.libraries.plugin.manager.PluginManager;

import java.io.File;

public class TestHost {

    public static void main(String[] args) throws Exception{
        RuntimeEnvironment<TestHost> environment = new RuntimeEnvironment<>("test",new TestHost());

        PluginManager manager = new DefaultPluginManager(environment);
        manager.getLogger().setLevel(LogLevel.DEBUG);

        LoggingPrintStream.hook(manager.getLogger());

        manager.enablePlugins(new File("plugins/"));

        Thread.sleep(2000);

        manager.disablePlugins();


    }
}
