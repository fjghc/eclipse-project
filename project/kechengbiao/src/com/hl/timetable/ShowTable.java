package com.hl.timetable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowTable extends Activity{
	private final int madmin = Menu.FIRST;
	private final int mclose = Menu.FIRST+1;
	private final int mhome = Menu.FIRST+2;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] weekarr = new String[]{"��","һ","��","��","��","��","��"};
        String[] daytype = new String[]{"0", "1", "2", "3", "4", "5"};
        String[] str = new String[]{
        							"��","��","��","��","��","��","��",
        							"��","��","��","��","��","��","��",
        							"��","��","��","��","��","��","��",
        							"��","��","��","��","��","��","��",
        							"��","��","��","��","��","��","��",
        							"��","��","��","��","��","��","��"
        		};
        setContentView(R.layout.main);
        
        GridView gweek = (GridView)findViewById(R.id.weekday);
        GridView gtype = (GridView)findViewById(R.id.gtable);
        GridView gclass = (GridView)findViewById(R.id.gclass);
        
        gweek.setBackgroundColor(0xff222222);
        gtype.setBackgroundColor(0xff222222);
        gweek.setClickable(false);
        gweek.setFocusable(false);
        gtype.setFocusable(false);
        gtype.setClickable(false);
        
        
        gweek.setAdapter(new ArrayAdapter<String>(this, R.layout.gridviewitem, weekarr));
        gtype.setAdapter(new ArrayAdapter<String>(this, R.layout.gridviewitem, daytype));
        gclass.setAdapter(new ArrayAdapter<String>(this, R.layout.c_item, str));
        gclass.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				
				String[] cType = new String[]{"00", "12", "34", "56", "78", "90"}; 
				String[] weekDay = new String[]{
		        		"������", "����һ", "���ڶ�", "������", "������", "������ ", "������"
		        };
				AlertDialog.Builder alert = new AlertDialog.Builder(ShowTable.this);
				alert.setIcon(android.R.drawable.ic_dialog_alert);
				alert.setTitle("��ϸ...");
				alert.setMessage("����������Ϣ����");
				alert.setPositiveButton("ȷ��", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				int row = pos/7;//��
				int col = pos%7;//��
				String sql="select * from "+DataBaseInfo.M_TABLE+" where "+
				DataBaseInfo.M_DAY + " = '"+weekDay[col]+"'"+" and "+DataBaseInfo.M_TYPE+" = '"+cType[row]+"'";
				DataBaseHelper helper = new DataBaseHelper(ShowTable.this, DataBaseInfo.DB_NAME, null, 1);
				SQLiteDatabase db = helper.getReadableDatabase();
				Cursor c = db.rawQuery(sql, null);
				c.moveToFirst();
				while(!c.isAfterLast()){
					if(c.getString(3).trim().length()>0)
						alert.setMessage("��Ŀ��"+c.getString(2)+"\n�ص㣺"+c.getString(3));
					else
						alert.setMessage("û�п�");
					
					c.moveToNext();
				}
				db.close();
				alert.show();
			}
		});
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, madmin, Menu.NONE, "����").setIcon(R.drawable.admin);
		menu.add(0, mhome, Menu.NONE, "home").setIcon(R.drawable.home);
		menu.add(0, mclose, Menu.NONE, "�ر�").setIcon(R.drawable.mclose);
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case madmin: {
	  		Intent i = new Intent(this, WeekDay.class);
	  		startActivity(i);
	  		finish();
	  		return true;
	  	}
	  	case mhome: {
	  		Intent i = new Intent(this, TimeTable.class);
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
