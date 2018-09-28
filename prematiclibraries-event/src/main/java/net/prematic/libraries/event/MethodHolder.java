package net.prematic.libraries.event;

import java.lang.reflect.Method;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 01.09.18 18:12
 *
 */

public class MethodHolder {

    private final int priority;
    private final ObjectOwner owner;
    private final Object listener;
    private final Method methode;

    public MethodHolder(int priority,ObjectOwner owner,Object listener, Method methode) {
        this.priority = priority;
        this.owner = owner;
        this.listener = listener;
        this.methode = methode;
    }
    public ObjectOwner getOwner() {
        return this.owner;
    }
    public int getPriority() {
        return this.priority;
    }
    public Object getListener() {
        return this.listener;
    }
    public Method getMethode() {
        return this.methode;
    }
    public void invoke(Event event) throws Exception{
        try{this.methode.invoke(this.listener,event);
        }catch (Exception exception){exception.printStackTrace();}
    }
}
