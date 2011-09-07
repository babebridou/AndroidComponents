package com.fairyteller.android.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FairyListActivity extends Activity {
	
	private FairyListAdapter<List<String>> stringAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        stringAdapter = new FairyListAdapter<List<String>>(this) {
			
			@Override
			public void updateRendererView(List<String> bean, View view) {
				ListView listView = (ListView)view;
				((FairyListAdapter<String>)listView.getAdapter()).setBeans(bean);
			}
			
			@Override
			public View createRendererView(ViewGroup parent, Context context) {
				FairyListAdapter<String> sublistAdapter = new FairyListAdapter<String>(FairyListActivity.this) {
					
					@Override
					public void updateRendererView(String bean, View view) {
						TextView textView = (TextView) view;
						textView.setText(bean);
					}
					
					@Override
					public View createRendererView(ViewGroup parent, Context context) {
						TextView textView = new TextView(context);
						return textView;
					}
				};
				ListView listView = new ListView(context);
				listView.setAdapter(sublistAdapter);
				return listView;
			}
		};
		ListView listView = (ListView)findViewById(R.id.list);
		listView.setAdapter(stringAdapter);
		List<List<String>> textsInListInList = new ArrayList<List<String>>();
		List<String> textsInList1 = Arrays.asList(new String[]{"hello", "world"});
		List<String> textsInList2 = Arrays.asList(new String[]{"hello too", "world2"});
		List<String> textsInList3 = Arrays.asList(new String[]{"hello three", "world3"});
		textsInListInList.add(textsInList1);
		textsInListInList.add(textsInList2);
		textsInListInList.add(textsInList3);
		stringAdapter.setBeans(textsInListInList);
    }
}