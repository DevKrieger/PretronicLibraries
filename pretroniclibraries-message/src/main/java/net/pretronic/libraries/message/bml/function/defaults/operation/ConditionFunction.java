/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.04.20, 19:48
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

package net.pretronic.libraries.message.bml.function.defaults.operation;

public class ConditionFunction {

    /*
    @Override
    public Object execute(BuildContext context, Object leftOperator, String operation, Object rightOperation, Object[] parameters) {
        if(parameters.length < 2){
            throw new IllegalArgumentException("Invalid parameter length");
        }

        boolean ok = false;
        if(parameters[0] instanceof Boolean){
            ok = (boolean) parameters[0];
        }else if(parameters[0] instanceof String){
            String condition = (String) parameters[0];
            if(condition.equalsIgnoreCase("true")){
                ok = true;
            }else if(!condition.equalsIgnoreCase("false")){
                if(condition.contains(" == ")){
                    String[] parts = condition.split(" == ");
                    if(parts.length != 2) throw new IllegalArgumentException("Invalid length of condition");
                }else if(condition.contains("!=")){

                }else if(condition.contains(">=")){

                }else if(condition.contains("<")){

                }else if(condition.contains(">")){

                }
            }
        }

        if(ok){
            return parameters[1];
        }else if(parameters.length >= 3){
            return parameters[2];
        }else{
            return "";
        }
    }

    @Override
    public Object execute(BuildContext context, Module leftOperator, String operation, Module rightOperation, Module[] parameters) {
        return null;
    }
     */
}
