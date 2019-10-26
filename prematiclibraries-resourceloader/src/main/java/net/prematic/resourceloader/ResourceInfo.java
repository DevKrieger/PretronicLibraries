/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.10.19, 20:53
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

import java.io.File;

/**
 * This object contains information about a resource for loading.
 *
 * <p>In the url you can use ${version} and ${build} placeholder.</p>
 */
public final class ResourceInfo {

    private final String name;
    private final String downloadUrl;
    private final String versionUrl;
    private final File location;
    private final ResourceAuthenticator authenticator;

    public ResourceInfo(String name, String downloadUrl, String versionUrl, File location){
        this(name,downloadUrl,versionUrl,location,null);
    }

    public ResourceInfo(String name, String downloadUrl, String versionUrl, File location,ResourceAuthenticator authenticator) {
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.versionUrl = versionUrl;
        this.location = location;
        this.authenticator = authenticator;
    }

    public String getName() {
        return name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public File getLocation() {
        return location;
    }

    public ResourceAuthenticator getAuthenticator() {
        return authenticator;
    }
}
