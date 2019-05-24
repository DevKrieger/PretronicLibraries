/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.03.19 17:00
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Primitives {

    public static final Set<Class<?>> PRIMITIVES = new HashSet<>();

    static{
        PRIMITIVES.add(byte.class);
        PRIMITIVES.add(Byte.class);
        PRIMITIVES.add(boolean.class);
        PRIMITIVES.add(Boolean.class);
        PRIMITIVES.add(short.class);
        PRIMITIVES.add(Short.class);
        PRIMITIVES.add(float.class);
        PRIMITIVES.add(Float.class);
        PRIMITIVES.add(double.class);
        PRIMITIVES.add(Double.class);
        PRIMITIVES.add(int.class);
        PRIMITIVES.add(Integer.class);
        PRIMITIVES.add(long.class);
        PRIMITIVES.add(Long.class);
        PRIMITIVES.add(char.class);
        PRIMITIVES.add(Character.class);
        PRIMITIVES.add(String.class);
    }

    public static boolean isPrimitive(Object object){
        return isPrimitive(object.getClass());
    }

    public static boolean isPrimitive(Class<?> clazz){
        return PRIMITIVES.contains(clazz);
    }

}
