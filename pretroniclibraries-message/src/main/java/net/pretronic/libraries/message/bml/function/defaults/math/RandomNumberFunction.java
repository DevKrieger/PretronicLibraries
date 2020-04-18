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

package net.pretronic.libraries.message.bml.function.defaults.math;

import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.function.ParametrizedFunction;
import net.pretronic.libraries.utility.Convert;
import net.pretronic.libraries.utility.GeneralUtil;

public class RandomNumberFunction implements ParametrizedFunction {

    @Override
    public Object execute(BuildContext context, Object[] parameters) {
        int start;
        int end;
        if(parameters.length >= 1){
            start = Convert.toInteger(parameters[0]);
        }else start = 0;
        if(parameters.length >= 2){
            end = Convert.toInteger(parameters[1]);
        }else end = Integer.MAX_VALUE;
        return GeneralUtil.getDefaultRandom().nextInt(end-start)+start;
    }
}
