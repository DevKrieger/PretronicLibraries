package net.pretronic.libraries.utility.reflect.versioned;

import net.pretronic.libraries.utility.reflect.ReflectException;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JDK9ReflectVersioned implements ReflectVersioned{

    @Override
    public void grantFinalPrivileges(Field field) {
        try{
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
            VarHandle modifiers = lookup.findVarHandle(Field.class, "modifiers", int.class);

            int mods = field.getModifiers();
            modifiers.set(field, mods & ~Modifier.FINAL);
        }catch (Exception exception){
            throw new ReflectException(exception);
        }
    }
}
