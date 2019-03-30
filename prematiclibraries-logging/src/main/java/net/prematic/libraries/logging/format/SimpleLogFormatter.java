/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 27.03.19 12:26
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

package net.prematic.libraries.logging.format;

import net.prematic.libraries.logging.LogRecord;
import net.prematic.libraries.logging.PrematicLogger;

import java.text.SimpleDateFormat;

/**
 * This class implements form the log formatter and is a simple formatter with date, leven and the message.
 *
 * <p>[DateTime] Level: (Service/Id) message</p>
 */
public class SimpleLogFormatter implements LogFormatter {

    private final SimpleDateFormat dateFormat;

    public SimpleLogFormatter() {
        this(new SimpleDateFormat("HH:mm:ss"));
    }

    public SimpleLogFormatter(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String format(PrematicLogger logger, LogRecord record) {
        StringBuilder builder = new StringBuilder();
        if(record.getMessage() != null && !record.getMessage().isEmpty()){
            builder.append('[').append(dateFormat.format(record.getTimeStamp())).append(']').append(' ')
                    .append(record.getLogLevel().getName()).append(':').append(' ');
            if(record.getInfo() != null){
                boolean has = false;
                if(record.getInfo().getService() != null){
                    has = true;
                    builder.append('(').append(record.getInfo().getService().getName());
                }
                if(record.getInfo().getId() > 0){
                    builder.append(has?'/':'(');
                    builder.append(record.getInfo().getId());
                    builder.append(") ");
                }else if(has) builder.append(") ");
            }
            builder.append(record.getMessage()).append(System.lineSeparator());
        }else if(record.getThrown() == null) builder.append(System.lineSeparator());

        if(record.getThrown() != null)
            FormatHelper.buildStackTrace(builder,record.getThread(),record.getThrown(),"["+dateFormat.format(record.getTimeStamp())+"] "+record.getLogLevel().getName()+": ");
        return builder.toString();
    }
}