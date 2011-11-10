package com.fairyteller.db.framework;

import android.database.sqlite.SQLiteDatabase;

public interface IDaoFactory {

	public IDao getDao(String name);
	
	public SQLiteDatabase openReadOnlyConnection();
	
	public SQLiteDatabase openWriteableConnection();
	
}
