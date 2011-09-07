package com.fairyteller.android.list;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class FairyListAdapter<T> extends BaseAdapter{
	private List<T> beans;
	private Context context;
	
	public FairyListAdapter(Context context) {
		super();
		this.context = context;
	}
	
	public void setBeans(List<T> beans){
		this.beans = beans;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return beans==null?0:beans.size();
	}
	@Override
	public Object getItem(int position) {
		
		return beans!=null?beans.get(position):null;
	}
	public T getBean(int position){
		return beans!=null?beans.get(position):null;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public abstract View createRendererView(ViewGroup parent, Context context);
	
	public abstract void updateRendererView(T bean, View view);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = createRendererView(parent, context);
		}
		
		T bean = getBean(position);
		updateRendererView(bean, convertView);
		
		return convertView;
	}
	
}
