package com.training;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	static final int DB_VERSION = 2; // ������ ��
	final String LOG_TAG = "myLogs";
	
	public DBHelper(Context context) {
		// ����������� �����������
		super(context, "reporterDB", null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	  	Log.d(LOG_TAG, "--- onCreate database ---");
	   	// ������� ������� users � ������
	   	db.execSQL("create table users ("
	   			+ "_id integer primary key autoincrement," 
	   			+ "login text,"
	   			+ "password text" + ");");
	   	// ������� ������� allreports � ������
	   	db.execSQL("create table allreports ("
	   			+ "_id integer primary key autoincrement," 
	   			+ "login text,"
	   			+ "date text,"
	   			+ "typeofactivities text,"
	   			+ "nameofproject text,"
	   			+ "info text,"
	   			+ "time text" + ");");
	}
	    	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   	if (oldVersion == 1 && newVersion == 2) {
	   		// ������ ��� ������� ����������
	        db.beginTransaction();
	        try {
	           	// ������� ������� allreports � ������
	    	   	db.execSQL("create table allreports ("
	    	   			+ "_id integer primary key autoincrement," 
	    	   			+ "login text,"
	    	   			+ "date text,"
	    	   			+ "typeofactivities text,"
	    	   			+ "nameofproject text,"
	    	   			+ "info text,"
	    	   			+ "time text" + ");");
	    	   	db.setTransactionSuccessful();
	        }
	       
	        finally {
	        	db.endTransaction(); 
	    	}
	    }
	}
	
}
