package net;


import android.util.Log;

import org.json.JSONObject;

import java.net.URLConnection;
import java.util.List;

import interfaces.NetConnectionInterface;
import utils.EntityUtil;
import utils.LogUtil;


public class DemoBaseNetConnection<T> extends NetConnection {

    Class objClass;

    private EntityType entityType;

    public enum EntityType {
        Object, List, Map, None
    }

    public DemoBaseNetConnection(boolean doEncode, String charset, String url, NetParams.HttpMethod method, NetConnectionInterface.iConnectListener connectListener, String... kvs) {
        this(null, EntityType.None, true, doEncode, charset, url, method, connectListener, NetParams.TIME_OUT, kvs);
    }

    public DemoBaseNetConnection(Class<T> obj, EntityType entityType, boolean doEncode, String charset, String url, NetParams.HttpMethod method, NetConnectionInterface.iConnectListener connectListener, String... kvs) {
        this(obj, entityType, true, doEncode, charset, url, method, connectListener, NetParams.TIME_OUT, kvs);
    }

    public DemoBaseNetConnection(Class<T> obj, EntityType entityType, boolean doEncode, String charset, String url, NetParams.HttpMethod method, NetConnectionInterface.iConnectListener connectListener, int timeOut, String... kvs) {
        this(obj, entityType, true, doEncode, charset, url, method, connectListener, timeOut, kvs);
    }

    public DemoBaseNetConnection(Class<T> obj, EntityType entityType, boolean sslMode, boolean doEncode, String charset, String url, NetParams.HttpMethod method, NetConnectionInterface.iConnectListener connectListener, int timeOut, String... kvs) {
        super(sslMode, doEncode, charset, url, method, new NetConnectionInterface.iSetHeader() {
            @Override
            public void setHeader(URLConnection urlConnection) {
                urlConnection.addRequestProperty("loginType", "R");
                urlConnection.addRequestProperty("loginSource", "A");
                Log.e("header loginType:", "R");
                Log.e("header loginSource:", "A");
//                    if (AccountEntity.getEntity() != null) {
//                        urlConnection.addRequestProperty("loginId", AccountEntity.getEntity().getId() + "");
//                        urlConnection.addRequestProperty("token", AccountEntity.getEntity().getReqToken());
//                        Log.e("header loginId:", AccountEntity.getEntity().getId() + "");
//                        Log.e("header token:", AccountEntity.getEntity().getToken());
//                    }

            }
        }, connectListener, timeOut, kvs);
        this.entityType = entityType;
        this.objClass = obj;
    }


    @Override
    protected Result getResult(String response) {
        Result result = new Result();
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (jsonObject.getInt("status")) {
                case 1:
                    result.responseStatus = NetParams.OPERATE_SUCCESS;
                    switch (entityType) {
                        case Object:
                            result.entityData = EntityUtil.createEntity(jsonObject.getJSONObject("data"), objClass);
                            break;
                        case List:
                            result.entityData = EntityUtil.createEntityList(jsonObject.getJSONArray("data"), objClass);
                            break;
                        case Map:
                            result.entityData = EntityUtil.createEntityHashMap(jsonObject.getJSONArray("data"), objClass);
                            break;
                    }
                default:
                    result.responseStatus = NetParams.OPERATE_FAIL;
                    break;
            }
            result.resultData = jsonObject;
        } catch (Exception ex) {
            LogUtil.printStackTrace(DemoBaseNetConnection.class, ex);
            result.responseStatus = NetParams.RESPONSE_ERROR;
        } finally {
            return result;
        }
    }

    @Override
    protected void setDefaultParams(List<String> params) {

    }

    @Override
    protected void onResult(Result result) {

    }
}
