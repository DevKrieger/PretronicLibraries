/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.10.19, 19:00
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

package net.prematic.libraries.dependency;

import net.prematic.libraries.logging.SimplePrematicLogger;

import java.io.File;

public class Test {

    public static void main(String[] args) throws Exception{
        DependencyManager manager = new DependencyManager(new SimplePrematicLogger(),new File("test/repo/"));

        manager.addRepository("Maven Central","https://repo1.maven.org/maven2/");
        manager.addRepository("CloudNet","https://cloudnetservice.eu/repositories/");

        Dependency dependency = new Dependency(manager,"de.dytanic.cloudnet","cloudnet-driver","3.0.0-RELEASE");

        dependency.resolve();
        dependency.install();

        Thread.sleep(2000);
        System.out.println("test");
    }
}
