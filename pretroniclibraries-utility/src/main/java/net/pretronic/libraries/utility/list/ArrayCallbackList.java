package net.pretronic.libraries.utility.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ArrayCallbackList<E> extends ArrayList<E> implements CallbackList<E> {

    private Consumer<E> addCallback = null;
    private Consumer<E> removeCallback = null;

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        callAddCallback(e);
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        c.forEach(this::callAddCallback);
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        callRemoveCallback((E) o);
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        c.forEach(entry -> callRemoveCallback((E) entry));
        return result;
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
