package net.prematic.libraries.caching;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EmptyCache<O> implements Cache<O> {

    public static final Cache EMPTY = new EmptyCache(false);

    public static final Cache EMPTY_EXCEPTED = new EmptyCache(true);

    private final boolean exception;

    private EmptyCache(boolean exception) {
        this.exception = exception;
    }

    @Override
    public Collection<O> getCachedObjects() {
        return Collections.emptyList();
    }

    @Override
    public int size() {
        return 0;//Unused
    }

    @Override
    public O get(String queryName, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public O get(CacheQuery<O> query, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public O get(Predicate<O> query) {
        return null;//Unused
    }

    @Override
    public O get(Predicate<O> query, Supplier<O> loader) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<O> getAsync(String queryName, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<O> getAsync(CacheQuery<O> query, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<O> getAsync(Predicate<O> query) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<O> getAsync(Predicate<O> query, Supplier<O> loader) {
        return null;//Unused
    }

    @Override
    public void insert(O object) {
        if(exception) throw new UnsupportedOperationException("It is not supported to insert an item into an empty cache.");
    }

    @Override
    public void insertAsync(O object) {
        if(exception) throw new UnsupportedOperationException("It is not supported to insert an item into an empty cache.");
    }

    @Override
    public O remove(String queryName, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public O remove(CacheQuery<O> query, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public O remove(Predicate<O> query) {
        return null;//Unused
    }

    @Override
    public boolean remove(O cachedObject) {
        return false;//Unused
    }

    @Override
    public CompletableFuture<O> removeAsync(String queryName, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<O> removeAsync(CacheQuery<O> query, Object... identifiers) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<O> removeAsync(Predicate<O> query) {
        return null;//Unused
    }

    @Override
    public CompletableFuture<Boolean> removeAsync(O cachedObject) {
        return null;//Unused
    }

    @Override
    public Cache<O> setMaxSize(int maxSize) {
        return this;//Unused
    }

    @Override
    public Cache<O> setRefresh(long refreshTime, TimeUnit unit) {
        return this;//Unused
    }

    @Override
    public Cache<O> setExpire(long expireTime, TimeUnit unit) {
        return this;//Unused
    }

    @Override
    public Cache<O> setExpireAfterAccess(long expireTime, TimeUnit unit) {
        return this;//Unused
    }

    @Override
    public Cache<O> setInsertListener(Consumer<O> onInsert) {
        return this;//Unused
    }

    @Override
    public Cache<O> setRemoveListener(Predicate<O> onRemove) {
        return this;//Unused
    }

    @Override
    public Cache<O> registerQuery(String name, CacheQuery<O> query) {
        return this;//Unused
    }

    @Override
    public Cache<O> unregisterQuery(String name) {
        return this;//Unused
    }

    @Override
    public void clear() {
        //Unused
    }

    @Override
    public void shutdown() {
        //Unused
    }

    @SuppressWarnings("uncechked")
    public static <R> Cache<R> newEmptyCache(){
        return EMPTY;
    }

    @SuppressWarnings("uncechked")
    public static <R> Cache<R> newEmptyExceptedCache(){
        return EMPTY_EXCEPTED;
    }
}
