/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:43
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

package net.pretronic.libraries.dependency;

import net.pretronic.libraries.dependency.loader.DependencyClassLoader;
import net.pretronic.libraries.utility.http.HttpClient;
import net.pretronic.libraries.utility.http.HttpResult;
import net.pretronic.libraries.utility.io.FileUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
The {@link Dependency} class represents a dependency which is located in a maven repository.
 This class provides methods for installing and loading the dependency at runtime.
 */
public class Dependency {

    private final transient DependencyManager manager;
    private final String repository;
    private final String groupId;
    private final String artifactId;
    private final String version;

    private transient ClassLoader loader;

    Dependency(DependencyManager manager, String repository, String groupId, String artifactId, String version) {
        this.manager = manager;
        this.repository = repository;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * Get the manager to which this dependency belongs.
     *
     * @return The manager
     */
    public DependencyManager getManager() {
        return manager;
    }

    public String getRepository() {
        return repository;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public File getLocalJar(){
        return new File(manager.getInstallationFolder(),artifactId+"/"+artifactId+"-"+version+".jar");
    }

    public URL getRemoteJar(){
        return FileUtil.newUrl(repository+groupId.replace(".","/")+"/"+artifactId+"/"+version+"/"+artifactId+"-"+version+".jar");
    }

    public boolean isInstalled(){
        return getLocalJar().exists();
    }

    public boolean isLoaded(){
        return loader != null;
    }

    /**
     * Install this dependency om the local machine
     */
    public void install(){
        File jar = getLocalJar();
        if(!jar.exists()){
            if(manager.getLogger() != null) {
                manager.getLogger().info(manager.getLoggerPrefix()+"Installing dependency " + groupId + " | " + artifactId + " | " + version);
            }
            jar.getParentFile().mkdirs();
            try{
                HttpClient client = new HttpClient();
                client.setTimeout(2000);
                client.setUrl(getRemoteJar());
                HttpResult result = client.connect();
                if(result.getCode() == 200){
                    result.save(jar);
                }else{
                    throw new DependencyException("Could not install dependency "+artifactId+" v"+version+" (Server responded with "+result.getCode()+")");
                }
            }catch (Exception exception){
                throw new DependencyException("Could not install dependency "+artifactId+" v"+version+" ("+exception.getMessage()+")",exception);
            }
        }
        if(manager.getLogger() != null){
            manager.getLogger().info(manager.getLoggerPrefix()+"Dependency "+groupId+" | "+artifactId+" | "+version+" is up to date");
        }
    }

    public ClassLoader load(){
        return load((ClassLoader) null);
    }

    public ClassLoader load(ClassLoader parent){
        if(manager.getDefaultLoader() == null){
            throw new IllegalArgumentException("No default class loader available");
        }
        return load(manager.getDefaultLoader(),parent);
    }

    /**
     * Load the dependency,
     *
     * @param loader The dependency loader
     * @return The created java class loader
     */
    public ClassLoader load(DependencyClassLoader loader){
        return load(loader,null);
    }

    /**
     * Load the dependency,
     *
     * @param loader The dependency loader
     * @param parent The parent java class loader
     * @return The created java class loader
     */
    public ClassLoader load(DependencyClassLoader loader,ClassLoader parent){
        if(isLoaded()) return null;
        File jar = getLocalJar();
        if(!jar.exists()) throw new DependencyException("Could not load dependency "+artifactId+" v"+version+" (Dependency is not installed)");
        try {
            return loader.load(parent,FileUtil.fileToUrl(jar));
        } catch (Exception exception) {
            throw new DependencyException("Could not load dependency "+artifactId+" v"+version+" ("+exception.getMessage()+")",exception);
        }

    }


}
