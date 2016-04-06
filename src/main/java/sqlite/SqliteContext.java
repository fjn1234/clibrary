package sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import io.CFile;


public class SqliteContext extends ContextWrapper {

	private String path;

	public SqliteContext(Context base, String path) {
		super(base);
		this.path = path;
	}

	private String getPath(String dbName) {
		CFile file = new CFile(path,dbName);
		if (!file.exists())
			file.createNewFileAndDirectory();
		return file.getAbsolutePath();
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		return SQLiteDatabase.openOrCreateDatabase(getPath(name),
				factory);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		return SQLiteDatabase.openOrCreateDatabase(getPath(name), factory, errorHandler);
	}

}
