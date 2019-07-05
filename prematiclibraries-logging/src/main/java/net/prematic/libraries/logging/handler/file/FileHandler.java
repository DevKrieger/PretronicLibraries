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
import net.prematic.libraries.logging.handler.LogHandler;
import net.prematic.libraries.logging.level.LogLevel;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

/**
 * This is a simple handler for writing log files.
 */
public class FileHandler implements LogHandler {

    public static SimpleDateFormat LOG_FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    protected final File location, latest;
    protected final LogLevel level;
    protected final String fileEnding;
    protected FileWriter fileWriter;
    protected BufferedWriter writer;
    protected boolean changing;

    public FileHandler(File location){
        this(location,null);
    }

    public FileHandler(File location, LogLevel level){
        this(location,level,"log");
    }

    public FileHandler(File location, LogLevel level, String fileEnding) {
        if(!location.isDirectory() && location.exists()) throw new IllegalArgumentException("Location is not a directory.");
        location.mkdirs();
        this.location = location;
        this.latest = new File(location,"latest."+fileEnding);
        this.level = level;
        this.fileEnding = fileEnding;
        changeFile();
    }

    @Override
    public String getName() {
        return "PrematicFileLogHandler";
    }

    @Override
    public void handleLog(LogRecord record, String formattedMessage) {
        if(level != null && !level.canLog(level)) return;
        write(formattedMessage);
    }

    public void write(String content){
        try{
            while(changing) Thread.sleep(1);
            writer.write(content);
            writer.flush();
        }catch (ArrayIndexOutOfBoundsException exception){
            changeFile();
            try {
                writer.write(content);
                writer.flush();
            } catch (IOException ignored) {}
        }catch (IOException | InterruptedException ignored){}
    }

    private void changeFile(){
        this.changing = true;
        try{
            if(writer != null) writer.close();
            if(fileWriter != null) fileWriter.close();
            if(latest.exists()){
                try{
                    FileReader fileReader = new FileReader(latest);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String info = reader.readLine();
                    long start = Long.valueOf(info.substring(0,info.indexOf("/")));
                    reader.close();
                    fileReader.close();
                    Files.move(latest.toPath(),new File(location,LOG_FILE_DATE_FORMAT.format(start)+"."+fileEnding).toPath());
                }catch (Exception ignored){
                    Files.move(latest.toPath(),new File(location,"unknown-"+LOG_FILE_DATE_FORMAT.format(System.currentTimeMillis())+"."+fileEnding).toPath());
                }
            }
            if(!latest.exists()) latest.createNewFile();

            this.fileWriter = new FileWriter(latest);
            this.writer = new BufferedWriter(fileWriter,32768);
            this.writer.write(System.currentTimeMillis()+"/"+LOG_FILE_DATE_FORMAT.format(System.currentTimeMillis()));
            writer.newLine();
        }catch (Exception exception){
            throw new UnsupportedOperationException("Could not change file",exception);
        }
        changing = false;
    }

    @Override
    public void shutdown(){
        try {
            while(changing) Thread.sleep(1);
            this.writer.close();
            this.fileWriter.close();
        } catch (Exception ignored) {}
    }
}
