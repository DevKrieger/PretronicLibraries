/*
 * (C) Copyright 2021 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.05.21, 14:22
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

package net.pretronic.libraries.document.injection;

import net.pretronic.libraries.utility.reflect.UnsafeInstanceCreator;

public class DefaultInjectionObjectInstanceFactory implements ObjectInstanceFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T newInstance(Class<?> clazz) {
        return (T) UnsafeInstanceCreator.newInstance(clazz);
    }

    @Override
    public void inject(Class<?> clazz) {
        //Unused, default not available
    }

    @Override
    public void inject(Object object) {
        //Unused, default not available
    }
}
