/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 15.06.19 23:06
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

package net.prematic.libraries.copy.adapter;

import net.prematic.libraries.copy.CopyUtil;
import net.prematic.libraries.utility.reflect.UnsafeInstanceCreator;

import java.util.Collection;

public class CollectionAdapter implements CopyAdapter<Collection> {

    @Override
    public Collection copy(Collection object) {
        Collection collection = UnsafeInstanceCreator.newInstance(object.getClass());
        for(Object value : object) collection.add(CopyUtil.copyDeep(value));
        return collection;
    }
}
