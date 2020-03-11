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

package net.pretronic.libraries.document.entry;

import net.pretronic.libraries.utility.annonations.Internal;

public interface DocumentAttributes extends DocumentNode {

    DocumentAttributes set(String key, Object value);

    default DocumentAttributes rename(String source, String destination){
        DocumentEntry entry = getEntry(source);
        if(entry != null) entry.setKey(destination);
        return this;
    }

    @Internal
    void addEntry(DocumentEntry entry);

    @Internal
    void removeEntry(DocumentEntry entry);


    DocumentAttributes remove(String key);

    DocumentAttributes clear();


    DocumentAttributes copy();

    default DocumentAttributes add(String key, Object value){
        if(value != null && !contains(key)) set(key, value);
        return this;
    }

}
