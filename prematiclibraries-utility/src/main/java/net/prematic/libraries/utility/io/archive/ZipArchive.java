/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.05.19 19:34
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

package net.prematic.libraries.utility.io.archive;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This simple zip library helps compressing and extracting zip files.
 *
 * <p>It is possible to add, remove, extract and compress existing and new zip files.</p>
 */
public class ZipArchive {

    /**
     * This is for checking if a zip entry should be deleted.
     */
    private static final byte[] REMOVE = new byte[]{-1,-1,-1,-1,-1,-1,-1,-1};

    private final File file;
    private final Map<String,byte[]> content;
    private boolean clear;

    public ZipArchive(File file) {
        this.file = file;
        this.content = new LinkedHashMap<>();
    }

    /**
     * Get byte content by a zip entry path.
     *
     * <p>Example: test/text.txt</p>
     *
     * @param path The zip entry path
     * @return The byte content from the zip entry
     */
    public byte[] get(String path){
        checkExists();
        try {
            InputStream fileInput = Files.newInputStream(this.file.toPath());
            ZipInputStream input = new ZipInputStream(fileInput);
            byte[] buffer = new byte[1024];

            ZipEntry entry = input.getNextEntry();
            while(entry != null){//Todo update
                if(entry.getName().equals(path)) {
                    ByteBuf buf = Unpooled.buffer();
                    int length;
                    while ((length = input.read(buffer)) > 0) buf.writeBytes(buffer, 0,length);

                    byte[] result = new byte[buf.readableBytes()];
                    buf.readBytes(result);
                    return result;
                }
                entry = input.getNextEntry();
            }
            return new byte[0];
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Get a string content from zip entry path with the standard charset.
     *
     * @param path The zip entry path
     * @return The string content from the zip entry.
     */
    public String getString(String path){
        return new String(get(path));
    }

    /**
     * Get a string content from zip entry path.
     *
     * @param charset The charset for decoding the content
     * @param path The zip entry path
     * @return The string content from the zip entry.
     */
    public String getString(String path,Charset charset){
        return new String(get(path),charset);
    }

    /**
     * Add a file to the zip entry.
     *
     * <p>Do not forget to compress the zip archive after adding a new file.</p>
     *
     * @param file The file which should be added
     */
    public void add(File file){
        add(file.getName(),file);
    }

    /**
     * Add a file with a custom zip entry path.
     *
     * <p>Do not forget to compress the zip archive after adding a new file.</p>
     *
     * @param path The zip entry path
     * @param file The file which should be added
     */
    public void add(String path, File file){
        if(!file.exists() || !file.isFile()) return;
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] content = new byte[input.available()];
            input.read(content,0, content.length);
            this.content.put(path,content);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Add string content to a zip entry path with the standard charset.
     *
     * @param path The zip entry path
     * @param content The string content
     */
    public void add(String path, String content){
        add(path, content, Charset.defaultCharset());
    }

    /**
     * Add string content to a zip entry path.
     *
     * @param charset The charset for encoding the string
     * @param path The zip entry path
     * @param content The string content
     */
    public void add(String path, String content, Charset charset){
        this.content.put(path,content.getBytes(charset));
    }

    /**
     * Add byte content to a zip entry path.
     *
     * @param path The zip entry path
     * @param content The string content
     */
    public void add(String path,byte[] content){
        this.content.put(path,content);
    }

    /**
     * Remove a zip entry path.
     *
     * @param path The zip entry path
     */
    public void remove(String path){
        this.content.put(path,REMOVE);
    }

    /**
     * Clear a zip entry.
     */
    public void clear(){
        this.clear = true;
        this.content.clear();
    }

    /**
     * Compress the zip file.
     */
    public void compress(){
        try {
            OutputStream fileOutput;
            ZipOutputStream output;

            if(!clear && this.file.exists()){
                Path temp = Files.createTempFile(file.getName(),null);
                Files.copy(this.file.toPath(),temp, StandardCopyOption.REPLACE_EXISTING);

                InputStream fileInput = Files.newInputStream(temp);
                ZipInputStream input = new ZipInputStream(fileInput);

                fileOutput = Files.newOutputStream(this.file.toPath());
                output = new ZipOutputStream(fileOutput);

                byte[] buffer = new byte[1024];

                ZipEntry entry = input.getNextEntry();
                while(entry != null){
                    byte[] content = this.content.get(entry.getName());
                    if(content != null){
                        if(content != REMOVE){
                            output.putNextEntry(new ZipEntry(entry.getName()));
                            output.write(content,0,content.length);
                            output.closeEntry();
                        }
                        this.content.remove(entry.getName());
                    }else{
                        output.putNextEntry(new ZipEntry(entry.getName()));
                        int length;
                        while ((length = input.read(buffer)) > 0) output.write(buffer, 0,length);
                    }
                    entry = input.getNextEntry();
                }

                input.close();
                fileInput.close();
                temp.toFile().delete();
            }else{
                fileOutput = Files.newOutputStream(this.file.toPath());
                output = new ZipOutputStream(fileOutput);
            }

            for(Map.Entry<String,byte[]> mapEntry : this.content.entrySet()){
                if(mapEntry.getValue() == REMOVE) continue;
                output.putNextEntry(new ZipEntry(mapEntry.getKey()));
                output.write(mapEntry.getValue(),0,mapEntry.getValue().length);
                output.closeEntry();
            }

            output.close();
            fileOutput.close();
            this.clear = false;
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Extract the whole content from existing zip archive.
     * @param directory The output directory
     */
    public void extract(File directory){
        checkExists();
        try {
            if(!directory.exists()) directory.mkdirs();
            InputStream fileInput = Files.newInputStream(this.file.toPath());
            ZipInputStream input = new ZipInputStream(fileInput);
            byte[] buffer = new byte[1024];

            ZipEntry entry = input.getNextEntry();
            while(entry != null){
                extractEntry(directory, input, buffer, entry);
                entry = input.getNextEntry();
            }
            input.closeEntry();
            input.close();
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extract a directory or file from a existing zip entry.
     *
     * <p>If the path ends with "/", it will extract a directory.</p>
     *
     * @param path The zip entry path
     * @param destination The output directory or file.
     */
    public void extract(String path, File destination){
        if(path.endsWith("/")) extractDirectory(path,destination);
        else extractFile(path,destination);
    }

    /**
     * Extract a file (zip entry) from a existing zip archive.
     *
     * @param path The zip entry path
     * @param destination The output file
     */
    public void extractFile(String path, File destination){
        checkExists();
        try{
            if(path.endsWith("/")) throw new IORuntimeException(path+" is a directory.");
            if(!destination.exists()){
                destination.getParentFile().mkdirs();
                destination.createNewFile();
            }else if(destination.isDirectory()) throw new IORuntimeException(destination+" is a directory.");

            InputStream fileInput = Files.newInputStream(this.file.toPath());
            ZipInputStream input = new ZipInputStream(fileInput);
            byte[] buffer = new byte[1024];

            ZipEntry entry = input.getNextEntry();

            while(entry != null){
                if(entry.getName().equals(path)){
                    try(OutputStream output = Files.newOutputStream(destination.toPath())){
                        int length;
                        while ((length = input.read(buffer)) > 0) output.write(buffer, 0,length);
                    }
                    input.closeEntry();
                    input.close();
                    fileInput.close();
                    return;
                }
                entry = input.getNextEntry();
            }
            input.closeEntry();
            input.close();
            fileInput.close();

        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Extract a directory from a zip archive.
     *
     * @param path The zip entry path
     * @param destination The output directory.
     */
    public void extractDirectory(String path,File destination){
        checkExists();
        try{
            if(!path.endsWith("/")) throw new IORuntimeException(path+" is not a directory.");
            if(!destination.exists()) destination.mkdirs();
            else if(!destination.isDirectory()) throw new IORuntimeException(destination+" is not a directory.");

            InputStream fileInput = Files.newInputStream(this.file.toPath());
            ZipInputStream input = new ZipInputStream(fileInput);
            byte[] buffer = new byte[1024];

            ZipEntry entry = input.getNextEntry();
            while(entry != null){
                if(entry.getName().startsWith(path)) extractEntry(destination, input, buffer, entry);
                entry = input.getNextEntry();
            }
            input.closeEntry();
            input.close();
            fileInput.close();

        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    private void checkExists(){
        if(!this.file.exists() || !file.isFile()) throw new IORuntimeException("Zip archive does not exists.");
    }

    private void extractEntry(File destination, ZipInputStream input, byte[] buffer, ZipEntry entry) throws IOException {
        File outFile = new File(destination+File.separator+entry.getName());
        File parent = outFile.getParentFile();
        if(!parent.exists()) parent.mkdirs();
        if(entry.isDirectory()) outFile.mkdir();
        else{
            try(OutputStream output = Files.newOutputStream(outFile.toPath())){
                int length;
                while ((length = input.read(buffer)) > 0) output.write(buffer, 0,length);
            }
        }
    }

}
