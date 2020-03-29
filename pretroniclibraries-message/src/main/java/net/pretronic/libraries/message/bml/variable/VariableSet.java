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

import net.pretronic.libraries.message.bml.variable.reflect.ReflectVariableSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface VariableSet extends Set<Variable> {

    Set<Variable> getVariables();

    Variable get(String name);

    Variable getOrCreate(String name);

    Object getValue(String name);

    VariableSet add(String name, Object source);

    VariableSet remove(String name);

    default String replace(String text){
        return replace(text,this);
    }


    static VariableSet of(Variable... variables){
        return new HashVariableSet(Arrays.asList(variables));
    }

    static VariableSet of(Collection<Variable> variables){
        return new HashVariableSet(variables);
    }

    static VariableSet of(Object... object){
        if(object.length %2 != 0) throw new IllegalArgumentException("Invalid length");
        VariableSet variables = VariableSet.create();
        for (int i = 0; i < object.length; i+=2) {
            variables.add((String) object[i],object[i+1]);
        }
        return variables;
    }

    static VariableSet create(){
        return new HashVariableSet();
    }

    static VariableSet createEmpty(){
        return new ReflectVariableSet();
    }

    static VariableSet createReflected(){
        return EmptyVariableSet.newEmptySet();
    }

    static VariableSet newEmptySet(){
        return createReflected();
    }

    static String replace(String text, VariableSet variables){
        char[] content = text.toCharArray();
        StringBuilder builder = new StringBuilder(content.length);
        int start = -1;
        for (int i = 0; i < content.length; i++) {
            if(content[i] == '{'){
                if(i != 0 && content[i-1] == '\\'){
                    builder.setCharAt(builder.length()-1,content[i]);
                }else{
                    start = i;
                }
            }else if(content[i] == '}' && start != -1){
                String key = text.substring(start+1,i);
                Variable variable = variables.get(key);
                if(variable != null){
                    builder.append(variable.getObject());
                }else builder.append("NULL");
                start = -1;
            }else if(start == -1){
                builder.append(content[i]);
            }
        }
        return builder.toString();
    }
}
