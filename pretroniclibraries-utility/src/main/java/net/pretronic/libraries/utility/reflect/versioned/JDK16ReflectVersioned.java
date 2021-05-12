package net.pretronic.libraries.utility.reflect.versioned;

import java.lang.reflect.Field;

public class JDK16ReflectVersioned implements ReflectVersioned{

    @Override
    public void grantFinalPrivileges(Field field) {
        throw new UnsupportedOperationException("Unfortunately this method is no longer available in Java 16, consider using ReflectionUtil#setUnsafeObjectFieldValue to manipulate final values");
    }
}
