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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	public ImageView toggle;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
    		SharedPreferences.Editor editor=pref.edit();
    	    
        fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
        name=(TextView)findViewById(R.id.mainname);
        helper = new MyHelper(this, "kidsmind.db", null, 1);
        
        addlist=(ListView)findViewById(R.id.listView1);
        addlist.setDivider(null);
        
        selectAll();
        int height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*babyitem.size();
        LayoutParams lp=(LayoutParams) addlist.getLayoutParams();
        lp.height=height;
        addlist.setLayoutParams(lp);
    	//editor.putString("login_check", "checked");
		
		
        adapter=new AddBabyAdater(MainActivity.this, R.layout.babyadapter, babyitem);
       
        addlist.setAdapter(adapter);
        addlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				ArrayList<BabyInformationItem> list=new ArrayList<BabyInformationItem>();
				list=adapter.getList();
				BabyInformationItem info=list.get(position);
				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				editor.putString("userid", info.getUser_id());
				editor.commit();
				Log.v(TAG,"userid"+info.getUser_id());
				//시작 앨범으로 시작~~ 그해당 userid를 통해  km_check 테이블에서 해당 image_id뽑아내고 그다음 image_id를 통해 detail_id를 이용해서 선택항목 추출
				
			}
		});
        //리스트에 들어가는 부분에서 선택하면 user id를 쉐어드로 넣어준다.
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
       toggle=(ImageView) findViewById(R.id.menuToggler);
    		   toggle.setOnClickListener(new MainSideMenuListener());
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
        editor.putInt("abs", 0);
		editor.putString("list", "");
		editor.putInt("temp", 0);
		editor.commit();
		String ch=pref.getString("just","");
		if("".equals(ch)){
			editor.putString("just", "on");
			editor.commit();
			Intent in=new Intent(MainActivity.this,KidsMindAddActivity.class);
			startActivityForResult(in, 0);
		}

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
		Log.v(TAG, "탭 디비 시작");
		SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
	    
		String checking=pref.getString("login_check", "");
		String user_name;
		if("".equals(checking)){
			//not login
			 user_name="untitle";
			 name.setText(user_name);
			editor.putString("user_name", user_name);
	        editor.commit();
	        	
		}else{
			//로그인된경우
			 user_name=pref.getString("user_name", "");
			 name.setText(user_name);
		}
        Log.v(TAG,"username"+user_name);
		// String sql
		// ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c = null;
		babyitem=new ArrayList<BabyInformationItem>();
		
		String wStr = "user_name=?";
		String[] wherStr = { user_name };
		String[] colNames = {"user_id","name","birth","sex","image_path" };
		try {
			c = db2.query("km_baby", colNames, wStr, wherStr, null,
					null, null);
		
//		
//		String sql ="select * from km_baby where "+user_name"+ order by _id DESC;";
//		Cursor c=null;
		Log.v(TAG,"select db");
//		try{
//			Log.v(TAG,"select db");
//			
//			c=db2.rawQuery(sql, null);
			Log.v(TAG,"숫자:"+c.getCount());
			while(c.moveToNext()){
			BabyInformationItem item= new BabyInformationItem();
			item.setUser_id(c.getString(c.getColumnIndex("user_id")));
			item.setName(c.getString(c.getColumnIndex("name")));
			item.setBirth(c.getString(c.getColumnIndex("birth")));
			item.setSex(c.getString(c.getColumnIndex("sex")));
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
	
		closeDB();
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
	
}
// dbClose();
void closeDB(){
	if(db != null){
		if(db.isOpen()){
			db.close();
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
