package net.pretronic.libraries.message;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Validate;

import java.util.function.Function;

public class StringTextable implements Textable {

    private final String message;

    public StringTextable(String message) {
        this.message = message;
    }

    @Override
    public String toText(VariableSet variableSet) {
        return variableSet.replace(this.message);
    }

    @Override
    public Document toDocument() {
        return Document.newDocument()
                .add("class", StringTextable.class)
                .add("message", this.message);
    }

    public static class DocumentStringTextAbleConverter implements Function<Document, StringTextable> {

        @Override
        public StringTextable apply(Document document) {
            Validate.notNull(document);
            return new StringTextable(document.getString("message"));
        }
    }
}
