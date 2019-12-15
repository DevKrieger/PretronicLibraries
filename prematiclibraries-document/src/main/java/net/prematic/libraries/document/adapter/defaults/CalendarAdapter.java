/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 16:47
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

import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.entry.DocumentBase;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarAdapter implements DocumentAdapter<Calendar> {

    @Override
    public Calendar read(DocumentBase entry, TypeReference<Calendar> type) {
        if(entry.isObject()){
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(entry.toDocument().getString("timeZone")));
            calendar.setTimeInMillis(entry.toDocument().getLong("time"));
            return calendar;
        }
        throw new IllegalArgumentException("Can't convert entry into a calendar object.");
    }

    @Override
    public DocumentEntry write(String key, Calendar object) {
        Document document = DocumentRegistry.getFactory().newDocument(key);
        document.entries().add(DocumentRegistry.getFactory().newPrimitiveEntry("time",object.getTimeInMillis()));
        document.entries().add(DocumentRegistry.getFactory().newPrimitiveEntry("timeZone",object.getTimeZone().getID()));
        return document;
    }
}
