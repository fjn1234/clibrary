package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SqliteHandler extends SQLiteOpenHelper {

    public SqliteHandler(Context context, String name, CursorFactory factory,
                         int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SqliteHandler(Context context, String name, CursorFactory factory,
                         int version) {
        super(context, name, factory, version);
    }

    public SqliteHandler(Context context, String path, String dbName) {
        super(new SqliteContext(context, path), dbName, null, 1);
    }

    public SqliteHandler(Context context, String dbName) {
        super(context, dbName, null, 1);
    }

    public boolean createTable(String table, String... columns) {
        if (columns.length < 1)
            return false;
        try {
            String column = "";
            for (String col : columns) {
                column += "," + col;
            }
            String id = "_id" + SqliteKeyWords.INTEGER
                    + SqliteKeyWords.PRIMARY_KEY + SqliteKeyWords.AUTOINCREMENT;
            String SqlQuery = "CREATE TABLE if not exists " + table + "(" + id
                    + column + ")";
            this.getWritableDatabase().execSQL(SqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Cursor select(String table, String columns[], String where,
                         String groupBy, String having, String orderBy, String limit) {
        try {
            return this.getWritableDatabase().query(table, columns, where,
                    null, groupBy, having, orderBy, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cursor select(String sqlQuery) {
        try {
            return this.getReadableDatabase().rawQuery(sqlQuery, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(String table, ContentValues values, String where) {
        try {
            this.getWritableDatabase().update(table, values, where, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insert(String table, ContentValues keyValue) {
        if (insertOutID(table, keyValue) > 0)
            return true;
        else
            return false;
    }

    public long insertOutID(String table, ContentValues keyValue) {
        try {
            return this.getWritableDatabase().insert(table, null, keyValue);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean delete(String table, String where) {
        try {
            this.getWritableDatabase().delete(table, where, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void exeTransaction(Transaction tran) {
        if (tran == null)
            return;
        this.getWritableDatabase().beginTransaction();
        boolean isSucces = tran.doInTransaction(this);
        if (isSucces)
            this.getWritableDatabase().setTransactionSuccessful();
        this.getWritableDatabase().endTransaction();
    }

    public int getCount(String table, String column, String where) {
        try {
            String wh = where;
            if (wh == null || TextUtils.isEmpty(wh))
                wh = "";
            else
                wh = " where " + wh;
            if (column == null || TextUtils.isEmpty(column))
                column = "*";
            String sqlQuery = "select count(%s) from " + table + " " + wh;
            sqlQuery = String.format(sqlQuery, column);
            Cursor cursor = select(sqlQuery);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean exeSQL(String sqlQuery) {
        try {
            this.getWritableDatabase().execSQL(sqlQuery);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void release() {
        this.close();
    }

    public interface Transaction {
        boolean doInTransaction(SqliteHandler sqliteHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean dropTable(String table) {
        String sql = "drop table if exists %s;";
        return exeSQL(String.format(sql, table));
    }

    // 判断表是否存在
    public boolean exsitTable(String table) {
        String sql = "select * from sqlite_master where name='%s' and type='table'";
        Cursor cursor = select(String.format(sql, table));
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public List<String> getColumnsName(String table) {
        String sql = "PRAGMA table_info(%s)";
        List<String> columns = new ArrayList<>();
        Cursor cursor = select(String.format(sql, table));
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            columns.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        return columns;
    }

    public boolean addColumn(String table, String column, String type, String defaultVal) {
        String sql = "alter table %s add column %s %s";
        if (!TextUtils.isEmpty(defaultVal)) {
            sql += " default %s";
        }
        sql = String.format(sql, table, column, type, defaultVal);
        return exeSQL(sql);
    }

    public boolean addColumn(String table, String column, String type) {
        return addColumn(table, column, type, "");
    }


}
