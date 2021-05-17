/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
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

package net.pretronic.libraries.utility.reflect;

import net.pretronic.libraries.utility.SystemUtil;
import net.pretronic.libraries.utility.reflect.versioned.JDK16ReflectVersioned;
import net.pretronic.libraries.utility.reflect.versioned.JDK9ReflectVersioned;
import net.pretronic.libraries.utility.reflect.versioned.LegacyReflectVersioned;
import net.pretronic.libraries.utility.reflect.versioned.ReflectVersioned;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil {

    private final static ReflectVersioned VERSIONED;
    private static Unsafe UNSAFE;

    public static final long STRING_VALUE_FIELD_OFFSET;

    static {
        ReflectVersioned versioned;
        if(SystemUtil.getJavaBaseVersion() >= 16) versioned = new JDK16ReflectVersioned();
        else if(SystemUtil.getJavaBaseVersion() >= 9) versioned = new JDK9ReflectVersioned();
        else versioned = new LegacyReflectVersioned();
        VERSIONED = versioned;

        STRING_VALUE_FIELD_OFFSET = getStringFieldOffset("value");
    }

    public static Unsafe getUnsafe(){
        if(UNSAFE == null){
            try {
                Field fu = Unsafe.class.getDeclaredField("theUnsafe");
                fu.setAccessible(true);
                UNSAFE = (Unsafe) fu.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new ReflectException(e);
            }
        }
        return UNSAFE;
    }

    public static Collection<Field> getAllFields(Class<?> clazz) {
        Collection<Field> result = new HashSet<>();
        Class<?> subClass = clazz;
        while (subClass != null && subClass != Object.class) {
            Collections.addAll(result, subClass.getDeclaredFields());
            subClass = subClass.getSuperclass();
        }
        return result;
    }

    public static Field getField(Class<?> clazz, String fieldName){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception exception) {throw new ReflectException("Field "+fieldName+" in class "+clazz+" not found.",exception);}
    }

    public static Field findFieldByType(Class<?> clazz, Class<?> type){
        for (Field declaredField : clazz.getDeclaredFields()) {
            if(declaredField.getType().equals(type)) return declaredField;
        }
        throw new ReflectException("No field with type "+type+" in "+clazz+" found");
    }

    public static Field findFieldBySimpleName(Class<?> clazz, String name){
        for (Field declaredField : clazz.getDeclaredFields()) {
            if(declaredField.getType().getSimpleName().equals(name)) return declaredField;
        }
        throw new ReflectException("No field with type name"+name+" in "+clazz+" found");
    }

    public static Object getFieldValue(Class<?> clazz, String fieldName){
        return getFieldValue(clazz,fieldName,Object.class);
    }

    public static <R> R getFieldValue(Class<?> clazz, String fieldName, Class<R> value){
       return getFieldValue(clazz,null,fieldName,value);
    }

    public static Object getFieldValue(Object object, String fieldName){
        return getFieldValue(object,fieldName,Object.class);
    }

    public static <R> R getFieldValue(Object object, String fieldName, Class<R> value){
        return getFieldValue(object.getClass(),object,fieldName,value);
    }

    public static Object getFieldValue(Class<?> clazz, Object object, String fieldName){
        return getFieldValue(clazz,object,fieldName,Object.class);
    }

    public static <R> R getFieldValue(Class<?> clazz,Object object, String fieldName, Class<R> value){
        try {
            Field field = getField(clazz, fieldName);
            field.setAccessible(true);
            return value.cast(field.get(object));
        } catch (Exception exception) {throw new ReflectException(exception);}
    }

    public static void changeFieldValue(Class<?> clazz,String fieldName,Object value){
        changeFieldValue(clazz,null, fieldName, value);
    }

    public static void changeFieldValue(Object object,String fieldName,Object value){
        changeFieldValue(object.getClass(),object, fieldName, value);
    }

    public static void setUnsafeObjectFieldValue(Field field,Object value){
        setUnsafeObjectFieldValue(null,field,value);
    }

    public static void setUnsafeObjectFieldValue(Object target,Field field,Object value){
        field.setAccessible(true);
        Class<?> type = field.getType();
        Unsafe unsafe = getUnsafe();
        if (Modifier.isStatic(field.getModifiers())) {
            if(type.isPrimitive()){
                if(type == int.class || type == Integer.class){
                    unsafe.putIntVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (int) value);
                }else if(type == long.class || type == Long.class){
                    unsafe.putLongVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (long) value);
                }else if(type == double.class || type == Double.class){
                    unsafe.putDoubleVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (double) value);
                }else if(type == short.class || type == Short.class){
                    unsafe.putShortVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (short) value);
                }else if(type == float.class || type == Float.class){
                    unsafe.putFloatVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (float) value);
                }else if(type == byte.class || type == Byte.class){
                    unsafe.putByteVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (byte) value);
                }else if(type == boolean.class || type == Boolean.class){
                    unsafe.putBooleanVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (boolean) value);
                }else if(type == char.class || type == Character.class){
                    unsafe.putCharVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field), (char) value);
                }else{
                    throw new UnsupportedOperationException("Invalid primitive type");
                }
            }else {
                if(type.equals(String.class)) {
                    try {
                        String newValue = (String) value;
                        char[] chars = newValue.toCharArray();

                        String string = (String) field.get(null);

                        unsafe.putObjectVolatile(string, STRING_VALUE_FIELD_OFFSET, chars);
                    } catch (IllegalAccessException e) {
                        throw new ReflectException(e);
                    }
                } else {
                    unsafe.putObjectVolatile(field.getDeclaringClass(), unsafe.staticFieldOffset(field),value);
                }
            }
        }else{
            if(target == null) throw new IllegalArgumentException("Target can only be null for static fields");
            if(type.isPrimitive()){
                if(type == int.class || type == Integer.class){
                    unsafe.putInt(target, unsafe.objectFieldOffset(field), (int) value);
                }else if(type == long.class || type == Long.class){
                    unsafe.putLong(target, unsafe.objectFieldOffset(field), (long) value);
                }else if(type == double.class || type == Double.class){
                    unsafe.putDouble(target, unsafe.objectFieldOffset(field), (double) value);
                }else if(type == short.class || type == Short.class){
                    unsafe.putShort(target, unsafe.objectFieldOffset(field), (short) value);
                }else if(type == float.class || type == Float.class){
                    unsafe.putFloat(target, unsafe.objectFieldOffset(field), (float) value);
                }else if(type == byte.class || type == Byte.class){
                    unsafe.putByte(target, unsafe.objectFieldOffset(field), (byte) value);
                }else if(type == boolean.class || type == Boolean.class){
                    unsafe.putBoolean(target, unsafe.objectFieldOffset(field), (boolean) value);
                }else if(type == char.class || type == Character.class){
                    unsafe.putChar(target, unsafe.objectFieldOffset(field), (char) value);
                }else{
                    throw new UnsupportedOperationException("Invalid primitive type");
                }
            }else{
                unsafe.putObject(target, unsafe.objectFieldOffset(field),value);
            }
        }
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
            return clazz.getDeclaredMethod(methodName);
        } catch (Exception exception) {throw new ReflectException(exception);}
    }

    public static Method getMethod(Class<?> clazz, String methodName,Class<?>[] parameters){
        try {
            return clazz.getDeclaredMethod(methodName,parameters);
        } catch (Exception exception) {throw new ReflectException(exception);}
    }

    public static Object invokeMethod(Object object, String methodName, Object...parameters){
        return invokeMethod(object.getClass(),object, methodName,parameters);
    }

    public static Object invokeMethod(Class<?> clazz, Object object, String methodName, Object[] parameters){
        return invokeMethod(clazz, object, methodName,buildTyeArray(parameters), parameters);
    }

    public static Object invokeMethod(Object object, String methodName,Class<?>[] classes, Object[] parameters){
        return invokeMethod(object.getClass(),object,methodName,classes,parameters);
    }

    public static Object invokeMethod(Class<?> clazz, Object object, String methodName,Class<?>[] classes, Object[] parameters){
        try {
            Method method = getMethod(clazz,methodName,classes);
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
     * Get all implemented interfaces of {@code clazz} and implemented interfaces of all super classes
     * @param clazz to get interfaces
     * @return collection with classes
     */
    public static Collection<Class<?>> getAllInterfaces(Class<?> clazz) {
        Set<Class<?>> classes = new HashSet<>();
        for(Class<?> interfaceClass : clazz.getInterfaces()) {
            classes.add(interfaceClass);
            for (Class<?> superInterfaceClass : interfaceClass.getInterfaces()) {
                classes.add(superInterfaceClass);
                classes.addAll(getAllInterfaces(superInterfaceClass));
            }
        }
        return classes;
    }

    public static void grantFinalPrivileges(Field field){
        if(Modifier.isFinal(field.getModifiers())){
            VERSIONED.grantFinalPrivileges(field);
        }
    }

    private static long getStringFieldOffset(String fieldName) {
        try {
            return getUnsafe().objectFieldOffset(String.class.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new ReflectException(e);
        }
    }
}
