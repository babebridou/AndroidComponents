package com.fairyteller.db;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import com.fairyteller.db.example.UserBean;
import com.fairyteller.db.example.UserDao;
import com.fairyteller.db.framework.AbstractSQLiteHelper;
import com.fairyteller.db.framework.DBManager;
import com.fairyteller.db.framework.DefaultDaoFactory;
import com.fairyteller.db.framework.SqlSequenceHacker;

public class FairyDBActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        DBManager dbManager = DBManager.getInstance();
        AbstractSQLiteHelper sqlHelper = new AbstractSQLiteHelper(this, "testdb", null, 1) {
			
			@Override
			protected String getUpgradeDatabaseFullScript(Context context,
					int oldVersion, int newVersion) {
				StringBuilder sb = new StringBuilder();
				sb.append(SqlSequenceHacker.dropSequenceScript("USER"));
				sb.append(SqlSequenceHacker.createSequenceScript("USER"));
				sb.append(context.getString(R.string.db_drop_table_t_user));
				sb.append(context.getString(R.string.db_create_table_t_user));
				return sb.toString();
			}
			
			@Override
			protected String getCreateDatabaseFullScript(Context context) {
				StringBuilder sb = new StringBuilder();
//				sb.append(SqlSequenceHacker.dropSequenceScript("USER"));
				sb.append(SqlSequenceHacker.createSequenceScript("USER"));
//				sb.append(context.getString(R.string.db_drop_table_t_user));
				sb.append(context.getString(R.string.db_create_table_t_user));
				return sb.toString();
			}
		};
		DefaultDaoFactory daoFactory = new DefaultDaoFactory(sqlHelper);
        UserDao userDao = new UserDao();
        daoFactory.registerDao("userDao", userDao);
        dbManager.initDBManager(daoFactory);
        
        UserBean user = new UserBean();
        user.setFirstName("Thomas-"+SystemClock.uptimeMillis());
        user.setLastName("Philipakis-"+SystemClock.uptimeMillis());
        System.out.println(user);
        UserDao myDao = ((UserDao)dbManager.getDaoFactory().getDao("userDao"));
        List<UserBean> users = myDao.getUsers(this);
        System.out.println("querying users in db : ");
        if(users!=null){
        for(UserBean u : users) {
        	System.out.println(u);
        }
        } else {
        	System.out.println("no users.");
        }
        myDao.insertUser(user, this);
        users = myDao.getUsers(this);
        System.out.println("querying users in db : ");
        if(users!=null){
        for(UserBean u : users) {
        	System.out.println(u);
        }
        } else {
        	System.out.println("no users.");
        }
    }
}