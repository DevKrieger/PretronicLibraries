/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 28.03.20, 17:59
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

package net.pretronic.libraries.message.bml.variable.reflect;

import net.pretronic.libraries.utility.Validate;
import net.pretronic.libraries.utility.map.caseintensive.CaseIntensiveHashMap;
import net.pretronic.libraries.utility.map.caseintensive.CaseIntensiveMap;
import net.pretronic.libraries.utility.reflect.ReflectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectVariableDescriber {

    private final CaseIntensiveMap<Method> methods;

    private ReflectVariableDescriber() {
        this.methods = new CaseIntensiveHashMap<>();
    }

    public Object getValue(String name,Object value){
        Method method = this.methods.get(name);
        if(method != null){
            try {
                return method.invoke(method.getDeclaringClass().cast(value));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ReflectException(e);
            }
        }
        return null;
    }

    public void registerGetter(String name,Class<?> clazz){
        String methodName = name;
        char first = name.charAt(0);
        if(!Character.isUpperCase(first)) methodName = Character.toUpperCase(first)+name.substring(1);
        methodName = "get"+methodName;
        register(name,methodName,clazz);
    }

    public void register(String name,String methodName,Class<?> clazz){
        Validate.notNull(methodName);
        try {
            register(name,clazz.getDeclaredMethod(methodName));
        } catch (NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    public void register(String name, Method method){
        Validate.notNull(name,method);
        this.methods.put(name,method);
    }

    public static ReflectVariableDescriber newDescriber(){
        return new ReflectVariableDescriber();
    }

    public static ReflectVariableDescriber ofSuper(Class<?> clazz){
        return of(clazz,true);
    }

    public static ReflectVariableDescriber of(Class<?> clazz){
        return of(clazz,false);
    }

    public static ReflectVariableDescriber of(Class<?> clazz, boolean superClass){
        ReflectVariableDescriber describer = new ReflectVariableDescriber();
        of(describer,clazz,superClass);
        return describer;
    }

    public static void of(ReflectVariableDescriber describer,Class<?> clazz, boolean superClass){
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(!Modifier.isStatic(declaredMethod.getModifiers()) && Modifier.isPublic(declaredMethod.getModifiers())){
                if(declaredMethod.getName().startsWith("get")){
                    describer.register(declaredMethod.getName().substring(3),declaredMethod);
                }
            }
        }
        if(superClass){
            if(clazz.getSuperclass() != null && clazz.getSuperclass().equals(Object.class)){
                of(describer,clazz.getSuperclass(),true);
            }
            for (Class<?> sub : clazz.getInterfaces()) {
                of(describer,sub,true);
            }
        }
    }


}
