package com.fairyteller.db.framework;

import android.database.sqlite.SQLiteDatabase;

public class SqlSequenceHacker {

	public static void createSequence(SQLiteDatabase db, String sequenceName){
		db.execSQL("CREATE TABLE S_"+sequenceName+" (VAL INTEGER PRIMARY KEY AUTOINCREMENT, INFO TEXT);");
	}
	
	public static String createSequenceScript(String sequenceName){
		return "CREATE TABLE S_"+sequenceName+" (VAL INTEGER PRIMARY KEY AUTOINCREMENT, INFO TEXT);";
	}
	
	public static String dropSequenceScript(String sequenceName){
		return "DROP TABLE S_"+sequenceName+";";
	}
	
	public static long sequenceNextVal(SQLiteDatabase db, String sequenceName){
		long id = -1;
		id = db.insert("S_"+sequenceName, "INFO", null);
		return id;
	}
}
 