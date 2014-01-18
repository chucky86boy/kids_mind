package com.mb.kids_mind.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mb.kids_mind.R;

public class MainSideMenuListener implements OnClickListener {
	Activity activity;
	boolean first = true, animating;
	LinearLayout sideMenu, itemListView2;
	FrameLayout frameLayout;
	@Override
	public void onClick(View v) {
		activity = (Activity) v.getContext();
		if(first){
			sideMenu = (LinearLayout)activity.findViewById(R.id.sideMenu);
			sideMenu.setX(-300);
			sideMenu.requestLayout();
			first = false;
		}
		switch(itemListView2.getVisibility()){
		case View.VISIBLE :
			animateMenu(300, View.VISIBLE);
			break;
		case View.GONE :
			animateMenu(-300, View.GONE);
			break;
		}
	}
	
	private void animateMenu(int animationLength, final int visibility){
		if(animating){
			animating = true;
			if(sideMenu.getVisibility() == View.GONE) sideMenu.setVisibility(View.VISIBLE);
			sideMenu.animate()
				.translationYBy(animationLength)
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
