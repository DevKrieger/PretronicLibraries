/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.04.20, 19:49
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
import net.pretronic.libraries.utility.GeneralUtil;

public class SumFunction implements ParametrizedFunction {

    @Override
    public Object execute(BuildContext context, Object[] parameters) {
        double result = 0;
        for (Object parameter : parameters) {
            if(parameter instanceof Number) result += ((Number)parameter).doubleValue();
            else{
                String number = parameter.toString();
                if(GeneralUtil.isNumber(number)){
                    result += Double.parseDouble(number);
                }
            }
        }
        return result;
    }
}
