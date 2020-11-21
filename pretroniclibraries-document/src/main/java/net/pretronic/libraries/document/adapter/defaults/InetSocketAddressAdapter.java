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
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class InetSocketAddressAdapter implements DocumentAdapter<InetSocketAddress> {

    @Override
    public InetSocketAddress read(DocumentBase entry, TypeReference<InetSocketAddress> type) {
        if(entry.isObject()) {
            return new InetSocketAddress(InetAddressAdapter.INSTANCE.read(
                    entry.toDocument().getEntry("address"),new TypeReference<>()),
                    entry.toDocument().getEntry("port").toPrimitive().getAsInt());
        } else if(entry.isPrimitive()){
            String[] split = entry.toPrimitive().getAsString().split(":");
            if(split.length == 2){
                try {
                    return new InetSocketAddress(InetAddress.getByName(split[0]),Integer.parseInt(split[1]));
                } catch (UnknownHostException ignored) {}
            } else if(split.length == 1) {
                try {
                    return new InetSocketAddress(InetAddress.getByName(split[0]), 0);
                } catch (UnknownHostException ignored) {}
            }
        }
        throw new IllegalArgumentException("Can't convert a primitive to a inet socket address.");
    }

    @Override
    public DocumentEntry write(String key, InetSocketAddress object) {
        String address = object.getAddress().getHostAddress()+":"+object.getPort();
        return DocumentRegistry.getFactory().newPrimitiveEntry(key,address);
    }
}
