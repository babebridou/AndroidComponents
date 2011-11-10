package com.fairyteller.db.framework;

import java.util.HashMap;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DefaultDaoFactory implements IDaoFactory {
	private SQLiteOpenHelper sqlhelper;
	
	private Map<String, IDao> daos;
	
	public DefaultDaoFactory(SQLiteOpenHelper sqlhelper) {
		this.daos = new HashMap<String, IDao>();
		this.sqlhelper = sqlhelper;
	}
	
	public void registerDao(String name, IDao dao){
		this.daos.put(name, dao);
		dao.setDaoFactory(this);
	}
	
	public IDao getDao(String name){
		return this.daos.get(name);
	}
	
	public SQLiteDatabase openReadOnlyConnection(){
		return this.sqlhelper.getReadableDatabase();
	}
	@Override
	public SQLiteDatabase openWriteableConnection() {
		return this.sqlhelper.getWritableDatabase();
	}

}
