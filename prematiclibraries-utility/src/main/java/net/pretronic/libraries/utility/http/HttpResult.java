/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.utility.http;


import net.pretronic.libraries.utility.io.FileUtil;
import net.pretronic.libraries.utility.io.IORuntimeException;
import net.pretronic.libraries.utility.io.InputStreamReadable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpResult {

    private int code;
    private Map<String,List<String>> properties;
    private InputStream inputStream;

    public HttpResult(int code, Map<String,List<String>> properties, InputStream inputStream) {
        this.code = code;
        this.properties = properties;
        this.inputStream = inputStream;
    }

    public int getCode(){
        return this.code;
    }

    public Map<String,List<String>> getProperties() {
        return properties;
    }

    public List<String> getRawProperty(String key){
        return this.properties.get(key);
    }

    public String getProperty(String key){
        return this.properties.get(key).get(0);
    }

    public String getRawCharset(){
        String raw = getProperty(HttpClient.PROPERTY_CONTENT_TYPE);
        if(raw != null){
            int index = raw.lastIndexOf('=');
            if(index != -1) return raw.substring(index+1,raw.length()-1);
        }
        return null;
    }

    public Charset getCharset(){
        try{
            String raw = getRawCharset();
            return raw != null?Charset.forName(getRawCharset()):null;
        }catch (UnsupportedCharsetException ignored){
            return null;
        }
    }

    public String getContentType(){
        String raw = getProperty(HttpClient.PROPERTY_CONTENT_TYPE);
        if(raw != null){
            int index = raw.indexOf(';');
            if(index != -1) return raw.substring(1,index);
        }
        return null;
    }

    public List<HttpCookie> getCookies(){
        List<String> cookies = properties.get(HttpClient.PROPERTY_COOKIE_SET);
        ArrayList<HttpCookie> result = new ArrayList<>();
        if(cookies != null){
            for(String cookie : cookies) result.addAll(HttpCookie.parse(cookie));
        }
        return result;
    }

    public InputStream getInputStream(){
        return this.inputStream;
    }

    public <R> R getContent(InputStreamReadable<R> readable){
        try {
            Charset charset = getCharset();
            return charset!=null?readable.read(inputStream,charset):readable.read(inputStream);
        }finally {
            close();
        }
    }

    public String getContent(){
        try {
            Charset charset = getCharset();
            return charset!=null?FileUtil.readContent(inputStream,charset):FileUtil.readContent(inputStream);
        }finally {
            close();
        }
    }

    public void save(File location){
        try {
            Files.copy(this.inputStream,location.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
        close();
    }

    public void close(){
        try {
            this.inputStream.close();
        } catch (IOException ignored) {}
    }
}
