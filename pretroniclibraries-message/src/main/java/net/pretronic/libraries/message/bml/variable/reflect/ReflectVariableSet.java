/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 28.03.20, 17:57
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

package net.pretronic.libraries.message.bml.variable.reflect;

import net.pretronic.libraries.message.bml.variable.HashVariableSet;
import net.pretronic.libraries.message.bml.variable.Variable;

public class ReflectVariableSet extends HashVariableSet {

    @Override
    public Object getValue(String name) {
        Object result;
        if(name.contains(".")){
            String[] parts = name.split("\\.");
            Variable variable = get(parts[0]);
            if(variable == null) return null;
            Object object = variable.getObject();
            for (int i = 1; i < parts.length; i++) {
                if(object == null) break;
                String part = parts[i];
                ReflectVariableDescriber describer = ReflectVariableDescriberRegistry.getDescriber(object.getClass());
                if(describer != null) object = describer.getValue(part,object);
                else throw new IllegalArgumentException("No variable describer for "+object.getClass()+" found");
            }
            result =  object;
        }else{
            Variable variable = get(name);
            if(variable != null) result = variable.getObject();
            else return null;
        }
        if(result instanceof ReflectVariableObjectToString) return ((ReflectVariableObjectToString) result).toStringVariable();
        else return result;
    }
}
