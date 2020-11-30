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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeReference<T> {

    private final Type type;
    private final Type rawType;

    public TypeReference(){
        this.type = getGenericSuperclass(getClass());
        this.rawType = getRawType(this.type);
    }

    public TypeReference(Type type) {
        this.type = type;
        this.rawType = getRawType(type);
    }

    public TypeReference(Class<? super T> type) {
        this.type = type;
        this.rawType = getRawType(type);
    }

    public Type getType() {
        return type;
    }

    public Type getRawType() {
        return rawType;
    }

    public Class<?> getRawClass(){
        return (Class<?>) this.rawType;
    }

    public boolean hasArgument(int index) {
        return getArguments().length > index;
    }

    public Type getArgument(int index) {
        Type[] arguments = getArguments();
        if(index >= arguments.length) throw new IndexOutOfBoundsException();
        return arguments[index];
    }

    public Type[] getArguments() {
        if(this.type instanceof ParameterizedType){
            return ((ParameterizedType)type).getActualTypeArguments();
        }
        return new Type[]{};
    }

    public List<Type> getArgumentsAsList() {
        return Arrays.asList(getArguments());
    }

    public Type[] getGenericInterfaceArgument(int interfaceIndex, int... indices) {
        Type genericInterface = this.getRawClass().getGenericInterfaces()[interfaceIndex];
        if (genericInterface instanceof ParameterizedType) {
            Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
            Type[] returnGenericTypes = new Type[indices.length];
            for (int i = 0; i < indices.length; i++) {
                returnGenericTypes[i] = genericTypes[indices[i]];
            }
            return returnGenericTypes;
        }
        return null;
    }

    public boolean isAssignableFrom(Class<?> clazz){
        if(this.type instanceof Class<?>){
            return ((Class<?>) this.type).isAssignableFrom(clazz);
        }
        return false;
    }

    public boolean isGeneric(){
        return !this.rawType.equals(this.type);
    }

    public boolean isArray(){
        return this.type instanceof Class<?> && ((Class<?>)type).isArray();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof TypeReference) return type.equals(((TypeReference<?>) obj).type);
        return false;
    }

    public static Type getGenericSuperclass(Class<?> clazz){
        Type superClass = clazz.getGenericSuperclass();

        if(superClass instanceof Class<?>) {
            throw new IllegalArgumentException("No generic type arguments found.");
        }

        return ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public static Type getRawType(Type type){
        if(type instanceof Class<?>){
            if(((Class<?>)type).isArray()) return ((Class<?>)type).getComponentType();
            else return type;
        }else if(type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType();
        return type;
    }
}
