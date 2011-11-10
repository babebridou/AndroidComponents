package com.fairyteller.db.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

public class SQLAdapter<T> {

	/**
	 * TAG used for logging
	 */
	private static final String TAG = "SQLAdapter";

	/**
	 * replaces sections enclosed # with their values in the params map 
	 * 
	 * TODO : add an escape method
	 * 
	 * @param sql: the sql query with parameters enclosed between '#'s
	 * @param params
	 * @return the final sql query to be executed
	 * 
	 * example:
	 * sql:
	 * select #someVariable#||' '||#someOtherVariable# as THE_ANSWER from dual;
	 * params:
	 * {"someVariable" : 42, "someOtherVariable", "is the answer"}
	 * 
	 * returns:
	 * "select '42'||' '||'is the answer' as THE_ANSWER from dual;"
	 * 
	 */
	@Deprecated
	private String prepareSqlStatement(String sql, Map<String, Object> params) {
		if (params != null) {
			Iterator<String> mapIterator = params.keySet().iterator();
			while (mapIterator.hasNext()) {
				String key = mapIterator.next();
				Object value = params.get(key);
				sql = sql.replaceAll("#" + key + "#", value == null ? null
						: "'" + value.toString() + "'");
			}
		}
		if (sql.indexOf('#') != -1) {
			Log.e(TAG, "undefined parameter : " + sql);
			return null;
		}
		return sql;
	}

	private SqlStatementArguments prepareStatement(String sql, Map<String, Object> params){
		List<String> orderedParams = null;
		if(params==null){
			SqlStatementArguments arg = new SqlStatementArguments();
			arg.sql = sql;
			return arg;	
		}
		if(sql.indexOf("#")<0){
			SqlStatementArguments arg = new SqlStatementArguments();
			arg.sql = sql;
			return arg;	
		}
		orderedParams = new ArrayList<String>();
		SparseArray<String> sparray = new SparseArray<String>();
		Iterator<String> mapIterator0 = params.keySet().iterator();
		List<Integer> indexes = new ArrayList<Integer>();
		
		while (mapIterator0.hasNext()) {
			String key = mapIterator0.next();
			int i = 0;
			String labelToSearch = "#"+key+"#";
			int indexOfKey = sql.indexOf(labelToSearch, i);
			while(indexOfKey>-1){
				indexes.add(indexOfKey);
				sparray.append(indexOfKey, key);
				i+=indexOfKey+labelToSearch.length();
				indexOfKey = sql.indexOf(labelToSearch, i);
			}
		}
		
		Collections.sort(indexes);
		
		for(Integer index : indexes){
			String key = sparray.get(index);
			Object value = params.get(key);
			orderedParams.add(value.toString());
		}
		
		//replace the query with ?s
		Iterator<String> mapIterator = params.keySet().iterator();
		while (mapIterator.hasNext()) {
			String key = mapIterator.next();
			sql = sql.replaceAll("#" + key + "#", "?");
		}
		
		SqlStatementArguments arg = new SqlStatementArguments();
		arg.sql = sql;
		arg.arguments = orderedParams.toArray(new String[orderedParams.size()]);
		return arg;
	}
	
	
	/**
	 * go lookup the query in the Resources managed by the Android framework
	 * @param resId : the id of the resource, for example R.string.myquery
	 * @param context
	 * @return
	 */
	private String fetchSqlStatement(int resId, Context context) {
		return context.getString(resId);
	}

	/**
	 * go lookup the query in the Resources managed by the Android framework
	 * the String is the name of the String resource
	 * @param queryId : the name of the String resource, for example "myquery"
	 * @param context
	 * @return
	 */
	private String fetchSqlStatement(String queryId, Context context) {
		int pointer = context.getResources().getIdentifier(queryId, "string",
				context.getPackageName());
		if (pointer == 0) {
			Log.e(TAG, "undefined sql id " + queryId);
			return null;
		}
		String sql = context.getResources().getString(pointer);
		return sql;
	}

	/**
	 * Queries the database using an Android Resource Id and returns a bean.
	 * Will use the BeanBuilder to actually creates a bean from the cursor.
	 * The Bean Builder can in turn use QueryForObject using some refId to build properties that require subqueries
	 * @param context
	 * @param db
	 * @param queryResId
	 * @param params
	 * @param beanBuilder
	 * @return
	 */
	public T queryForObject(Context context, SQLiteDatabase db, int queryResId,
			Map<String, Object> params, BeanBuilder<T> beanBuilder) {
		return queryForObject(
				db,
				prepareStatement(fetchSqlStatement(queryResId, context),
						params), beanBuilder);
	}

