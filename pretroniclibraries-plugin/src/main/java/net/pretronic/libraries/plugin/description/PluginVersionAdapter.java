/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 04.04.20, 19:58
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

package net.pretronic.libraries.plugin.description;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.utils.SerialisationUtil;
import net.pretronic.libraries.utility.reflect.TypeReference;

public class PluginVersionAdapter implements DocumentAdapter<PluginVersion> {

    @Override
    public PluginVersion read(DocumentBase base, TypeReference<PluginVersion> type) {
        if(base.isPrimitive()) return PluginVersion.parse(base.toPrimitive().getAsString());
        else if(base.isObject()) return SerialisationUtil.deserialize(base.toDocument().getContext(),base.toDocument(),PluginVersion.class);
        return null;
    }

    @Override
    public DocumentEntry write(String key, PluginVersion object) {
        return Document.factory().newPrimitiveEntry(key,object.getName());
    }
}
