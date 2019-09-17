package net.prematic.libraries.utility.annonations;

@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER,ElementType.LOCAL_VARIABLE,ElementType.CONSTRUCTOR,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Internal {

    String description() default "Unknown";
}
