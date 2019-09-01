package net.prematic.libraries.utility.io;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

public final class FileUtil {


    //Filesystem utils

    public static OutputStream newFileOutputStream(File location){
        try {
            return new FileOutputStream(location);
        } catch (FileNotFoundException exception) {
            throw new IORuntimeException(exception);
        }
    }

    public static InputStream newFileInputStream(File location){
        try {
            return new FileInputStream(location);
        } catch (FileNotFoundException exception) {
            throw new IORuntimeException(exception);
        }
    }

    /**
     *  Read the content from a file with the standard charset.
     *
     * @param file The source file
     * @return The file content
     */
    public static String readContent(File file){
       return readContent(file,Charset.defaultCharset());
    }

    /**
     * Read the content from a file with a specified charset.
     *
     * @param file The file for reading
     * @param charset The charset of the file
     * @return The file cotnent
     */
    public static String readContent(File file,Charset charset){
        try {
            return readContent(Files.newInputStream(file.toPath()),charset);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Read an input stream with the default charset.
     *
     * @param stream The input stream for reading
     * @return The content as sting.
     */
    public static String readContent(InputStream stream){
        return readContent(stream,Charset.defaultCharset());
    }

    /**
     * Read an inout with a charset name.
     *
     * @param stream The input stream for reading
     * @param charsetName The name of the stream charset.
     * @return The content as sting.
     */
    public static String readContent(InputStream stream,String charsetName){
        return readContent(stream,Charset.forName(charsetName));
    }

    /**
     * Read an input stream with a charset.
     *
     * @param stream The input stream for reading
     * @param charset The charset of the stream
     * @return The content as sting.
     */
    public static String readContent(InputStream stream,Charset charset){
        if(!Charset.isSupported(charset.name())) throw new UnsupportedOperationException("Charset "+charset.name()+" is not supported.");
        try{
            byte[] content = new byte[stream.available()];
            stream.read(content);
            return new String(content,charset);
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    public static List<File> getFilesHierarchically(File source){
        List<File> files = new ArrayList<>();
        processFilesHierarchically(source, files::add);
        return files;
    }

    public static void processFilesHierarchically(File source, Consumer<File> processor){
        File[] files = source.listFiles();
        if(files != null){
            for(File file : files) {
                if(file.exists()){
                    if(file.isDirectory()) processFilesHierarchically(file,processor);
                    else processor.accept(file);
                }
            }
        }
    }

    /**
     * Delete a directory and all files in this directory.
     *
     * @param path The directory path to delete
     */
    public static void deleteDirectory(String path){
        deleteDirectory(new File(path));
    }

    /**
     * Delete a directory and all files in this directory.
     *
     * @param directory The directory to delete
     */
    public static void deleteDirectory(File directory){
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(files != null){
                for(File entries : files){
                    if(entries.isDirectory()) deleteDirectory(entries);
                    else entries.delete();
                }
            }
            directory.delete();
        }
    }

    /**
     * Copy a file to destination.
     *
     * @param sourceFile The source file
     * @param destinationFile The destination file
     */
    public static void copyFile(File sourceFile,File destinationFile){
        if(sourceFile == null || destinationFile == null) throw new NullPointerException("source file or destination file is null.");

        if(!sourceFile.exists()) throw new IORuntimeException("Source file does not exist.");
        else if(!sourceFile.isFile()) throw new IORuntimeException("Source file is not a file.");

        try{
            if(!destinationFile.exists()) destinationFile.createNewFile();
            else if(!destinationFile.isFile()) throw  new IORuntimeException("Destination is not a file.");

            Files.copy(sourceFile.toPath(),destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Copy a single file into a directory.
     *
     * @param sourceFile The source file
     * @param destinationDirectory The destination directory
     */
    public static void copyFileToDirectory(File sourceFile, File destinationDirectory) {
        if(destinationDirectory == null || sourceFile == null) throw new NullPointerException("source file or destination directory is null.");

        if(!sourceFile.exists()) throw new IORuntimeException("Source file does not exist.");
        else if(!sourceFile.isFile()) throw new IORuntimeException("Source file is not a file.");

        if(!destinationDirectory.exists()) destinationDirectory.mkdirs();
        else if(!destinationDirectory.isDirectory()) throw  new IORuntimeException("Destination is not a directory.");

        try{
            File destinationFile = new File(destinationDirectory,sourceFile.getName());
            Files.copy(sourceFile.toPath(),destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    /**
     * Copy a directory into another directory.
     *
     * @param sourceDirectory The source directory
     * @param destinationDirectory The destination directory.
     */
    public static void copyDirectoryToDirectory(File sourceDirectory, File destinationDirectory) {
        copyFilesFromDirectoryToDirectory(sourceDirectory,new File(destinationDirectory,sourceDirectory.getName()));
    }

    /**
     * Copy files in a directory in another directory.
     *
     * @param sourceDirectory The source directory
     * @param destinationDirectory The destination directory.
     */
    public static void copyFilesFromDirectoryToDirectory(File sourceDirectory, File destinationDirectory) {
        if(destinationDirectory == null || sourceDirectory == null) throw new NullPointerException("source directory or destination directory is null.");

        if(!sourceDirectory.exists()) throw new IORuntimeException("Source directory does not exist.");
        else if(!sourceDirectory.isDirectory()) throw new IORuntimeException("Source directory is not a directory.");

        if(!destinationDirectory.exists()) destinationDirectory.mkdirs();
        else if(!destinationDirectory.isDirectory()) throw new IORuntimeException("destination is not a directory");

        File[] childFiles = sourceDirectory.listFiles();
        if(childFiles != null){
            for(File children : childFiles) {
                if(children.isDirectory()) copyFilesFromDirectoryToDirectory(children, new File(destinationDirectory,children.getName()));
                else{
                    try{
                        File destinationFile = new File(destinationDirectory,children.getName());
                        Files.copy(children.toPath(),destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
                    }catch (IOException exception) {throw new IORuntimeException(exception);}
                }
            }
        }
    }


    //Resource file utils Todo update resource file utils

    @Deprecated
    public static Set<String> getResourceFiles(String folder) {
        try{
            Set<String> result = new HashSet<>();
            URL url = FileUtil.class.getClassLoader().getResource(folder);
            if(url != null && url.getProtocol().equals("file")) return new HashSet<>();
            if(url == null){
                String me = FileUtil.class.getName().replace(".", "/")+".class";
                url = FileUtil.class.getClassLoader().getResource(me);
            }
            if(url != null && url.getProtocol() != null && url.getProtocol().equals("jar")){
                String path = url.getPath().substring(5, url.getPath().indexOf("!"));
                JarFile jar = new JarFile(URLDecoder.decode(path, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries();
                while(entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if(name.startsWith(folder)) {
                        String entry = name.substring(folder.length());
                        int subDir = entry.indexOf("/");
                        if(subDir >= 0) entry = entry.substring(0, subDir);
                        result.add(entry);
                    }
                }
            }
            return result;
        }catch (IOException exception) {throw new IORuntimeException(exception);}
    }

    @Deprecated
    public static String getResourceFileContent(String name) {
        return getResourceFileContent(name,true);
    }

    @Deprecated
    public static String getResourceFileContent(String name, Boolean separate) {
        StringBuilder result = new StringBuilder();
        InputStream stream =  Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        InputStreamReader streamReader = new InputStreamReader(stream,StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        try {
            for(String line; (line = reader.readLine()) != null;){
                result.append(line);
                if(separate) result.append(System.lineSeparator());
            }
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }finally {
            try{
                stream.close();
                streamReader.close();
                reader.close();
            }catch (Exception exception){}
        }
        return result.toString();
    }

    @Deprecated
    public static void copyResourceFileToDirectory(String resourceFile, File dest) {
        InputStream input = FileUtil.class.getClassLoader().getResourceAsStream(resourceFile);
        if(input == null) throw new IORuntimeException("Resource file not found");

        try {
            Files.copy(input, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException exception) {throw new IORuntimeException(exception);}
    }


    //General utils


    /**
     * Translate a file to a url.
     *
     * @param file The file to translate
     * @return The translated url
     */
    public static URL fileToUrl(File file){
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Could npt translate a file to a url.",exception);
        }
    }

    public static URL newUrl(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Invalid url",exception);
        }
    }
}
