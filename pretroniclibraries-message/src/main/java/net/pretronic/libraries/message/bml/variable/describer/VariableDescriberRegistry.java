/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 28.03.20, 18:47
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

package net.pretronic.libraries.message.bml.variable.describer;

import net.pretronic.libraries.utility.Validate;

import java.util.HashMap;
import java.util.Map;

public final class VariableDescriberRegistry {

    private final static Map<Class<?>,VariableDescriber<?>> DESCRIBERS = new HashMap<>();

    public static void registerDescriber(Class<?> clazz, VariableDescriber<?> describer){
        Validate.notNull(clazz,describer);
        DESCRIBERS.put(clazz,describer);
    }

    public static <T> VariableDescriber<T> registerDescriber(Class<T> clazz){
        Validate.notNull(clazz);
        VariableDescriber<T> describer = VariableDescriber.ofSuper(clazz);
        registerDescriber(clazz,describer);
        return describer;
    }

    @SuppressWarnings("unchecked")
    public static <T> VariableDescriber<T> getDescriber(Class<T> clazz){
        Validate.notNull(clazz);
        return (VariableDescriber<T>) DESCRIBERS.get(clazz);
    }

}
