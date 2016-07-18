package obj;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

import interfaces.IMapping;
import interfaces.IObject;
import sqlite.SqliteMapping;
import utils.EntityUtil;
import utils.JsonUtil;
import view.ViewMapping;

public class CBaseEntity implements Serializable, IMapping.ISQLiteMapping, IMapping.IObjectMapping {

    private Context context = CApplication.getAppContext();

    public Context getContext() {
        return context;
    }

    private SqliteMapping sqliteMapping = new SqliteMapping(this);

    public CBaseEntity() {
    }

    public void fill(String json) {
        try {
            fill(new JSONObject(json));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fill(JSONObject json) {
        Object obj = EntityUtil.createEntity(json, this.getClass());
        fill(obj);
    }

    public void fill(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) return;
        EntityUtil.copyEntity(this, obj);
    }

    public JSONObject getJSON(boolean outNull) {
        return JsonUtil.objectToJson(this, outNull);
    }

    public JSONObject getJSON() {
        return getJSON(false);
    }

    public String toJSONString(boolean outNull) {
        return getJSON(outNull).toString();
    }

    public String toJSONString() {
        return getJSON().toString();
    }

    @Override
    public SqliteMapping getSqliteMapping() {
        return sqliteMapping;
    }

    public String getEntityID() {
        return sqliteMapping.getID();
    }

    public void setEntityID(String id) {
        sqliteMapping.setID(id);
    }

    //-------------- entity map view--------------

    private IObject.IViewMapping viewMapping = new ViewMapping(this);

    public IObject.IViewMapping getViewMapping() {
        return viewMapping;
    }

}

