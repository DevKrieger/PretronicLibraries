/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.09.19, 23:03
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.prematic.libraries.document.utils;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.annotations.DocumentIgnored;
import net.prematic.libraries.document.annotations.DocumentKey;
import net.prematic.libraries.utility.reflect.ReflectException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ConfigurationUtil {

    public static void loadConfigurationClass(Class<?> clazz, Document data){
        loadConfigurationClass(clazz, data,true);
    }

    public static void loadConfigurationClass(Class<?> clazz, Document data, boolean appendMissing){
        try{
            for(Field field : clazz.getDeclaredFields()){
                if(Modifier.isStatic(field.getModifiers()) && field.getAnnotation(DocumentIgnored.class) == null){
                    field.setAccessible(true);
                    DocumentKey key = field.getAnnotation(DocumentKey.class);
                    String name = key != null ? key.value() : field.getName().toLowerCase().replace('_','.');
                    Object result = data.getObject(name,field.getType());
                    if(result != null) field.set(null,result);
                    else if(appendMissing){
                        Object defaultValue = field.get(null);
                        if(defaultValue != null) data.set(name,defaultValue);
                    }
                }
            }
        }catch (IllegalAccessException exception){
            throw new ReflectException(exception);
        }
    }
}
