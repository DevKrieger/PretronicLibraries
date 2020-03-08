/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.12.19, 21:15
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

package net.prematic.libraries.dependency.builder;

import net.prematic.libraries.dependency.DependencyException;
import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.http.HttpClient;
import net.prematic.libraries.utility.http.HttpResult;

import java.util.*;

/**
 * The {@link Pom} class represents a maven pom, which contains information about the repositories and dependencies.
 */
public class Pom {

    public static String REPOSITORY_MAVEN_CENTRAL = "https://repo1.maven.org/maven2/";

    private final DependencyInfo current;
    private final DependencyInfo parent;
    private final Map<String,String> properties;
    private final List<DependencyInfo> dependencies;
    private final Set<String> repositories;

    private Pom parentPom;

    public Pom(DependencyInfo current,DependencyInfo parent,Map<String,String> properties, List<DependencyInfo> dependencies,Set<String> repositories) {
        this.current = current;
        this.parent = parent;
        this.properties = properties;
        this.dependencies = dependencies;
        this.repositories = repositories;

        this.properties.put("${project.version}",current.getVersion());
        this.properties.put("${project.groupId}",current.getGroupId());
        this.properties.put("${project.artifactId}",current.getArtifactId());
    }

    public DependencyInfo getCurrent() {
        return current;
    }

    public DependencyInfo getParent() {
        return parent;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<DependencyInfo> getDependencies() {
        return dependencies;
    }

    public Map<String, String> findProperties(){
        Map<String, String> properties = new HashMap<>();
        if(parentPom != null) properties = parentPom.findProperties();
        properties.putAll(this.properties);
        return properties;
    }

    public List<DependencyInfo> findDependencies() {
        List<DependencyInfo> dependencies = new ArrayList<>(this.dependencies);
        if(parentPom != null) dependencies.addAll(parentPom.findDependencies());

        Map<String, String> properties = findProperties();
        dependencies.forEach(dependency -> {
            dependency.setArtifactId(replaceProperties(dependency.getArtifactId(),properties));
            dependency.setGroupId(replaceProperties(dependency.getGroupId(),properties));
            dependency.setVersion(replaceProperties(dependency.getVersion(),properties));
        });
        return dependencies;
    }

    private String replaceProperties(String input, Map<String, String> properties){
        if(input != null){
            String output = input;
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                output = output.replace(entry.getKey(),entry.getValue());
            }
            return output;
        }
        return input;
    }

    public Set<String> findRepositories(){
        Set<String> repositories = new HashSet<>();
        if(parentPom != null) repositories = parentPom.findRepositories();
        repositories.addAll(this.repositories);
        return repositories;
    }

    public void loadParents(){
        if(parent != null){
            this.parentPom = load(parent);
            parentPom.loadParents();
        }
    }

    public static Pom load(DependencyInfo dependency){
        if(dependency.getVersion() == null){
            dependency.setVersion(findLatestVersion(dependency));
        }
        try{
            HttpClient client = new HttpClient();
            client.setTimeout(2000);
            client.setUrl(dependency.getRemotePom());
            HttpResult result = client.connect();
            if(result.getCode() == 200){
                return build(dependency,result.getContent(DocumentFileType.XML.getReader()));
            }else{
                throw new DependencyException("Could not load pom "+dependency.getArtifactId()+" v"+dependency.getVersion()+" (Server responded with "+result.getCode()+")");
            }
        }catch (Exception exception){
            throw new DependencyException("Could not load pom "+dependency.getArtifactId()+" v"+dependency.getVersion()+" ("+exception.getMessage()+")",exception);
        }
    }

    private static String findLatestVersion(DependencyInfo dependency){
        try{
            HttpClient client = new HttpClient();
            client.setTimeout(2000);
            client.setUrl(dependency.getRemoteMetadata());
            HttpResult result = client.connect();
            if(result.getCode() == 200){
                Document meta = result.getContent(DocumentFileType.XML.getReader());
                Document versioning = meta.getDocument("versioning");
                if(versioning != null){
                    String latest = versioning.getString("latest");
                    if(latest != null) return latest;
                }
                throw new DependencyException("Could not find latest version for "+dependency.getArtifactId());
            }else{
                throw new DependencyException("Could not find latest version for "+dependency.getArtifactId()+" (Server responded with "+result.getCode()+")");
            }
        }catch (Exception exception){
            throw new DependencyException("Could not find latest version for "+dependency.getArtifactId()+" ("+exception.getMessage()+")",exception);
        }
    }

    public static Pom build(DependencyInfo dependency,Document rawPom){
        return new Pom(dependency
                ,buildParent(dependency,rawPom)
                ,buildProperties(rawPom)
                ,buildDependencies(rawPom,dependency)
                ,buildRepositories(rawPom));
    }

    private static DependencyInfo buildParent(DependencyInfo dependency,Document rawPom){
        Document parent = rawPom.getDocument("parent");
        if(parent != null){
            return new DependencyInfo(dependency.getRepository()
                    ,parent.getString("groupId")
                    ,parent.getString("artifactId")
                    ,parent.getString("version"));
        }
        return null;
    }

    private static Map<String,String> buildProperties(Document rawPom) {
        Map<String,String> properties = new HashMap<>();
        Document rawProperties = rawPom.getDocument("properties");
        if(rawProperties != null){
            for (DocumentEntry entry : rawProperties) {
                if(entry.isPrimitive()){
                    properties.put(entry.getKey(),entry.toPrimitive().getAsString());
                }
            }
        }
        return properties;
    }

    private static List<DependencyInfo> buildDependencies(Document rawPom,DependencyInfo currentDependency) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        Document rawDependencies = rawPom.getDocument("dependencies");
        if (rawDependencies != null) {
            for (DocumentEntry entry : rawDependencies) {
                if (entry.isObject()) {
                    Document rawDependency = entry.toDocument();
                    String scope = rawDependency.getString("scope");
                    if(scope == null || scope.equalsIgnoreCase("compile") || scope.equalsIgnoreCase("runtime")){
                        DependencyInfo dependency = new DependencyInfo(
                                null,
                                rawDependency.getString("groupId")
                                ,rawDependency.getString("artifactId")
                                ,rawDependency.getString("version")
                        );
                        if(dependency.getGroupId().equalsIgnoreCase(currentDependency.getGroupId())){
                            dependency.setRepository(currentDependency.getRepository());
                        }
                        dependencies.add(dependency);
                    }
                }
            }
        }
        return dependencies;
    }

    private static Set<String> buildRepositories(Document rawPom) {
        Set<String> repositories = new HashSet<>();
        Document rawRepositories = rawPom.getDocument("repositories");
        if(rawRepositories != null){
            for (DocumentEntry entry : rawRepositories) {
                if(entry.isObject() && entry.toDocument().contains("url")){
                    repositories.add(entry.toDocument().getString("url"));
                }
            }
        }
        return repositories;
    }

}
