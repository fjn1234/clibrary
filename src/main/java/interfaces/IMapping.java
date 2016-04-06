package interfaces;

import sqlite.SqliteMapping;

public class IMapping {

    public interface IObjectMapping{
        IObject.IViewMapping getViewMapping();
    }

    public interface ISQLiteMapping{
        SqliteMapping getSqliteMapping();
    }
}
