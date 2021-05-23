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

import java.util.function.Supplier;

public interface ClassRegistry {

    <O> O getObject(Class<O> oClazz, InjectorService injector);

    Class<?> getMapped(Class<?> clazz);


    void registerSingleton(Object object);

    <O> void registerSingleton(Class<O> oClazz,O object);

    <O> void registerFactory(Class<O> clazz, Supplier<O> factory);

    <O> void registerMapped(Class<O> interfaceClass,Class<? extends O> mappedClass);


}
