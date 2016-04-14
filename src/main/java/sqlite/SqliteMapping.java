package sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.DatabaseAnnotation;
import entities.AnnotationEntity;
import entities.DBAnnotationEntity;
import interfaces.IMapping;
import obj.CApplication;
import utils.AnnotationUtil;

public class SqliteMapping {
    public String _id;
    private String table;
    private Object obj;
    private static Map<String, List<DBAnnotationEntity>> existTable = new HashMap<>(100, 50);
    private static SqliteHandler sqliteHandler = null;

    public SqliteMapping(Object obj) {
        this.obj = obj;
        table = getTableName(obj.getClass());
    }

    public String getID() {
        return _id == null ? "" : _id;
    }

    public void setID(String id) {
        _id = id;
    }

    public List<DBAnnotationEntity> createTable() {
        try {
            if (existTable.containsKey(table)) return existTable.get(table);
            if (!openSqlConnection()) return null;
            List<DBAnnotationEntity> columnList = getAnnotationList();
            if (columnList.size() == 0) return columnList;
            if (!sqliteHandler.exsitTable(table)) {
                List<String> colSql = new ArrayList<>();
                for (DBAnnotationEntity entity : columnList) {
                    if (TextUtils.isEmpty(entity.getDefaultVal()))
                        colSql.add(entity.getColumn() + entity.getType());
                    else
                        colSql.add(entity.getColumn() + entity.getType() + SqliteKeyWords.DEFAULT + entity.getDefaultVal());
                }
                if (sqliteHandler.createTable(table, (String[]) colSql.toArray(new String[colSql.size()]))) {
                    existTable.put(table, columnList);
                    return columnList;
                }
            } else {
                List<String> list = sqliteHandler.getColumnsName(table);
                for (DBAnnotationEntity entity : columnList) {
                    if (!list.contains(entity.getColumn()))
                        sqliteHandler.addColumn(table, entity.getColumn(), entity.getType(), entity.getDefaultVal());
                }
                existTable.put(table, columnList);
                return columnList;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean selectTop1(String where, String orderBy) {
        return sel(where, orderBy);
    }

    public boolean selectSingle() {
        return sel("", null);
    }

    private boolean sel(String where, String orderBy) {
        try {
            if (!openSqlConnection()) return false;
            createTable();
            Cursor cursor = getEntityCursor(where, orderBy);
            if (!cursor.moveToFirst()) return false;
            Field[] fields = this.obj.getClass().getFields();
            Object object = this.obj.getClass().newInstance();
            int index;
            setID(cursor.getString(cursor.getColumnIndex("_id")));
            for (Field f : fields) {
                f.setAccessible(true);
                index = cursor.getColumnIndex(f.getName());
                if (index > -1 && !cursor.isNull(index)) {
                    if (f.getType().equals(String.class))
                        f.set(this.obj, cursor.getString(index));
                    else if (f.getType().equals(int.class) || f.getType().equals(Integer.class))
                        f.set(this.obj, cursor.getInt(index));
                    else if (f.getType().equals(long.class) || f.getType().equals(Long.class))
                        f.set(this.obj, cursor.getLong(index));
                    else if (f.getType().equals(float.class) || f.getType().equals(Float.class))
                        f.set(this.obj, cursor.getFloat(index));
                    else if (f.getType().equals(double.class) || f.getType().equals(Double.class))
                        f.set(this.obj, cursor.getDouble(index));
                    else if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
                        f.set(this.obj, Boolean.parseBoolean(cursor.getString(index)));
                } else {
                    f.set(this.obj, object.getClass().getField(f.getName()).get(object));
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean selectByID() {
        if (TextUtils.isEmpty(getID())) return false;
        return selectTop1("_id=" + getID(), null);
    }

    public boolean insertSingle() {
        if (existTable.containsKey(table)) {
            if (!sqliteHandler.delete(table, "")) return false;
        }
        return insert();
    }

    public boolean insert() {
        List<DBAnnotationEntity> columnList = createTable();
        if (columnList == null) return false;
        Field field;
        long id;
        try {
            ContentValues cv = new ContentValues();
            for (DBAnnotationEntity entity : columnList) {
                field = this.obj.getClass().getField(entity.getColumn());
//                System.out.println(entity.getColumn());
                cv.put(entity.getColumn(), field.get(this.obj).toString());
            }
            id = sqliteHandler.insertOutID(table, cv);
            if (id > 0) {
                setID(id + "");
                return true;
            } else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(String where) {
        List<DBAnnotationEntity> columnList = createTable();
        if (columnList == null) return false;
        Field field;
        try {
            ContentValues cv = new ContentValues();
            for (DBAnnotationEntity entity : columnList) {
                field = this.obj.getClass().getField(entity.getColumn());
                cv.put(entity.getColumn(), field.get(this.obj).toString());
            }
            return sqliteHandler.update(table, cv, where);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateByID() {
        if (TextUtils.isEmpty(getID())) return false;
        return update("_id=" + getID());
    }

    public boolean updateOrInsert(String where) {
        if (existEntity(where)) {
            return update(where);
        } else {
            return insert();
        }
    }

    public boolean updateOrInsertByID() {
        if (TextUtils.isEmpty(getID()))
            setID("-1");
        return updateOrInsert("_id=" + getID());
    }

    public static <T extends Object> boolean delete(Class<T> obj, String where) {
        if (!openSqlConnection() || TextUtils.isEmpty(where)) return false;
        return sqliteHandler.delete(getTableName(obj), where);
    }

    public boolean delete() {
        if (TextUtils.isEmpty(getID())) return false;
        return delete(obj.getClass(), "_id=" + getID());
    }

    public static boolean deleteAll(Object obj) {
        SqliteMapping mapping = new SqliteMapping(obj);
        return mapping.dropTable();
    }

    public boolean existTable() {
        if (!openSqlConnection()) throw new RuntimeException("can not open sqlite");
        return sqliteHandler.exsitTable(table);
    }

    public boolean existEntity(String where) {
        Cursor cursor = getEntityCursor(where, null);
        if (cursor == null || cursor.getCount() == 0)
            return false;
        else
            return true;
    }

    public boolean dropTable() {
        if (!openSqlConnection()) throw new RuntimeException("can not open sqlite");
        if (sqliteHandler.dropTable(table)) {
            existTable.remove(table);
            return true;
        } else
            return false;
    }

    public Cursor getEntityCursor(String where, String orderBy) {
        if (!openSqlConnection()) return null;
        StringBuilder sqlQuery = new StringBuilder("select * from " + table);
        if (!TextUtils.isEmpty(where)) {
            sqlQuery.append(" where " + where);
        }
        if (!TextUtils.isEmpty(orderBy))
            sqlQuery.append(" order by " + orderBy);
        return sqliteHandler.select(sqlQuery.toString());
    }

    private List<DBAnnotationEntity> getAnnotationList() {
        List<DBAnnotationEntity> columList = new ArrayList<>(50);
        AnnotationEntity map = AnnotationUtil.getFieldAnnotation(DatabaseAnnotation.IDatabaseAnnotation.class, this.obj.getClass());
        if (map.size() == 0) return columList;
        DBAnnotationEntity fieldMap;
        String type = "";
        for (String column : map.keySet()) {
            fieldMap = new DBAnnotationEntity((AnnotationEntity) map.get(column));
            fieldMap.setColumn(column);
            fieldMap.setType(fieldMap.get(DatabaseAnnotation.TYPE).toString());
            fieldMap.setLength(Integer.parseInt(fieldMap.get(DatabaseAnnotation.MAXLENGTH).toString()));
            fieldMap.setPoint(Integer.parseInt(fieldMap.get(DatabaseAnnotation.POINT).toString()));
            fieldMap.setDefaultVal(fieldMap.get(DatabaseAnnotation.DEFVALUE).toString());
            if (fieldMap.getLength() > 0)
                type = SqliteKeyWords.getFixedColumnType(fieldMap.getType(), fieldMap.getLength(), fieldMap.getPoint());
            if (!type.equals(""))
                fieldMap.setType(type);
            columList.add(fieldMap);
        }
        return columList;
    }


    public static boolean openSqlConnection() {
        try {
            if (sqliteHandler == null) {
                sqliteHandler = new SqliteHandler(CApplication.getGolbalContext(), CApplication.getEntityDB());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean closeSqlConnection() {
        try {
            if (sqliteHandler != null) {
                sqliteHandler.release();
                sqliteHandler = null;
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static SqliteHandler getSqliteHandler() {
        return sqliteHandler;
    }

    public static Map<String, List<DBAnnotationEntity>> getExistTable() {
        return existTable;
    }

    public String getTable() {
        return table;
    }


    public static void run(SqlOperation sqlOperation) {
        try {
            openSqlConnection();
            sqlOperation.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeSqlConnection();
        }
    }

    public interface SqlOperation {
        void run();
    }

    public static String getTableName(Class clazz) {
        return clazz.getName().replace(".", "_");
    }


    public static <T> List<T> createEntityList(Class<T> clazz) {
        return createEntityList(clazz, null);
    }

    public static <T> List<T> createEntityList(Class<T> clazz, String where) {
        return createEntityList(clazz, where, null);
    }

    public static <T> List<T> createEntityList(Class<T> clazz, String where, String orderBy) {
        List<T> list = new ArrayList<>();
        T entity;
        Field[] fields;
        int index;
        SqliteHandler sqliteHandler = new SqliteHandler(CApplication.getGolbalContext(), CApplication.getEntityDB());
        try {
            String table = SqliteMapping.getTableName(clazz);
            if (!sqliteHandler.exsitTable(table)) {
                Log.i("sqlite", table + " table no exist");
                return list;
            }
            StringBuilder sqlQuery = new StringBuilder("select * from " + table);
            if (!TextUtils.isEmpty(where)) {
                sqlQuery.append(" where " + where);
            }
            if (!TextUtils.isEmpty(orderBy))
                sqlQuery.append(" order by " + orderBy);
            Cursor cursor = sqliteHandler.select(sqlQuery.toString());
            if (cursor == null || cursor.getCount() == 0) return list;
            fields = clazz.getFields();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                entity = (T) clazz.newInstance();
                if (entity instanceof IMapping.ISQLiteMapping) {
                    ((IMapping.ISQLiteMapping) entity).getSqliteMapping().setID(cursor.getString(cursor.getColumnIndex("_id")));
                }
                for (Field f : fields) {
                    f.setAccessible(true);
                    index = cursor.getColumnIndex(f.getName());
                    if (index > -1 && !cursor.isNull(index)) {
                        if (f.getType().equals(String.class))
                            f.set(entity, cursor.getString(index));
                        else if (f.getType().equals(int.class) || f.getType().equals(Integer.class))
                            f.set(entity, cursor.getInt(index));
                        else if (f.getType().equals(long.class) || f.getType().equals(Long.class))
                            f.set(entity, cursor.getLong(index));
                        else if (f.getType().equals(float.class) || f.getType().equals(Float.class))
                            f.set(entity, cursor.getFloat(index));
                        else if (f.getType().equals(double.class) || f.getType().equals(Double.class))
                            f.set(entity, cursor.getDouble(index));
                        else if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class))
                            f.set(entity, Boolean.parseBoolean(cursor.getString(index)));
                    }
                }
                list.add(entity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (sqliteHandler != null)
                sqliteHandler.close();
        }
        return list;
    }
}
