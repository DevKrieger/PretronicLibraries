/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.03.20, 17:04
 * @web %web%
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

package net.pretronic.libraries.message.bml.variable;

import net.pretronic.libraries.message.bml.variable.describer.DescribedObjectVariable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HashVariableSet extends HashSet<Variable> implements VariableSet{

    public HashVariableSet() {}

    public HashVariableSet(Collection<? extends Variable> c) {
        super(c);
    }

    @Override
    public Set<Variable> getVariables() {
        return this;
    }

    @Override
    public Variable get(String name) {
        for (Variable variable : this){
            if(variable.matches(name)) return variable;
        }
        return null;
    }

    @Override
    public Variable getOrCreate(String name) {
        Variable variable = get(name);
        if(variable == null){
            variable = new ObjectVariable(name,null);
            add(variable);
        }
        return variable;
    }

    @Override
    public Variable getOrCreateDescribed(String name) {
        Variable variable = get(name);
        if(variable == null){
            variable = new DescribedObjectVariable(name,null);
            add(variable);
        }
        return variable;
    }

    @Override
    public Object getValue(String name) {
        Variable variable = get(name);
        return variable != null ? variable.getObject(name) : null;
    }

    @Override
    public VariableSet remove(String name) {
        super.remove(name);
        return this;
    }

}
