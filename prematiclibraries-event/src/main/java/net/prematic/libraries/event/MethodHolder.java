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
    private final Method method;

    public MethodHolder(int priority,ObjectOwner owner,Object listener, Method methode) {
        this.priority = priority;
        this.owner = owner;
        this.listener = listener;
        this.method = methode;
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
    public Method getMethod() {
        return this.method;
    }
    public void invoke(Object event) throws Exception{
        try{this.method.invoke(this.listener,event);
        }catch (Exception exception){exception.printStackTrace();}
    }
}
