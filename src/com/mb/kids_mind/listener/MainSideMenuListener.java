package com.mb.kids_mind.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.SlidingDrawer;

import com.mb.kids_mind.MainActivity;
import com.mb.kids_mind.R;

public class MainSideMenuListener implements OnClickListener {
	Activity activity;
	boolean first = true, animating;
	View sideMenu, fragmentHolder;
	private static final String TAG="MainActivity";
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();

		if(first){
			sideMenu = activity.findViewById(R.id.sideMenu);
			fragmentHolder = activity.findViewById(R.id.fragmentHolder);
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
			//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
			animateMenu(-300, View.GONE);
			//((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
			
			break;
		case View.GONE :
			Log.v(TAG,"Gone");
			((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
			animateMenu(300, View.VISIBLE);
			((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
			
			break;
		}
	}
	
	private void animateMenu(int animationLength, final int visibility){
		if(!animating){
			animating = true;
			if(sideMenu.getVisibility() == View.GONE) sideMenu.setVisibility(View.VISIBLE);
			/*
			float y = sideMenu.getY();
			float x = sideMenu.getX();
			float x1 = fragmentHolder.getX();
			Animation sideMenuAnimation = new TranslateAnimation(x, x+animationLength, y, y);
			Animation bodyFramAnimation = new TranslateAnimation(x1, x1+animationLength, y, y);
			AnimationSet as = new AnimationSet(true);
			as.setInterpolator(new DecelerateInterpolator());
			as.setDuration(500);
			as.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					sideMenu.setVisibility(visibility);
					animating = false;
				}
			});
			sideMenu.setAnimation(sideMenuAnimation);
			fragmentHolder.setAnimation(bodyFramAnimation);
			as.addAnimation(sideMenu.getAnimation());
			as.addAnimation(fragmentHolder.getAnimation());
			as.start();
			*/
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
							
							break;
						case View.GONE:
							((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
							
							break;
						}
						animating = false;
					}
					
				});
			
				
		}
	}
}
