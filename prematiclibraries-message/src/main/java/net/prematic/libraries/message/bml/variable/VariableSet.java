/*
 * (C) Copyright 2019 The McNative Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.08.19, 18:11
 *
 * The McNative Project is under the Apache License, version 2.0 (the "License");
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

package net.prematic.libraries.message.bml.variable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface VariableSet extends Set<Variable> {

    Set<Variable> getVariables();

    Variable get(String name);

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

    static VariableSet newEmptySet(){
        return EmptyVariableSet.newEmptySet();
    }

    static String replace(String text, VariableSet variables){
        char[] chars = text.toCharArray();
        int start = -1;
        StringBuilder builder = new StringBuilder(text.length());
        boolean in = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(c == '%'){
                if(start == -1) start = i;
                else{
                    if(start < i){
                        Variable variable = variables.get(text.substring(start+1,i));
                        if(variable != null){
                            builder.append(variable.getObject());//@Todo parse with dot (advanced string processing)
                            start = -1;
                            continue;
                        }
                    }
                    builder.append("{Unknown}");
                }
            }
            else builder.append(c);
        }
        return builder.toString();
    }
}
