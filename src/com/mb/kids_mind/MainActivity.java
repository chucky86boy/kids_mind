package com.mb.kids_mind;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mb.kids_mind.Adapter.AddBabyAdater;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.BabyInformationItem;
import com.mb.kids_mind.fragment.SketchMenu;
import com.mb.kids_mind.listener.MainSideMenuListener;

public class MainActivity extends Activity {
	FragmentManager fm=null;
	KidsMindDBHelper myDbHelper=null;
    private static final String TAG="MainActivity";
    ArrayList<BabyInformationItem> babyitem;
View.OnClickListener bHandler =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.mypage:
				//my page
				
				break;
			case R.id.add:
				Intent in=new Intent(MainActivity.this,KidsMindAddActivity.class);
				startActivityForResult(in, 0);
				//adapter notifydatachaged 해주기
				break;
			case R.id.notic://이벤트 공지사항
				break;
			case R.id.setting://
			break;
//			case R.id.login:
//				popuplogin(MainActivity.this);
//				
//				break;
			}
		}
	};
	SQLiteDatabase db,db2;
	MyHelper helper;
	TextView name;
	ListView addlist;
	ImageView mypage,login;
	AddBabyAdater adapter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
        name=(TextView)findViewById(R.id.mainname);
        helper = new MyHelper(this, "kidsmind.db", null, 1);
        myDbHelper=new KidsMindDBHelper(MainActivity.this);
		 
		try{
			myDbHelper.createDataBase();
			closeDB();
		}catch(IOException ioe){
			Log.v(TAG,"error"+ioe);
		}
        addlist=(ListView)findViewById(R.id.listView1);
        addlist.setDivider(null);
        selectAll();
        int height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*babyitem.size();
        LayoutParams lp=(LayoutParams) addlist.getLayoutParams();
        lp.height=height;
        addlist.setLayoutParams(lp);
        
        adapter=new AddBabyAdater(MainActivity.this, R.layout.babyadapter, babyitem);
       
        addlist.setAdapter(adapter);
        login=(ImageView)findViewById(R.id.login);
        login.setImageResource(R.drawable.btn_login);
        login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v(TAG,"onclick");
				Intent intent=new Intent(MainActivity.this,KidsMindLoginActivity.class);
				startActivity(intent);
			}
		});
        findViewById(R.id.menuToggler).setOnClickListener(new MainSideMenuListener());
        findViewById(R.id.add).setOnClickListener(bHandler);
        mypage=(ImageView)findViewById(R.id.mypage);
        mypage.setOnClickListener(bHandler);
        findViewById(R.id.notic).setOnClickListener(bHandler);
        findViewById(R.id.setting).setOnClickListener(bHandler);
        
        //        findViewById(R.id.linear).setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch(event.getAction()){
//				case MotionEvent.ACTION_DOWN:
//
//					//doAction(v);
//					break;
//				case MotionEvent.ACTION_UP:
//				
//					break;
//				case MotionEvent.ACTION_MOVE:
//					
//					break;
//				
//			}
//			
//				
//				return false;
//			}
//		});
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putInt("abs", 0);
		editor.putString("list", "");
		editor.putInt("temp", 0);
		editor.commit();
		
//		try{
//			myDbHelper.openDataBase();
//		}catch(SQLException sqle){
//			throw sqle;
//		}
//		if(!checkDB(MainActivity.this)){
//			dumpDB(MainActivity.this);
//		}
		//selectAll();
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(resultCode==RESULT_OK){
			//Log.
			if(requestCode==0){
				Log.v(TAG,"리절트 왔어요");
				selectAll();
				Log.v(TAG,"size"+babyitem.size()+"");
				int height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*babyitem.size();
		        LayoutParams lp=(LayoutParams) addlist.getLayoutParams();
		        lp.height=height;
		        addlist.setLayoutParams(lp);
				adapter.setList(babyitem);
				adapter.notifyDataSetChanged();
				Log.v(TAG,"리절트로 왔어요");
				//리스트뷰 어댑터에 이미지를 추가해 주고 setdata -> notifydata changed
	}
		}
	}
	void selectAll(){
		openDB();
		babyitem=new ArrayList<BabyInformationItem>();
		//諛⑸쾿 1
		String sql ="select image_path from km_baby order by user_id DESC;";
		Cursor c=null;
		Log.v(TAG,"select db");
		try{
			Log.v(TAG,"select db");
			
			c=db2.rawQuery(sql, null);
			Log.v(TAG,"숫자:"+c.getCount());
			while(c.moveToNext()){
			BabyInformationItem item= new BabyInformationItem();
			//item.setUser_id(c.getInt(c.getColumnIndex("user_id")));
//			item.setName(c.getString(c.getColumnIndex("name")));
//			item.setBirth(c.getString(c.getColumnIndex("birth")));
//			item.setSex(c.getString(c.getColumnIndex("sex")));
			item.setImage_path(c.getString(c.getColumnIndex("image_path")));
			babyitem.add(item);
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
	

	}	
			
	EditText id,pw;
	void popuplogin(Activity context)
	{
		// Create dialog
		Log.v(TAG,"1");
			final Dialog dialog = new Dialog(context);
			  dialog.getWindow().setBackgroundDrawable

	             (new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialoglogin);
		id=(EditText)dialog.findViewById(R.id.editText1);
		pw=(EditText)dialog.findViewById(R.id.editText2);
		Log.v(TAG,"2");
		dialog.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//로그인 시도
			Log.v(TAG,"3");			login.setImageResource(R.drawable.btn_logout);
			dialog.dismiss();
		}
	});
		Log.v(TAG,"4");
	}
	



void openDB(){
	Log.v(TAG,"opendb");
	
//	db = openOrCreateDatabase("sample.db", wi, null);
	db2=helper.getWritableDatabase();
	db = myDbHelper.getWritableDatabase();
}
// dbClose();
void closeDB(){
	if(db != null){
		if(db.isOpen()){
			db.close();
		}
	}if(db2 != null){
		if(db2.isOpen()){
			db2.close();
		}
	}
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
