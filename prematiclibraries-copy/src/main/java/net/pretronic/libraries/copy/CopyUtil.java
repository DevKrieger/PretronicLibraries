/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:43
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

package net.pretronic.libraries.copy;

import net.pretronic.libraries.copy.adapter.*;
import net.pretronic.libraries.copy.annonations.CopyNull;
import net.pretronic.libraries.copy.annonations.ForbidCopy;
import net.pretronic.libraries.copy.annonations.ObjectCopyable;
import net.pretronic.libraries.copy.annonations.ObjectDeepCopyable;
import net.pretronic.libraries.utility.interfaces.Copyable;
import net.pretronic.libraries.utility.interfaces.DeepCopyable;
import net.pretronic.libraries.utility.reflect.Primitives;
import net.pretronic.libraries.utility.reflect.TypeReference;
import net.pretronic.libraries.utility.reflect.UnsafeInstanceCreator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public class CopyUtil {

    private static Map<Type, CopyAdapter> ADAPTERS = new LinkedHashMap<>();
    private static Collection<CopyAdapterFactory> ADAPTER_FACTORIES = new HashSet<>();

    static {
        registerHierarchyAdapter(Collection.class,new CollectionAdapter());
        registerHierarchyAdapter(Map.class,new MapAdapter());

        registerReturnAdapter(UUID.class);
        registerReturnAdapter(Class.class);
    }

    public static <T> void registerAdapter(Class<T> classOf, CopyAdapter<T> adapter){
        ADAPTERS.put(classOf,adapter);
    }

    public static <T> void registerHierarchyAdapter(Class<T> classOf, CopyAdapter<T> adapter){
        ADAPTER_FACTORIES.add(HierarchyCopyAdapterFactory.newFactory(adapter,classOf));
    }

    public static <T> void registerReturnAdapter(Class<T> classOf){
        registerAdapter(classOf,new ReturnAdapter<>());
    }

    public static <O> O copy(O object){
        return copy(object,(CopyOption) null);
    }

    public static <O> O copyDeep(O object){
        return copy(object,CopyOption.DEEP_ALL);
    }

    public static <O> O copy(O object,CopyOption... options){
        return copy(object,Arrays.asList(options));
    }

    @SuppressWarnings("unchecked")
    public static <O> O copy(O object, Collection<CopyOption> options){
        if(object == null) return null;
        Class<?> clazz = object.getClass();
        if(Primitives.isPrimitive(clazz)) return object;
        else if(object instanceof DeepCopyable){
            return (O) ((DeepCopyable<?>) object).copyDeep();
        }else if(object instanceof Copyable){
            return (O) ((Copyable<?>) object).copy();
        }
        CopyAdapter<?> adapter = findAdapter(new TypeReference<>(clazz));
        if(adapter != null) return (O) adapter.invoke(object);

        try{
            if(clazz.isArray()){
                final int length = Array.getLength(object);
                Object copy = Array.newInstance(clazz.getComponentType(),length);
                for(int i = 0;i < length;i++) Array.set(copy,i,copy(Array.get(object,i),options));
                return (O) copy;
            }
            Object copy = UnsafeInstanceCreator.newInstance(clazz);

            if (copyMembers(object, options, clazz, copy)) return null;
            return (O)copy;
        }catch (Exception exception) {
            throw new CopyException("Could not copy "+object.toString()+" / "+object.getClass().getName(),exception);
        }
    }

    private static <O> boolean copyMembers(O object, Collection<CopyOption> options, Class<?> clazz, Object copy) throws IllegalAccessException {
        for(Field field : clazz.getDeclaredFields()){
            if(!Modifier.isStatic(field.getModifiers())){
                field.setAccessible(true);
                Object value = field.get(object);
                if(value == null) return true;
                if(options.contains(CopyOption.DEEP_NOTHING) || Primitives.isPrimitive(field.getType())){
                    field.set(copy,value);
                }else{
                    if(Primitives.isPrimitive(value.getClass())){
                        field.set(copy,value);
                    }else if(field.getAnnotation(ObjectDeepCopyable.class) != null){
                        field.set(copy,copy(value,options));
                    }else if(field.getAnnotation(ObjectCopyable.class) != null){
                        field.set(copy,copy(value));
                    }else if(field.getAnnotation(CopyNull.class) != null){
                        field.set(copy,null);
                    }else if(options.contains(CopyOption.DEEP_ALL)){
                        if(field.getAnnotation(ForbidCopy.class) != null && !options.contains(CopyOption.IGNORE_FORBIDDEN)){
                            field.set(copy,copyDeep(value));
                        }else field.set(copy,copy(value,options));
                    }else field.set(copy,value);
                }

            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> CopyAdapter<T> findAdapter(TypeReference<T> reference){
        CopyAdapter<T>  adapter = ADAPTERS.get(reference.getRawType());
        if(adapter != null) return adapter;
        for(CopyAdapterFactory factory : ADAPTER_FACTORIES){
            adapter = factory.create(reference);
            if(adapter != null){
                ADAPTERS.put(reference.getRawType(),adapter);
                return adapter;
            }
        }
        return null;
    }
}
