package com.fairyteller.db.framework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractSQLiteHelper extends SQLiteOpenHelper{
	private Context context;
	

	public AbstractSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}
	
	protected abstract String getCreateDatabaseFullScript(Context context);
	
	protected abstract String getUpgradeDatabaseFullScript(Context context, int oldVersion, int newVersion);
	
	
	protected String[] getCreateDatabaseScript(Context context){
		return getCreateDatabaseFullScript(context).split(";");
	}
	
	protected String[] getUpgradeDatabaseScript(Context context, int oldVersion, int newVersion){
		return getUpgradeDatabaseFullScript(context, oldVersion, newVersion).split(";");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] script = getCreateDatabaseScript(context);
		for(int i = 0; i<script.length;i++){
			db.execSQL(script[i]);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String[] script = getUpgradeDatabaseScript(context, oldVersion, newVersion);
		for(int i = 0; i<script.length;i++){
			db.execSQL(script[i]);
		}
	}
}
