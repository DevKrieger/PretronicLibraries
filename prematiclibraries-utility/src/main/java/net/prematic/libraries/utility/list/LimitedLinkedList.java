package net.prematic.libraries.utility.list;

import net.prematic.libraries.utility.exceptions.LimitedListReachedLimitException;
import java.util.Collection;
import java.util.LinkedList;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 02.09.18 12:52
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 12:52
 *
 */

public class LimitedLinkedList extends LinkedList {

    private final int maxsize;

    public LimitedLinkedList(int maxsize) {
        this.maxsize = maxsize;
    }
    public LimitedLinkedList(Collection collection, int maxsize) {
        super(collection);
        this.maxsize = maxsize;
    }
    public int getMaxSize() {
        return this.maxsize;
    }
    public boolean canEnter() {
        return super.size() < this.maxsize;
    }
    @Override
    public boolean add(Object object) {
        if(canEnter()) return super.add(object);
        else throw new LimitedListReachedLimitException(this.maxsize);
    }
    @Override
    public void addFirst(Object object) {
        if(canEnter()) super.addFirst(object);
        else throw new LimitedListReachedLimitException(this.maxsize);
    }
    @Override
    public void addLast(Object object) {
        if(canEnter()) super.addLast(object);
        else throw new LimitedListReachedLimitException(this.maxsize);
    }
    @Override
    public boolean offer(Object o) {
        if(canEnter()) return super.offer(o);
        else throw new LimitedListReachedLimitException(this.maxsize);
    }
    @Override
    public boolean offerFirst(Object object) {
        if(canEnter()) return super.offerFirst(object);
        else throw new LimitedListReachedLimitException(this.maxsize);
    }
    @Override
    public boolean offerLast(Object object) {
        if(canEnter()) return super.offerLast(object);
        else throw new LimitedListReachedLimitException(this.maxsize);
    }
    @Override
    public boolean addAll(Collection collection) {
        for(Object object : collection) if(!this.add(object)) return false;
        else throw new LimitedListReachedLimitException(this.maxsize);
        return true;
    }
}