package utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import interfaces.IEntity;
import obj.CArrayList;
import obj.CBaseEntity;
import obj.CHashMap;
import interfaces.IKeyValue;


public class EntityUtil {

    public static Object instanceEntity(String className, JSONObject json) {
        Object entity = null;
        try {
            Class clazz = Class.forName(className);
            entity = clazz.newInstance();//通过反射创建对象
            Field[] fields = entity.getClass().getFields();
            String k;
            for (Field f : fields) {
                f.setAccessible(true);
                k = f.getName();
                if (!Character.isLowerCase(k.toCharArray()[0])) continue;
                if (json.has(k)) {
                    if (f.getType().equals(String.class)) {
                        f.set(entity, json.optString(k));
                    } else if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                        f.set(entity, json.optInt(k));
                    } else if (f.getType().equals(long.class) || f.getType().equals(Long.class)) {
                        f.set(entity, json.optLong(k));
                    } else if (f.getType().equals(float.class) || f.getType().equals(Float.class)) {
                        f.set(entity, StringUtil.stringToFloat(json.getString(k)));
                    } else if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                        f.set(entity, json.optDouble(k));
                    } else if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)) {
                        f.set(entity, json.optBoolean(k));
                    } else if (List.class.isAssignableFrom(f.getType()) && !json.isNull(k)) {
                        String strClass = f.getGenericType().toString().split("<")[1];
                        Class t = Class.forName(strClass.substring(0, strClass.length() - 1));
                        f.set(entity, EntityUtil.createEntityList(json.getJSONArray(k), t));
                    } else if (HashMap.class.isAssignableFrom(f.getType()) && !json.isNull(k)) {
                        String strClass = f.getGenericType().toString().split(",")[1].trim();
                        Class t = Class.forName(strClass.substring(0, strClass.length() - 1));
                        f.set(entity, EntityUtil.createEntityHashMap(json.getJSONArray(k), t));
                    } else if (CBaseEntity.class.isAssignableFrom(f.getType())) {
                        f.set(entity, EntityUtil.createEntity(json.getJSONObject(k), f.getType()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public static <T> T createEntity(JSONObject jsonObject, Class<T> clazz) {
        return (T) EntityUtil.instanceEntity(clazz.getName(), jsonObject);
    }

    public static <T> T createEntity(String jsonString, Class<T> clazz) {
        try {
            if (TextUtils.isEmpty(jsonString)) return null;
            JSONObject json = new JSONObject(jsonString);
            return createEntity(json, clazz);
        } catch (Exception e) {
            try {
                return clazz.newInstance();
            } catch (Exception e1) {
                return null;
            }
        }
    }

    public static <T> T createEntity(String json, T obj, Class<T> clazz) {
        if (obj != null) return obj;
        obj = EntityUtil.createEntity(json, clazz);
        if (obj == null) {
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static <T> CArrayList<T> createEntityList(String json, Class<T> clazz) {
        return createEntityList(null, clazz, json);
    }


    public static <T> CArrayList<T> createEntityList(CArrayList<T> list, Class<T> clazz, String json) {
        if (list != null) return list;
        CArrayList<T> tempList = new CArrayList<>();
        try {
            tempList = createEntityList(new JSONArray(json), clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempList;
    }

    public static <T> CArrayList<T> createEntityList(JSONArray array, Class<T> clazz) {
        CArrayList<T> list = new CArrayList<>();
        try {
            for (int i = 0, length = array.length(); i < length; i++) {
                list.add(createEntity(array.getJSONObject(i), clazz));
            }
            return list;
        } catch (Exception e) {
            return list;
        }
    }

    public static <T> CHashMap<String, T> createEntityHashMap(CHashMap<String, T> list, Class<T> clazz, String json) {
        if (list != null) return list;
        List<T> tempList = EntityUtil.createEntityList(json, clazz);
        if (tempList != null) {
            list = new CHashMap<>(tempList.size());
            for (T e : tempList) {
                if (!(e instanceof IEntity.ID)) return list;
                list.put(((IEntity.ID) e).getId(), e);
            }
        } else {
            list = new CHashMap<>();
        }
        return list;
    }

    public static <T> CHashMap<String, T> createEntityHashMap(String jsonArray, Class<T> clazz) {
        try {
            return createEntityHashMap(new JSONArray(jsonArray), clazz);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new CHashMap<>();
    }

    public static <T> CHashMap<String, T> createEntityHashMap(JSONArray jsonArray, Class<T> clazz) {
        int size = jsonArray.length();
        CHashMap<String, T> hs = new CHashMap<>(size);
        if (!IEntity.ID.class.isAssignableFrom(clazz)) return hs;
        T e;
        try {
            for (int i = 0; i < size; i++) {
                e = EntityUtil.createEntity(jsonArray.getJSONObject(i), clazz);
                hs.put(((IEntity.ID) e).getId(), e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hs;
    }


    public static CHashMap<Long, String> createEntityHashMap(String jsonData) {
        CHashMap<Long, String> list;
        try {
            JSONObject json = new JSONObject(jsonData);
            Iterator it = json.keys();
            list = new CHashMap<>();
            while (it.hasNext()) {
                String key = it.next().toString();
                String value = json.getString(key);
                list.put(StringUtil.stringToLong(key), value);
            }
        } catch (JSONException e) {
            list = new CHashMap<>();
        }
        return list;
    }

    public static <T> List<T> createKeyValueEntityList(JSONObject json, JSONArray array, Class clazz) {
        try {
            IKeyValue entity;
            List<T> list = new ArrayList<>();
            for (int i = 0, length = array.length(); i < length; i++) {
                entity = (IKeyValue) clazz.newInstance();
                String key = array.getString(i);
                String value = json.getString(key);
                entity.setKey(key);
                entity.setValue(value);
                list.add((T) entity);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> createKeyValueEntityList(String jsonStr, Class clazz, List<T> dataArr) {
        if (dataArr != null) return dataArr;
        List<T> newDataArr = new ArrayList<>();
        if (TextUtils.isEmpty(jsonStr)) return newDataArr;
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray array = json.names();
            newDataArr = createKeyValueEntityList(json, array, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDataArr;
    }

    public static <T> List<T> createKeyValueEntityList(String jsonStr, Class clazz) {
        return createKeyValueEntityList(jsonStr, clazz, null);
    }

    public static <T> List<T> hashMapToList(HashMap<Long, T> hashMap) {
        List<T> list = new ArrayList<>();
        Set<Long> keySet = hashMap.keySet();
        for (long k : keySet) {
            list.add(hashMap.get(k));
        }
        return list;
    }

    public static Object copyEntity(Object obj, Object newObj) {
        return copyEntity(obj, newObj, true);
    }

    private static Object copyEntity(Object obj, Object newObj, boolean backup) {
        Object backupObj = null;
        Object value;
        try {
            if (backup) {
                Class clazz = Class.forName(obj.getClass().getName());
                backupObj = clazz.newInstance();
                backupObj = copyEntity(backupObj, obj, false);
                if (backupObj == null) return obj;
            }
            Field[] fields = obj.getClass().getFields();
            for (Field f : fields) {
                f.setAccessible(true);
                value = newObj.getClass().getField(f.getName()).get(newObj);
                if (value != null)
                    f.set(obj, value);
            }
            return obj;
        } catch (Exception e) {
            LogUtil.loge(EntityUtil.class, e.getMessage());
            if (backup)
                return backupObj;
            else
                return null;
        }
    }
}
