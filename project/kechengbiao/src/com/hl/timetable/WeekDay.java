package com.hl.timetable;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WeekDay extends ListActivity{
	
	private final int madmin = Menu.FIRST;
	private final int mclose = Menu.FIRST+1;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        String[] weekDay = new String[]{
	        		"������", "����һ", "���ڶ�", "������", "������", "������ ", "������"
	        };
	        ArrayAdapter<String> adapter = 
	        	new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekDay);
	        setListAdapter(adapter);
	        setTitle("�γ̱����");
	    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(this, Edit.class);
		i.putExtra("dayofweek", ((TextView)v).getText().toString());
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, madmin, Menu.NONE, "�γ̱�")
		.setIcon(R.drawable.timetable);
		menu.add(0, mclose, Menu.NONE, "�ر�")
		.setIcon(R.drawable.mclose);
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case madmin: {
	  		Intent i = new Intent(this, ShowTable.class);
	  		startActivity(i);
	  		finish();
	  		return true;
	  	}
	  	case mclose: {
	  		finish();
	  		return true;
	  	}
	  	default:
		  return false;
	  }   
	}
}
