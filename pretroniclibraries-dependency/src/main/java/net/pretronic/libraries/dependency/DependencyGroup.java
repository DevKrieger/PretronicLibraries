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
import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.type.DocumentFileType;
import net.pretronic.libraries.utility.Iterators;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link DependencyGroup} provides a collection of different dependencies. It is possible to create
 * the dependency manually or by loading a dependency.json via the manager.
 */
public class DependencyGroup {

    private final String name;
    private final List<Dependency> dependencies;

    public DependencyGroup(String name, List<Dependency> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public Dependency getDependency(String groupId, String artifactId){
        return Iterators.findOne(this.dependencies, dependency
                -> dependency.getGroupId().equals(groupId)
                && dependency.getArtifactId().equals(artifactId));
    }

    /**
     * Install al dependencies.
     */
    public void install(){
        dependencies.forEach(Dependency::install);
    }


    /**
     * Load all dependencies
     * @return A list with all loader
     */
    public List<ClassLoader> load(){
        return load((ClassLoader) null);
    }

    public List<ClassLoader> load(ClassLoader parent){
        List<ClassLoader> loaders = new ArrayList<>();
        this.dependencies.forEach(dependency -> loaders.add(dependency.load(parent)));
        return loaders;
    }

    public List<ClassLoader> load(DependencyClassLoader loader){
        return load(loader,null);
    }

    public List<ClassLoader> load(DependencyClassLoader loader,ClassLoader parent){
        List<ClassLoader> loaders = new ArrayList<>();
        this.dependencies.forEach(dependency -> loaders.add(dependency.load(loader,parent)));
        return loaders;
    }

    @Deprecated
    public void loadReflected(URLClassLoader loader){
        this.dependencies.forEach(dependency -> dependency.loadReflected(loader));
    }

    public static DependencyGroup load(DependencyManager manager,File location){
        return load(manager,DocumentFileType.JSON.getReader().read(location));
    }

    public static DependencyGroup load(DependencyManager manager,Document raw){
        if(raw.containsOne("name","dependencies")){
            List<Dependency> dependencies = new ArrayList<>();
            for (DocumentEntry entry : raw.getDocument("dependencies")) {
                if(entry.isObject() && entry.toDocument().containsOne("repository","artifactId","groupId","version")){
                    Document data = entry.toDocument();
                    dependencies.add(manager.addDependency(
                            data.getString("repository")
                            ,data.getString("groupId")
                            ,data.getString("artifactId")
                            ,data.getString("version")));
                }else throw new IllegalArgumentException("Invalid or corrupt dependency file");
            }
            return new DependencyGroup(raw.getString("name"),dependencies);
        }else throw new IllegalArgumentException("Invalid or corrupt dependency file");
    }

    public Document save(){
        return Document.newDocument(this);
    }

    public void save(File location){
        DocumentFileType.JSON.getWriter().write(location,save());
    }

}
