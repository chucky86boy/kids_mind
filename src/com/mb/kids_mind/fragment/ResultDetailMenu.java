package com.mb.kids_mind.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mb.kids_mind.KidsMindAnalyzeActivity;
import com.mb.kids_mind.KidsMindDrawActivity;
import com.mb.kids_mind.R;

public class ResultDetailMenu extends Fragment {
	Activity activity;
	public ViewPager pager; 
	public ScreenSlidePagerAdapter mPagerAdapter;
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
				while(!ResultDetailMenu.this.isVisible()){
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
				super.onPostExecute(result);
			}
			
		}.execute(view); 
		
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
