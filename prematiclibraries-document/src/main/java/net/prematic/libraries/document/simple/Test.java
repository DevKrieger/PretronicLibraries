/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 15.12.19, 16:11
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

package net.prematic.libraries.document.simple;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.prematic.libraries.document.entry.Document;
import net.prematic.libraries.document.type.json.JsonDocumentReader;
import net.prematic.libraries.document.type.json.JsonDocumentWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Test {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        gson();
        start = System.currentTimeMillis()-start;
        System.out.println(start+"ms");
    }

    public static void pretronic(){
        JsonDocumentReader reader = new JsonDocumentReader();
        JsonDocumentWriter writer = new JsonDocumentWriter();

        Document result = reader.read(new File("compare.json"));
        writer.write(new File("output.json"),result);
    }

    public static void gson() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonReader reader = new JsonReader(new FileReader("compare.json"));
        JsonElement result = JsonParser.parseReader(reader);
        gson.toJson(result,new JsonWriter(new FileWriter(new File("output.json"))));
    }

    public static void jackson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.reader().readTree(new FileReader("compare.json"));
        mapper.writer().writeValue(new FileWriter(new File("output.json")),result);
    }


}
