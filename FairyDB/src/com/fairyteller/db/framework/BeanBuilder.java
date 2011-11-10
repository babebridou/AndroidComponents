package com.fairyteller.db.framework;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class BeanBuilder<T> {
	public abstract T buildBean(Cursor cursor, SQLiteDatabase db);
	
	protected static final String getString(String name, Cursor cursor) throws Exception{
		int col = cursor.getColumnIndex(name);
		if(col<0){
			throw new Exception("Column "+name+" not found!");
		}
		return cursor.getString(col);
	}
	protected static final long getLong(String name, Cursor cursor) throws Exception{
		int col = cursor.getColumnIndex(name);
		if(col<0){
			throw new Exception("Column "+name+" not found!");
		}
		return cursor.getLong(col);
	}
	protected static final int getInt(String name, Cursor cursor) throws Exception{
		int col = cursor.getColumnIndex(name);
		if(col<0){
			throw new Exception("Column "+name+" not found!");
		}
		return cursor.getInt(col);
	}
}
