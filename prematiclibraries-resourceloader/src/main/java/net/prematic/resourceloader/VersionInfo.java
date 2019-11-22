/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.10.19, 20:52
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

package net.prematic.resourceloader;

import java.util.Objects;

/**
 * The version info object contains information about a version.
 *
 * <p>Every version has a name and a build id.</p>
 */
public class VersionInfo {

    public static VersionInfo UNKNOWN = new VersionInfo("Unknown",-1);

    private final String name;
    private int build;

    public VersionInfo(String name, int build) {
        this.name = name;
        this.build = build;
    }

    public String getName() {
        return name;
    }

    public int getBuild() {
        return build;
    }

    @Override
    public String toString() {
        return name+"#"+build;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VersionInfo)) return false;
        VersionInfo info = (VersionInfo) o;
        return build == info.build;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, build);
    }

    static VersionInfo parse(String versionString){
        try{
            String[] versions = versionString.split("#");
            return new VersionInfo(versions[0],Integer.parseInt(versions[1]));
        }catch (Exception exception){
            throw new IllegalArgumentException(versionString+" is not a valid version string.");
        }
    }
}
