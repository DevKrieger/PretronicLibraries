package net.prematic.libraries.event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public class EventManager {

    private final ExecutorService executor;
    private final Map<Class<?>,Collection<MethodHolder>> methodes;

    public EventManager() {
        this.executor = Executors.newSingleThreadExecutor();
        this.methodes = new LinkedHashMap<>();
    }
    public Collection<MethodHolder> getHolders(ObjectOwner owner){
        Collection<MethodHolder> holders = new LinkedList<>();
        for(Collection<MethodHolder> list : this.methodes.values())
            for(MethodHolder holder : new LinkedList<>(list)) if(holder.getOwner().equals(owner)) holders.add(holder);
        return holders;
    }
    public void registerListener(ObjectOwner owner, Object listener){
        for(Method method : listener.getClass().getDeclaredMethods()){
            try{
                EventHandler handler = method.getAnnotation(EventHandler.class);
                if(handler != null){
                    if(method.getParameterTypes().length == 1){
                        if(!this.methodes.containsKey(method.getParameterTypes()[0]))
                            this.methodes.put(method.getParameterTypes()[0],new LinkedList<>());
                        this.methodes.get(method.getParameterTypes()[0]).add(new MethodHolder(handler.priority(),owner,listener,method));
                    }
                }
            }catch (Exception exception){}
        }
    }
    public void unregisterListener(Object listener){
        for(Collection<MethodHolder> list : this.methodes.values())
            for(MethodHolder holder : new LinkedList<>(list)) if(holder.getListener().equals(listener)) list.remove(holder);
    }
    public void unregisterListener(ObjectOwner owner){
        for(Collection<MethodHolder> list : this.methodes.values())
            for(MethodHolder holder : new LinkedList<>(list)) if(holder.getOwner().equals(owner)) list.remove(holder);
    }
    public void callEvent(Object event){
        this.executor.execute(()->{
            try{
                Collection<MethodHolder> methodes = this.methodes.get(event.getClass());
                if(methodes != null){
                    TreeMap<Integer,MethodHolder> sorter = new TreeMap<>();
                    for(MethodHolder methode : methodes) sorter.put(methode.getPriority(),methode);
                    for(MethodHolder methode : sorter.values()) methode.invoke(event);
                }
            }catch (Exception exception){}
        });
    }
    public void shutdown(){
        this.executor.shutdown();
    }
}
