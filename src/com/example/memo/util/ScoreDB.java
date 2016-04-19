package com.example.memo.util;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class ScoreDB {

	public static final String DATABASE_NAME = "Score.db";
	private static final String DATABASE_TABLE = "Score";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + ScoreDBEntry.IDX
			+ " integer primary key autoincrement,"+ ScoreDBEntry.NAME
			+ " TEXT not null,"+ScoreDBEntry.VALUE 
			+ " integer not null );";
	private static final String TAG = "SCOREDbAdapter";
	
	private String[] COLUMNS = new String[] { ScoreDBEntry.IDX,ScoreDBEntry.NAME,ScoreDBEntry.VALUE};

	private Context mContext;
	private HistoryDatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	public ScoreDB(Context context) {
		mContext = context;
	}

	public ScoreDB Open() throws SQLException {
		mDbHelper = new HistoryDatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		System.out.println("dbanalhtml"+mDb.getPath());
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createScore(String name,int value) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ScoreDBEntry.NAME, name);
		initialValues.put(ScoreDBEntry.VALUE, value);
		
				
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteScore(int idx) {
		int result = mDb.delete(DATABASE_TABLE, ScoreDBEntry.IDX + "="
				+ idx, null);
		return result > 0;
	}

	public Cursor fetchAllEntry() {
		
		return mDb.query(DATABASE_TABLE, COLUMNS, null, null, null, null, "value desc");
	}
	public int getAllEntry() {
		
		return mDb.query(DATABASE_TABLE, COLUMNS, null, null, null, null, null).getCount();
	}
	
	
	
	

	
	public Cursor fetchIdEntry(long id) {
		return mDb.query(DATABASE_TABLE, COLUMNS, 
				ScoreDBEntry.IDX + "=" + id, 
				null, null, null, null);
	}

	

	
	

	private class HistoryDatabaseHelper extends SQLiteOpenHelper {

		public HistoryDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destory all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
				}

	}
	
	

	
	public class ScoreDBEntry implements BaseColumns {
		public static final String IDX = "_ID";
		public static final String NAME = "name";
		public static final String VALUE = "value";
		
		
	}

}
