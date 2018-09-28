package net.prematic.libraries.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:10
 *
 */

public class PluginClassloader extends URLClassLoader {

    public static final Set<PluginClassloader> loaders = new CopyOnWriteArraySet<>();
    private final Map<String,Class<?>> classes;

    public PluginClassloader(URL[] urls) {
        super(urls);
        this.classes = new LinkedHashMap<>();
        loaders.add(this);
    }
    public Map<String, Class<?>> getClasses() {
        return this.classes;
    }
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve,true);
    }
    private Class<?> loadClass(String name,boolean resolve, boolean checkother) throws ClassNotFoundException {
        Class result = classes.get(name);
        if(result == null && checkother) for(PluginClassloader loader : loaders) result = loader.getClass(name);
        if(result == null){
            try {
                return super.loadClass(name,resolve);
            }catch(ClassNotFoundException exception){}
        }
        if(result == null) throw new ClassNotFoundException(name);
        return result;
    }
    public Class<?> getClass(String name){
        return this.classes.get(name);
    }
}
