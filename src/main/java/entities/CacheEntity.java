package entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import utils.DecodeUtil;


/**
 * Created by Administrator on 2015/3/14.
 */
public class CacheEntity extends Hashtable<String, CacheEntity.ActionEntity> {


    public CacheEntity() {
//        String action,String response,String updateTime
//        ActionEntity actionEntity=new ActionEntity(response,updateTime);
//        this.put(action,actionEntity);
    }

    public class ActionEntity {
        public String activity, action, responseStr = "";
        public String updateTime = null;
        public JSONObject responseJson = null;

        public ActionEntity(String activity, String action, String responseStr, String updateTime) {
            this.activity = activity;
            this.action = action;
            this.responseStr = responseStr;
            this.updateTime = updateTime;
            try {
                responseJson = new JSONObject(this.responseStr);
            } catch (JSONException e) {
                responseJson = null;
            }
        }

        public boolean isChange(String responseStr) {
            Log.e("old", DecodeUtil.getMD5(this.responseStr));
            Log.e("new", DecodeUtil.getMD5(responseStr));
            return !DecodeUtil.getMD5(this.responseStr).equals(DecodeUtil.getMD5(responseStr));
        }

        public void setResponseStr(String responseStr) {
            this.responseStr = responseStr;
        }

        public String getActivity() {
            return activity;
        }

        public String getAction() {
            return action;
        }

        public String getResponseStr() {
            return responseStr;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public JSONObject getResponseJson() {
            return responseJson;
        }
    }

}
