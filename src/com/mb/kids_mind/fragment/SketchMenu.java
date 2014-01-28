package com.mb.kids_mind.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mb.kids_mind.R;

public class SketchMenu extends Fragment {
	Activity activity;
	public ViewPager pager; 
	public ScreenSlidePagerAdapter mPagerAdapter;
	
	private int[] menuImage = {R.drawable.menu_01,R.drawable.menu_02,R.drawable.menu_03,R.drawable.menu_04};
	private int currentPage;
	@Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.menu_selector, null);
			
		new AsyncTask<View, Void, View>() {

			@Override
			protected View doInBackground(View... params) {
				while(!SketchMenu.this.isVisible()){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
				return params[0];
			}

			@Override
			protected void onPostExecute(View result) {
				pager = (ViewPager) result.findViewById(R.id.menu_pager);
				pager.setOffscreenPageLimit(5);
				mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager());
				pager.setAdapter(mPagerAdapter);
				pager.setPageMargin(
						activity.getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
				pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

					@Override
					public void onPageSelected(int position) {
						View prevViewG = pager.getChildAt(currentPage);
						View prevView = prevViewG.findViewById(R.id.singeMenu); 
						View viewG = pager.getChildAt(position);
						View view = viewG.findViewById(R.id.singeMenu);
						if(position == 0){
							view.animate().scaleX(1.0f).setDuration(500);
							view.animate().scaleY(1.0f).setDuration(500);
						}else{
							view.animate().scaleX(33f/30f).setDuration(500);
							view.animate().scaleY(33f/30f).setDuration(500);
						}
						if(currentPage == 0){
							prevView.animate().scaleX(30f/33f).setDuration(500);
							prevView.animate().scaleY(30f/33f).setDuration(500);
						}else{
							prevView.animate().scaleX(1f).setDuration(500);
							prevView.animate().scaleY(1f).setDuration(500);
						}
						currentPage = position;
					}
					
				});
				
				new AsyncTask<Void, Void, Void>(){
					View view;
					@Override
					protected Void doInBackground(Void... params) {
						view = pager.getChildAt(0);
						while(!view.isShown()){
							SystemClock.sleep(50);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						view.animate().scaleX(33f/30f).setDuration(500);
						view.animate().scaleY(33f/30f).setDuration(500);
						currentPage = 0;
					}
				}.execute();
				super.onPostExecute(result);
			}
		}.execute(view);
		
		return view ;
	}

	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public SparseArray<Fragment> fragMap = new SparseArray<Fragment>();
		
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
			frag.menuImage = menuImage;
			frag.setPosition(position);
			fragMap.put(position, frag);
			return frag;
		}
	}

}
