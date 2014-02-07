package com.mb.kids_mind.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;

import com.mb.kids_mind.MainActivity;
import com.mb.kids_mind.R;

public class MainSideMenuListener implements OnClickListener {
	Activity activity;
	boolean first = true, animating;
	View sideMenu, fragmentHolder,info;
	private static final String TAG="MainActivity";
	@Override
	public void onClick(View v) {
		
		MainActivity.listener2 =new BackListener() {
			
			@Override
			public void onBackPressed() {
			//리스너 받았어
				Log.v(TAG,"리스너 받았어");
				animateMenu(-300, View.GONE);
								
			}
		};
		activity = (Activity) v.getContext();
		
		if(first){
			sideMenu = activity.findViewById(R.id.sideMenu);
			fragmentHolder = activity.findViewById(R.id.fragmentHolder);
			info=activity.findViewById(R.id.info);
			sideMenu.setX(-300);
			sideMenu.requestLayout();
			sideMenu.setVisibility(View.GONE);
			first = false;
			Log.v(TAG,"first");
			((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
		}

		switch(sideMenu.getVisibility()){
		case View.VISIBLE :
			Log.v(TAG,"visible");
			
		
			
			//((MainActivity)activity).info.setVisibility(View.GONE);
			//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
			animateMenu(-300, View.GONE);
			//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
			break;
		case View.GONE :
			Log.v(TAG,"Gone");
			
			((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
			animateMenu(300, View.VISIBLE);
			((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
		
			((MainActivity)activity).info.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					animateMenu(-300, View.GONE);
					//((MainActivity)activity).info.setVisibility(View.GONE);
					
				};
			});
			((MainActivity)activity).info.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private void animateMenu(int animationLength, final int visibility){
		if(!animating){
			animating = true;
			if(sideMenu.getVisibility() == View.GONE) sideMenu.setVisibility(View.VISIBLE);
			
			fragmentHolder.animate()
				.translationXBy(animationLength)
				.setDuration(500)
				.setInterpolator(new DecelerateInterpolator());
			sideMenu.animate()
				.translationXBy(animationLength)
				.setDuration(500)
				.setInterpolator(new DecelerateInterpolator())
				
				.setListener(new AnimatorListenerAdapter() {
					

					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub
						super.onAnimationStart(animation);
						Log.v(TAG,"start");
						//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
						
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						Log.v(TAG,"end");
						sideMenu.setVisibility(visibility);
						Log.v(TAG,"visible 0=visible 8=gone"+sideMenu.getVisibility()+"");
						switch(sideMenu.getVisibility())
						{
						case View.VISIBLE:
							((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
							((MainActivity)activity).info.setVisibility(View.VISIBLE);
							((MainActivity)activity).test="1";
							break;
						case View.GONE:
							((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
							((MainActivity)activity).info.setVisibility(View.GONE);
							((MainActivity)activity).test="0";
							break;
						}
						animating = false;
					}
				});
			
				
		}
	}
	
}
