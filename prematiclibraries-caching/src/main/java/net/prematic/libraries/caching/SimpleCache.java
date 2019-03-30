package net.prematic.libraries.caching;

import net.prematic.libraries.caching.object.CacheObjectLoader;
import net.prematic.libraries.caching.object.CacheObjectQuery;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

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

public class SimpleCache<O> implements Cache<O>{

    public static ExecutorService EXECUTOR;

    private final List<CacheEntry> objects;
    private final Map<String,CacheObjectQuery<O>> queries;
    private final Map<String,CacheObjectLoader<O>> loaders;

    private int maxSize;
    private long expireTime;
    private Consumer<O> removeListener;
    private CacheExpireTask expireTask;

    public SimpleCache() {
        this(0);
    }

    public SimpleCache(int maxSize) {
        this(maxSize,0);
    }

    public SimpleCache(int maxSize, long expireTime) {
        this(maxSize,expireTime,null);
    }

    public SimpleCache(int maxSize, long expireTime, Consumer<O> removeListener) {
        this.objects = new ArrayList<>();
        this.queries = new ConcurrentHashMap<>();
        this.loaders = new ConcurrentHashMap<>();

        this.maxSize = maxSize;
        this.expireTime = expireTime;
        this.removeListener = removeListener;

        if(this.expireTime > 20) createNewExpireTask();
    }

    @Override
    public Collection<O> getObjects() {
        List<O> objects = new ArrayList<>();
        Iterator<CacheEntry> iterator = this.objects.iterator();
        while(iterator.hasNext()) objects.add(iterator.next().object);
        return objects;
    }
    @Override
    public O get(String queryName, Object identifier) {
        CacheObjectQuery<O> query = this.queries.get(queryName.toLowerCase());
        if(query != null){
            Iterator<CacheEntry> iterator = this.objects.iterator();
            CacheEntry entry;
            while(iterator.hasNext() && (entry = iterator.next()) != null) if(query.accept(identifier,entry.object)) return entry.object;
        }
        CacheObjectLoader<O> loader = this.loaders.get(queryName.toLowerCase());
        if(loader != null){
            O object = loader.load(identifier);
            if(object != null) insert(object);
            return object;
        }
        return null;
    }

    @Override
    public Future<O> getAsync(String queryName, Object identifier) {
        if(EXECUTOR == null) EXECUTOR = Executors.newSingleThreadExecutor();
        return EXECUTOR.submit(()-> get(queryName, identifier));
    }

    @Override
    public int size() {
        return this.objects.size();
    }

    @Override
    public O insert(O object) {
        if(this.maxSize > 0 && size() >= this.maxSize) removeFirst();
        remove(object);
        this.objects.add(new CacheEntry(System.currentTimeMillis(),object));
        return object;
    }

    @Override
    public O remove(String queryName, Object identifier) {
        CacheObjectQuery<O> query = this.queries.get(queryName.toLowerCase());
        if(query != null){
            Iterator<CacheEntry> iterator = this.objects.iterator();
            CacheEntry entry;
            while(iterator.hasNext() && (entry = iterator.next()) != null){
                if(entry.equals(identifier)){
                    if(query.accept(identifier,entry.object)){
                        iterator.remove();
                        return entry.object;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public O removeFirst() {
        Iterator<CacheEntry> iterator = this.objects.iterator();
        if(iterator.hasNext()){
            CacheEntry entry = iterator.next();
            iterator.remove();
            if(this.removeListener != null) this.removeListener.accept(entry.object);
            return entry.object;
        }
        return null;
    }

    @Override
    public void remove(O cachedObject) {
        Iterator<CacheEntry> iterator = this.objects.iterator();
        CacheEntry entry;
        while(iterator.hasNext() && (entry = iterator.next()) != null){
            if(entry.object.equals(cachedObject)){
                iterator.remove();
                if(this.removeListener != null) this.removeListener.accept(entry.object);
            }
        }
    }

    @Override
    public Cache<O> setMaxSize(int maxSize) {
        final int oldSize = this.maxSize;
        this.maxSize = maxSize;
        while(oldSize >= maxSize) removeFirst();
        return this;
    }

    @Override
    public Cache<O> setExpire(long expireTime, TimeUnit unit) {
        this.expireTime = unit.toMillis(expireTime);
        if(this.expireTime > 20){
            if(this.expireTask != null) this.expireTask.shutdown();
        }else if(this.expireTask == null) createNewExpireTask();
        return this;
    }

    @Override
    public Cache<O> setRemoveListener(Consumer<O> onRemove) {
        this.removeListener = onRemove;
        return this;
    }

    @Override
    public Cache<O> registerQuery(String name, CacheObjectQuery<O> query) {
        this.queries.put(name.toLowerCase(),query);
        return this;
    }

    @Override
    public Cache<O> registerLoader(String name, CacheObjectLoader<O> loader) {
        this.loaders.put(name.toLowerCase(),loader);
        return this;
    }

    @Override
    public Cache<O> unregisterQuery(String name) {
        this.queries.remove(name);
        return this;
    }

    @Override
    public Cache<O> unregisterLoader(String name) {
        this.loaders.remove(name);
        return this;
    }

    @Override
    public void clear() {
        if(this.removeListener != null){
            Iterator<CacheEntry> iterator = this.objects.iterator();
            while(iterator.hasNext()){
                this.removeListener.accept(iterator.next().object);
                iterator.remove();
            }
        }else this.objects.clear();
    }

    @Override
    public void shutdown() {
        if(this.expireTask != null) this.expireTask.shutdown();
        clear();
    }

    public void createNewExpireTask(){
        if(EXECUTOR == null) EXECUTOR = Executors.newSingleThreadExecutor();
        if(this.expireTask != null) this.expireTask.shutdown();
        this.expireTask = new CacheExpireTask();
        EXECUTOR.execute(this.expireTask);
    }

    private class CacheEntry {

        private long entered;
        private O object;

        public CacheEntry(long entered, O object) {
            this.entered = entered;
            this.object = object;
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
                    Iterator<CacheEntry> iterator = objects.iterator();
                    CacheEntry entry;
                    while(iterator.hasNext() && (entry = iterator.next()) != null)
                        if(entry.entered+expireTime <= System.currentTimeMillis()){
                            iterator.remove();
                            if(removeListener != null) removeListener.accept(entry.object);
                        }
                }catch (Exception exception){}
            }
        }
    }
}
