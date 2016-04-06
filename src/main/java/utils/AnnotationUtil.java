package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import entities.AnnotationEntity;

public class AnnotationUtil {

    public static AnnotationEntity getFieldAnnotation(Class annotationInterface, Class clazz) {
        Method[] annotationMothod =annotationInterface.getDeclaredMethods();
        AnnotationEntity outMap = new AnnotationEntity();
        try {
            AnnotationEntity fieldAnnoMap;
            Method m;
            String v;
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotationInterface)) {
                    fieldAnnoMap = new AnnotationEntity();
                    Annotation p = field.getAnnotation(annotationInterface);
                    for (Method af : annotationMothod) {
                        m = p.getClass().getDeclaredMethod(af.getName(), new Class[0]);
                        v = m.invoke(p, new Object[]{}).toString();
                        fieldAnnoMap.put(af.getName(), v);
                    }
                    if (fieldAnnoMap.size() > 0)
                        outMap.put(field.getName(), fieldAnnoMap);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outMap;
    }

}
