package com.fairyteller.db.framework;


public abstract class BaseDao implements IDao {

	protected IDaoFactory daoFactory;
	
	
	@Override
	public void setDaoFactory(IDaoFactory factory) {
		this.daoFactory = factory;
	}
	
	protected IDaoFactory getFactory(){
		return this.daoFactory;
	}
}
