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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.LinkOption;

/**
 * The resource loader manages the installation of an application.
 *
 * <p>With the resource loader you are able to check new versions and update the application.</p>
 */
public class ResourceLoader {

    private final static String VERSION_INFO_FILE_NAME = "version.dat";
    private final static String UPDATE_CONFIGURATION_FILE_NAME = "update.dat";
    private final static Method METHOD_ADD_URL;

    static {
        try {
            METHOD_ADD_URL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            METHOD_ADD_URL.setAccessible(true);
        } catch (NoSuchMethodException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private final ResourceInfo info;

    private VersionInfo currentVersion;
    private VersionInfo latestVersion;
    private UpdateConfiguration updateConfiguration;

    public ResourceLoader(ResourceInfo info) {
        this.info = info;
        info.getLocation().mkdirs();
    }

    public UpdateConfiguration getUpdateConfiguration(){
        if (updateConfiguration == null) {
            File file = new File(info.getLocation(),UPDATE_CONFIGURATION_FILE_NAME);
            if (file.exists()) {
                try {
                    InputStream input = new FileInputStream(file);
                    updateConfiguration = UpdateConfiguration.parse(readFirstLine(input));
                    input.close();
                } catch (IOException exception) {
                    throw new ResourceException("Could not load update configuration (" + exception.getMessage() + ")", exception);
                }
            }
        }
        if(updateConfiguration != null) return updateConfiguration;
        return UpdateConfiguration.DEFAULT;
    }

    public void setUpdateConfiguration(UpdateConfiguration updateConfiguration) {
        this.updateConfiguration = updateConfiguration;
    }

    /**
     * Gert the latest version of a resource
     *
     * @return the latest version
     */
    public VersionInfo getLatestVersion(){
        if(latestVersion == null){
            try {
                InputStream input = openHttpConnection(prepareUrl(info.getVersionUrl(),null));
                latestVersion = VersionInfo.parse(readFirstLine(input));
                input.close();
            } catch (IOException exception) {
                throw new ResourceException("Could not get latest version ("+exception.getMessage()+")",exception);
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
                    InputStream input = new FileInputStream(file);
                    currentVersion =  VersionInfo.parse(readFirstLine(input));
                    input.close();
                } catch (IOException exception) {
                    throw new ResourceException("Could not read version info file ("+exception.getMessage()+")",exception);
                }
            }else currentVersion = VersionInfo.UNKNOWN;
        }
        return currentVersion;
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
            if(!version.equals(currentVersion)) changeCurrentVersion(version);
            return file;
        } catch (IOException exception) {
            throw new ResourceException("Could not download version "+version.getName()+"#"+version.getBuild()+" ("+exception.getMessage()+")",exception);
        }
    }

    public ClassLoader load(){
        return load(null,null);
    }

    public ClassLoader load(ClassLoader parent){
        return load(null,parent);
    }

    public ClassLoader load(VersionInfo version){
        return load(version,null);
    }

    /**
     * Load a downloaded resource.
     *
     * @param version The version for loading.
     * @param parent The parent class loader
     * @return THe class loader
     */
    public ClassLoader load(VersionInfo version,ClassLoader parent){
        if(version == null) version = getCurrentVersion();
        if(version == null) throw new ResourceException("No installed version found");
        File file = getLocalFile(version);
        if(file.exists() && file.isFile()){
            try {
                if(parent == null) return new URLClassLoader(new URL[]{file.toURI().toURL()},parent);
                else return new URLClassLoader(new URL[]{file.toURI().toURL()});
            } catch (MalformedURLException ignored) {}
        }
        throw new ResourceException(file.getAbsolutePath()+" is not a valid resource (jar) file");
    }

    public void loadReflected(URLClassLoader loader){
        loadReflected(loader,null);
    }

    /**
     * Invoke a jar file into another url class loader
     *
     * @param loader The class loader to add this jar file
     * @param version the version for loading
     */
    public void loadReflected(URLClassLoader loader, VersionInfo version){
        if(version == null) version = getCurrentVersion();
        if(version == null) throw new ResourceException("No installed version found");
        File file = getLocalFile(version);
        if(file.exists() && file.isFile()){
            try {
                METHOD_ADD_URL.invoke(loader, file.toURI().toURL());
            } catch (IllegalAccessException | InvocationTargetException | MalformedURLException ignored) {}
        }else throw new ResourceException(file.getAbsolutePath()+" is not a valid resource (jar) file");
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
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(version.getName());
            writer.close();
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new ResourceException("Could not update current version ("+exception.getMessage()+")");
        }
    }

    private String prepareUrl(String url, VersionInfo version){
        url = url.replace("{qualifier}",getUpdateConfiguration().getQualifier());
        if(version != null){
            url = url.replace("{version}",version.getName())
                    .replace("{version.build}",String.valueOf(version.getBuild()))
                    .replace("{version.minor}",String.valueOf(version.getMinor()))
                    .replace("{version.major}",String.valueOf(version.getMajor()))
                    .replace("{version.patch}",String.valueOf(version.getPatch()))
                    .replace("{version.qualifier}",version.getQualifier())
                    .replace("{qualifier}",getUpdateConfiguration().getQualifier());
        }
        return url;
    }

    private File getLocalFile(VersionInfo version){
        return new File(info.getLocation(),(info.getName()+"-"+version.getName()+".jar").toLowerCase());
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
        connection.connect();
        if(connection.getResponseCode() != 200){
            throw new IllegalArgumentException(connection.getResponseCode()+" | "+readFirstLine(connection.getErrorStream()));
        }
        return connection.getInputStream();
    }

    private static String readFirstLine(InputStream input) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(input);
        try (BufferedReader reader = new BufferedReader(inputStream)) {
            String result =  reader.readLine();
            inputStream.close();
            return result;
        }
    }

}
