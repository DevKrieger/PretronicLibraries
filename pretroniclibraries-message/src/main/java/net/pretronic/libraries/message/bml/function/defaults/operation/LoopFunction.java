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
import net.pretronic.libraries.message.bml.variable.ObjectVariable;
import net.pretronic.libraries.message.bml.variable.Variable;
import net.pretronic.libraries.utility.GeneralUtil;

import java.util.Collection;
import java.util.Iterator;

public class LoopFunction implements Function {

    @Override
    public Object execute(BuildContext context, Module leftOperator, String operation, Module rightOperation, Module[] parameters) {
        if(operation != null){
            if("in".equalsIgnoreCase(operation)){
                return inLoop(context, leftOperator, rightOperation, parameters);
            }else if("to".equalsIgnoreCase(operation)){
                return toLoop(context, leftOperator, rightOperation, parameters);
            }else throw new IllegalArgumentException(operation+" is not a supported operation in a loop function");
        }
        throw new IllegalArgumentException("Invalid operator");
    }

    private Object inLoop(BuildContext context, Module leftOperator, Module rightOperation0, Module[] parameters) {
        if(parameters.length < 1) throw new IllegalArgumentException("Invalid parameter length");
        Object rightOperation = Module.build(rightOperation0,context,true);
        if(rightOperation == null) return new Object[]{};
        else if(rightOperation instanceof Collection<?>){
            Iterator<?> iterator = ((Iterable<?>) rightOperation).iterator();
            int index = 0;
            int indexCount = 0;
            int size = ((Collection<?>) rightOperation).size();
            String var = leftOperator.build(context,true).toString();
            Variable indexVar = context.getVariables().getOrCreate("index");
            Variable positionVar = context.getVariables().getOrCreate("position");
            Variable objectVar = context.getVariables().getOrCreate(var);
            String separator = null;
            if(parameters.length > 1){
                separator = parameters[1].build(context,true).toString();
                size = (size*2)-1;
                if(size < 1) return new Object[]{};
            }

            String firstSeparator = null;
            if(parameters.length > 2){
                firstSeparator = parameters[2].build(context,true).toString();
                size++;
            }

            Object[] result = new Object[size];
            boolean first = true;
            while (iterator.hasNext()){
                Object item = iterator.next();
                indexVar.setObject(indexCount);
                positionVar.setObject(indexCount+1);
                objectVar.setObject(item);
                if(separator != null){
                    if(first){
                        if(firstSeparator != null) result[index++] = firstSeparator;
                        first = false;
                    }else result[index++] = separator;
                }
                result[index] = parameters[0].build(context,false);
                index++;
                indexCount++;
            }
            return result;
        }else throw new IllegalArgumentException("Object is not iterable");
    }

    private Object toLoop(BuildContext context, Module leftOperator, Module rightOperation, Module[] parameters) {
        int right = getNumber(context, rightOperation);
        int left = getNumber(context, leftOperator);
        Variable indexVar = context.getVariables().getOrCreate("index");

        String separator = null;
        if(parameters.length > 1) separator = parameters[1].build(context,true).toString();

        if(left == right) return new Object[]{};
        else if(left < right){
            int size = getSize(left, right, separator);
            if(size < 0) return new Object[]{};

            Object[] result = new Object[size];
            int index = left;
            int arrayIndex = 0;
            boolean first = true;
            while (index < right){
                indexVar.setObject(index);

                if(separator != null){
                    if(first) first = false;
                    else result[arrayIndex++] = separator;
                }
                result[arrayIndex] = parameters[0].build(context,false);

                index++;
                arrayIndex++;
            }
            return result;
        } else {
            int size = getSize(right, left, separator);

            Object[] result = new Object[size];
            int index = left;
            int arrayIndex = 0;
            boolean first = true;
            while (index > right){
                indexVar.setObject(index);

                if(separator != null){
                    if(first) first = false;
                    else result[arrayIndex++] = separator;
                }
                result[arrayIndex] = parameters[0].build(context,false);

                index--;
                arrayIndex++;
            }
            return result;
        }
    }

    private int getSize(int right, int left, String separator) {
        int size = (left - right);
        if (separator != null) size = ((size) * 2) - 1;
        return size;
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
