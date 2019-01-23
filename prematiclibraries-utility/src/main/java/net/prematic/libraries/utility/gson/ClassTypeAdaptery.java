package net.prematic.libraries.utility.gson;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 12.01.19 18:56
 *
 */

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.prematic.libraries.utility.GeneralUtil;

import java.io.IOException;

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
