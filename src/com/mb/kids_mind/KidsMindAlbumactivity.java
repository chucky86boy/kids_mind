package com.mb.kids_mind;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mb.kids_mind.Adapter.KidsMindAlbumAdater;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.AlbumItem;

public class KidsMindAlbumactivity extends Activity {
KidsMindAlbumAdater adapter;
SQLiteDatabase db;
MyHelper helper;
private static final String TAG="MainActivity";
private String userid;
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
	 SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
     Intent intent=getIntent();
      userid=intent.getStringExtra("user_id");
     selectAlbum(userid);
    editor.putString("albumuserid", userid);
    editor.commit();
		
     adapter=new KidsMindAlbumAdater(KidsMindAlbumactivity.this, R.layout.albumlistview, albumitem);
    
     list.setAdapter(adapter);
     list.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view,
				int position, long arg3) {
			// TODO Auto-generated method stub
			Log.v(TAG,"delete");

			ArrayList<AlbumItem> list=new ArrayList<AlbumItem>();
			list=adapter.getList();
			AlbumItem info=list.get(position);
			albumdelete(KidsMindAlbumactivity.this,info.getImage_path());
			
			return true;
		}
    });

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
				Log.v(TAG,"select");	
				
			}
		});
	    // TODO Auto-generated method stub
	}
	void deleteRec(String filename){
		openDB();
		try{
			int cnt=db.delete("km_check","fName=?",new String[]{filename});
		}catch(SQLException e){
		}
		closeDB();
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
	
	 Dialog dialog=null;
		
     void albumdelete(Activity context,final String imagepath) {
 		// Create dialog
 		 dialog = new Dialog(context);
 		dialog.getWindow().setBackgroundDrawable

 		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
 		dialog.setCancelable(false);
 		dialog.setCanceledOnTouchOutside(false);
 		dialog.setContentView(R.layout.profile);
 		dialog.findViewById(R.id.regpic).setOnClickListener(
 				new OnClickListener() {

 					@Override
 					public void onClick(View v) {
 						deleteRec(imagepath);
 						String path2 = Environment.getExternalStorageDirectory()+"/KidsMind/"+imagepath;
 						File file =new File(path2);
 						file.delete();
 						Log.v(TAG,"삭제 성공");
 						SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
 						userid=pref.getString("albumuserid", "");
 						selectAlbum(userid);
 						adapter.setList(albumitem);
 						adapter.notifyDataSetChanged();
 						dialog.dismiss();
 					}
 				});
 		dialog.findViewById(R.id.canpic).setOnClickListener(
 				new OnClickListener() {

 					@Override
 					public void onClick(View v) {
 						
 						dialog.dismiss();
 					}
 				});

 		dialog.show();
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
