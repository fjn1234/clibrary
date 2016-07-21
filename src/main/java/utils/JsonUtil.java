package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import obj.CBaseEntity;

/**
 * Created by Administrator on 2014/12/26.
 */
public class JsonUtil {

    public static JSONArray mapToJson(Map<Object, Object> map, boolean outNull) {
        try {
            JSONArray array = new JSONArray();
            Object[] keySet = map.keySet().toArray();
            for (int i = 0, size = keySet.length; i < size; i++) {
                array.put(i, objectToJson(map.get(keySet[i]), outNull));
            }
            return array;
        } catch (Exception ex) {
            return null;
        }
    }

    public static JSONArray listToJson(List<Object> list, boolean outNull) {
        try {
            JSONArray array = new JSONArray();
            for (int i = 0, size = list.size(); i < size; i++) {
                array.put(i, objectToJson(list.get(i), outNull));
            }
            return array;
        } catch (Exception ex) {
            return null;
        }
    }

    public static JSONArray arrayToJson(Object[] list, boolean outNull) {
        try {
            JSONArray array = new JSONArray();
            for (int i = 0, size = list.length; i < size; i++) {
                array.put(i, objectToJson(list[i], outNull));
            }
            return array;
        } catch (Exception ex) {
            return null;
        }
    }

    public static JSONObject objectToJson(Object obj, boolean outNull) {
        if (obj == null) return null;
        JSONObject json = new JSONObject();
        Object v;
        Class type;
        String name;
        try {
            Field[] fields = obj.getClass().getFields();
            for (Field f : fields) {
                v = f.get(obj);
                name = f.getName();
                type = f.getType();
                if (v != null) {
                    if (String.class.isAssignableFrom(type) ||
                            int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)
                            || long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)
                            || float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)
                            || double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)
                            || boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
                        json.put(f.getName(), v);
                    } else if (List.class.isAssignableFrom(type)) {
                        json.put(name, listToJson((List<Object>) v, outNull));
                    } else if (Map.class.isAssignableFrom(type)) {
                        json.put(name, mapToJson((Map<Object, Object>) v, outNull));
                    } else if (CBaseEntity.class.isAssignableFrom(type)) {
                        json.put(name, objectToJson(v, outNull));
                    }
                } else {
                    if (outNull) {
                        json.put(name, "");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
