/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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

package net.pretronic.libraries.plugin.service;

import net.pretronic.libraries.event.injection.InjectorService;
import net.pretronic.libraries.event.injection.registry.SimpleClassRegistry;

import java.util.function.Supplier;

public class ServiceClassRegistry extends SimpleClassRegistry {

    private final ServiceRegistry registry;

    public ServiceClassRegistry(ServiceRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O> O getObject(Class<O> clazz, InjectorService injector) {
        Object result = mappings.get(clazz);

        if(result == null){
            result = registry.getService(clazz);
            return result != null ? (O) result : injector.create(clazz);
        }else if(result instanceof Class<?>){
            return getObject((Class<O>) result,injector);
        }else if(result instanceof Supplier<?>){
            return (O) ((Supplier<?>) result).get();
        }else {
            return (O) result;
        }
    }
}
