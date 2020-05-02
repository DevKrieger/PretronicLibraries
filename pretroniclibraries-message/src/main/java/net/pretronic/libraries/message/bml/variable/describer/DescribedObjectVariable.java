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

package net.pretronic.libraries.message.bml.variable.describer;

import net.pretronic.libraries.message.bml.variable.Variable;

public class DescribedObjectVariable implements Variable {

    private final String name;
    private Object object;

    public DescribedObjectVariable(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public Object getObject(String name) {
        String[] parts = name.split("\\.");
        if(parts.length > 0){
            Object result =  VariableDescriber.get(object,parts,1);
            if(result instanceof VariableObjectToString) return ((VariableObjectToString) result).toStringVariable();
            return result;
        }
        return object;
    }

    @Override
    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public boolean matches(String name) {
        if(this.name.equalsIgnoreCase(name)) return true;
        else{
            int matchIndex = name.indexOf('.');
            if(matchIndex > 0){
                return this.name.equalsIgnoreCase(name.substring(0,matchIndex));
            }
        }
        return false;
    }
}
