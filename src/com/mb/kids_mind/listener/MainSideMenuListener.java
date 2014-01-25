package com.mb.kids_mind.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;

import com.mb.kids_mind.R;

public class MainSideMenuListener implements OnClickListener {
	Activity activity;
	boolean first = true, animating;
	View sideMenu, fragmentHolder;
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
		}

		switch(sideMenu.getVisibility()){
		case View.VISIBLE :
			animateMenu(-300, View.GONE);
			break;
		case View.GONE :
			animateMenu(300, View.VISIBLE);
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
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						sideMenu.setVisibility(visibility);
						animating = false;
					}
				});
				
		}
	}
}
