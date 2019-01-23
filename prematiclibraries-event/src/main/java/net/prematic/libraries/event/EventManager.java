package net.prematic.libraries.event;

import net.prematic.libraries.utility.owner.ObjectOwner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public class EventManager {

    private static ExecutorService EXECUTOR;
    private final Map<Class<?>,Collection<MethodHolder>> methods;

    public EventManager() {
        this.methods = new LinkedHashMap<>();
    }
    public Collection<MethodHolder> getHolders(ObjectOwner owner){
        Collection<MethodHolder> holders = new LinkedList<>();
        for(Collection<MethodHolder> list : this.methods.values())
            for(MethodHolder holder : new LinkedList<>(list)) if(holder.getOwner().equals(owner)) holders.add(holder);
        return holders;
    }
    public void registerListener(ObjectOwner owner, Object listener){
        for(Method method : listener.getClass().getDeclaredMethods()){
            try{
                EventHandler handler = method.getAnnotation(EventHandler.class);
                if(handler != null){
                    if(method.getParameterTypes().length == 1){
                        if(!this.methods.containsKey(method.getParameterTypes()[0]))
                            this.methods.put(method.getParameterTypes()[0],new LinkedList<>());
                        this.methods.get(method.getParameterTypes()[0]).add(new MethodHolder(handler.priority(),owner,listener,method));
                    }
                }
            }catch (Exception ignored){}
        }
    }
    public void unregisterListener(Object listener){
        for(Collection<MethodHolder> list : this.methods.values())
            for(MethodHolder holder : new LinkedList<>(list)) if(holder.getListener().equals(listener)) list.remove(holder);
    }
    public void unregisterListener(ObjectOwner owner){
        for(Collection<MethodHolder> list : this.methods.values())
            for(MethodHolder holder : new LinkedList<>(list)) if(holder.getOwner().equals(owner)) list.remove(holder);
    }
    public void callEventAsync(Object event){
        EXECUTOR.execute(() -> callEvent(event));
    }
    public void callEvent(Object event){
        try{
            Collection<MethodHolder> methods = this.methods.get(event.getClass());
            if(methods != null){
                TreeMap<Integer,MethodHolder> sorter = new TreeMap<>();
                for(MethodHolder method : methods) sorter.put(method.getPriority(),method);
                for(MethodHolder method : sorter.values()) method.invoke(event);
            }
        }catch (Exception ignored){}
    }
}
