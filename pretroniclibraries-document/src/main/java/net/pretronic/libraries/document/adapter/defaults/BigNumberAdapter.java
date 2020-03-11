/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.document.adapter.defaults;

import net.pretronic.libraries.document.DocumentRegistry;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.TypeReference;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigNumberAdapter {

    public static class Integer implements DocumentAdapter<BigInteger> {

        @Override
        public BigInteger read(DocumentBase entry, TypeReference<BigInteger> type) {
            if(entry.isPrimitive()) return BigInteger.valueOf(entry.toPrimitive().getAsInt());
            else throw new IllegalArgumentException("Entry is not primitive.");
        }

        @Override
        public DocumentEntry write(String key, BigInteger object) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.intValue());
        }
    }

    public static class Decimal implements DocumentAdapter<BigDecimal> {

        @Override
        public BigDecimal read(DocumentBase entry, TypeReference<BigDecimal> type) {
            if(entry.isPrimitive()) return BigDecimal.valueOf(entry.toPrimitive().getAsDouble());
            else throw new IllegalArgumentException("Entry is not primitive.");
        }

        @Override
        public DocumentEntry write(String key, BigDecimal object) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.doubleValue());
        }
    }

}
