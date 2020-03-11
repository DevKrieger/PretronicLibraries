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

import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.function.Supplier;

public interface ServiceRegistry {

    Collection<Class<?>> getAvailableServices();

    <T> Collection<T> getServices(Class<T> serviceClass);

    <T> T getService(Class<T> serviceClass);

    <T> T getServiceOrDefault(Class<T> serviceClass);

    <T> T getServiceOrDefault(Class<T> serviceClass, Supplier<T> consumer);

    default <T> void registerService(ObjectOwner owner, Class<T> serviceClass, T service){
        registerService(owner,serviceClass,service, ServicePriority.NORMAL);
    }

    <T> void registerService(ObjectOwner owner, Class<T> serviceClass, T service, byte priority);

    <T> boolean isServiceAvailable(Class<T> serviceClass);

    void unregisterService(Object service);

    void unregisterServices(Class<?> serviceClass);

    void unregisterServices(ObjectOwner owner);

}
