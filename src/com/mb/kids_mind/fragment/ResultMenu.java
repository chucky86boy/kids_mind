package com.mb.kids_mind.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mb.kids_mind.R;

public class ResultMenu extends Fragment {
	Activity activity;
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
				while(!ResultMenu.this.isVisible()){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
				return params[0];
			}

			@Override
			protected void onPostExecute(View result) {
				ViewPager pager = (ViewPager) result.findViewById(R.id.menu_pager);
				pager.setOffscreenPageLimit(5);
				ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(getFragmentManager());
				adapter.result = 4;
				adapter.drawables = new int[]{R.drawable.menu_01,R.drawable.menu_02,R.drawable.menu_03,R.drawable.menu_04};
				pager.setAdapter(adapter);
				super.onPostExecute(result);
			}
			
		}.execute(view); 
		
		return view ;
	}
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		int result;
		int[] drawables;
		
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
        }
	
        @Override
        public int getCount() {
            return result;
        }

		@Override
		public Fragment getItem(int position) {
			SingleResultSketchMenu frag = new SingleResultSketchMenu();
			frag.menuImage = drawables;
			frag.setPosition(position);
			return frag;
		}
	}	
}
