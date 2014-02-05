package com.mb.kids_mind.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
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
	View sideMenu, container;
	ViewPager pager;
	private static final String TAG="MainActivity";
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();

		if(first){
			sideMenu = activity.findViewById(R.id.side);
			container = activity.findViewById(R.id.contain);
			
			sideMenu.setY(-300);
			sideMenu.requestLayout();
			sideMenu.setVisibility(View.GONE);
			first = false;
			Log.v(TAG,"first");
			//((MainActivity)activity).text.setImageResource(R.drawable.navi_btn01_on);
			((KidsmindMapActivity)activity).text.setText("map on");
			
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
			
				animateMenu(300, View.VISIBLE);
				
				
			break;
		}
	}
	
	private void animateMenu(int animationLength, final int visibility){
		if(!animating){
			animating = true;
			if(sideMenu.getVisibility() == View.GONE) sideMenu.setVisibility(View.VISIBLE);
			container.animate()
				.translationYBy(animationLength)
				.setDuration(500)
				.setInterpolator(new DecelerateInterpolator());
			sideMenu.animate()
				.translationYBy(animationLength)
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
						
										
							break;
						case View.GONE:
						
							
							break;
						}
						animating = false;
					}
				});
			
				
		}
	}
}
