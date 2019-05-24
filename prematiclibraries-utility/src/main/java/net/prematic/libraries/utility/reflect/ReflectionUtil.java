/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 17.02.19 14:38
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

package net.prematic.libraries.utility.reflect;

import net.prematic.libraries.utility.map.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil {

    public static Field getField(Class<?> clazz, String fieldName){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception exception) {throw new ReflectException("Field "+fieldName+" in class "+clazz+" not found.",exception);}
    }

    public static Object getFieldValue(Class<?> clazz, String fieldName){
        return getFieldValue(clazz,fieldName,Object.class);
    }

    public static <R> R getFieldValue(Class<?> clazz, String fieldName, Class<R> value){
        try {
            Field field = getField(clazz, fieldName);
            field.setAccessible(true);
            return value.cast(field.get(null));
        } catch (Exception exception) {throw new ReflectException(exception);}
    }

    public static void changeFieldValue(Class<?> clazz,String fieldName,Object value){
        changeFieldValue(clazz,null, fieldName, value);
    }

    public static void changeFieldValue(Object object,String fieldName,Object value){
        changeFieldValue(object.getClass(),object, fieldName, value);
    }

    public static void changeFieldValue(Class<?> clazz, Object object,String fieldName,Object value){
        try {
            Field field = getField(clazz,fieldName);
            if(field != null){
                field.setAccessible(true);
                field.set(object,value);
            }
        } catch (Exception exception) {throw new ReflectException("Could not change file "+fieldName+" from class "+clazz,exception);}
    }

    public static Method getMethod(Class<?> clazz, String methodName){
        try {
            return clazz.getMethod(methodName);
        } catch (Exception exception) {throw new ReflectException(exception);}
    }

    public static Method getMethod(Class<?> clazz, String methodName,Class<?>[] parameters){
        try {
            return clazz.getMethod(methodName,parameters);
        } catch (Exception exception) {throw new ReflectException(exception);}
    }

    public static Object invokeMethod(Object object, String methodName, Object...parameters){
        return invokeMethod(object.getClass(),object, methodName,parameters);
    }

    public static Object invokeMethod(Class<?> clazz, Object object, String methodName, Object[] parameters){
        try {
            Method method = getMethod(clazz,methodName,buildTyeArray(parameters));
            method.setAccessible(true);
            return method.invoke(object,parameters);
        }  catch (Exception exception) {throw new ReflectException("Could not invoke method  "+methodName+" from class "+object.getClass(),exception);}
    }

    public static Object invokeMethod(Method method, Object object, Object parameters){
        try {
            return method.invoke(object,parameters);
        }  catch (Exception exception) {throw new ReflectException("Could not invoke method  "+method.getName()+" from class "+object.getClass());}
    }

    public static Class<?>[] buildTyeArray(Object[] array){
        Class<?>[] types = new Class<?>[array.length];
        for(int i = 0;i <array.length;i++) if(array[i] != null) types[i] = array[i].getClass();
        return types;
    }

    /**
     * Check if class {@code from} or super interfaces of {@code from} implements {@code to}
     * @param from class to check
     * @param to interface to check, if class implements it
     * @return boolean, if {@code from} implements {@code to}
     */
    public static boolean implementsInterface(Class<?> from, Class<?> to) {
        if(!to.isInterface()) return false;
        if(from == to) return true;
        return getAllInterfaces(from).contains(to);
    }

    /**
     * Get all implemented interfaces of {@code clazz} and implemented interfaces of all super classes
     * @param clazz to get interfaces
     * @return collection with classes
     */
    public static Collection<Class<?>> getAllInterfaces(Class<?> clazz) {
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> interfaceClass : clazz.getInterfaces()) {
            classes.add(interfaceClass);
            for (Class<?> superInterfaceClass : interfaceClass.getInterfaces()) {
                classes.add(superInterfaceClass);
                classes.addAll(getAllInterfaces(superInterfaceClass));
            }
        }
        return classes;
    }

    /**
     * Get the generic type of field of a collection
     * @param field of collection to get generic type
     * @return generic type of collection as class
     */
    public static Class<?> getCollectionGenericType(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    /**
     * Get the generic type of key and value of a field of a map
     * @param field of map to get generic types
     * @return generic type of key and value as class
     */
    public static Pair<Class<?>, Class<?>> getMapGenericTypes(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Type[] genericTypes = type.getActualTypeArguments();
        return new Pair<>((Class<?>) genericTypes[0], (Class<?>) genericTypes[1]);
    }
}