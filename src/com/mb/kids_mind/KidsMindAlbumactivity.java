package com.mb.kids_mind;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.mb.kids_mind.Adapter.KidsMindAdviceListAdater;
import com.mb.kids_mind.Adapter.KidsMindAlbumAdater;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.AlbumItem;
import com.mb.kids_mind.Item.Const;

import com.mb.kids_mind.listener.BackListener;

public class KidsMindAlbumactivity extends Activity {
KidsMindAlbumAdater adapter;
KidsMindAdviceListAdater ladapter;
SQLiteDatabase db;
private AQuery aquery;
MyHelper helper;
String check;
private static final String TAG="MainActivity";
private String userid;
ArrayList<AlbumItem> albumitem;
public static BackListener listener;
ImageView loadingimg;
private AnimationDrawable loadingViewAnim=null;
ListView list;
LinearLayout linear,loading;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	setContentView(R.layout.album);
	
	aquery = new AQuery(this);
	findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		finish();	
		}
	});
	findViewById(R.id.textView1).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		finish();	
		}
	});
findViewById(R.id.home).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		    SharedPreferences.Editor editor=pref.edit();
		    editor.putString("noti", "");
		    editor.commit();
			Intent intent=new Intent(KidsMindAlbumactivity.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("check", "1");
			startActivity(intent);
		}
	});
	 helper = new MyHelper(this, "kidsmind.db", null, 1);
	 list=(ListView)findViewById(R.id.albumlist);
	 list.setDivider(null);
	 loading=(LinearLayout)findViewById(R.id.loading);
		
		loadingimg=(ImageView)findViewById(R.id.loadingimg);
		loadingimg.setBackgroundResource(R.anim.progress);
		loading.setVisibility(View.GONE);
		loadingimg.setVisibility(View.GONE);
		loadingViewAnim = (AnimationDrawable) loadingimg.getBackground();
		loadingimg.post(new Starter());
	
	    // TODO Auto-generated method stub
	}
	 class Starter implements Runnable {
         

		public void run() {
           //start Asyn Task here   
           new LongOperation().execute();
         }
     }
	 private class LongOperation extends AsyncTask<View, Void, String> {
		  View v=null;
	        @Override
	        protected String doInBackground(View... params) {
	        	 SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
	     		SharedPreferences.Editor editor=pref.edit();
	          Intent intent=getIntent();
	           userid=intent.getStringExtra("user_id");
	           check=intent.getStringExtra("where");
	           if("advice".equals(check)){
	         	  selectAlbum2("1",userid);
//	         	  ladapter=new KidsMindAdviceListAdater(KidsMindAlbumactivity.this, R.layout.advicelistview, albumitem);
//	         	     list.setAdapter(ladapter);
	         	    
	           }else{
	          selectAlbum(userid);
//	          adapter=new KidsMindAlbumAdater(KidsMindAlbumactivity.this, R.layout.albumlistview, albumitem);
//	          list.setAdapter(adapter);
	          
	           }
	          editor.putString("albumuserid", userid);
	         editor.commit();
	     		
	         
	          list.setOnItemLongClickListener(new OnItemLongClickListener() {

	     		@Override
	     		public boolean onItemLongClick(AdapterView<?> arg0, View view,
	     				int position, long arg3) {
	     			// TODO Auto-generated method stub
	     			Log.v(TAG,"delete");

	     			if("advice".equals(check)){
	     				//advice list;
	     		    	  selectAlbum2("1",userid);
	     		    	  ladapter=new KidsMindAdviceListAdater(KidsMindAlbumactivity.this, R.layout.advicelistview, albumitem);
	     		    	    
	     		    	  
	     		      }else{
	     		     ArrayList<AlbumItem> list=new ArrayList<AlbumItem>();
	     				list=adapter.getList();
	     				AlbumItem info=list.get(position);
	     				int advice_id=info.getAdvice_id();
	     				
	     				albumdelete(KidsMindAlbumactivity.this,info.getImage_path(),advice_id+"");
	     				
	     		     
	     		      }
	     			
	     			return true;
	     		}
	         });

	          list.setOnItemClickListener(new OnItemClickListener() {

	     			@Override
	     			public void onItemClick(AdapterView<?> arg0, View view, int position,
	     					long arg3) {
	     			
	     				if("advice".equals(check)){
	     					ArrayList<AlbumItem> list=new ArrayList<AlbumItem>();
	     					list=ladapter.getList();
	     					AlbumItem info=list.get(position);
	     					
	     					
	     					
//	     						KidsMindAlbumactivity.listener.onBackPressed();
//	     					Log.v(TAG,"listener");
	     					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
	     					SharedPreferences.Editor editor=pref.edit();
	     					editor.putString("fName", info.getImage_path());
	     					editor.putString("qposition", info.getQuestioin());
	     					
	     					editor.commit();
	     					Intent in =new Intent(KidsMindAlbumactivity.this,KidsMindAdviceActivity.class);
	     					in.putExtra("where", "album");
	     					in.putExtra("advice_id", info.getAdvice_id());
	     					in.putExtra("savename", info.getImage_path());
	     					in.putExtra("date", info.getDate());
	     					startActivity(in);
	     				//	Intent in= new Intent(KidsMindAlbumactivity.this,K)
	     					Log.v(TAG,"select");	
	     					
	     			      }else{
	     			    	  ArrayList<AlbumItem> list=new ArrayList<AlbumItem>();
	     						list=adapter.getList();
	     						AlbumItem info=list.get(position);
	     						SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
	     						SharedPreferences.Editor editor=pref.edit();
	     						editor.putString("fName", info.getImage_path());
	     						editor.commit();
	     						Intent in= new Intent(KidsMindAlbumactivity.this,KidsMindAlbumDetailActivity.class);
	     						in.putExtra("savename", info.getImage_path());
	     						in.putExtra("date", info.getDate());
	     						in.putExtra("advice_id", info.getAdvice_id());
	     						in.putExtra("qposition", info.getQuestioin());
	     						startActivity(in);
	     						Log.v(TAG,"select");	
	     							
	     			     
	     			      }	
	     				
	     				
	     				
	     				
	     				
	     			}
	     		});
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return "Executed";
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	 Intent intent=getIntent();
	        	check=intent.getStringExtra("where");
		           if("advice".equals(check)){
		         	  selectAlbum2("1",userid);
		         	  ladapter=new KidsMindAdviceListAdater(KidsMindAlbumactivity.this, R.layout.advicelistview, albumitem);
		         	     list.setAdapter(ladapter);
		         	    
		           }else{
		          selectAlbum(userid);
		          adapter=new KidsMindAlbumAdater(KidsMindAlbumactivity.this, R.layout.albumlistview, albumitem);
		          list.setAdapter(adapter);
		          
		           }
	        //Stop Loading Animation
	        	loading.setVisibility(View.GONE);
	     
	        	loadingimg.setVisibility(View.GONE);
	        loadingViewAnim.stop();
	        }

	        @Override
	        protected void onPreExecute() {
	        //Start  Loading Animation
	        loading.setVisibility(View.VISIBLE);
	    
loadingimg.setVisibility(View.VISIBLE);
	        loadingViewAnim.start();
	        }

	        @Override
	        protected void onProgressUpdate(Void... values) {}
	    }

	public void asyncDeleteJson(String advice_id) {
		//openWaitDialog();

		//String url = "http://localhost:3083/namecheck" + "/" + name;
		String url=Const.DELETE+"/"+advice_id;
		Log.v(TAG,"URL"+url);
		aquery.ajax(url, JSONObject.class, this, "DeleteCallback");

		//sendView.setText(url);
	}


	String error;
	public void DeleteCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					Log.v(TAG,"삭제 완료");


					//	resultView.setText(json.toString());
				} else {
						openInfoMessageDialogBox(error);
				}
			} catch (JSONException e) {
				Log.v(TAG,"에러");
				openErrorDialog();
				e.printStackTrace();
			}
		} else {
			Log.v(TAG,"에러2");
			openErrorDialog();
		}
	}
	public void openInfoMessageDialogBox(String message) {
		Toast.makeText(KidsMindAlbumactivity.this, message, Toast.LENGTH_SHORT).show();
	}

	protected void openErrorDialog() { 
		//closeWaitDialog();
		aquery.ajaxCancel();

		openInfoMessageDialogBox("error.");
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
	
		String[] colNames = {"fName","question_id","detail_check","advice_id","question_title","date" };
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
			item.setAdvice_id(c.getInt(c.getColumnIndex("advice_id")));
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
	void selectAlbum2(String detail_check,String user_id){
		openDB();
		Log.v(TAG, "탭 디비 시작");
		Cursor c = null;
		albumitem=new ArrayList<AlbumItem>();
		String wStr = "detail_check=? AND user_id=?";
		String[] wherStr = { detail_check,user_id };
	
		String[] colNames = {"fName","question_id","detail_check","advice_id","question_title","date" };
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
			item.setAdvice_id(c.getInt(c.getColumnIndex("advice_id")));
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
		
     void albumdelete(Activity context,final String imagepath,final String advice_id) {
 		// Create dialog
 		 dialog = new Dialog(context);
 		dialog.getWindow().setBackgroundDrawable

 		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
 		dialog.setCancelable(false);
 		dialog.setCanceledOnTouchOutside(false);
 		dialog.setContentView(R.layout.deletedialog);
 		dialog.findViewById(R.id.enter).setOnClickListener(
 				new OnClickListener() {

 					@Override
 					public void onClick(View v) {
 						asyncDeleteJson(advice_id);
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
 		dialog.findViewById(R.id.cancel).setOnClickListener(
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
