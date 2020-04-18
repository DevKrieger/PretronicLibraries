/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:46
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

package net.pretronic.libraries.resourceloader;

import java.util.Objects;

/**
 * The version info object contains information about a version.
 *
 * <p>Every version has a name and a build id.</p>
 */
public class VersionInfo {

    public static VersionInfo UNKNOWN = new VersionInfo("Unknown",0,0,0,0,"Unknown");

    private final String name;
    private final int major;
    private final int minor;
    private final int patch;
    private final int build;
    private final String qualifier;

    public VersionInfo(String name, int major, int minor, int patch, int build, String qualifier) {
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

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VersionInfo)) return false;
        VersionInfo info = (VersionInfo) o;
        return name.equalsIgnoreCase(info.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, build);
    }

    public static VersionInfo parse(String version0){
        String version = version0;
        int index = version0.indexOf(';');
        if(index != -1) version = version0.substring(0,index);

        String[] versionAndQualifier = version.split("-");

        String[] parts = versionAndQualifier[0].split("\\.");
        int major = parts.length >= 1 && isNaturalNumber(parts[0]) ? Integer.parseInt(parts[0]) :0;
        int minor = parts.length >= 2 && isNaturalNumber(parts[1]) ? Integer.parseInt(parts[1]) :0;
        int patch = parts.length >= 3 && isNaturalNumber(parts[2]) ? Integer.parseInt(parts[2]) :0;
        int build = parts.length >= 4 && isNaturalNumber(parts[3]) ? Integer.parseInt(parts[3]) :0;
        String qualifier = versionAndQualifier.length > 1 ? versionAndQualifier[1] : "RELEASE";

        return new VersionInfo(version,major,minor,patch,build,qualifier);
    }

    private static boolean isNaturalNumber(String value){
        for(char c : value.toCharArray()) if(!Character.isDigit(c)) return false;
        return true;
    }
}

