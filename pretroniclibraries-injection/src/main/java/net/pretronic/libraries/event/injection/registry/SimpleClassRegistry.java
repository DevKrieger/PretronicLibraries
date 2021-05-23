/*
 * (C) Copyright 2021 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.05.21, 09:21
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

package net.pretronic.libraries.event.injection.registry;

import net.pretronic.libraries.event.injection.InjectorService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SimpleClassRegistry implements ClassRegistry{

    protected final Map<Class<?>,Object> mappings;

    public SimpleClassRegistry() {
        this.mappings = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> O getObject(Class<O> clazz, InjectorService injector) {
        Object result = mappings.get(clazz);

        if(result == null){
            return injector.create(clazz);
        }else if(result instanceof Class<?>){
            return getObject((Class<O>) result,injector);
        }else if(result instanceof Supplier<?>){
            return (O) ((Supplier<?>) result).get();
        }else {
            return (O) result;
        }
    }

    @Override
    public Class<?> getMapped(Class<?> clazz) {
        Object result = mappings.get(clazz);
        return result instanceof Class<?> ? (Class<?>) result : clazz;
    }

    @Override
    public void registerSingleton(Object object) {
        this.mappings.put(object.getClass(),object);
    }

    @Override
    public <O> void registerSingleton(Class<O> clazz, O object) {
        this.mappings.put(clazz,object);
    }

    @Override
    public <O> void registerFactory(Class<O> clazz, Supplier<O> factory) {
        this.mappings.put(clazz,factory);
    }

    @Override
    public <O> void registerMapped(Class<O> interfaceClass, Class<? extends O> mappedClass) {
        this.mappings.put(interfaceClass,mappedClass);
    }
}
