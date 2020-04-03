/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 27.03.20, 22:11
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

package net.pretronic.libraries.message.bml.function.defaults;

import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.function.Function;
import net.pretronic.libraries.message.bml.variable.Variable;
import net.pretronic.libraries.utility.GeneralUtil;

import java.util.Collection;
import java.util.Iterator;

/*
@for('object' in {objects},hi {object} @func()}

@for('1' to '10','-1')


 */
public class LoopFunction implements Function {

    @Override
    public Object execute(BuildContext context, Module leftOperator, String operation, Module rightOperation0, Module[] parameters) {
        if(operation != null){
            if("in".equalsIgnoreCase(operation)){
                return inLoop(context, leftOperator, rightOperation0, parameters);
            }else if("to".equalsIgnoreCase(operation)){
                int right = getNumber(context, rightOperation0);
                int left = getNumber(context, leftOperator);
                Variable indexVar = context.getVariables().getOrCreate("index");
                int nextStep;

                throw new UnsupportedOperationException("Currently not supported");
            }else throw new IllegalArgumentException(operation+" is not a supported operation in a loop function");
        }
        throw new IllegalArgumentException("Invalid operator");
    }

    private Object inLoop(BuildContext context, Module leftOperator, Module rightOperation0, Module[] parameters) {
        if(parameters.length < 1) throw new IllegalArgumentException("Invalid parameter length");
        Object rightOperation = Module.build(rightOperation0,context,true);
        if(rightOperation instanceof Collection<?>){
            Iterator<?> iterator = ((Iterable<?>) rightOperation).iterator();
            int index = 0;
            int size = ((Collection<?>) rightOperation).size();
            String var = leftOperator.build(context,true).toString();
            Variable indexVar = context.getVariables().getOrCreate("index");
            Variable objectVar = context.getVariables().getOrCreate(var);
            String separator = null;
            if(parameters.length > 1){
                separator = parameters[1].build(context,true).toString();
                size = (size*2)-1;
            }
            Object[] result = new Object[size];
            boolean first = true;
            while (iterator.hasNext()){
                Object item = iterator.next();
                indexVar.setObject(index);
                objectVar.setObject(item);
                if(separator != null){
                    if(first) first = false;
                    else result[index++] = separator;
                }
                result[index] = parameters[0].build(context,false);
                index++;
            }
            return result;
        }else throw new IllegalArgumentException("Object is not iterable");
    }

    private int getNumber(BuildContext context, Module operator) {
        int right = 0;
        Object rightOperation = Module.build(operator,context,true);
        if(rightOperation instanceof Integer) right = (int) rightOperation;
        else if(rightOperation != null){
            String rightText = rightOperation.toString();
            if(GeneralUtil.isNumber(rightText)) right = Integer.parseInt(rightText);
            else throw new IllegalArgumentException(rightText+" is not a valid number");
        }
        return right;
    }

}
