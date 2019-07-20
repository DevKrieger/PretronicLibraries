/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.07.19 22:35
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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.net.InetSocketAddress;

public class InetSocketAddressAdapter implements DocumentAdapter<InetSocketAddress> {

    @Override
    public InetSocketAddress read(DocumentEntry entry, TypeReference<InetSocketAddress> type) {
        if(entry.isObject()) {
            return new InetSocketAddress(InetAddressAdapter.INSTANCE.read(
                    entry.toDocument().getEntry("address"),new TypeReference<>()),
                    entry.toDocument().getEntry("port").toPrimitive().getAsInt());
        }
        throw new IllegalArgumentException("Can't convert a primitive to a inet socket address.");
    }

    @Override
    public DocumentEntry write(String key, InetSocketAddress object) {
        Document document = DocumentRegistry.getFactory().newDocument(key);
        document.entries().add(InetAddressAdapter.INSTANCE.write("address",object.getAddress()));
        document.entries().add(DocumentRegistry.getFactory().newPrimitiveEntry("port",object.getPort()));
        return document;
    }
}
