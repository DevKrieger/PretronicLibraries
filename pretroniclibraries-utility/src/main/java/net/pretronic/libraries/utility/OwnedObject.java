package net.pretronic.libraries.utility;

import net.pretronic.libraries.utility.interfaces.ObjectOwner;

public class OwnedObject<T> {

    private final ObjectOwner owner;
    private final T object;

    public OwnedObject(ObjectOwner owner, T object) {
        this.owner = owner;
        this.object = object;
    }

    public ObjectOwner getOwner() {
        return owner;
    }

    public T getObject() {
        return object;
    }
}
