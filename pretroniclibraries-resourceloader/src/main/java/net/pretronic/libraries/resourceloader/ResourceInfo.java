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

import java.io.File;

/**
 * This object contains information about a resource for loading.
 *
 * <p>In the url you can use ${version} and ${build} placeholder.</p>
 */
public final class ResourceInfo {

    private String name;
    private String downloadUrl;
    private String versionUrl;
    private File location;
    private ResourceAuthenticator authenticator;

    public ResourceInfo(String name, File location){
        this(name,null,null,location);
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public ResourceAuthenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(ResourceAuthenticator authenticator) {
        this.authenticator = authenticator;
    }
}
