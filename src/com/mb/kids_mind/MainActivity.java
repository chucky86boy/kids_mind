package com.mb.kids_mind;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.fragment.SketchMenu;
import com.mb.kids_mind.listener.MainSideMenuListener;

public class MainActivity extends Activity {
	FragmentManager fm=null;
	SQLiteDatabase db;
	KidsMindDBHelper myDbHelper=null;
    private static final String TAG="MainActivity";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
        
        findViewById(R.id.menuToggler).setOnClickListener(new MainSideMenuListener());
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
		 myDbHelper=new KidsMindDBHelper(MainActivity.this);
		try{
			myDbHelper.createDataBase();
		}catch(IOException ioe){
			throw new Error("error");
		}
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
//   public boolean checkDB(Context mContext){
//
//       String filePath = "/data/data/" + "com.mb.kids_mind" + "/databases/km_question_tag.sqlite.sqlite";
//
//       File file = new File(filePath);
//
//       return file.exists();
//
//}

	void selectAll(){
		openDB();
		//諛⑸쾿 1
		String sql ="select * from km_question_detail;";
		Cursor c=null;
		try{
			c=db.rawQuery(sql, null);
			Log.v(TAG,"숫자:"+c.getCount());
			while(c.moveToNext()){
				//c.getString(c.getColumnIndex("question_id"));
			Log.v(TAG, c.getString(0)+ c.getString(1)+ c.getString(2)+c.getString(3)+c.getString(4));
				//	c.getString(0);
			}
			
			
		}catch(SQLException e){
		}finally{
			if(c!=null){
			c.close();
			}
		}
		
		closeDB();
	}

// Dump DB

public void dumpDB(Context mContext){

       AssetManager manager = mContext.getAssets();

       String folderPath = "/data/data/" +"com.mb.kids_mind" + "/databases";

       String filePath = "/data/data/" + "com.mb.kids_mind" + "/databases/km_question_tag.sqlite.sqlite";



       File folder = new File(folderPath);

       File file = new File(filePath);



       FileOutputStream fos = null;

       BufferedOutputStream bos = null;



       try {

               InputStream is = manager.open("km_question_tag.sqlite.sqlite");

               BufferedInputStream bis = new BufferedInputStream(is);



               if (folder.exists()) {

               }else{

                       folder.mkdirs();

               }



               if (file.exists()) {

                       file.delete();

                       file.createNewFile();

               }



               fos = new FileOutputStream(file);

               bos = new BufferedOutputStream(fos);

               int read = -1;

               byte[] buffer = new byte[1024];

               while ((read = bis.read(buffer, 0, 1024)) != -1) {

                       bos.write(buffer, 0, read);

               }



               bos.flush();

               bos.close();

               fos.close();

               bis.close();

               is.close();



       } catch (IOException e) {

               Log.e("ErrorMessage : ", e.getMessage());

       } 

}

void openDB(){
//	db = openOrCreateDatabase("sample.db", wi, null);
	db = myDbHelper.getWritableDatabase();
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
