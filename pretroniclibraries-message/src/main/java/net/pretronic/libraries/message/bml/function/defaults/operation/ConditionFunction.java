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

import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.function.Function;

public class ConditionFunction implements Function {

    @Override
    public Object execute(BuildContext context, Module leftOperator0, String operation, Module rightOperation0, Module[] parameters) {
        if(parameters.length < 1){
            throw new IllegalArgumentException("Condition function needs at least one parameter");
        }

        Object leftOperation = Module.build(leftOperator0,context,true);

        boolean ok = false;
        if(operation == null){
            ok = leftOperation.equals(true) || "true".equalsIgnoreCase(leftOperation.toString());
        }else if(rightOperation0 != null){
            Object rightOperation = Module.build(rightOperation0,context,true);

            ok = leftOperation.equals(rightOperation) || leftOperation.toString().equals(rightOperation.toString());

            if(operation.equals("!=")) ok = !ok;
            else if(!operation.equals("==")) throw new UnsupportedOperationException("Unsupported operation ("+operation+")");
        }else throw new IllegalArgumentException("function has not right operation");

        if(ok){
            return parameters[0].build(context,false);
        }else if(parameters.length > 1){
            return parameters[1].build(context,false);
        }else return "";
    }
}
