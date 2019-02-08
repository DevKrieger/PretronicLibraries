package net.prematic.libraries.logging.handler;

import net.prematic.libraries.logging.SimplePrematicLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.LogRecord;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public class FileHandler extends LogHandler {

    private String startup;
    private final File file, location;
    private final FileOutputStream output;

    public FileHandler(File loglocation){
        super("PrematicFileLogger");
        loglocation.mkdirs();
        if(!loglocation.isDirectory()) throw new IllegalArgumentException("Path musst be a directory");
        this.location = loglocation;
        this.file = new File(loglocation.getPath(),"latest.log");
        try{
            this.output = new FileOutputStream(this.file);
        }catch (Exception exception){
            throw new RuntimeException("Counl not setup logging filehandler ("+exception.getMessage()+")");
        }
    }
    @Override
    public void onInit(SimplePrematicLogger logger) {
        this.startup = getLogger().getDateFormat().format(System.currentTimeMillis());
    }
    @Override
    public void log(LogRecord record, String formatedmessage) {
        try {
            this.output.write(formatedmessage.getBytes());
        }catch (Exception exception) {}
    }
    @Override
    public void shutdown() {
        try {
            this.output.close();
            File newfile = new File(this.location.getPath(),this.startup+" - "+getLogger().getDateFormat().format(System.currentTimeMillis())+".log");
            newfile.createNewFile();
            this.file.renameTo(newfile);
        }catch (Exception exception){}
    }
}
