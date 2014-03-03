package com.mb.kids_mind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.mb.kids_mind.Helper.MyHelper;

public class KidsMindNotiActivity extends Activity {
	SQLiteDatabase db;
	MyHelper helper;
View.OnClickListener bHandler =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.button1:
			 PushWakeLock.releaseCpuLock();
			//메세제 받아서 해당 id값 보내서 보여주자
			 //히스토리 남기지 말고 부르자
			 Intent intent =new Intent(KidsMindNotiActivity.this,KidsMindAdviceActivity.class);
			 Log.v("MainActivity","date"+date+"savename"+savename);
			 intent.putExtra("date",date);
			 intent.putExtra("savename", savename);
			startActivity(intent);
			break;
		case R.id.button2:
			SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		    SharedPreferences.Editor editor=pref.edit();
		    editor.putString("noti", "on");
		    editor.commit();
			 PushWakeLock.releaseCpuLock();
			finish();
			break;
		}
	}
};
String advice_id;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    

	    setContentView(R.layout.noti);
	    findViewById(R.id.button1).setOnClickListener(bHandler);
	    findViewById(R.id.button2).setOnClickListener(bHandler);
	    Bundle bun = getIntent().getExtras();
	   advice_id = bun.getString("notiMessage");
	    Log.v("MainActivity","advice_id"+advice_id);
	    helper = new MyHelper(KidsMindNotiActivity.this, "kidsmind.db", null,
				1);
	    selectAll(advice_id);
	    // TODO Auto-generated method stub
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		    SharedPreferences.Editor editor=pref.edit();
		    editor.putString("noti", "on");
		    editor.commit();
		}
		return true;
	}
	void openDB() {
		// db = openOrCreateDatabase("sample.db", wi, null);
		if(helper!=null){
			db = helper.getWritableDatabase();
				
		}else{
			helper = new MyHelper(KidsMindNotiActivity.this, "kidsmind.db", null,
					1);
			db = helper.getWritableDatabase();
			
		}
	}

	// dbClose();
	void closeDB() {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	String savename;
	String date;
	public void selectAll(String advice_id) {
		openDB();
		
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		Cursor c = null;
		int adviceid = 0;
		String wStr = "advice_id=?";
		String[] wherStr = { advice_id };

		String[] colNames = { "fName","date" };
		
			
			try {
			c = db.query("km_check", colNames, wStr, wherStr, null, null,
					"_id desc");
			while (c.moveToNext()) {
				// c.getString(c.getColumnIndex("question_id"));
				savename = c.getString(c.getColumnIndex("fName"));
				date=c.getString(c.getColumnIndex("date"));
			}

		} catch (SQLException e) {
			//Log.v(TAG, "selec error" + e);
		} catch (IllegalArgumentException e){
			//Log.v(TAG,"illagal"+e);
		}finally {
		
			if (c != null) {
				c.close();
			}
		}

	}
}
