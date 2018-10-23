package net.prematic.libraries.caching.defaults;

import net.prematic.libraries.caching.Cache;
import net.prematic.libraries.caching.object.CacheObjectExpireDeletion;
import net.prematic.libraries.caching.object.CacheObjectLoader;
import net.prematic.libraries.caching.object.CacheObjectQuery;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 20:06
 *
 */

public class PrematicCache<O> implements Cache<O> {

    public static ExecutorService EXECUTOR;

    private int maxSize;
    private long expireTime;
    private CacheExpireTask expireTask;
    private CacheObjectExpireDeletion<O> expireDeletion;
    private List<CacheEntry> objects;
    private Map<String,CacheObjectQuery<O>> queries;
    private Map<String,CacheObjectLoader<O>> loaders;

    public PrematicCache() {
        this.objects = new ArrayList<>();
        this.queries = new LinkedHashMap<>();
        this.loaders = new LinkedHashMap<>();
    }
    public List<O> getAll(){
        List<O> objects = new LinkedList<>();
        Iterator<CacheEntry> iterator = this.objects.iterator();
        CacheEntry entry = null;
        while((entry = iterator.next()) != null) objects.add(entry.getObject());
        return objects;
    }

    public CacheObjectExpireDeletion<O> getExpireDeletion() {
        return expireDeletion;
    }

    public List<CacheEntry> getObjects() {
        return this.objects;
    }
    public Map<String, CacheObjectQuery<O>> getQueries() {
        return this.queries;
    }
    public Map<String, CacheObjectLoader<O>> getLoaders() {
        return this.loaders;
    }
    public int size(){
        return this.objects.size();
    }
    public long getExpireTime() {
        return this.expireTime;
    }
    public List<CacheEntry> getAsList(){
        return this.objects;
    }
    public O get(String identifierName, Object identifier) {
        try{
            CacheObjectQuery<O> query = this.queries.get(identifierName.toLowerCase());
            if(query != null){
                Iterator<CacheEntry> iterator = this.objects.iterator();
                CacheEntry entry = null;
                while((entry = iterator.next()) != null) if(query.is(identifier,entry.getObject())) return entry.getObject();
            }
        }catch (Exception exception){}
        CacheObjectLoader<O> loader = this.loaders.get(identifierName.toLowerCase());
        return loader.load(identifier);
    }
    public PrematicCache<O> setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }
    public PrematicCache<O> setExpire(long expireTime, TimeUnit unit) {
        this.expireTime = unit.toMillis(expireTime);
        createExpireTask();
        return this;
    }
    public PrematicCache<O>  setExpireDeletion(CacheObjectExpireDeletion<O> expireDeletion) {
        this.expireDeletion = expireDeletion;
        return this;
    }
    public void insert(O object) {
        if(this.maxSize > 0 && size() >= this.maxSize) this.objects.remove(0);
        this.objects.add(new CacheEntry(System.currentTimeMillis(),object));
    }
    public O remove(String identifierName, Object identifier) {
        CacheObjectQuery<O> query = this.queries.get(identifierName.toLowerCase());
        if(query != null){
            Iterator<CacheEntry> iterator = this.objects.iterator();
            CacheEntry entry = null;
            while((entry = iterator.next()) != null){
                if(query.is(identifier,entry.getObject())){
                    iterator.remove();
                    return entry.getObject();
                }
            }
        }
        return null;
    }
    public void registerQuery(String name, CacheObjectQuery<O> query){
        this.queries.put(name.toLowerCase(),query);
    }
    public void registerLoader(String name, CacheObjectLoader<O> loader){
        this.loaders.put(name.toLowerCase(),loader);
    }
    private void createExpireTask(){
        if(EXECUTOR == null) EXECUTOR = Executors.newSingleThreadExecutor();
        shutdown();
        this.expireTask = new CacheExpireTask();
        EXECUTOR.execute(this.expireTask);
    }
    public void shutdown(){
        if(this.expireTask != null) this.expireTask.shutdown();
    }

    private class CacheEntry {

        private long entered;
        private O object;

        public CacheEntry(long entered, O object) {
            this.entered = entered;
            this.object = object;
        }
        public long getEntered() {
            return entered;
        }
        public O getObject() {
            return object;
        }
    }
    private class CacheExpireTask implements Runnable{

        private boolean running;

        public CacheExpireTask() {
            this.running = false;
        }
        public boolean isRunning() {
            return running;
        }
        public void shutdown(){
            this.running = false;
        }
        @Override
        public void run() {
            this.running = true;
            while(this.running){
                try{
                    Thread.sleep(200L);
                    try{
                        Iterator<PrematicCache<O>.CacheEntry> iterator = objects.iterator();
                        PrematicCache.CacheEntry entry = null;
                        while((entry = iterator.next()) != null) if(entry.getEntered()+getExpireTime() <= System.currentTimeMillis()
                                && (expireDeletion == null || !(expireDeletion.delete((O)entry.getObject())))) iterator.remove();
                    }catch (Exception exception){}
                }catch (Exception exception){}
            }
        }
    }
}


