/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.10.19, 19:55
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
import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.utility.Iterators;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DependencyManager {

    private final PrematicLogger logger;
    private final File localRepository;
    private final Collection<Repository> repositories;
    private Collection<Dependency> dependencies;

    public DependencyManager(PrematicLogger logger,File localRepository) {
        this.logger = logger;
        this.localRepository = localRepository;
        this.repositories = new ArrayList<>();
        this.dependencies = new ArrayList<>();
    }

    public PrematicLogger getLogger() {
        return logger;
    }

    public File getLocalRepository(){
        return localRepository;
    }

    public Collection<Repository> getRepositories() {
        return repositories;
    }

    public Repository getRepository(String id){
        return Iterators.findOne(this.repositories, repository -> repository.getId().equals(id));
    }

    public void addRepository(String id, String repository){
        if(getRepository(id) != null) throw new IllegalArgumentException("A repository with the id "+id+" is already registered.");
        System.out.println(id+" | "+repository);
        if(repository.charAt(repository.length()-1) == '/') repository = repository.substring(0,repository.length()-1);
        this.repositories.add(new Repository(id,repository));
    }

    public Dependency getDependency(String groupId, String artifactId){
        return Iterators.findOne(this.dependencies, dependency -> dependency.getArtifactId().equals(artifactId) && dependency.getGroupId().equals(groupId));
    }

    public Dependency addDependency(String groupId, String artifactId, String version){
        Dependency dependency = getDependency(groupId, artifactId);
        if(dependency == null){
            dependency = new Dependency(this,groupId, artifactId, version);
            this.dependencies.add(dependency);
        }else if(!dependency.getVersion().equals(version)){
            logger.warn("Found a possibly version conflict in "+artifactId+" ("+version+"/"+dependency.getVersion()+")");
        }
        return dependency;
    }

    public Dependency addDependency(Document dependency){
        return addDependency(dependency.getString("groupId"),dependency.getString("artifactId"),dependency.getString("version"));
    }

    public Dependency addDependency(Document dependency, Repository repository){
        return addDependency(dependency.getString("groupId"),dependency.getString("artifactId"),dependency.getString("version"));
    }

    public Collection<Dependency> addDependencies(Document dependencies){
        Collection<Dependency> result = new ArrayList<>();
        dependencies.forEach(entry -> result.add(addDependency(entry.toDocument())));
        return result;
    }

    public Collection<Dependency> loadPom(Document pom){
        return loadPom(pom,null,null);
    }

    public Collection<Dependency> loadPom(Document pom, String currentGroupId, Repository currentRepository){
        Document repositories = pom.getDocument("repositories");
        if(repositories != null){
            repositories.forEach(rawRepository -> {
                Document repository = rawRepository.toDocument();
                addRepository(repository.getString("id"),repository.getString("url"));
            });
        }
        Document dependencies = pom.getDocument("dependencies");
        if(dependencies != null){
            Collection<Dependency> result = new ArrayList<>();
            dependencies.forEach(entry -> {
                if(entry.isObject()){
                    Document rawDependency = entry.toDocument();
                    String scope = rawDependency.getString("scope");
                    if(scope == null || scope.equalsIgnoreCase("compile")){
                        Dependency dependency = addDependency(rawDependency);
                        if(rawDependency.getString("groupId").equals(currentGroupId)){
                            dependency.setRepository(currentRepository);
                        }
                        result.add(dependency);
                    }
                }
            });
            return result;
        }
        return Collections.emptyList();
    }

    public void resolveDependencies(){

    }

    public void installDependencies(){

    }

    public void loadDependencies(){

    }

    public void load(String group){

    }
}
