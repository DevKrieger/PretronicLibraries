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

package net.pretronic.libraries.message.bml.variable.reflect;

import net.pretronic.libraries.utility.Validate;

import java.util.HashMap;
import java.util.Map;

public final class ReflectVariableDescriberRegistry {

    private final static Map<Class<?>,ReflectVariableDescriber> DESCRIBERS = new HashMap<>();

    public static void registerDescriber(Class<?> clazz, ReflectVariableDescriber describer){
        Validate.notNull(clazz,describer);
        DESCRIBERS.put(clazz,describer);
    }

    public static void registerDescriber(Class<?> clazz){
        Validate.notNull(clazz);
        registerDescriber(clazz,ReflectVariableDescriber.of(clazz));
    }


    public static ReflectVariableDescriber getDescriber(Class<?> clazz){
        Validate.notNull(clazz);
        return DESCRIBERS.get(clazz);
    }

}
