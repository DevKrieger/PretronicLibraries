/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 12.06.19 08:37
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
import net.prematic.libraries.utility.reflect.TypeReference;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicAdapter {

    public static class Integer implements DocumentAdapter<AtomicInteger> {

        @Override
        public AtomicInteger read(DocumentEntry entry, TypeReference<AtomicInteger> type) {
            if(entry.isPrimitive()) return new AtomicInteger(entry.toPrimitive().getAsInt());
            else throw new IllegalArgumentException("Entry is not primitive.");
        }

        @Override
        public DocumentEntry write(String key, AtomicInteger object) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.intValue());
        }
    }

    public static class Long implements DocumentAdapter<AtomicLong> {

        @Override
        public AtomicLong read(DocumentEntry entry, TypeReference<AtomicLong> type) {
            if(entry.isPrimitive()) return new AtomicLong(entry.toPrimitive().getAsLong());
            else throw new IllegalArgumentException("Entry is not primitive.");
        }

        @Override
        public DocumentEntry write(String key, AtomicLong object) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.longValue());
        }
    }

    public static class Boolean implements DocumentAdapter<AtomicBoolean> {

        @Override
        public AtomicBoolean read(DocumentEntry entry, TypeReference<AtomicBoolean> type) {
            if(entry.isPrimitive()) return new AtomicBoolean(entry.toPrimitive().getAsBoolean());
            else throw new IllegalArgumentException("Entry is not primitive.");
        }

        @Override
        public DocumentEntry write(String key, AtomicBoolean object) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.get());
        }
    }
}
