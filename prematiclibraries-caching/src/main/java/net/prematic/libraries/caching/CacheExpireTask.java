package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 18:38
 *
 */

import java.util.Iterator;

public class CacheExpireTask<O> implements Runnable{

    private boolean running;
    private PrematicCache<O> cache;

    public CacheExpireTask(PrematicCache<O> cache) {
        this.cache = cache;
        this.running = false;
    }
    public boolean isRunning() {
        return running;
    }
    public PrematicCache<O> getCache() {
        return cache;
    }
    public void shutdown(){
        this.running = false;
    }
    @Override
    public void run() {
        this.running = true;
        while(this.running){
            try{
                Thread.sleep(3000L);
                try{
                    Iterator<CacheEntry<O>> iterator = this.cache.getAsList().iterator();
                    CacheEntry entry = null;
                    while((entry = iterator.next()) != null){
                        if(entry.getEntered()+this.cache.getExpireTime() < System.currentTimeMillis())
                            cache.getAsList().remove(entry.getObject());
                    }
                }catch (Exception exception){}
            }catch (Exception exception){}
        }
    }
}
