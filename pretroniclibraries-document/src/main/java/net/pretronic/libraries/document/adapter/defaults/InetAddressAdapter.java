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

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressAdapter implements DocumentAdapter<InetAddress> {

    public static InetAddressAdapter INSTANCE = new InetAddressAdapter();

    @Override
    public InetAddress read(DocumentBase entry, TypeReference<InetAddress> type) {
        if(entry.isPrimitive()) {
            try {
                return InetAddress.getByName(entry.toPrimitive().getAsString());
            } catch (UnknownHostException exception) {
                throw new IllegalArgumentException("Invalid ip address format.");
            }
        }
        throw new IllegalArgumentException("Can't convert a object to a inet address.");
    }

    @Override
    public DocumentEntry write(String key, InetAddress object) {
        return DocumentRegistry.getFactory().newPrimitiveEntry(key, object.getHostAddress());
    }
}
