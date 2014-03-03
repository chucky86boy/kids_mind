package com.mb.kids_mind.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mb.kids_mind.KidsMindAdviceActivity;
import com.mb.kids_mind.KidsMindConsultActivity;
import com.mb.kids_mind.KidsMindLoginSelectActivity;
import com.mb.kids_mind.KidsmindMapActivity;
import com.mb.kids_mind.MainActivity;
import com.mb.kids_mind.R;

public class SketchMenu extends Fragment {
	Activity activity;
	public ViewPager pager; 
	public ScreenSlidePagerAdapter mPagerAdapter;
	public Dialog dialog=null;
	private int[] menuImage = {R.drawable.note_01,R.drawable.menu_01,R.drawable.menu_02,R.drawable.menu_03,R.drawable.menu_04,R.drawable.note_01};
	private int currentPage;
	private ImageView doctor;
	
	@Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			
			super.onAttach(activity);
		}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(resultCode==activity.RESULT_OK){
			//Log.
			if(requestCode==99){
				//((MainActivity)activity)
				Log.v("MainActivity","lastre");
				Intent intent2=new Intent(activity,KidsMindConsultActivity.class);
				startActivity(intent2);// TODO Auto-generated method stub
				//Intent intent= new Intent(activity,KidsMindAdviceActivity.class);
				//Intent intent= new Intent(activity,KidsmindMapActivity.class);
				
				//startActivity(intent);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
		final SharedPreferences.Editor editor=pref.edit();
		View view = inflater.inflate(R.layout.menu_selector, null);
			doctor=(ImageView)view.findViewById(R.id.docbtn);
			doctor.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					String check=pref.getString("select", "");
//					editor.putString("doctor", "on");
//					editor.commit();
//					if("".equals(check)){
//						Intent intent=new Intent(activity,KidsMindLoginSelectActivity.class);
//					startActivityForResult(intent, 99);// TODO Auto-generated method stub
//						
//					}else{
//					
//						String c=pref.getString("login_check", "");
//						if("".equals(c)){
//							editor.putString("lfacebook", "");
//							editor.commit();
//							((MainActivity)activity).loginbtn.callOnClick();
//									
//						}else{
//							Intent intent2=new Intent(activity,KidsMindConsultActivity.class);
							//startActivity(intent2);// TODO Auto-generated method stub
							Toast.makeText(activity, "KidsMind입니다 서비스 준비중 입니다", Toast.LENGTH_SHORT).show();	
							
//						}
//						
//					}
				}
			});
			
		
			//닥터 다이렉트로 접속할시 서비스되지 않음
//			doctor.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					String ch=pref.getString("login_check", "");	
//					if(!ch.equals("")){
//						//로그인상태
//						Intent intent= new Intent(activity,KidsMindAdviceActivity.class);
//						intent.putExtra("where", "direct");
//						startActivity(intent);
//						
//					}else{
//						Intent in=new Intent(activity,KidsMindLoginSelectActivity.class);
//						 SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
//						SharedPreferences.Editor editor=pref.edit();
//									
//						editor.putString("last", "");
//						editor.commit();
//						
//						startActivity(in);
//					}
//					
//				}
//			});
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
				
//				SharedPreferences pref =activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
//				String ch=pref.getString("infocheck", "");
//				if("".equals(ch)){
//				pager.setCurrentItem(0);}
//				else{
//					pager.setCurrentItem(1);
//				}

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
						try{
						view = pager.getChildAt(0);
						while(!view.isShown()){
							SystemClock.sleep(50);
						}
						return null;
					}catch(NullPointerException e){
						return null;
					}
					}
					@Override
					protected void onPostExecute(Void result) {
						try{
						view.animate().scaleX(33f/30f).setDuration(500);
						view.animate().scaleY(33f/30f).setDuration(500);
						currentPage = 0;
						}catch(NullPointerException e){
							Log.v("main",e+"");
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
									
//									SharedPreferences pref =activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
//									String ch=pref.getString("infocheck", "");
//									if("".equals(ch)){
//									pager.setCurrentItem(0);}
//									else{
//										pager.setCurrentItem(1);
//									}

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
											try{
											view = pager.getChildAt(0);
											while(!view.isShown()){
												SystemClock.sleep(50);
											}
											return null;
										}catch(NullPointerException e){
											return null;
										}
										}
										@Override
										protected void onPostExecute(Void result) {
											try{
											view.animate().scaleX(33f/30f).setDuration(500);
											view.animate().scaleY(33f/30f).setDuration(500);
											currentPage = 0;
											}catch(NullPointerException e){
												Log.v("main",e+"");
											}
										}
									}.execute();
									super.onPostExecute(result);
								}
							}.execute(view);
						}
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
            return 6;
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
