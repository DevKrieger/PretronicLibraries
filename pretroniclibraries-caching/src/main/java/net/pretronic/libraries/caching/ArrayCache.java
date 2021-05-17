/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.caching;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * An array cached is based on a dynamic growing array.
 *
 * Todo: Integrate @{@link CacheStateAble}
 *
 * @param <O> The object to cache.
 */
public class ArrayCache<O> implements Cache<O>{

    private static final long TASK_SLEEP_TIME = 1000;
    private static final int DEFAULT_MAX_SIZE = 1000;
    private static final int DEFAULT_BUFFER = 128;

    private final Map<String,CacheQuery<O>> queries;
    private final ExecutorService executor;
    private Consumer<O> insertListener;
    private Predicate<O> removeListener;
    private CacheTask task;
    private long refreshTime;
    private long expireTime;
    private long expireTimeAfterAccess;

    private int maxSize;
    private int buffer;

    private CacheEntry[] entries;
    private final AtomicInteger size;
    private final AtomicBoolean arrayOperationsLocked;

    public ArrayCache() {
        this(DEFAULT_MAX_SIZE);
    }

    public ArrayCache(int maxSize) {
        this(maxSize,DEFAULT_BUFFER);
    }

    public ArrayCache(int maxSize, int buffer) {
        this(getDefaultExecutor(),maxSize,buffer);
    }

    public ArrayCache(ExecutorService executor) {
        this(executor,DEFAULT_MAX_SIZE,DEFAULT_BUFFER);
    }

    public ArrayCache(ExecutorService executor, int maxSize) {
        this(executor,maxSize,DEFAULT_BUFFER);
    }

