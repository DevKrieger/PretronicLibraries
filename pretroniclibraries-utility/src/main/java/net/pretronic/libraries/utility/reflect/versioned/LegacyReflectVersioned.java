package net.pretronic.libraries.utility.reflect.versioned;

import net.pretronic.libraries.utility.reflect.ReflectException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class LegacyReflectVersioned implements ReflectVersioned{

    @Override
    public void grantFinalPrivileges(Field field) {
        try{
            Field modifiersField = Field.class.getDeclaredField("modifiers");

            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                modifiersField.setAccessible(true);
                return null;
            });

            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        }catch (NoSuchFieldException | IllegalAccessException e){
            throw new ReflectException(e);
        }
    }
}
