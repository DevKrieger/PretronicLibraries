/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.03.20, 19:42
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

package net.pretronic.libraries.message.bml.function;

import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.BuildContext;

public interface ParametrizedFunction extends Function {

    Object execute(BuildContext context,Object[] parameters);

    @Override
    default Object execute(BuildContext context, Module leftOperator, String operation, Module rightOperation, Module[] parameters0) {
        Object[] parameters;
        if(leftOperator != null){
            parameters = new Object[parameters0.length+1];
            parameters[0] = leftOperator;
            System.arraycopy(parameters0,0,parameters,1,parameters0.length);
        }else parameters = parameters0;
        return execute(context,parameters);
    }
}
