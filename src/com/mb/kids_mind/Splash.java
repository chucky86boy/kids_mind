package com.mb.kids_mind;

import com.example.kids_mind.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class Splash extends Activity{
	boolean activityStarted;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
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
