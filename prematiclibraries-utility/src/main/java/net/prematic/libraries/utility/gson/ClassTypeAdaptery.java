package net.prematic.libraries.utility.gson;

import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.prematic.libraries.utility.GeneralUtil;

import java.io.IOException;

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

public class ClassTypeAdaptery<T> extends TypeAdapter<T> {

    private Class<? extends T> classNotFoundAdapter;

    public ClassTypeAdaptery() {}

    public ClassTypeAdaptery(Class<? extends T> classNotFoundAdapter) {
        this.classNotFoundAdapter = classNotFoundAdapter;
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        JsonObject object = GeneralUtil.GSON.toJsonTree(value).getAsJsonObject();
        object.addProperty("adapterClassName",value.getClass().getName());
        Streams.write(GeneralUtil.GSON.toJsonTree(value),out);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        in.beginObject();
        String clazz = null;
        while(in.hasNext()) if(in.nextName().equals("adapterClassName")) clazz = in.nextString();
        if(clazz != null){
            in.beginObject();
            try {
                return GeneralUtil.GSON.fromJson(in,Class.forName(clazz));
            }catch (ClassNotFoundException ignore) {}
        }
        if(classNotFoundAdapter != null) return GeneralUtil.GSON.fromJson(in,classNotFoundAdapter);
        throw new IllegalArgumentException("Class for this adapter not found.");
    }
}
