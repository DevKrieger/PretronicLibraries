package net.prematic.libraries.utility.map;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 28.09.18 21:30
 *
 */

public class MultiValue<K, V> {

    private K key;
    private V value;

    public MultiValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof MultiValue) {
            MultiValue multiValue = (MultiValue) o;
            if (key != null ? !key.equals(multiValue.key) : multiValue.key != null) return false;
            return value != null ? value.equals(multiValue.value) : multiValue.value == null;
        }
        return false;
    }
}