/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.document.utils;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.annotations.DocumentIgnored;
import net.pretronic.libraries.document.annotations.DocumentKey;
import net.pretronic.libraries.document.annotations.OnDocumentConfigurationLoad;
import net.pretronic.libraries.document.type.DocumentFileType;
import net.pretronic.libraries.utility.exception.OperationFailedException;
import net.pretronic.libraries.utility.reflect.ReflectException;
import net.pretronic.libraries.utility.reflect.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Ref;

public class ConfigurationUtil {

    public static void loadConfigurationClass(Class<?> clazz, Document data){
        loadConfigurationClass(clazz, data,true);
    }

    public static void loadConfigurationClass(Class<?> clazz, Document data, boolean appendMissing){
        try{
            for(Field field : clazz.getDeclaredFields()){
                if(Modifier.isStatic(field.getModifiers()) && field.getAnnotation(DocumentIgnored.class) == null && !Modifier.isTransient(field.getModifiers())){

                    DocumentKey key = field.getAnnotation(DocumentKey.class);
                    String name = key != null ? key.value() : field.getName().toLowerCase().replace('_','.');
                    Object result = data.getObject(name,field.getGenericType());

                    try{
                        if(result != null) {
                            ReflectionUtil.setUnsafeObjectFieldValue(field,result);
                        } else if(appendMissing){
                            field.setAccessible(true);
                            Object defaultValue = field.get(null);
                            if(defaultValue != null) data.set(name,defaultValue);
                        }
                    }catch (Exception exception){
                        throw new OperationFailedException("Failed initializing field "+field.getName()+" is configuration class "+clazz,exception);
                    }
                }
            }
            for (Method method : clazz.getDeclaredMethods()){
                if(Modifier.isStatic(method.getModifiers()) && method.getAnnotation(OnDocumentConfigurationLoad.class) != null){
                    method.setAccessible(true);
                    method.invoke(null);
                }
            }
        }catch (IllegalAccessException | InvocationTargetException exception){
            throw new ReflectException(exception);
        }
    }
}
