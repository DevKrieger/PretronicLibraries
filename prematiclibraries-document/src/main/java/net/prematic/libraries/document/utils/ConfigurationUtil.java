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
import net.prematic.libraries.document.annotationss.DocumentFile;
import net.prematic.libraries.document.annotationss.DocumentKey;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.io.IORuntimeException;
import net.prematic.libraries.utility.reflect.ReflectException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static net.prematic.libraries.document.DocumentRegistry.getType;

public class ConfigurationUtil {

    public static void loadConfigurationClass(Class<?> clazz){
        DocumentFile info = clazz.getAnnotation(DocumentFile.class);
        if(info == null) throw new IllegalArgumentException("Class requires @DocumentFile annotation.");

        String path = info.source();
        if(path.endsWith(".")){
            int directoryIndex = path.lastIndexOf('/');

            File directory;
            if(directoryIndex == -1) directory = new File(System.getProperty("user.dir"));
            else{
                directory = new File(path.substring(0,directoryIndex+1));
                path = path.substring(directoryIndex+1);
            }

            File[] files = directory.listFiles();

            File source = null;
            DocumentFileType type = null;
            if(files != null){
                for(File entry : files) {
                    String name = entry.getName();
                    if(name.startsWith(path)){
                        type = getType(name.substring(name.indexOf('.')+1));
                        source = entry;
                        if(type != null) break;
                    }
                }
            }

            if(source == null){
                if(!info.type().equals("Unknown")){
                    source = new File(path+info.type());
                    type = getType(info.type());
                }else throw new IllegalArgumentException("No file with valid file type found.");
            }

            if(type == null) throw new IllegalArgumentException(info.type()+" is not a supported document format.");
            loadConfigurationClass(clazz,type.getReader().read(source),source,type);
        }else{
            DocumentFileType type = getType(info.type());
            if(type == null) throw new IllegalArgumentException(info.type()+" is not a supported document format.");
            File source = new File(path);
            loadConfigurationClass(clazz,type.getReader().read(source),source,type);
        }
    }

    public static void loadConfigurationClass(Class<?> clazz, Document data){
        loadConfigurationClass(clazz, data,null,null);
    }

    public static void loadConfigurationClass(Class<?> clazz, Document data, File source, DocumentFileType type){
        DocumentFile info = clazz.getAnnotation(DocumentFile.class);
        if(info == null) throw new IllegalArgumentException("Class requires @DocumentFile annotation.");

        try{
            boolean update = false;
            for(Field field : clazz.getDeclaredFields()){
                if(Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);

                    DocumentKey key = field.getAnnotation(DocumentKey.class);
                    if(key == null && !info.loadAll()) continue;

                    String name;
                    if(key != null) name = key.value();
                    else name = field.getName().toLowerCase().replace('-','.');


                    Object result = data.getObject(name,field.getType());
                    if(result != null){
                        field.set(null,result);
                    }
                    else if(info.appendMissing()){
                        Object defaultValue = field.get(null);
                        if(defaultValue != null){
                            data.set(name,defaultValue);
                            update = true;
                        }
                    }
                }
            }
            if(update && source != null){
                source.createNewFile();
                type.getWriter().write(source,data);
            }
        }catch (IllegalAccessException exception){
            throw new ReflectException(exception);
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

}
