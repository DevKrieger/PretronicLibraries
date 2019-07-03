/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 14:31
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

package net.prematic.libraries.document.adapter.defaults;

import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.simple.SimplePrimitiveEntry;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.util.UUID;

public class UUIDAdapter implements DocumentAdapter<UUID> {


    /*
    @Override
    public TypeReference<UUID> getTypeReference() {
        return new TypeReference<UUID>(){};
    }
     */


    @Override
    public UUID read(DocumentEntry entry, TypeReference<UUID> reference) {
        if(entry.isPrimitive()) return UUID.fromString(entry.toPrimitive().getAsString());
        throw new IllegalArgumentException("It is not possible to convert this entry into a uuid.");
    }

    @Override
    public DocumentEntry write(String key, UUID object) {
        return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.toString());
    }
}
