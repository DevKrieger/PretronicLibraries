/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.10.19, 19:54
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

import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.annonations.Internal;
import net.prematic.libraries.utility.http.HttpClient;
import net.prematic.libraries.utility.http.HttpResult;
import net.prematic.libraries.utility.io.IORuntimeException;
import net.prematic.libraries.utility.reflect.ReflectException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

public class Dependency {

    private static final Method METHOD_ADD_URL;

    static {
        METHOD_ADD_URL = null;
    }

    private DependencyManager manager;
    private final String groupId;
    private final String artifactId;
    private final String version;

    private Repository repository;
    private Collection<Dependency> dependencies;

    Dependency(DependencyManager manager, String groupId, String artifactId, String version) {
        this.manager = manager;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public URL getPomUrl(){
        try {
            return new URL(buildBaseUrl()+buildFileName("pom"));
        } catch (MalformedURLException e) {
            return null;//io run
        }
    }

    public URL getJarUrl(){
        try {
            return new URL(buildBaseUrl()+buildFileName("jar"));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public File getLocalFile(){
        return new File(manager.getLocalRepository(),artifactId+"/"+buildFileName("jar"));
    }

    private String buildBaseUrl(){
        if(repository == null) throw new IllegalArgumentException("Repository is not defined.");
        return repository.getUrl()+"/"+groupId.replace(".","/")+"/"+artifactId+"/"+version+"/";
    }

    private String buildFileName(String ending){
        return artifactId+"-"+version+"."+ending;
    }

    public boolean isResolved(){
        return dependencies != null;
    }

    public void reset(){
        this.repository = null;
        this.dependencies = null;
    }

    public void resolve(Repository repository){
        this.repository = repository;
        if(!resolveInternal()) throw new IllegalArgumentException("Could not resolve dependency (Resource "+repository.getUrl()+" in not found)");
    }

    public boolean resolve(){
        if(isResolved()) throw new IllegalArgumentException("Dependency is already resolved");
        if(this.repository == null){
            for (Repository repository : manager.getRepositories()) {
                this.repository = repository;
                if(resolveInternal()) return true;
            }
            manager.getLogger().info("Could not resolve dependency {} (Dependency in {} not found)",artifactId,repository.getUrl());
            return false;
        }else return resolveInternal();
    }

    @Internal
    private boolean resolveInternal(){
        HttpClient client = new HttpClient();
        client.setTimeout(2000);
        client.setUrl(getPomUrl());
        System.out.println(getPomUrl().toString());
        HttpResult result = null;
        try{
            result = client.connect();
            if(result.getCode() == 200){
                Document pom = result.getContent(DocumentFileType.XML.getReader());
                dependencies = manager.loadPom(pom,groupId,this.repository);
                manager.getLogger().info("Resolved dependency {}",artifactId);
                boolean success = true;
                for (Dependency dependency : dependencies) {
                    if(!dependency.isResolved()){
                        if(!dependency.resolve()) success = false;
                    }
                }
                return success;
            }
        }catch (Exception ignored){
            ignored.printStackTrace();
        }finally {
            if(result != null) result.close();
        }
        return false;
    }

    public void install(){
        if(!isResolved()) throw new IllegalArgumentException("Dependency is not resolved.");
        HttpClient client = new HttpClient();
        client.setTimeout(2000);
        client.setUrl(getJarUrl());
        HttpResult result = null;
        try{
            result = client.connect();
            if(result.getCode() == 200){
                File folder = new File(manager.getLocalRepository(),artifactId+"/");
                folder.mkdirs();
                result.save(new File(folder,buildFileName("jar")));
                result.close();
                dependencies.forEach(Dependency::install);
            }else throw new IllegalArgumentException("Could not install dependency "+artifactId+" (Server respond with code "+result.getCode()+")");
        }catch (Exception exception){
            if(result != null) result.close();
            throw new IllegalArgumentException("Could not install dependency "+artifactId+" ("+exception.getMessage()+")");
        }
    }

    public void load(URLClassLoader loader){
        try {
            METHOD_ADD_URL.invoke(loader, getLocalFile().toURI().toURL());
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new ReflectException(exception);
        }catch (MalformedURLException exception) {
            throw new IORuntimeException(exception);
        }
    }
}
