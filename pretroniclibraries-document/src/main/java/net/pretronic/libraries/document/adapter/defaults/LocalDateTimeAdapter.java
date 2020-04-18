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
import net.pretronic.libraries.utility.GeneralUtil;
import net.pretronic.libraries.utility.reflect.TypeReference;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements DocumentAdapter<LocalDateTime> {//Created LocalDateTimeAdapter for document library

    public static DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime read(DocumentBase entry, TypeReference<LocalDateTime> type) {
        if(entry.isPrimitive()){
            if(entry.toPrimitive().getAsObject() instanceof Number){
                return LocalDateTime.from(Instant.ofEpochMilli(entry.toPrimitive().getAsLong()));
            }
            String input = entry.toPrimitive().getAsString();
            if(GeneralUtil.isNaturalNumber(input)){
                return LocalDateTime.from(Instant.ofEpochMilli(entry.toPrimitive().getAsLong()));
            }else if(input.contains("T")){
                if(input.contains("+")){
                    return LocalDateTime.parse(input,DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                }else{
                    return LocalDateTime.parse(input,DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            }else if(input.contains("-")){
                if(input.contains("+")){
                    return LocalDateTime.parse(input,DateTimeFormatter.ISO_OFFSET_DATE);
                }else{
                    return LocalDateTime.parse(input,DateTimeFormatter.ISO_LOCAL_DATE);
                }
            }
        }
        throw new IllegalArgumentException("Can't convert a object to a date.");
    }

    @Override
    public DocumentEntry write(String key, LocalDateTime object) {
        return DocumentRegistry.getFactory().newPrimitiveEntry(key,object.format(DEFAULT_FORMATTER));
    }
}