	/**
	 * Queries the database for a bean using the Name of the Android resource as query.
	 * 
	 * @param context
	 * @param db
	 * @param queryId
	 * @param params
	 * @param beanBuilder
	 * @return
	 */
	public T queryForObject(Context context, SQLiteDatabase db, String queryId,
			Map<String, Object> params, BeanBuilder<T> beanBuilder) {
		return queryForObject(
				db,
				prepareStatement(fetchSqlStatement(queryId, context), params),
				beanBuilder);
	}

	public T queryForObject(SQLiteDatabase db, String sql,
			Map<String, Object> params, BeanBuilder<T> beanBuilder) {
		return queryForObject(db, prepareStatement(sql, params), beanBuilder);
	}

	public T queryForObject(Context context, SQLiteDatabase db, int queryResId,
			BeanBuilder<T> beanBuilder) {
		return queryForObject(db, fetchSqlStatement(queryResId, context),
				beanBuilder);
	}

	public T queryForObject(Context context, SQLiteDatabase db, String queryId,
			BeanBuilder<T> beanBuilder) {
		return queryForObject(db, fetchSqlStatement(queryId, context),
				beanBuilder);
	}

	public List<T> queryForList(Context context, SQLiteDatabase db,
			int queryResId, Map<String, Object> params,
			BeanBuilder<T> beanBuilder) {
		return queryForList(
				db,
				prepareStatement(fetchSqlStatement(queryResId, context),
						params), beanBuilder);
	}

	public List<T> queryForList(Context context, SQLiteDatabase db,
			String queryId, Map<String, Object> params,
			BeanBuilder<T> beanBuilder) {
		return queryForList(
				db,
				prepareStatement(fetchSqlStatement(queryId, context), params),
				beanBuilder);
	}

	public List<T> queryForList(SQLiteDatabase db, String sql,
			Map<String, Object> params, BeanBuilder<T> beanBuilder) {
		return queryForList(db, prepareStatement(sql, params), beanBuilder);
	}

	public List<T> queryForList(Context context, SQLiteDatabase db,
			int queryResId, BeanBuilder<T> beanBuilder) {
		return queryForList(db, fetchSqlStatement(queryResId, context),
				beanBuilder);
	}

	public List<T> queryForList(Context context, SQLiteDatabase db,
			String queryId, BeanBuilder<T> beanBuilder) {
		return queryForList(db, fetchSqlStatement(queryId, context),
				beanBuilder);
	}

	public T queryForObject(SQLiteDatabase db, String sql,
			BeanBuilder<T> beanBuilder) {
		T result = null;
		Cursor c = db.rawQuery(sql, null);
		Log.d("DATABASE", "query executed : " + c.getCount() + " rows found ");
		if (c != null && c.getCount() == 1) {
			c.moveToFirst();
			result = beanBuilder.buildBean(c, db);
			c.close();
		} else if (c != null) {
			c.close();
		}
		return result;
	}
	
	private T queryForObject(SQLiteDatabase db,  SqlStatementArguments arguments,
			BeanBuilder<T> beanBuilder) {
		T result = null;
		Cursor c = db.rawQuery(arguments.sql, arguments.arguments);
		Log.d("DATABASE", "query executed : " + c.getCount() + " rows found ");
		if (c != null && c.getCount() == 1) {
			c.moveToFirst();
			result = beanBuilder.buildBean(c, db);
			c.close();
		} else if (c != null) {
			c.close();
		}
		return result;
	}

