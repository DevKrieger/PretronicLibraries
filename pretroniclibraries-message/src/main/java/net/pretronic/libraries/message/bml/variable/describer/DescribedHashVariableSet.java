/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 07.04.20, 18:39
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

import net.pretronic.libraries.message.bml.variable.HashVariableSet;
import net.pretronic.libraries.message.bml.variable.Variable;

public class DescribedHashVariableSet extends HashVariableSet {

    @Override
    public Object getValue(String name) {
        Variable variable = get(name);

        if(variable != null){
            Object result =  variable.getObject();
            if(result instanceof VariableObjectToString) return ((VariableObjectToString) result).toStringVariable();
            return result;
        }

        String[] parts = name.split("\\.");
        if(parts.length > 1){
            variable = get(parts[0]);
            if(variable != null){
                Object result =  VariableDescriber.get(variable.getObject(),parts,1);
                if(result instanceof VariableObjectToString) return ((VariableObjectToString) result).toStringVariable();
                return result;
            }
        }
        return null;
    }
}
