package com.fairyteller.db.framework;


public class DBManager {

	private static DBManager instance;
	
	public static final DBManager getInstance(){
		if(instance==null){
			instance = new DBManager();
		}
		return instance;
	}
	
	private DBManager(){
		
	}
	
	public void initDBManager(IDaoFactory dao){
		this.dao = dao;
	}
	
	private IDaoFactory dao;
	
	public IDaoFactory getDaoFactory() {
		return dao;
	}
	
}