	public List<T> queryForList(SQLiteDatabase db, String sql,
			BeanBuilder<T> beanBuilder) {
		List<T> result = null;
		
		Cursor c = db.rawQuery(sql, null);
		Log.d("DATABASE", "query executed : " + c.getCount() + " rows found ");
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isLast()) {
				T bean = beanBuilder.buildBean(c, db);
				if (result == null) {
					result = new ArrayList<T>();
				}
				result.add(bean);
				c.moveToNext();
			}
			if (c.isLast()) {
				T bean = beanBuilder.buildBean(c, db);
				if (result == null) {
					result = new ArrayList<T>();
				}
				result.add(bean);
			}
			c.close();
		} else if (c != null) {
			c.close();
		}
		return result;
	}
	
	private List<T> queryForList(SQLiteDatabase db, SqlStatementArguments arguments,
			BeanBuilder<T> beanBuilder) {
		List<T> result = null;
		
		Cursor c = db.rawQuery(arguments.sql, arguments.arguments);
		Log.d("DATABASE", "query executed : " + c.getCount() + " rows found ");
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isLast()) {
				T bean = beanBuilder.buildBean(c, db);
				if (result == null) {
					result = new ArrayList<T>();
				}
				result.add(bean);
				c.moveToNext();
			}
			if (c.isLast()) {
				T bean = beanBuilder.buildBean(c, db);
				if (result == null) {
					result = new ArrayList<T>();
				}
				result.add(bean);
			}
			c.close();
		} else if (c != null) {
			c.close();
		}
		return result;
	}

	private void update(SQLiteDatabase db, String sql) {
		db.execSQL(sql);
	}

	private void update(SQLiteDatabase db, SqlStatementArguments arguments) {
		System.out.println("DEBUGGING QUERY");
		System.out.println(arguments.sql);
		if(arguments.arguments!=null&&arguments.arguments.length>0){
			for(int i = 0; i<arguments.arguments.length;i++){
				System.out.println(arguments.arguments[i]);
			}
		}
		db.execSQL(arguments.sql, arguments.arguments);
	}
	
	public void update(SQLiteDatabase db, String sql, Map<String, Object> params) {
		if(params==null)
			params = new HashMap<String, Object>();
		update(db, prepareStatement(sql, params));
	}

	public void update(Context context, SQLiteDatabase db, int queryResId,
			Map<String, Object> params) {
		if(params==null)
			params = new HashMap<String, Object>();
		update(db,
				prepareStatement(fetchSqlStatement(queryResId, context),
						params));
	}

	public void update(Context context, SQLiteDatabase db, String queryId,
			Map<String, Object> params) {
		if(params==null)
			params = new HashMap<String, Object>();
		update(db,
				prepareStatement(fetchSqlStatement(queryId, context), params));
	}

	public void update(SQLiteDatabase db, String sql, String beanPrefix,
			Map<String, Object> params, T bean, BeanInserter<T> beanInserter) {
		if(params==null)
			params = new HashMap<String, Object>();
		beanInserter.updateMapWithBeanValue(bean, params, beanPrefix);
		update(db, prepareStatement(sql, params));
	}

	public void update(Context context, SQLiteDatabase db, int queryResId,
			String beanPrefix, Map<String, Object> params, T bean,
			BeanInserter<T> beanInserter) {
		if(params==null)
			params = new HashMap<String, Object>();
		beanInserter.updateMapWithBeanValue(bean, params, beanPrefix);
		update(db,
				prepareStatement(fetchSqlStatement(queryResId, context),
						params));
	}

	public void update(Context context, SQLiteDatabase db, String queryId,
			String beanPrefix, Map<String, Object> params, T bean,
			BeanInserter<T> beanInserter) {
		if(params==null)
			params = new HashMap<String, Object>();
		beanInserter.updateMapWithBeanValue(bean, params, beanPrefix);
		update(db,
				prepareStatement(fetchSqlStatement(queryId, context), params));
	}

	public long insert(Context context, SQLiteDatabase db, int queryResId,
			String beanPrefix, Map<String, Object> params, T bean,
			BeanInserter<T> beanInserter) {
		if(params==null)
			params = new HashMap<String, Object>();
		beanInserter.updateMapWithBeanValue(bean, params, beanPrefix);
		long sequenceNextVal = SqlSequenceHacker.sequenceNextVal(db,
				beanInserter.getSequenceName());
		params.put(beanInserter.getSequenceProperty(beanPrefix), sequenceNextVal);
		update(context, db, queryResId, params);
		beanInserter.updateBeanWithSequenceValue(bean, sequenceNextVal);
		return sequenceNextVal;
	}

	public long insert(Context context, SQLiteDatabase db, String queryId,
			String beanPrefix, Map<String, Object> params, T bean,
			BeanInserter<T> beanInserter) {
		if(params==null)
			params = new HashMap<String, Object>();
		beanInserter.updateMapWithBeanValue(bean, params, beanPrefix);
		long sequenceNextVal = SqlSequenceHacker.sequenceNextVal(db,
				beanInserter.getSequenceName());
		params.put(beanInserter.getSequenceProperty(beanPrefix), sequenceNextVal);
		update(context, db, queryId, params);
		beanInserter.updateBeanWithSequenceValue(bean, sequenceNextVal);
		return sequenceNextVal;
	}

	public long insert(SQLiteDatabase db, String sql, String beanPrefix,
			Map<String, Object> params, T bean, BeanInserter<T> beanInserter) {
		if(params==null)
			params = new HashMap<String, Object>();
		beanInserter.updateMapWithBeanValue(bean, params, beanPrefix);
		long sequenceNextVal = SqlSequenceHacker.sequenceNextVal(db,
				beanInserter.getSequenceName());
		params.put(beanInserter.getSequenceProperty(beanPrefix), sequenceNextVal);
		update(db, sql, params);
		beanInserter.updateBeanWithSequenceValue(bean, sequenceNextVal); 
		return sequenceNextVal;
	}

	private final class SqlStatementArguments{
		String sql;
		String[] arguments;
	}
}
