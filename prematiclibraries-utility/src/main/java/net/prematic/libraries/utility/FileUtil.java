package net.prematic.libraries.utility;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

//Todo update file utils
public final class FileUtil {

    public static void deleteDirectory(String path){
        deleteDirectory(new File(path));
    }
    public static void deleteDirectory(File file){
        if(file.exists()){
            File[] files = file.listFiles();
            if(files == null) return;
            for(File entries : files){
                if(entries.isDirectory()) deleteDirectory(entries.getAbsolutePath());
                else entries.delete();
            }
        }
        file.delete();
    }
    public static byte[] getFileContent(File file){
        if(!file.exists()) return null;
        try{
            return Files.readAllBytes(file.toPath());
        }catch (Exception exception){throw new RuntimeException(exception);}
    }

    public static String getStringFileContent(File file){
        return getStringFileContent(file,StandardCharsets.UTF_8);
    }

    public static String getStringFileContent(File file, Charset charset){
        if(!file.exists()) return "";
        try{
            BufferedReader reader = new BufferedReader(new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file),charset)));
            StringBuilder content = new StringBuilder();
            String line = reader.readLine();
            while(line != null){
                content.append(line);
                content.append(System.getProperty("line.separator"));
                line = reader.readLine();
            }
            return content.toString();
        }catch (Exception exception){ throw new RuntimeException(exception); }
    }
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
        }catch (Exception exception){throw new RuntimeException(exception.getMessage());}
    }

    public static String getResourceFileContent(String name) {
        return getResourceFileContent(name,true);
    }

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
            throw new RuntimeException(exception);
        }finally {
            try{
                stream.close();
                streamReader.close();
                reader.close();
            }catch (Exception exception){}
        }
        return result.toString();
    }
    public static void copyResourceFileToDirectory(String resourcefile, File dest) {
        InputStream localInputStream = FileUtil.class.getClassLoader().getResourceAsStream(resourcefile);
        try {
            Files.copy(localInputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception exception) {throw new RuntimeException(exception);}
    }
    public static void copyFileToDirectory(File file, File dest) {
        if(dest == null || file == null) return;
        if(!file.exists()) return;
        if(!dest.exists()) dest.mkdirs();
        File n = new File(dest.getAbsolutePath()+"/"+file.getName());
        try{
            Files.copy(file.toPath(),n.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception exception){throw new RuntimeException(exception);}
    }
    public static void copyFilesToDirectory(File from, File dest) {
        if(dest == null || from == null) return;
        if(!dest.exists()) dest.mkdirs();
        if(!from.exists()) return;
        File[] files = from.listFiles();
        if(files == null) return;
        for(File file : files) {
            if(file == null) continue;
            if(file.isDirectory()) copyFilesToDirectory(file, new File(dest.getAbsolutePath()+"/"+file.getName()));
            else{
                File n = new File(dest.getAbsolutePath()+"/"+file.getName());
                try{
                    Files.copy(file.toPath(),n.toPath(),StandardCopyOption.REPLACE_EXISTING);
                }catch (Exception exception){throw new RuntimeException(exception);}
            }
        }
    }
    public static void compressToZIP(File src, File dest){
        try{
            OutputStream os = Files.newOutputStream(dest.toPath());
            ZipOutputStream zs = new ZipOutputStream(os);
            Path pp = src.toPath();
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        if(Thread.interrupted()) throw new RuntimeException();
                        if(path.equals(dest.toPath())) return;
                        String sp = path.toAbsolutePath().toString().replace(pp.toAbsolutePath().toString(),"");
                        if(sp.length() > 0) sp = sp.substring(1);
                        ZipEntry zipEntry = new ZipEntry(pp.getFileName()+((sp.length() > 0)?(File.separator + sp):""));
                        try {
                            zs.putNextEntry(zipEntry);
                            zs.write(Files.readAllBytes(path));
                            zs.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }catch (Exception exception){throw new RuntimeException(exception);}
    }
    public static void extractFromZIP(File src, File dest){
        try{
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(src);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                if(Thread.interrupted()) throw new RuntimeException();
                File newFile = new File(dest + File.separator + ze.getName());
                File parent = newFile.getParentFile();
                if(!parent.exists() && !parent.mkdirs()) throw new IllegalStateException("Couldn't create dir: "+parent);
                if(ze.isDirectory()){
                    newFile.mkdir();
                    ze = zis.getNextEntry();
                    continue;
                }
                try(FileOutputStream fos = new FileOutputStream(newFile)){
                    int len;
                    while((len = zis.read(buffer)) > 0) fos.write(buffer, 0, len);
                }
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
        }catch (Exception exception){throw new RuntimeException(exception);}
    }
    public static void downloadFile(String url, File destination){
        try{
            URLConnection connection = null;
            try{
                connection = new java.net.URL(url).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                connection.setRequestProperty("Cookie", "foo=bar");
                connection.connect();
                Files.copy(connection.getInputStream(),destination.toPath());
            }finally {
                if(connection != null) connection.getInputStream().close();
            }
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    public static URL toUrl(File file){
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}
