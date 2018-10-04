package net.prematic.libraries.utility;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 16:43
 *
 */

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
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

public class FileUtil {

    private static FileUtil instance = new FileUtil();

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
    public static void deleteFile(File file){
        if(file.exists()) file.delete();
    }
    public static byte[] getFileContent(File file){
        if(!file.exists()) return null;
        try{
            return Files.readAllBytes(file.toPath());
        }catch (Exception exception){throw new RuntimeException(exception);}
    }
    public static String getStringFileContent(File file){
        if(!file.exists()) return "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
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
    public static Set<String> getResourceFiles (String folder) {
        try{
            Set<String> result = new HashSet<>();
            URL url = instance.getClass().getClassLoader().getResource(folder);
            if(url != null && url.getProtocol().equals("file")) return new HashSet<>();
            if(url == null){
                String me = instance.getClass().getName().replace(".", "/")+".class";
                url = instance.getClass().getClassLoader().getResource(me);
            }
            if(url.getProtocol().equals("jar")){
                String path = url.getPath().substring(5, url.getPath().indexOf("!"));
                JarFile jar = new JarFile(URLDecoder.decode(path, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries();
                while(entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if(name.startsWith(folder)) {
                        String entry = name.substring(folder.length());
                        int subdir = entry.indexOf("/");
                        if(subdir >= 0) entry = entry.substring(0, subdir);
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
        InputStreamReader streamreader = new InputStreamReader(stream,StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamreader);
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
                streamreader.close();
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
}
