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

import net.prematic.libraries.dependency.Dependency;
import net.prematic.libraries.dependency.DependencyException;
import net.prematic.libraries.dependency.DependencyGroup;
import net.prematic.libraries.dependency.DependencyManager;
import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.logging.PrematicLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link DependencyGroupBuilder} provides methods for building a {@link DependencyGroup} (Inheritance dependencies included)
 */
public class DependencyGroupBuilder {

    private final PrematicLogger logger;
    private final String name;
    private final List<DependencyInfo> dependencies;

    public DependencyGroupBuilder(String name) {
        this(PrematicLoggerFactory.getLogger(DependencyGroupBuilder.class),name);
    }

    public DependencyGroupBuilder(PrematicLogger logger,String name) {
        this.logger = logger;
        this.name = name;
        this.dependencies = new ArrayList<>();
    }

    public void search(String repository, String groupId, String artifactId, String version){
       process(new DependencyInfo(repository, groupId, artifactId, version));
    }

    public DependencyGroup build(DependencyManager manager){
        List<DependencyInfo> result = dependencies.stream().distinct().collect(Collectors.toList());
        List<Dependency> dependencies = new ArrayList<>();
        result.forEach(info
                -> dependencies.add(manager.addDependency(info.getRepository()
                ,info.getGroupId(),info.getArtifactId(),info.getVersion())));
        return new DependencyGroup(name,dependencies);
    }

    private boolean process(DependencyInfo dependency){
        if(this.dependencies.contains(dependency)) return true;
        Pom pom;
        try{
            pom = Pom.load(dependency);
        }catch (DependencyException exception){
            return false;
        }
        dependencies.add(dependency);
        logger.info("Resolved dependency "+dependency);

        pom.loadParents();

        pom.findDependencies().forEach(info -> processDependency(pom, info));
        return true;
    }

    private void processDependency(Pom pom, DependencyInfo info) {
        if(info.getRepository() == null){
            Set<String> repositories = pom.findRepositories();
            info.setRepository(Pom.REPOSITORY_MAVEN_CENTRAL);
            if(!process(info)){
                boolean ok = false;
                for (String repository : repositories) {
                    info.setRepository(repository);
                    if(process(info)){
                        ok = true;
                        break;
                    }
                }
                if(!ok) logger.error("Could not resolve dependency "+info);
            }
        }else if(!process(info)) logger.error("Could not resolve dependency "+info);
    }
}
