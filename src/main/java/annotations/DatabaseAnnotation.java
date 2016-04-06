package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class DatabaseAnnotation {

    public final static String TYPE = "type";
    public final static String MAXLENGTH = "maxLength";
    public final static String POINT = "point";
    public final static String DEFVALUE = "defValue";

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IDatabaseAnnotation {
        String type() default "";

        int maxLength() default -1;

        int point() default 0;

        String defValue() default "";
    }
}