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

package net.pretronic.libraries.plugin.description;

import net.pretronic.libraries.utility.GeneralUtil;

public class PluginVersion {

    private final String name;
    private final int major;
    private final int minor;
    private final int patch;
    private final int build;
    private final String qualifier;

    public PluginVersion(String name, int major, int minor, int patch, int build, String qualifier) {
        this.name = name;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.build = build;
        this.qualifier = qualifier;
    }

    public String getName() {
        return name;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public int getBuild() {
        return build;
    }

    public String getQualifier() {
        return qualifier;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PluginVersion parse(String version){
        String[] versionAndQualifier = version.split("-");

        String[] parts = versionAndQualifier[0].split("\\.");
        int major = parts.length >= 1 && GeneralUtil.isNaturalNumber(parts[0]) ? Integer.parseInt(parts[0]) :0;
        int minor = parts.length >= 2 && GeneralUtil.isNaturalNumber(parts[1]) ? Integer.parseInt(parts[1]) :0;
        int patch = parts.length >= 3 && GeneralUtil.isNaturalNumber(parts[2]) ? Integer.parseInt(parts[2]) :0;
        int build = parts.length >= 4 && GeneralUtil.isNaturalNumber(parts[3]) ? Integer.parseInt(parts[3]) :0;
        String qualifier = versionAndQualifier.length > 1 ? versionAndQualifier[1] : "RELEASE";

        return new PluginVersion(version,major,minor,patch,build,qualifier);
    }

    public static PluginVersion ofImplementation(Class<?> clazz){
        return parse(clazz.getPackage().getImplementationVersion());
    }
}
