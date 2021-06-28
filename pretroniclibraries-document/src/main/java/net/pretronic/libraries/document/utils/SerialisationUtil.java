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
import net.pretronic.libraries.document.DocumentContext;
import net.pretronic.libraries.document.DocumentRegistry;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.annotations.*;
import net.pretronic.libraries.document.entry.ArrayEntry;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.entry.DocumentNode;
import net.pretronic.libraries.utility.reflect.Primitives;
import net.pretronic.libraries.utility.reflect.TypeReference;
import net.pretronic.libraries.utility.reflect.UnsafeInstanceCreator;

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
        else if(DocumentEntry.class.isAssignableFrom(value.getClass())) {
            DocumentEntry entry = ((DocumentEntry) value);
            if(entry.getKey() == null || entry.getKey().equals(key)){
                entry.setKey(key);
                return ((DocumentEntry) value);
            }else return ((DocumentEntry) value).copy(key);
        }
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
        for(Class<?> clazz = objectClass; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()){
                if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())){
                    try{
                        field.setAccessible(true);
                        if(field.getAnnotation(DocumentIgnored.class) == null){
                            Object fieldValue = field.get(value);
                            if(fieldValue == null) continue;
                            if (isIgnored(field, fieldValue)) continue;

                            DocumentKey name = field.getAnnotation(DocumentKey.class);
                            String endName = name!=null?name.value():field.getName();

                            DocumentAttribute attribute = field.getAnnotation(DocumentAttribute.class);
                            DocumentNode node = attribute == null ? document : document.toDocument().getAttributes();

                            String[] keys = endName.split("\\.");
                            if(keys.length > 1 && node.isObject()){
                                for (int i = 0; i < keys.length-1; i++) {
                                    Document next = node.toDocument().getDocument(keys[i]);
                                    if(next == null){
                                        next = Document.factory().newDocument(keys[i]);
                                        node.entries().add(next);
                                    }
                                    node = next;
                                }
                                endName = keys[keys.length-1];
                            }

                            node.entries().add(serialize(context,endName,fieldValue));
                        }
                    }catch (Exception ignored){}
                }
            }
        }
        return document;
    }

    private static boolean isIgnored(Field field, Object fieldValue) {
        if(field.isAnnotationPresent(DocumentIgnoreZeroValue.class)
                && fieldValue instanceof Number) {
            Number valueNumber = (Number) fieldValue;
            if(valueNumber instanceof Integer && valueNumber.intValue() == 0) return true;
            else if(valueNumber instanceof Long && valueNumber.longValue() == 0) return true;
            else if(valueNumber instanceof Double && valueNumber.doubleValue() == 0) return true;
            else if(valueNumber instanceof Float && valueNumber.floatValue() == 0) return true;
            else if(valueNumber instanceof Byte && valueNumber.byteValue() == 0) return true;
            else if(valueNumber instanceof Short && valueNumber.shortValue() == 0) return true;
        }
        if(field.isAnnotationPresent(DocumentIgnoreBooleanValue.class) && fieldValue instanceof Boolean) {
            DocumentIgnoreBooleanValue ignoreBooleanValue = field.getAnnotation(DocumentIgnoreBooleanValue.class);
            return ignoreBooleanValue.ignore() == (boolean) fieldValue;
        }
        return false;
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
                else throw new IllegalArgumentException("Invalid Primitive type " + type.getType());
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
        }else throw new IllegalArgumentException("Invalid Primitive type " + type.getType());
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

        if(Primitives.isPrimitive(type.getRawClass())) throw new IllegalArgumentException("Entry is not a object");

        Object instance = DocumentRegistry.getInstanceFactory().newInstance(type.getRawClass());

        DocumentNode document = entry.toNode();
        for(Class<?> clazz = type.getRawClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()) {
                if(!Modifier.isStatic(field.getModifiers())){
                    try{
                        field.setAccessible(true);
                        if(field.getAnnotation(DocumentIgnored.class) == null){
                            DocumentAttribute attribute = field.getAnnotation(DocumentAttribute.class);
                            DocumentNode node = attribute == null ? document :document.toDocument().getAttributes();

                            DocumentKey name = field.getAnnotation(DocumentKey.class);
                            String endName = name!=null?name.value():field.getName();

                            if(node.contains(endName)) field.set(clazz.cast(instance),deserialize(context,node.getEntry(endName),field.getGenericType()));
                            else if(field.getAnnotation(DocumentRequired.class) == null){
                                if(field.getType().isPrimitive()){
                                    if(field.getType().equals(boolean.class)) field.set(clazz.cast(instance),false);
                                    else field.set(clazz.cast(instance),0);
                                }else field.set(clazz.cast(instance),null);
                            }else throw new IllegalArgumentException("The key "+name+" is required");
                        }
                    }catch (IllegalAccessException ignored){

                    }catch (Exception e){
                        System.out.println("(Document) Failed to initialize field "+field.getName()+" for "+field.getDeclaringClass());
                        System.out.println("(Document) Error "+e.getMessage());//@Todo remove in future
                    }
                }
            }
        }
        if(DocumentRegistry.getInstanceFactory() != null){
            DocumentRegistry.getInstanceFactory().inject(instance);
        }
        return instance;
    }
}
