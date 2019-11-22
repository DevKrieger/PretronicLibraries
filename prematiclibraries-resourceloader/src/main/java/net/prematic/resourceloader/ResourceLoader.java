/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.10.19, 20:47
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

/**
 * The resource loader manages the installation of an application.
 *
 * <p>With the resource loader you are able to check new versions and update the application.</p>
 */
public class ResourceLoader {

    private final static String VERSION_INFO_FILE_NAME = "version.txt";

    private final ResourceInfo info;

    private VersionInfo currentVersion;
    private VersionInfo latestVersion;

    public ResourceLoader(ResourceInfo info) {
        this.info = info;
        info.getLocation().mkdirs();
    }

    public boolean isLatestVersion(){
        return getLatestVersion().equals(getCurrentVersion());
    }

    public File download(){
        return download(null);
    }

    /**
     * Download a resource by a version.
     *
     * @param version The version for downloading
     * @return The downloaded resource file
     */
    public File download(VersionInfo version){
        if(version == null) version = getLatestVersion();
        try {
            File file = getLocalFile(version);
            if(file.exists()) return file;
            InputStream input = openHttpConnection(prepareUrl(info.getDownloadUrl(),version));
            Files.copy(input,file.toPath());
            if(version != currentVersion) changeCurrentVersion(version);
            return file;
        } catch (IOException exception) {
            throw new ResourceException("Could not download version "+version.getName()+"#"+version.getBuild(),exception);
        }
    }

    public ResourceClassLoader load(){
        return load(null);
    }

    /**
     * Load a downloaded resource.
     *
     * @param version The version for loading.
     * @return THe class loader
     */
    public ResourceClassLoader load(VersionInfo version){
        if(version == null) version = getCurrentVersion();
        if(version == null) throw new ResourceException("No installed version found");
        File file = getLocalFile(version);
        if(file.exists() && file.isFile()) return new ResourceClassLoader(file);
        throw new ResourceException(file.getAbsolutePath()+" is not a valid resource (jar) file");
    }

    /**
     * Gert the latest version of a resource
     *
     * @return the latest version
     */
    public VersionInfo getLatestVersion(){
        if(latestVersion == null){
            try {
                InputStream input = openHttpConnection(info.getVersionUrl());
                latestVersion = VersionInfo.parse(readFirstLine(input));
                input.close();
            } catch (IOException exception) {
                throw new IllegalArgumentException("Could not check latest version",exception);
            }
        }
        return latestVersion;
    }

    /**
     * Get the current installed version.
     *
     * @return The installed version
     */
    public VersionInfo getCurrentVersion(){
        if(currentVersion == null){
            File file = new File(info.getLocation(),VERSION_INFO_FILE_NAME);
            if(file.exists()){
                try {
                    currentVersion =  VersionInfo.parse(readFirstLine(new FileInputStream(file)));
                } catch (IOException exception) {
                    throw new ResourceException("Could not read version info file",exception);
                }
            }else currentVersion = VersionInfo.UNKNOWN;
        }
        return currentVersion;
    }

    /**
     * Set the current installed version.
     *
     * @param version The installed version
     */
    public void changeCurrentVersion(VersionInfo version){
        this.currentVersion = version;
        File file = new File(info.getLocation(),VERSION_INFO_FILE_NAME);
        try {
            BufferedWriter writer = new BufferedWriter( new FileWriter(file));
            writer.write(version.toString());
            writer.close();
        } catch (IOException exception) {
            throw new ResourceException("Could not update current version");
        }
    }


    private String prepareUrl(String url, VersionInfo version){
        if(version == null) version = getCurrentVersion();
        if(version != null) return url.replace("${version}",version.getName()).replace("${build}",String.valueOf(version.getBuild()));
        throw new ResourceException("Could not get latest resource version");
    }

    private File getLocalFile(VersionInfo version){
        return new File(info.getLocation(),(info.getName()+"-"+version.getName()+"-"+ version.getBuild()+".jar").toLowerCase());
    }
    private InputStream openHttpConnection(String rawUrl) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(rawUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("User-Agent", "Pretronic Resource Loader");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        connection.setInstanceFollowRedirects(true);
        if(info.getAuthenticator() != null) info.getAuthenticator().invokeAuthentication(connection);
        return connection.getInputStream();
    }

    private static String readFirstLine(InputStream input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return reader.readLine();
        }
    }

}
