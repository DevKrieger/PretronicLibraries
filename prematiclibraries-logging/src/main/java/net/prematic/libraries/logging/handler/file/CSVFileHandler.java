/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.04.19 12:57
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

package net.prematic.libraries.logging.handler.file;

import net.prematic.libraries.logging.LogRecord;
import net.prematic.libraries.logging.format.FormatHelper;
import net.prematic.libraries.logging.level.LogLevel;

import java.io.File;

/**
 *
 * This file log handler creates csv files for simple importing in other programmes.
 *
 */
public class CSVFileHandler extends FileHandler {

    private final char separator;

    public CSVFileHandler(File location) {
        this(location,';');
    }

    public CSVFileHandler(File location, char separator) {
        this(location,null,separator);
    }

    public CSVFileHandler(File location, LogLevel level, char separator) {
        this(location, level,"csv",separator);
    }

    public CSVFileHandler(File location, LogLevel level, String fileEnding, char separator) {
        super(location, level, fileEnding);
        this.separator = separator;

        write("TimeStamp"+separator+"LogLevel"+separator+"DebugLevel"+separator+"ThreadId"+separator
                +"ThreadName"+separator+"MessageId"+separator+"Service"+separator+"Message"+separator+"StackTrace"+separator+System.lineSeparator());
    }

    @Override
    public String getName() {
        return "PrematicCSVFileLogHandler";
    }

    @Override
    public void handleLog(LogRecord record, String formattedMessage) {
        try{
            if(level != null && !level.canLog(level)) return;
            StringBuilder csvFormatter = new StringBuilder()
                    .append(record.getTimeStamp())
                    .append(separator)
                    .append(record.getLogLevel()!=null?record.getLogLevel().getName():"null")
                    .append(separator)
                    .append(record.getDebugLevel()!=null?record.getDebugLevel().getName():"null")
                    .append(separator)
                    .append(record.getThread()!=null?record.getThread().getId():"-1")
                    .append(separator)
                    .append(record.getThread()!=null?record.getThread().getName():"Unknown")
                    .append(separator);

            if(record.getInfo() != null){
                csvFormatter.append(record.getInfo().getId()).append(separator)
                        .append(record.getInfo().getService()!=null?record.getInfo().getService().getName():"Unknown").append(separator);
            }else csvFormatter.append("null").append(separator).append("null").append(separator);

            csvFormatter.append(record.getMessage()!=null?record.getMessage():(record.getThrown()!=null?record.getThrown().getMessage():"null"))
                    .append(separator);

            if(record.getThrown() != null) FormatHelper.buildStackTrace(csvFormatter,record.getThread(),record.getThrown(),"","</>");

            csvFormatter.append(System.lineSeparator());

            write(csvFormatter.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