    public ArrayCache(ExecutorService executor, int maxSize, int buffer) {
        Objects.requireNonNull(executor,"Executor service is null.");
        this.executor = executor;
        this.buffer = buffer;
        this.maxSize = maxSize;

        this.queries = new LinkedHashMap<>();
        this.entries = new CacheEntry[buffer];
        this.removeListener = null;
        this.size = new AtomicInteger(0);
        this.arrayOperationsLocked = new AtomicBoolean(false);
        this.refreshTime = 0;
        this.expireTime = 0;
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public boolean isEmpty() {
        return this.size.get() == 0;
    }

    @Override
    public void clear() {
        CacheEntry[] entries = this.entries;
        this.entries = new CacheEntry[buffer];
        this.size.set(0);
        Arrays.fill(entries, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<O> getCachedObjects() {
        ArrayList<O> values = new ArrayList<>();
        int index = 0;
        for(CacheEntry entry : entries){
            if(index >= size.get()) return values;
            if(entry != null){
                values.add((O) entry.value);
                index++;
            }
        }
        return values;
    }

    @Override
    public O get(String queryName, Object... identifiers) {
        Objects.requireNonNull(queryName,"Query is null");
        Objects.requireNonNull(identifiers,"Identifiers are null");
        CacheQuery<O> query = queries.get(queryName.toLowerCase());
        if(query == null) throw new IllegalArgumentException(queryName+" not found.");
        return get(query,identifiers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public O get(CacheQuery<O> query, Object... identifiers) {
        query.validate(identifiers);
        for(int i = 0; i < size.get(); i++) {
            CacheEntry entry = this.entries[i];
            if(entry != null && query.check((O) this.entries[i].value,identifiers)){
                move(i);
                entry.lastUsed = System.currentTimeMillis();
                this.entries[size.get()-1] = entry;
                return (O) entry.value;
            }
        }
        O value = callInternalLoad(query,identifiers);
        if(value == null) value = query.load(identifiers);
        if(value!= null) insert(value);
        return value;
    }

    @Override
    public O get(Predicate<O> query) {
        return get(query,null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public O get(Predicate<O> query, Supplier<O> loader) {
        Objects.requireNonNull(query,"Query is null");
        for(int i = 0; i < size.get(); i++) {
            CacheEntry entry = this.entries[i];
            if(entry != null && query.test((O) this.entries[i].value)){
                move(i);
                entry.lastUsed = System.currentTimeMillis();
                this.entries[size.get()-1] = entry;
                return (O) entry.value;
            }
        }
        O value = callInternalLoad(query);
        if(loader != null && value == null) value = loader.get();
        if(value!= null) insert(value);
        return value;
    }

    @Override
    public CompletableFuture<O> getAsync(String queryName, Object... identifiers) {
        return doAsync(() -> ArrayCache.this.get(queryName, identifiers));
    }

    @Override
    public CompletableFuture<O> getAsync(CacheQuery<O> query, Object... identifiers) {
        return doAsync(() -> ArrayCache.this.get(query, identifiers));
    }

    @Override
    public CompletableFuture<O> getAsync(Predicate<O> query) {
        return getAsync(query,null);
    }

    @Override
    public CompletableFuture<O> getAsync(Predicate<O> query, Supplier<O> loader) {
        return doAsync(() -> ArrayCache.this.get(query,loader));
    }

    @Override
    public void insert(O value) {
        Objects.requireNonNull(value,"Object is null");
        if(size.get() >= maxSize){
            move(0);
            this.entries[size.get()-1] = new CacheEntry(value);
        }else{
            int size = this.size.getAndIncrement();
            if(size >= this.entries.length){
                grow();
            }
            this.entries[size] = new CacheEntry(value);
        }
        if(insertListener != null) {
            insertListener.accept(value);
        }
    }

    @Override
    public void insertAsync(O object) {
        this.executor.execute(()-> insert(object));
    }

    @Override
    public O remove(String queryName, Object... identifiers) {
        Objects.requireNonNull(queryName,"Query is null");
        Objects.requireNonNull(identifiers,"Identifiers are null");
        CacheQuery<O> query = queries.get(queryName.toLowerCase());
        if(query == null) throw new IllegalArgumentException(queryName+" not found.");
        return remove(query,identifiers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public O remove(CacheQuery<O> query, Object... identifiers) {
        query.validate(identifiers);
        for(int i = 0; i < size.get(); i++) {
            CacheEntry entry = this.entries[i];
            if(entry != null){
                O value = (O)entry.value;
                if(query.check(value,identifiers)){
                    callInternalRemove(value);
                    move(i);
                    this.entries[size.getAndDecrement()] = null;
                    return value;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public O remove(Predicate<O> query) {
        Objects.requireNonNull(query,"Query is null");
        for(int i = 0; i < this.size.get(); i++) {
            CacheEntry entry = this.entries[i];
            if(entry != null){
                O value = (O)entry.value;
                if(query.test(value)){
                    callInternalRemove(value);
                    move(i);
                    this.entries[size.getAndDecrement()] = null;
                    return value;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object value) {
        Objects.requireNonNull(value,"Object is null");
        for(int i = 0; i < size.get(); i++) {
            CacheEntry entry = this.entries[i];
            if(entry != null && entry.value.equals(value)){
                callInternalRemove((O) entry.value);
                move(i);
                this.entries[size.getAndDecrement()] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public CompletableFuture<O> removeAsync(String queryName, Object... identifiers) {
        return doAsync(() -> remove(queryName, identifiers));
    }

    @Override
    public CompletableFuture<O> removeAsync(CacheQuery<O> query, Object... identifiers) {
        return doAsync(() -> remove(query, identifiers));
    }

    @Override
    public CompletableFuture<O> removeAsync(Predicate<O> query) {
        return doAsync(() -> remove(query));
    }

    @Override
    public CompletableFuture<Boolean> removeAsync(O cachedObject) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.executor.execute(()->{
            try{
                future.complete(remove(cachedObject));
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    @Override
    public Cache<O> setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    @Override
    public Cache<O> setRefresh(long expireTime, TimeUnit unit) {
        this.refreshTime = unit.toMillis(expireTime);
        createTask();
        return this;
    }

    @Override
    public Cache<O> setExpire(long expireTime, TimeUnit unit) {
        this.expireTime = unit.toMillis(expireTime);
        createTask();
        return this;
    }

    @Override
    public Cache<O> setExpireAfterAccess(long expireTime, TimeUnit unit) {
        this.expireTimeAfterAccess = unit.toMillis(expireTime);
        createTask();
        return this;
    }

    @Override
    public Cache<O> setInsertListener(Consumer<O> onInsert) {
        this.insertListener = onInsert;
        return this;
    }

    @Override
    public Cache<O> setRemoveListener(Predicate<O> removeListener) {
        this.removeListener = removeListener;
        return this;
    }

    @Override
    public Cache<O> registerQuery(String name, CacheQuery<O> query) {
        this.queries.put(name.toLowerCase(),query);
        return this;
    }

    @Override
    public Cache<O> unregisterQuery(String name) {
        this.queries.remove(name);
        return this;
    }

    @Override
    public void shutdown() {
        this.task.stop();
        clear();
    }

    public void setBuffer(int buffer){
        if(buffer > maxSize) throw new IllegalArgumentException("Buffer is higher then the maximum size.");
        this.buffer = buffer;
    }

    private void createTask(){
        if(task == null){
            task = new CacheTask();
            this.executor.execute(task);
        }
    }

    private void grow(){
        if(!arrayOperationsLocked.compareAndSet(false,true)){
            while (arrayOperationsLocked.get()) {
                try {
                    Thread.sleep(0,1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        int newLength = this.entries.length+buffer;

        if(newLength > maxSize) this.entries = Arrays.copyOf(this.entries,maxSize);
        else this.entries = Arrays.copyOf(this.entries,newLength);
        arrayOperationsLocked.set(false);
    }

    private void shrink(){
        int different = this.entries.length-size.get();
        if(different > buffer) entries = Arrays.copyOf(this.entries,size.get()+buffer);
    }

    private void move(int index){
        int move = size.get()- index - 1;
        System.arraycopy(entries,index+1,entries,index, move);
    }

    private CompletableFuture<O> doAsync(Supplier<O> runner){
        CompletableFuture<O> future = new CompletableFuture<>();
        this.executor.execute(()->{
            try{
                future.complete(runner.get());
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    private static ExecutorService getDefaultExecutor(){
        try{
            return (ExecutorService) Class.forName("net.pretronic.libraries.utility.GeneralUtil").getMethod("getDefaultExecutorService").invoke(null);
        }catch (Exception ignored){}
        return Executors.newCachedThreadPool();
    }

    protected void callInternalRemove(O object){
        /*Unused, required for abstract implementation */
    }

    protected O callInternalLoad(CacheQuery<O> query, Object[] identifiers){
        /*Unused, required for abstract implementation */
        return null;
    }

    protected O callInternalLoad(Predicate<O> query){
        /*Unused, required for abstract implementation */
        return null;
    }

    private static class CacheEntry {

        private long entered;
        private long lastUsed;
        private Object value;

        public CacheEntry(Object value) {
            this.lastUsed = this.entered = System.currentTimeMillis();
            this.value = value;
        }
    }

    private class CacheTask implements Runnable{

        private boolean running;

        CacheTask() {
            this.running = false;
        }

        public boolean isRunning() {
            return running;
        }

        public void stop(){
            this.running = false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            this.running = true;
            while(!Thread.interrupted() && this.running){
                try{
                    Thread.sleep(TASK_SLEEP_TIME);
                    for(int i = 0; i < size.get(); i++) {
                        CacheEntry entry = entries[i];
                        if(entry == null || (expireTimeAfterAccess > 0 && entry.lastUsed+expireTimeAfterAccess <= System.currentTimeMillis())
                                || (expireTime > 0 && entry.entered+expireTime <= System.currentTimeMillis())
                                || (refreshTime > 0 && entry.entered+refreshTime <= System.currentTimeMillis())){
                            boolean canceled = false;
                            if(entry != null && removeListener != null) {
                                canceled = removeListener.test((O) entry.value);
                            }
                            if(!canceled) {
                                if(entry != null) callInternalRemove((O) entry.value);
                                move(i);
                                entries[size.getAndDecrement()] = null;
                                i--;
                            }
                        }
                    }
                    shrink();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }
}
