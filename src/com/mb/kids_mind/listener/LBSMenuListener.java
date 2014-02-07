package com.mb.kids_mind.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.SlidingDrawer;

import com.mb.kids_mind.KidsmindMapActivity;
import com.mb.kids_mind.R;

public class LBSMenuListener implements OnClickListener {
	Activity activity;
	boolean first = true, animating;
	View sideMenu,hide;
	ViewPager pager;
	int height;
	private static final String TAG="MainActivity";
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		SharedPreferences pref =activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		height=pref.getInt("height", 0);
		if(first){
			sideMenu = activity.findViewById(R.id.placeinfo);
			hide=activity.findViewById(R.id.hide);
						
						Log.v(TAG,"높이"+height+"");
		//	sideMenu.setY(height);
			sideMenu.requestLayout();
			sideMenu.setVisibility(View.GONE);
		
			
			
			first = false;
			Log.v(TAG,"first");
			//((MainActivity)activity).text.setImageResource(R.drawable.navi_btn01_on);
			
		}

		switch(sideMenu.getVisibility()){
		case View.VISIBLE :
			Log.v(TAG,"visible");
			
			//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
			animateMenu(0, View.GONE);
			//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
			
			break;
		case View.GONE :
			Log.v(TAG,"Gone");
			hide.setVisibility(View.GONE);
			
				animateMenu(0, View.VISIBLE);
				
				
			break;
		}
	}
	
	private void animateMenu(int animationLength, final int visibility){
		if(!animating){
			animating = true;
			if(sideMenu.getVisibility() == View.GONE) sideMenu.setVisibility(View.VISIBLE);
		
		
			sideMenu.animate()
				.translationYBy(animationLength)
				.setDuration(100)
				.setInterpolator(new DecelerateInterpolator())
				
				.setListener(new AnimatorListenerAdapter() {
					

					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub
						super.onAnimationStart(animation);
						Log.v(TAG,"start");
						//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
						switch(sideMenu.getVisibility())
						{
						case View.VISIBLE:
										
							break;
						case View.GONE:
						
							break;
						}
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
							hide.setVisibility(View.GONE);
										
							break;
						case View.GONE:
							hide.setVisibility(View.VISIBLE);
							break;
						}
						animating = false;
					}
				});
			
				
		}
	}
}
