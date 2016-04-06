package utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Administrator on 2014/12/26.
 */
public class JsonUtil {

    public static JSONObject hashtableToJson(Hashtable<String,String> ht)
    {
        JSONObject json=new JSONObject();
        Enumeration e = ht.keys();
        while (e.hasMoreElements()) {
            String k = (String) e.nextElement();
            String v = ht.get(k);
            try {
                json.put(k,v);
            } catch (JSONException e1) {
                Log.e(JsonUtil.class.getName(), e1.getMessage());
            }
        }
        return json;
    }



}
