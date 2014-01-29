package com.mb.kids_mind;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.mb.kids_mind.Helper.KidsMindDBHelper;


public class Splash extends Activity{
	boolean activityStarted;
	SQLiteDatabase db,db2;
	KidsMindDBHelper myDbHelper=null;
	 SharedPreferences prefs ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
	    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_out);

	    myDbHelper=new KidsMindDBHelper(Splash.this);
		 
		try{
			myDbHelper.createDataBase();
			//closeDB();
		}catch(IOException ioe){
			
		}finally{
			myDbHelper.close();
		}
	}
	void openDB(){
		
//		db = openOrCreateDatabase("sample.db", wi, null);
		
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
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView splashView = (ImageView) findViewById(R.id.splashImg);
		final AnimationDrawable splashDrawable = (AnimationDrawable)splashView.getDrawable();
		splashDrawable.start();
		Handler mAnimationHandler = new Handler();
	    mAnimationHandler.postDelayed(new Runnable() {
	        public void run() {
	        	if(!activityStarted){
	        		activityStarted = true;
	        		
	        		Intent startMainPage = new Intent(Splash.this, MainActivity.class);
		            startActivity(startMainPage);
	        	}
	        }
	    }, getTotalDuration(splashDrawable));
	}

	private long getTotalDuration(AnimationDrawable splashDrawable) {
		long iDuration = 0;

	    for (int i = 0; i < splashDrawable.getNumberOfFrames(); i++) {
	        iDuration += splashDrawable.getDuration(i);
	    }
	    return iDuration+500;
	}
}
