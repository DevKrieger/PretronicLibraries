package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 16:21
 *
 */


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PrematicCache<O> implements Cache<O> {

    public static ExecutorService EXECUTOR;

    private int maxSize;
    private long expireTime;
    private CacheExpireTask expireTask;
    private List<CacheEntry<O>> objects;
    private Map<String, ObjectQuery<O>> queries;

    public PrematicCache() {
        this.objects = new ArrayList<>();
        this.queries = new LinkedHashMap<>();
    }
    public int size(){
        return this.objects.size();
    }
    public long getExpireTime() {
        return this.expireTime;
    }
    public List<CacheEntry<O>> getAsList(){
        return this.objects;
    }
    public O get(String identifierName, Object identifier) {
        try{
            ObjectQuery<O> query = this.queries.get(identifierName.toLowerCase());
            if(query != null){
                Iterator<CacheEntry<O>> iterator = this.objects.iterator();
                CacheEntry entry = null;
                while((entry = iterator.next()) != null) if(query.is(identifier,(O)entry.getObject())) return (O)entry.getObject();
            }
            return null;
        }catch (Exception exception){
            return null;
        }
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
    public void insert(O object) {
        if(this.maxSize > 0 && size() >= this.maxSize) this.objects.remove(0);
        this.objects.add(new CacheEntry<>(System.currentTimeMillis(),object));
    }
    public O remove(String identifierName, Object identifier) {
        O object = get(identifierName,identifier);
        this.objects.remove(object);
        return object;
    }
    public void registerQuery(String name, ObjectQuery<O> query){
        this.queries.put(name.toLowerCase(),query);
    }
    private void createExpireTask(){
        if(EXECUTOR == null) EXECUTOR = Executors.newSingleThreadExecutor();
        shutdown();
        this.expireTask = new CacheExpireTask<>(this);
        EXECUTOR.execute(this.expireTask);
    }
    public void shutdown(){
        if(this.expireTask != null) this.expireTask.shutdown();
    }
}
