package com.example.classtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "studentClass.db";
	private static final String TABLE_NAME = "Class";
	private static final int DB_VERSION = 1;

	private static final String TAG = "Class11";

	public MyDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String SQL_CREATE_TABLE = "create table "
				+ TABLE_NAME
				+ "(_id integer primary key autoincrement,"
				+ " name varchar(20),rom varchar(20), day integer,start_point integer,end_point integer)";

		Log.d(TAG, SQL_CREATE_TABLE);

		db.execSQL(SQL_CREATE_TABLE);
	}

	public void insert(String name, String rom, int day, int start_point, int end_point) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("rom", rom);
		values.put("day", day);
		values.put("start_point", start_point);
		values.put("end_point", end_point);
		long rid = db.insert(TABLE_NAME, null, values);
		db.close();
	}

	public void delete(String _id) {
		SQLiteDatabase db = getWritableDatabase();

		String whereClause = "_id = ?";
		String[] whereArgs = { _id };

		int row = db.delete(TABLE_NAME, whereClause, whereArgs);

		db.close();
	}

	public void update(String key, String oldValue, String newValue) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(key, newValue);

		String whereClause = key + " = ?";
		String[] whereArgs = { oldValue };

		int rows = db.update(TABLE_NAME, values, whereClause, whereArgs);
		db.close();
	}

	public void update(String key, int oldValue, int newValue) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(key, newValue);

		String whereClause = key + " = ?";
		String[] whereArgs = { String.valueOf(oldValue) };

		int rows = db.update(TABLE_NAME, values, whereClause, whereArgs);
		db.close();
	}

	public Cursor query() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);

		// db.close();	Error!
		return c;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
