package net.pretronic.libraries.message;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.utility.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DocumentTextableConverterFactory {

    private final static Map<Class<? extends Textable>, Function<Document, ? extends Textable>> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(StringTextable.class, new StringTextable.DocumentStringTextAbleConverter());
    }

    public static void registerConverter(Class<? extends Textable> clazz, Function<Document, ? extends Textable> converter) {
        Validate.notNull(clazz, converter);
        CONVERTERS.put(clazz, converter);
    }

    public static boolean unregisterConverter(Class<? extends Textable> clazz) {
        Validate.notNull(clazz);
        return CONVERTERS.remove(clazz) != null;
    }

    public static Function<Document, ? extends Textable> getConverter(Class<? extends Textable> clazz) {
        Validate.notNull(clazz);
        return CONVERTERS.get(clazz);
    }

    public static Textable convert(Document document) {
        Validate.notNull(document);

        Class<?> rawClass = document.getObject("class", Class.class);
        Validate.isTrue(Textable.class.isAssignableFrom(rawClass), rawClass.getName() + " does not implement Textable");

        Class<? extends Textable> clazz = (Class<? extends Textable>) rawClass;

        Function<Document, ? extends Textable> converter = getConverter(clazz);
        Validate.notNull(converter, "No DocumentTextableConverter found for class " + clazz.getName());

        return converter.apply(document);
    }
}
