package net.pretronic.libraries.message;

import net.pretronic.libraries.message.bml.variable.VariableSet;

public class StringTextable implements Textable {

    private final String message;

    public StringTextable(String message) {
        this.message = message;
    }

    @Override
    public String toText(VariableSet variableSet) {
        return variableSet.replace(this.message);
    }
}
