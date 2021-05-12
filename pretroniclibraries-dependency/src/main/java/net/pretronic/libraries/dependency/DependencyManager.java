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
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.PretronicLoggerFactory;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The {@link DependencyManager} is the responsible for installing and laodering the dependencies.
 */
public class DependencyManager {

    private final PretronicLogger logger;
    private final File installationFolder;
    private final Collection<Dependency> dependencies;

    private DependencyClassLoader defaultLoader;

    private String loggerPrefix;

    public DependencyManager(File installationFolder) {
        this(PretronicLoggerFactory.getLogger(DependencyManager.class),installationFolder);
    }

    public DependencyManager(PretronicLogger logger, File installationFolder) {
        this.logger = logger;
        this.installationFolder = installationFolder;
        this.dependencies = new ArrayList<>();
        this.loggerPrefix = "(Dependency-Manager) ";
    }

    public PretronicLogger getLogger() {
        return logger;
    }


    public String getLoggerPrefix() {
        return loggerPrefix;
    }

    public void setLoggerPrefix(String loggerPrefix) {
        Validate.notNull(loggerPrefix);
        this.loggerPrefix = loggerPrefix;
    }

    public DependencyClassLoader getDefaultLoader() {
        return defaultLoader;
    }

    public void setDefaultLoader(DependencyClassLoader defaultLoader) {
        this.defaultLoader = defaultLoader;
    }

    public File getInstallationFolder() {
        return installationFolder;
    }

    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    public Dependency getDependency(String groupId, String artifactId){
        return Iterators.findOne(this.dependencies, dependency
                -> dependency.getGroupId().equals(groupId)
                && dependency.getArtifactId().equals(artifactId));
    }

    public Dependency addDependency(String repository, String groupId, String artifactId, String version){
        Dependency dependency = getDependency(groupId, artifactId);
        if(dependency == null) {
            dependency = new Dependency(this,repository,groupId,artifactId,version);
            this.dependencies.add(dependency);
        }else{
            if(!dependency.getVersion().equals(version)){
                logger.warn("Found a possible version conflict with dependency "+artifactId+" (Installed: "+version+", Required:"+dependency.getVersion()+")");
            }
        }
        return dependency;
    }

    public DependencyGroup load(File location){
        return DependencyGroup.load(this,location);
    }

    public DependencyGroup load(Document raw){
        return DependencyGroup.load(this,raw);
    }


}
