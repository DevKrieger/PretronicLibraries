package net.prematic.libraries.utility;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public class OperatingSystemInfo {

    private final String name, version, architecture, javaVersion;

    public OperatingSystemInfo(String name, String version, String architecture, String javaVersion) {
        this.name = name;
        this.version = version;
        this.architecture = architecture;
        this.javaVersion = javaVersion;
    }
    public String getName() {
        return this.name;
    }
    public String getVersion() {
        return this.version;
    }
    public String getArchitecture() {
        return this.architecture;
    }
    public String getJavaVersion() {
        return this.javaVersion;
    }
    public static OperatingSystemInfo build(){
        return new OperatingSystemInfo(System.getProperty("os.name"),System.getProperty("os.version")
                ,System.getProperty("os.arch"),System.getProperty("java.version"));
    }
}
