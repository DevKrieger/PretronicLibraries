/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 22:22
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
import net.prematic.libraries.document.DocumentContext;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.annotations.DocumentAttribute;
import net.prematic.libraries.document.annotations.DocumentIgnored;
import net.prematic.libraries.document.annotations.DocumentKey;
import net.prematic.libraries.document.annotations.DocumentRequired;
import net.prematic.libraries.document.entry.ArrayEntry;
import net.prematic.libraries.document.entry.DocumentBase;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.document.entry.DocumentNode;
import net.prematic.libraries.utility.reflect.Primitives;
import net.prematic.libraries.utility.reflect.TypeReference;
import net.prematic.libraries.utility.reflect.UnsafeInstanceCreator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SerialisationUtil {

    public static DocumentEntry serialize(DocumentContext context, Object value){
        return serialize(context,null,value);
    }

    public static DocumentEntry serialize(DocumentContext context,String key, Object value){
        if(value == null || Primitives.isPrimitive(value)) return DocumentRegistry.getFactory().newPrimitiveEntry(key,value);
        else if(DocumentEntry.class.isAssignableFrom(value.getClass())) return (DocumentEntry) value;
        else if(value.getClass().isArray()) return serializeArray(context,key, value);
        return serializeObject(context,key, value);
    }

    public static DocumentEntry serializeArray(DocumentContext context,String key, Object object){
        ArrayEntry array = DocumentRegistry.getFactory().newArrayEntry(key);
        for(int i = 0; i< Array.getLength(object); i++) array.entries().add(serialize(context,"array-index-"+i,Array.get(object,i)));
        return array;
    }

    public static DocumentEntry serializeObject(DocumentContext context,String key, Object value){
        return serializeObject(context,key,value.getClass(),value);
    }

    @SuppressWarnings("unchecked")
    private static DocumentEntry serializeObject(DocumentContext context,String key,Class<?> objectClass, Object value){
        DocumentAdapter adapter = context.findAdapter(new TypeReference<>(objectClass));
        if(adapter != null) return adapter.write(key, value);

        Document document = DocumentRegistry.getFactory().newDocument(key);
        for(Field field : objectClass.getDeclaredFields()){
            if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())){
                try{
                    field.setAccessible(true);
                    if(field.getAnnotation(DocumentIgnored.class) == null){
                        DocumentKey name = field.getAnnotation(DocumentKey.class);
                        document.entries().add(serialize(context,name!=null?name.value():field.getName(),field.get(value)));
                    }
                }catch (Exception ignored){}
            }
        }
        return document;
    }

    public static <T> T deserialize(DocumentContext context, DocumentBase entry, Class<T> clazz){
        return deserialize(context,entry,new TypeReference<>(clazz));
    }

    public static <T> T deserialize(DocumentContext context,DocumentBase entry, Type type){
        return deserialize(context,entry,new TypeReference<>(type));
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(DocumentContext context,DocumentBase entry,TypeReference type){
        return (T) deserializeRaw(context,entry, type);
    }

    @SuppressWarnings("unchecked")
    private static Object deserializeRaw(DocumentContext context,DocumentBase entry,TypeReference type){
        if(entry.isPrimitive()){
            if(Primitives.isPrimitive(type.getRawClass())) return deserializePrimitive(entry, type);
            else if(type.getRawClass().isEnum()) return Enum.valueOf(type.getRawClass(),entry.toPrimitive().getAsString());
            else{
                DocumentAdapter<?> adapter = context.findAdapter(type);
                if(adapter != null) return adapter.read(entry, type);
                else throw new IllegalArgumentException("Invalid Primitive type");
            }
        }else if(type.isArray() && entry.isObject()) return deserializeArray(context,entry, type);
        else if(entry.isObject()) return deserializeObject(context,entry, type);
        else throw new IllegalArgumentException("Invalid object type");
    }

    public static Object deserializePrimitive(DocumentBase entry,TypeReference type){
        if(type.getRawClass() == String.class){
            return entry.toPrimitive().getAsString();
        }else if(type.getRawClass() == int.class || type.getRawClass() == Integer.class){
            return entry.toPrimitive().getAsInt();
        }else if(type.getRawClass() == long.class || type.getRawClass() == Long.class){
            return entry.toPrimitive().getAsLong();
        }else if(type.getRawClass() == double.class || type.getRawClass() == Double.class){
            return entry.toPrimitive().getAsDouble();
        }else if(type.getRawClass() == short.class || type.getRawClass() == Short.class){
            return entry.toPrimitive().getAsShort();
        }else if(type.getRawClass() == float.class || type.getRawClass() == Float.class){
            return entry.toPrimitive().getAsFloat();
        }else if(type.getRawClass() == byte.class || type.getRawClass() == Byte.class){
            return entry.toPrimitive().getAsByte();
        }else if(type.getRawClass() == boolean.class || type.getRawClass() == Boolean.class){
            return entry.toPrimitive().getAsBoolean();
        }else if(type.getRawClass() == char.class || type.getRawClass() == Character.class){
            return entry.toPrimitive().getAsCharacter();
        }else throw new IllegalArgumentException("Invalid Primitive type");
    }

    public static Object deserializeArray(DocumentContext context,DocumentBase entry,TypeReference type){
        ArrayList<?> instance = new ArrayList<>();
        entry.toDocument().forEach(entry1 -> instance.add(deserialize(context,entry1,type.getRawType())));

        Object array = Array.newInstance((Class<?>) type.getRawType(), instance.size());
        for (int i = 0; i < instance.size(); i++) Array.set(array, i, instance.get(i));

        return array;
    }

    @SuppressWarnings("unchecked")
    public static Object deserializeObject(DocumentContext context,DocumentBase entry,TypeReference type){
        DocumentAdapter<?> adapter = context.findAdapter(type);
        if(adapter != null) return adapter.read(entry, type);

        Object instance = UnsafeInstanceCreator.newInstance(type.getRawClass());

        //@Todo implement attribute annotation
        DocumentNode document = entry.toNode();
        for(Class<?> clazz = type.getRawClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()) {
                try{
                    field.setAccessible(true);
                    if(field.getAnnotation(DocumentIgnored.class) == null){
                        DocumentAttribute attribute = field.getAnnotation(DocumentAttribute.class);
                        DocumentNode node = attribute == null ? document :document.toDocument().getAttributes();

                        DocumentKey name = field.getAnnotation(DocumentKey.class);
                        String endName = name!=null?name.value():field.getName();

                        if(node.contains(endName)) field.set(clazz.cast(instance),deserialize(context,node.getEntry(endName),field.getGenericType()));
                        else if(field.getAnnotation(DocumentRequired.class) == null){
                            if(Primitives.isPrimitive(field.getType())){
                                if(field.getType().equals(boolean.class)) field.set(clazz.cast(instance),false);
                                else field.set(clazz.cast(instance),0);
                            }else field.set(clazz.cast(instance),null);
                        }else throw new IllegalArgumentException("The key "+name+" is required");
                    }
                }catch (Exception ignored){}
            }
        }
        return instance;
    }
}
