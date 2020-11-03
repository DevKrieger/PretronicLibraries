package net.pretronic.libraries.utility.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ArrayCallbackList<E> extends ArrayList<E> implements CallbackList<E> {

    private Consumer<E> addCallback = null;
    private Consumer<E> removeCallback = null;

    @Override
    public boolean add(E e) {
        callAddCallback(e);
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::callAddCallback);
        return super.addAll(c);
    }

    @Override
    public boolean remove(Object o) {
        callRemoveCallback((E) o);
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(entry -> callRemoveCallback((E) entry));
        return super.removeAll(c);
    }

    @Override
    public void setAddCallback(Consumer<E> consumer) {
        this.addCallback = consumer;
    }

    @Override
    public void setRemoveCallback(Consumer<E> consumer) {
        this.removeCallback = consumer;
    }

    private void callAddCallback(E e) {
        if(this.addCallback != null) {
            this.addCallback.accept(e);
        }
    }

    private void callRemoveCallback(E e) {
        if(this.removeCallback != null) {
            this.removeCallback.accept(e);
        }
    }
}
