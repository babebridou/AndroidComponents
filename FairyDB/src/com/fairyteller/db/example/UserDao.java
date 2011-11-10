package com.fairyteller.db.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fairyteller.db.R;
import com.fairyteller.db.framework.BaseDao;
import com.fairyteller.db.framework.BeanBuilder;
import com.fairyteller.db.framework.BeanInserter;
import com.fairyteller.db.framework.SQLAdapter;

public class UserDao extends BaseDao{
	
	private SQLAdapter<UserBean> userSqlAdapter;
	
	public UserDao() {
		this.userSqlAdapter = new SQLAdapter<UserBean>();
	}
	
	public List<UserBean> getUsers(Context context){
		SQLiteDatabase db = getFactory().openReadOnlyConnection();
		List<UserBean> users = userSqlAdapter.queryForList(context, db, R.string.db_user_findAllUsers, new UserBeanBuilder());
		db.close();
		return users;
	}
	
	public void insertUser(UserBean user, Context context){
		SQLiteDatabase db = getFactory().openWriteableConnection();
		db.beginTransaction();
		userSqlAdapter.insert(context, db, R.string.db_user_insertUser, "user.", null, user, new UserBeanInserter());
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	private final class UserBeanBuilder extends BeanBuilder<UserBean>{
		@Override
		public UserBean buildBean(Cursor cursor, SQLiteDatabase db) {
			try {
			UserBean bean = new UserBean();
			bean.setIdUser(getLong("ID_USER", cursor));
			bean.setFirstName(getString("FIRST_NAME", cursor));
			bean.setLastName(getString("LAST_NAME", cursor));
			return bean;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private final class UserBeanInserter extends BeanInserter<UserBean>{
		@Override
		public String getSequenceName() {
			return "USER";
		}
		@Override
		public String getSequenceProperty(String prefix) {
			return prefix+"idUser";
		}
		@Override
		public void updateBeanWithSequenceValue(UserBean bean, long sequenceId) {
			bean.setIdUser(sequenceId);
		}
		@Override
		public void updateMapWithBeanValue(UserBean bean,
				Map<String, Object> values, String prefix) {
			if(values == null){
				values = new HashMap<String, Object>();
			}
			values.put(prefix+"idUser", bean.getIdUser());
			values.put(prefix+"firstName", bean.getFirstName());
			values.put(prefix+"lastName", bean.getLastName());
		}
	}
}
