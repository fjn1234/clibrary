package entities;

import java.util.HashMap;
import java.util.Map;

public class AnnotationEntity extends HashMap<String,Object> {
    public AnnotationEntity() {
    }

    public AnnotationEntity(int capacity) {
        this(50,25);
    }

    public AnnotationEntity(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    public AnnotationEntity(Map<? extends String, ?> map) {
        super(map);
    }
}
