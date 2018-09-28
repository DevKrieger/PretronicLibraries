package net.prematic.libraries.logging.handler;

import net.prematic.libraries.logging.PrematicLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.LogRecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
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
    public void onInit(PrematicLogger logger) {
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
