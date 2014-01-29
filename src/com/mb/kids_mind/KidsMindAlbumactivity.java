package com.mb.kids_mind;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mb.kids_mind.Adapter.KidsMindAlbumAdater;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.AlbumItem;
import com.mb.kids_mind.Item.BabyInformationItem;

public class KidsMindAlbumactivity extends Activity {
KidsMindAlbumAdater adapter;
SQLiteDatabase db;
MyHelper helper;
private static final String TAG="MainActivity";
ArrayList<AlbumItem> albumitem;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	setContentView(R.layout.album);
	findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		finish();	
		}
	});
	 helper = new MyHelper(this, "kidsmind.db", null, 1);
	ListView list=(ListView)findViewById(R.id.albumlist);
	 list.setDivider(null);
     Intent intent=getIntent();
     String user_id=intent.getStringExtra("user_id");
     selectAlbum(user_id);
    	
		
     adapter=new KidsMindAlbumAdater(KidsMindAlbumactivity.this, R.layout.albumlistview, albumitem);
    
     list.setAdapter(adapter);
     list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				ArrayList<AlbumItem> list=new ArrayList<AlbumItem>();
				list=adapter.getList();
				AlbumItem info=list.get(position);
				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				editor.putString("fName", info.getImage_path());
				editor.commit();
			//	Intent in= new Intent(KidsMindAlbumactivity.this,K)
				
				
			}
		});
	    // TODO Auto-generated method stub
	}
	void selectAlbum(String user_id){
		openDB();
		Log.v(TAG, "탭 디비 시작");
		Cursor c = null;
		albumitem=new ArrayList<AlbumItem>();
		String wStr = "user_id=?";
		String[] wherStr = { user_id };
	
		String[] colNames = {"fName","question_id","detail_check","question_title","date" };
		try {
			c = db.query("km_check", colNames, wStr, wherStr, null,
					null, "_id desc");
		
//		
//		String sql ="select * from km_baby where "+user_name"+ order by _id DESC;";
//		Cursor c=null;
		Log.v(TAG,"select db");
//		try{
//			Log.v(TAG,"select db");
//			
//			c=db2.rawQuery(sql, null);
			Log.v(TAG,"숫자:"+c.getCount());
//			editor.putInt("babycount", c.getCount());
//			editor.commit();
			while(c.moveToNext()){
				AlbumItem item= new AlbumItem();
			item.setImage_path(c.getString(c.getColumnIndex("fName")));
			item.setQuestioin(c.getString(c.getColumnIndex("question_id")));
			item.setNewmessage(c.getString(c.getColumnIndex("detail_check")));
			item.setTitle(c.getString(c.getColumnIndex("question_title")));
			item.setDate(c.getString(c.getColumnIndex("date")));
			albumitem.add(item);
				//c.getString(c.getColumnIndex("question_id"));
	//	Log.v(TAG, c.getString(0));
				//	c.getString(0);
			}
			
			
		}catch(SQLException e){
			Log.v(TAG,"selec error"+e);
		}finally{
			if(c!=null){
			c.close();
			}
		}
	
		closeDB();
	}	
	
	void openDB(){
		Log.v(TAG,"opendb");
		
//		db = openOrCreateDatabase("sample.db", wi, null);
		db=helper.getWritableDatabase();
		
	}
	// dbClose();
	void closeDB(){
		if(db != null){
			if(db.isOpen()){
				db.close();
			}
		}
	}
}
