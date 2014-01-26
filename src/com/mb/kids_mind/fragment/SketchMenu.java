package com.mb.kids_mind.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mb.kids_mind.R;
import com.mb.kids_mind.listener.PageChagedListener;

import com.mb.kids_mind.view.PagerContainer;

public class SketchMenu extends Fragment {
	Activity activity;
	public ViewPager pager; 
	public ScreenSlidePagerAdapter mPagerAdapter;
	
	 private static final String TAG="MainActivity";
	 View v=null;
	@Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_selector, null);
//		new AsyncTask<View, Void, View>() {
//
//			@Override
//			protected View doInBackground(View... params) {
//				while(!SketchMenu.this.isVisible()){
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {}
//				}
//				return params[0];
//			}
//
//			@Override
//			protected void onPostExecute(View result) {
//				 pager = (ViewPager) result.findViewById(R.id.menu_pager);
//				pager.setOffscreenPageLimit(5);
//				mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager()); 
//				pager.setAdapter(mPagerAdapter);
//				PagerContainer.listener=new PageChagedListener() {
//					
//					@Override
//					public void onPageChange(int position) {
//						
//					}
//				};
//				super.onPostExecute(result);
//			}
//			
//		}.execute(view); 
		 pager = (ViewPager)view.findViewById(R.id.menu_pager);
			pager.setOffscreenPageLimit(5);
			mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager()); 
			pager.setAdapter(mPagerAdapter);
			
			PagerContainer contai =new PagerContainer(activity);
			
			
			//pager.setOnPageChangeListener(contai);
		return view ;
	}

	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ViewPager pager2; 
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
        }
	
        @Override
        public int getCount() {
            return 4;
        }

		@Override
		public Fragment getItem(int position) {
			SingleSketchMenu frag = new SingleSketchMenu();
			frag.setPosition(position);
			
			return frag;
		}
	}


	
}
