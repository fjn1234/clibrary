package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import entities.CacheEntity;
import obj.CApplication;
import sqlite.SqliteHandler;
import sqlite.SqliteKeyWords;
import utils.DateUtil;
import utils.DecodeUtil;


public class CacheDB {

    private SqliteHandler sqliteHandler;
    private Context context;
    private static final String TABLE = "net_cache";
    private static final String ACTIVITY_NAME = "activity_name";
    private static final String ACTION = "action";
    private static final String RESPONSE_DATA = "response_data";
    private static final String UPDATE_TIME = "update_time";


    public CacheDB(Context context) {
        this.context = context;
        sqliteHandler = new SqliteHandler(context, CApplication.getEntityDB());
        create();
    }

    public boolean create() {
        String c1 = ACTIVITY_NAME + SqliteKeyWords.getVarchar(100);
        String c2 = ACTION + SqliteKeyWords.getVarchar(50);
        String c3 = RESPONSE_DATA + SqliteKeyWords.TEXT;
        String c4 = UPDATE_TIME + SqliteKeyWords.DATE;
        return sqliteHandler.createTable(TABLE, c1, c2, c3, c4);
    }


    //    返回activity的所有缓存数据
    public CacheEntity getCache(String atyName) {
        String sqlQuery = "select * from %s where %s='%s'";
        sqlQuery = String.format(sqlQuery, TABLE, ACTIVITY_NAME, atyName);
        Cursor cursor = sqliteHandler.select(sqlQuery);
        CacheEntity cacheEntity = new CacheEntity();
        String action, response;
        String updateTime;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            action = cursor.getString(cursor.getColumnIndex(ACTION));
            response = cursor.getString(cursor.getColumnIndex(RESPONSE_DATA));
            updateTime = cursor.getString(cursor.getColumnIndex(UPDATE_TIME));
            CacheEntity.ActionEntity actionEntity = cacheEntity.new ActionEntity(atyName, action, response, updateTime);
            cacheEntity.put(action, actionEntity);
        }
        cursor.close();
        return cacheEntity;
    }

    public boolean isChange(String atyName, String action, String response) {
        boolean b = false;
        String sqlQuery = "select %s from %s where %s='%s' and %s='%s'";
        sqlQuery = String.format(sqlQuery, RESPONSE_DATA, TABLE, ACTIVITY_NAME, atyName, ACTION, action);
        Cursor cursor = sqliteHandler.select(sqlQuery);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String oldResponse = cursor.getString(cursor.getColumnIndex(RESPONSE_DATA));
            if (DecodeUtil.getMD5(response).equals(DecodeUtil.getMD5(oldResponse)))
                b = true;
        }
        cursor.close();
        return b;
    }

    public boolean isExsit(String atyName, String action) {
        boolean b = false;
        String sqlQuery = "select * from %s where %s='%s' and %s='%s'";
        sqlQuery = String.format(sqlQuery, TABLE, ACTIVITY_NAME, atyName, ACTION, action);
        Cursor cursor = sqliteHandler.select(sqlQuery);
        b = cursor.moveToFirst();
        cursor.close();
        return b;
    }

    public boolean update(String atyName, String action, String response, String upTime) {
        ContentValues cv = new ContentValues();
        if (!isExsit(atyName, action)) {
            cv.put(ACTIVITY_NAME, atyName);
            cv.put(ACTION, action);
            cv.put(RESPONSE_DATA, response);
            if (TextUtils.isEmpty(upTime))
                cv.put(UPDATE_TIME, DateUtil.getCurrentDateString("yyyy-MM-dd hh:mm:ss"));
            else
                cv.put(UPDATE_TIME, upTime);
            return sqliteHandler.insert(TABLE, cv);
        } else {
            String where = "%s='%s' and %s='%s'";
            where = String.format(where, ACTIVITY_NAME, atyName, ACTION, action);
            cv.put(RESPONSE_DATA, response);
            if (TextUtils.isEmpty(upTime))
                cv.put(UPDATE_TIME, DateUtil.getCurrentDateString("yyyy-MM-dd hh:mm:ss"));
            else
                cv.put(UPDATE_TIME, upTime);
            return sqliteHandler.update(TABLE, cv, where);
        }
    }

    public boolean update(CacheEntity.ActionEntity actionEntity, String upTime) {
        return update(actionEntity.getActivity(), actionEntity.getAction(), actionEntity.getResponseStr(), upTime);
    }

    public void close() {
        sqliteHandler.close();
    }

}
