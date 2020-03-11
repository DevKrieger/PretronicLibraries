/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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

package net.pretronic.libraries.message.bml.function;

public class SubstringFunction implements Function {

    @Override
    public Object execute(Object[] parameters) {
        if(parameters.length >= 2){
            if(parameters[0] instanceof String && parameters[1] instanceof Integer){
                String content = (String) parameters[0];
                int start = (int) parameters[1];
                int end = 0;
                if(parameters.length == 3){
                    if(parameters[2] instanceof Integer){
                        end = (int) parameters[2];
                    }else throw new IllegalArgumentException("Invalid parameter type");
                }else{
                    end = content.length();
                }
                return content.substring(start,end);
            }else throw new IllegalArgumentException("Invalid parameter type");
        }else throw new IllegalArgumentException("Invalid parameter length");
    }
}
