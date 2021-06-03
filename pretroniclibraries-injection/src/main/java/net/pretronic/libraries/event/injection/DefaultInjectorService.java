/*
 * (C) Copyright 2021 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.05.21, 22:29
 * @web %web%
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

package net.pretronic.libraries.event.injection;

import net.pretronic.libraries.event.injection.annotations.Inject;
import net.pretronic.libraries.event.injection.annotations.InjectNoConstructor;
import net.pretronic.libraries.event.injection.registry.ClassRegistry;
import net.pretronic.libraries.utility.exception.OperationFailedException;
import net.pretronic.libraries.utility.interfaces.InjectorAdapterAble;
import net.pretronic.libraries.utility.reflect.ReflectException;
import net.pretronic.libraries.utility.reflect.ReflectionUtil;
import net.pretronic.libraries.utility.reflect.UnsafeInstanceCreator;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultInjectorService implements InjectorService{

    private final ClassRegistry registry;

    public DefaultInjectorService(ClassRegistry registry) {
        this.registry = registry;
    }

    @Override
    public ClassRegistry getClassRegistry() {
        return registry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> O create(Class<O> clazz0) {
        Class<?> clazz = registry.getMapped(clazz0);
        if(clazz.getAnnotation(InjectNoConstructor.class) != null){
            Object obj = UnsafeInstanceCreator.newInstance(clazz0);
            inject(obj);
            if(obj instanceof InjectorAdapterAble) ((InjectorAdapterAble) obj).setInjector(this);
            return (O) obj;
        }else{
            try{
                Constructor<?> constructor = null;
                if(clazz.getConstructors().length == 0 ) constructor = clazz.getConstructor();
                else if(clazz.getConstructors().length == 1 ) constructor = clazz.getConstructors()[0];
                else {
                    for (Constructor<?> constructor0 : clazz.getConstructors()) {
                        if(constructor0.getAnnotation(Inject.class) != null){
                            constructor = constructor0;
                            break;
                        }
                    }
                    if(constructor == null) throw new OperationFailedException("If multiple constructors are defined, the @Inject annotation must be used");
                }

                constructor.setAccessible(true);
                Object[] objects = new Object[constructor.getParameterCount()];
                int i = 0;
                for (Class<?> parameterType : constructor.getParameterTypes()) {
                    objects[i] = registry.getObject(parameterType,this);
                    i++;
                }
                Object obj = constructor.newInstance(objects);
                inject(obj);
                if(obj instanceof InjectorAdapterAble) ((InjectorAdapterAble) obj).setInjector(this);
                return (O) obj;
            }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new ReflectException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> O createSilent(Class<O> clazz) {
        Object obj = UnsafeInstanceCreator.newInstance(clazz);
        inject(obj);
        return (O) obj;
    }

    @Override
    public void inject(Class<?> clazz) {
        inject(clazz,null);
    }

    @Override
    public void inject(Object obj) {
        inject(obj.getClass(),obj);
    }

    private void inject(Class<?> clazz, Object obj){
        try{
            for (Field field : clazz.getDeclaredFields()) {
                if(field.getAnnotation(Inject.class) != null){
                    field.setAccessible(true);
                    if(Modifier.isStatic(field.getModifiers())){
                        Object toSet = registry.getObject(field.getType(),this);
                        ReflectionUtil.setUnsafeObjectFieldValue(field,toSet);
                    }else if(obj != null && field.get(obj) == null){
                        Object toSet = registry.getObject(field.getType(),this);
                        ReflectionUtil.setUnsafeObjectFieldValue(obj,field,toSet);
                    }
                }
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if(method.getAnnotation(Inject.class) != null){
                    method.setAccessible(true);
                    if(Modifier.isStatic(method.getModifiers())){
                        method.invoke(null,getParameters(method));
                    }else if(obj != null){
                        method.invoke(obj,getParameters(method));
                    }
                }
            }
        }catch (InvocationTargetException | IllegalAccessException e){
            throw new ReflectException(e);
        }
    }

    private Object[] getParameters(Method method) {
        Object[] objects = new Object[method.getParameterCount()];
        int i = 0;
        for (Class<?> parameterType : method.getParameterTypes()) {
            objects[i] = registry.getObject(parameterType,this);
            i++;
        }
        return objects;
    }
}
