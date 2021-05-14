package net.pretronic.libraries.message;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.adapter.DocumentAdapter;
import net.pretronic.libraries.document.entry.DocumentBase;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.TypeReference;

public class TextableDocumentAdapter implements DocumentAdapter<Textable> {

    @Override
    public Textable read(DocumentBase base, TypeReference<Textable> type) {
        if(base.isObject()) {
            return DocumentTextableConverterFactory.convert(base.toDocument());
        } else {
            throw new IllegalArgumentException("Entry is not object.");
        }
    }

    @Override
    public DocumentEntry write(String key, Textable object) {
        Document document = object.toDocument();
        if(key != null) document.setKey(key);
        return document;
    }
}
