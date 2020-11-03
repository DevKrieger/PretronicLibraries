package net.pretronic.libraries.utility.list;

import net.pretronic.libraries.utility.CallbackAble;

import java.util.Collection;
import java.util.function.Consumer;

public interface CallbackCollection<E> extends Collection<E>, CallbackAble {

    void setAddCallback(Consumer<E> consumer);

    void setRemoveCallback(Consumer<E> consumer);
}
