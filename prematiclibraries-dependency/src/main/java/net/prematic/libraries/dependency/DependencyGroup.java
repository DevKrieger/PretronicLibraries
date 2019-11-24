/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.10.19, 20:40
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

import java.util.Collection;

public class DependencyGroup {

    private final String name;
    private final Collection<Dependency> dependencies;

    public DependencyGroup(String name, Collection<Dependency> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;
    }

    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    public void addDependency(Dependency dependency){

    }
}
