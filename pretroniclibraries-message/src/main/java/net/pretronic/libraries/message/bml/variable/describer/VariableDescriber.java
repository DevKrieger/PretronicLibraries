/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 07.04.20, 15:29
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

package net.pretronic.libraries.message.bml.variable.describer;

import net.pretronic.libraries.utility.Validate;
import net.pretronic.libraries.utility.exception.OperationFailedException;
import net.pretronic.libraries.utility.map.caseintensive.CaseIntensiveHashMap;
import net.pretronic.libraries.utility.map.caseintensive.CaseIntensiveMap;
import net.pretronic.libraries.utility.reflect.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;
import java.util.function.Function;

public class VariableDescriber<T> {

    private final CaseIntensiveMap<Function<T,?>> functions;
    private final CaseIntensiveMap<BiFunction<T,String,?>> parameterFunctions;

    private Function<T,?> forwardFunction;

    public VariableDescriber() {
        this.functions = new CaseIntensiveHashMap<>();
        this.parameterFunctions = new CaseIntensiveHashMap<>();
    }

    public CaseIntensiveMap<Function<T, ?>> getFunctions() {
        return functions;
    }

    public CaseIntensiveMap<BiFunction<T, String, ?>> getParameterFunctions() {
        return parameterFunctions;
    }

    public Function<T, ?> getForwardFunction() {
        return forwardFunction;
    }

    public void setForwardFunction(Function<T, ?> forwardFunction) {
        this.forwardFunction = forwardFunction;
    }

    public void registerGetter(String name, Class<?> clazz){
        String methodName = name;
        char first = name.charAt(0);
        if(!Character.isUpperCase(first)) methodName = Character.toUpperCase(first)+name.substring(1);
        methodName = "get"+methodName;
        registerMethod(name,methodName,clazz);
    }

    public void registerMethod(String key,String methodName,Class<?> clazz){
        registerMethod(key, ReflectionUtil.getMethod(clazz,methodName));
    }

    public void registerMethod(String key, Method method){
        registerFunction(key, value -> {
            try {
                return method.invoke(method.getDeclaringClass().cast(value));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new OperationFailedException("Could not invoke describer method "+method.getName()+" with key "+key,e);
            }
        });
    }

    public void registerFunction(String key, Function<T,?> function){
        Validate.notNull(key,function);
        this.functions.put(key,function);
    }

    public void registerParameterFunction(String key, BiFunction<T,String,?> function){
        Validate.notNull(key,function);
        this.parameterFunctions.put(key,function);
    }

    public static <T> VariableDescriber<T> newDescriber(Class<T> clazz){
        return new VariableDescriber<>();
    }

    public static <T> VariableDescriber<T> ofSuper(Class<T> clazz){
        return of(clazz,true);
    }

    public static <T> VariableDescriber<T> of(Class<T> clazz){
        return of(clazz,false);
    }

    public static <T> VariableDescriber<T> of(Class<T> clazz, boolean superClass){
        VariableDescriber<T> describer = new VariableDescriber();
        of(describer,clazz,superClass);
        return describer;
    }

    public static void of(VariableDescriber<?> describer,Class<?> clazz, boolean superClass){
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(!Modifier.isStatic(declaredMethod.getModifiers())
                    && Modifier.isPublic(declaredMethod.getModifiers())
                    && declaredMethod.getParameters().length == 0
                    && declaredMethod.getName().startsWith("get")){
                describer.registerMethod(declaredMethod.getName().substring(3),declaredMethod);
            }
        }
        if(superClass){
            if(clazz.getSuperclass() != null && !(Object.class.equals(clazz.getSuperclass()))){
                of(describer,clazz.getSuperclass(),true);
            }
            for (Class<?> sub : clazz.getInterfaces()) {
                of(describer,sub,true);
            }
        }
    }

    public static Object get(Object object,String[] parts, int index){
        Object current = object;
        for (int i = index; i < parts.length; i++) {
            if(current == null) break;
            String part = parts[i];
            VariableDescriber<?> describer = findDescriber(current.getClass());
            if(describer != null){
                Function function = describer.getFunctions().get(part);
                if(function != null){
                    current = function.apply(current);
                }else{
                    BiFunction parameterFunction = describer.getParameterFunctions().get(part);
                    if(parameterFunction != null){
                        current = parameterFunction.apply(current,i < parts.length-1 ? parts[++i] : null);
                    }else if(i == index && describer.getForwardFunction() != null){
                        Function result = describer.getForwardFunction();
                        current = result.apply(current);
                        return get(current,parts,index);
                    }
                }
            }else throw new IllegalArgumentException("No variable describer for "+current.getClass()+" found");
        }
        return current;
    }

    private static VariableDescriber<?> findDescriber(Class<?> clazz0){
        Class<?> clazz = clazz0;
        VariableDescriber<?> describer = VariableDescriberRegistry.getDescriber(clazz);
        if(describer == null){
            while (!(Object.class.equals(clazz.getSuperclass()))){
                clazz = clazz.getSuperclass();
                describer  = VariableDescriberRegistry.getDescriber(clazz);
                if(describer != null) return describer;
            }
        }
        return describer;
    }


}
