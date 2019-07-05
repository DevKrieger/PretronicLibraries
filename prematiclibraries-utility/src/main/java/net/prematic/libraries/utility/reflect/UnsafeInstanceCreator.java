/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.19 17:08
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public interface UnsafeInstanceCreator {

    UnsafeInstanceCreator DEFAULT = findCreator();

    <T> T createInstance(Class<T> clazz) throws Exception;

    static <T> T newInstance(Class<T> clazz){
        try{
            if(clazz.isEnum() || clazz.isInterface() || clazz.isEnum() ||  Modifier.isAbstract(clazz.getModifiers()))
                throw new IllegalArgumentException("It is not possible to create an instance of an interface, enum or abstract class.");
            return DEFAULT.createInstance(clazz);
        }catch (Exception exception){
            throw new ReflectException(exception);
        }
    }

    static UnsafeInstanceCreator findCreator(){

        try {//Sun Unsafe
            Class<?> clazz = Class.forName("sun.misc.Unsafe");
            Field field = clazz.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);
            Method allocator = clazz.getMethod("allocateInstance",Class.class);
            return new UnsafeInstanceCreator(){
                @Override
                public <T> T createInstance(Class<T> clazz) throws Exception{
                    return (T) allocator.invoke(unsafe,clazz);
                }
            };
        } catch (Exception ignored) {}

        try {//Java io ObjectStreamClass
            Class<?> clazz = Class.forName("java.io.ObjectStreamClass");
            Method constructor = clazz.getDeclaredMethod("getConstructorId", Class.class);
            constructor.setAccessible(true);
            int constructorId = (Integer) constructor.invoke(null, Object.class);
            Method creator = clazz.getDeclaredMethod("newInstance", Class.class, int.class);
            creator.setAccessible(true);
            return new UnsafeInstanceCreator(){
                @Override
                public <T> T createInstance(Class<T> clazz) throws Exception {
                    checkInstanceAble(clazz);
                    return (T) creator.invoke(null, clazz, constructorId);
                }
            };
        } catch (Exception ignored) {}

        try {//Java io ObjectInputStream
            final Method creator = Class.forName("java.io.ObjectInputStream").getDeclaredMethod("newInstance", Class.class, Class.class);
            creator.setAccessible(true);
            return new UnsafeInstanceCreator() {
                @Override
                public <T> T createInstance(Class<T> clazz) throws Exception {
                    checkInstanceAble(clazz);
                    return (T) creator.invoke(null, clazz, Object.class);
                }
            };
        } catch (Exception ignored) {}

        return new UnsafeInstanceCreator() {
            @Override
            public <T> T createInstance(Class<T> clazz) {
                throw new IllegalArgumentException("No unsafe instance creator found.");
            }
        };
    }

    static void checkInstanceAble(Class<?> clazz){
        if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
            throw new IllegalArgumentException("Can not create a instance of an interface or an abstract class ("+clazz.getName()+")");
        }
    }
}
