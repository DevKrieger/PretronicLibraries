package net.prematic.libraries.utility;

import com.google.gson.JsonObject;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 24.03.19 17:45
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

@Deprecated
public class Document {

    private JsonObject data;

    public Document(){
        this.data = new JsonObject();
    }
    
    public Document(JsonObject data){
        this.data = data;
    }
    public JsonObject getJsonObject(){
        return this.data;
    }
    public String getString(String key){
        if(this.data.has(key)) return this.data.get(key).getAsString();
        return null;
    }
    
    public int getInt(String key){
        if(this.data.has(key)) return this.data.get(key).getAsInt();
        return 0;
    }
    
    public Long getLong(String key){
        if(this.data.has(key)) return this.data.get(key).getAsLong();
        return 0L;
    }
    
    public Boolean getBoolean(String key){
        if(this.data.has(key)) return this.data.get(key).getAsBoolean();
        return false;
    }
    
    public Document getDocument(String key){
        JsonObject value = this.data.get(key).getAsJsonObject();
        if(value != null) return new Document(value);
        return new Document();
    }
    
    public <T> T getObject(String key,Class<T> classOf){
        if(!this.data.has(key)) return null;
        return GeneralUtil.GSON.fromJson(this.data.get(key),classOf);
    }
    
    public <T> T getObject(String key, Type type) {
        return GeneralUtil.GSON.fromJson(data.get(key), type);
    }
    public boolean contains(String key){
        return this.data.has(key);
    }
    public boolean contains(String... keys){
        for(String key : keys) if(!contains(key)) return false;
        return true;
    }
    public Document append(String key, String value){
        if(value != null) this.data.addProperty(key,value);
        return this;
    }
    public Document append(String key,Boolean value){
        if(value != null) this.data.addProperty(key,value);
        return this;
    }
    public Document append(String key, Number value){
        if(value != null) this.data.addProperty(key,value);
        return this;
    }
    public Document append(String key, Document value){
        if(value != null) return append(key,value.getJsonObject());
        return this;
    }
    public Document append(String key, Object value){
        if(value != null) this.data.add(key, GeneralUtil.GSON.toJsonTree(value));
        return this;
    }
    public Document appendDefault(String key, String value){
        if(contains(key) || value == null) return this;
        this.data.addProperty(key,value);
        return this;
    }
    public Document appendDefault(String key,Boolean value){
        if(contains(key) || value == null) return this;
        this.data.addProperty(key,value);
        return this;
    }
    public Document appendDefault(String key, Number value){
        if(contains(key) || value == null) return this;
        this.data.addProperty(key,value);
        return this;
    }
    public Document appendDefault(String key, Document value){
        if(contains(key) || value == null) return this;
        this.data.add(key,value.getJsonObject());
        return this;
    }
    public Document appendDefault(String key, Object value){
        if(contains(key) || value == null) return this;
        this.data.add(key, GeneralUtil.GSON.toJsonTree(value));
        return this;
    }
    public Document remove(String key){
        this.data.remove(key);
        return this;
    }
    public String toJson(){
        return data.toString();
    }
    public void setData(JsonObject data){
        this.data = data;
    }
    public void saveData(File file){
        try{
            saveDataSave(file);
        }catch (Exception exception) {}
    }

    public void saveDataSave(File file) throws Exception{
        file.createNewFile();
        OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), "UTF-8");
        GeneralUtil.GSON.toJson(this.data,writer);
        writer.close();
    }

    public static Document loadData(String gsonstring){
        try{
            return loadDataSave(gsonstring);
        }catch (Exception exception) {}
        return new Document();
    }

    public static Document loadDataSave(String gsonstring) throws Exception{
        InputStreamReader reader = new InputStreamReader(new StringBufferInputStream(gsonstring), "UTF-8");
        return new Document(GeneralUtil.PARSER.parse(new BufferedReader(reader)).getAsJsonObject());
    }

    public static Document loadData(File file){
        try{
            return loadDataSave(file);
        }catch (Exception exception) {}
        return new Document();
    }

    public static Document loadDataSave(File file) throws Exception{
        InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), "UTF-8");
        BufferedReader bufferedreader = new BufferedReader(reader);
        JsonObject data = GeneralUtil.PARSER.parse(bufferedreader).getAsJsonObject();
        return new Document(data);
    }

    @Override
    public String toString() {
        return toJson();
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Document)) return false;
        return ((Document)obj).toJson().equals(toJson());
    }
}