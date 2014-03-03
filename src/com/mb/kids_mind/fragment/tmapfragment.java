package com.mb.kids_mind.fragment;

import java.util.ArrayList;
import java.util.Hashtable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.maps.model.MarkerOptions;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Item.LBSitem;
import com.skp.Tmap.TMapView;


public class tmapfragment extends Fragment {
	Activity activity;
	public ViewPager pager; 
	LocationManager locationMgr;
	public Dialog dialog=null;
	LBSitem item;
	ArrayList<LBSitem> list = new ArrayList<LBSitem>();
	SQLiteDatabase db;
	// private MyHelper helper;
	KidsMindDBHelper myDbHelper = null;
	double geo_w = 0, geo_g = 0;
	private static final String MAP_FRAGMENT_TAG = "map";
	private int currentPage;
	private ImageView doctor;
	private Button btn;
	@Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			locationMgr = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);

			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setSpeedRequired(false);
			criteria.setCostAllowed(true);

			String bestProvider = locationMgr.getBestProvider(criteria, true);

			// 현재 위치
			Location location = locationMgr.getLastKnownLocation(bestProvider);

			if (location != null) {
				geo_w = location.getLatitude();
				geo_g = location.getLongitude();
			} else if (location == null) {
				geo_w = 35.000;
				geo_g = 120.000;
			}
			// helper = new MyHelper(activity, "kidsmind.db", null, 1);
			myDbHelper = new KidsMindDBHelper(activity);
			super.onAttach(activity);
		}
	
	TextView text;
	TextView title, address, phone;
	String test;
	EditText search;
	MarkerOptions markerOptions;
	ImageView popup, infopop, logo,loadingimg;
	LinearLayout linear,loading;
	LinearLayout ser,ser2,ser3;
	View sideMenu, hide;
	 private AnimationDrawable loadingViewAnim=null;
  
     private ImageView loadigIcon = null;
     private LinearLayout loadingLayout = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
		final SharedPreferences.Editor editor=pref.edit();
		
		View v = inflater.inflate(R.layout.mapfragment, container, false);
			
		ser = (LinearLayout) v.findViewById(R.id.ser);
		loading=(LinearLayout)v.findViewById(R.id.loading);
		
		loadingimg=(ImageView)v.findViewById(R.id.loadingimg);
		loadingimg.setBackgroundResource(R.anim.progress);
		loading.setVisibility(View.GONE);
		loadingimg.setVisibility(View.GONE);
		loadingViewAnim = (AnimationDrawable) loadingimg.getBackground();
		ser2 = (LinearLayout) v.findViewById(R.id.ser2);
		ser3 = (LinearLayout) v.findViewById(R.id.ser3);
		popup = (ImageView) v.findViewById(R.id.serchpop);
		logo = (ImageView) v.findViewById(R.id.logo);
		title = (TextView) v.findViewById(R.id.title);
		address = (TextView) v.findViewById(R.id.address);
		sideMenu = (LinearLayout) v.findViewById(R.id.placeinfo);
		hide = (LinearLayout) v.findViewById(R.id.hide);
		phone = (TextView) v.findViewById(R.id.phone);
		popup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		infopop = (ImageView) v.findViewById(R.id.infopop);
		infopop.setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		search = new EditText(activity) {
			@Override
			public boolean onKeyPreIme(int keyCode, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					switch (popup.getVisibility()) {
					case View.GONE:

						// activity.finish();
						break;
					case View.VISIBLE:
						popup.setVisibility(View.GONE);

						break;
					}
				}

				return super.onKeyPreIme(keyCode, event);
			}
		};
		DisplayMetrics metrics = new DisplayMetrics();

		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	
		editor.putInt("width", metrics.widthPixels);
		editor.putInt("height", metrics.heightPixels);
		editor.commit();
		((View) search).setPadding(100, 0, 0, 0);
		search.setBackgroundResource(R.drawable.box_search);
		search.setTextColor(0xffffffff);
		search.setHint("지역검색");
		search.setHintTextColor(0xffffffff);
		
		search.setLines(1);
		param.setMargins(10, 0, 0, 0);
		search.setLayoutParams(param);
		search.setSingleLine(true);
		search.setLines(1);
		search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

		ser2.addView(search);

		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if ((actionId == EditorInfo.IME_ACTION_NEXT)
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					String location = search.getText().toString();

					if (location != null && !location.equals("")) {
						
//						Log.v(TAG,"popup"+popup.getVisibility());
//						Log.v(TAG,"location"+location);
//						new GeocoderTask().execute(location);
						InputMethodManager imm = (InputMethodManager) activity
								.getSystemService(activity.INPUT_METHOD_SERVICE);

						imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
					
					//	search.setText(location);

					}
					// TODO Auto-generated method stub

				}
				// TODO Auto-generated method stub
				return false;
			}
		});

		btn=(Button)v.findViewById(R.id.home);

//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				infopop.setVisibility(View.GONE);
//				// getColorRGB();
//			}
//		}, 5000);
		popup.setVisibility(View.GONE);
		// btn.setOnClickListener(new LBSMenuListener());
		// v.findViewById(R.id.home).setOnClickListener(new LBSMenuListener());

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test = "1";
				infopop.setVisibility(View.GONE);
				// TODO Auto-generated method stub
				//Log.v(TAG,"onclick");
				popup.setVisibility(View.VISIBLE);

			}
		});
		FrameLayout framelayout = (FrameLayout) v.findViewById(R.id.contain);
//    	TMapView tmapview = new TMapView(this);
//    	tmapview.setSKPMapApiKey(“TMAP_ANDROID_DEMO_KEY”);
//    	tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
//    	tmapview.setIconVisibility(true);
//    	tmapview.setZoomLevel(10);
//    	tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
//    	tmapview.setCompassMode(true);
//    	tmapview.setTrackingMode(true);
//    	relativeLayout.addView(tmapview);
//    	setContentView(relativeLayout);
    	
    	TMapView tmapView = new TMapView(activity.getBaseContext());
    	framelayout.addView(tmapView);
    	tmapView.setSKPMapApiKey("07492b04-c57a-305a-9307-830a587850ce");
    	tmapView.setCenterPoint(geo_w, geo_g);
    	tmapView.setLocationPoint(geo_w, geo_g);
    	tmapView.setMapType(tmapView.MAPTYPE_STANDARD);
    	tmapView.setTrackingMode(true);
    	framelayout.addView(tmapView);
		loadingimg.post(new Starter(v));

		return v ;
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	 class Starter implements Runnable {
         View view=null;
		  public Starter(View v) {
			view=v;
		}

		public void run() {
           //start Asyn Task here   
           new LongOperation().execute(view);
         }
     }
	  private class LongOperation extends AsyncTask<View, Void, String> {
		  View v=null;
	        @Override
	        protected String doInBackground(View... params) {
	        	v=params[0];
	        	
	    		linear = (LinearLayout) v.findViewById(R.id.placeinfo);
	    		title = (TextView) v.findViewById(R.id.title);
	    		address = (TextView) v.findViewById(R.id.address);
	    		phone = (TextView) v.findViewById(R.id.phone);
	    		title.setTextColor(0xff000000);
	    		address.setTextColor(0xff000000);
	    		phone.setTextColor(0xff000000);
	    		v.findViewById(R.id.connectphone).setOnClickListener(
	    				new OnClickListener() {

	    					@Override
	    					public void onClick(View v) {
	    						// phone
	    						String phone2 = phone.getText().toString();
	    						if (phone2 != null) {
	    							String url = "tel:" + phone2;
	    							Intent in = new Intent(Intent.ACTION_DIAL, Uri
	    									.parse(url));
	    							startActivity(in);
	    						}
	    					}
	    				});

	    		selectDb();
	    		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return "Executed";
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        //ToDo with result you got from Task

	        //Stop Loading Animation
	        	loading.setVisibility(View.GONE);
	     
	        	loadingimg.setVisibility(View.GONE);
	        loadingViewAnim.stop();
	        }

	        @Override
	        protected void onPreExecute() {
	        //Start  Loading Animation
	        loading.setVisibility(View.VISIBLE);
	    
loadingimg.setVisibility(View.VISIBLE);
	        loadingViewAnim.start();
	        }

	        @Override
	        protected void onProgressUpdate(Void... values) {}
	    }

	Hashtable<String,Integer>map=new Hashtable<String,Integer>();
	void setHashMap(){

		map.put("01.png",R.drawable.l001);
		map.put("02.png",R.drawable.l002);
		map.put("03.png",R.drawable.l003);
	}
	public void selectAll(String phonenum) {
		openDB();

		//Log.v(TAG, "탭 디비 시작");
		setHashMap();
		// ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c = null;
		String wStr = "phone=?";
		String[] wherStr = { phonenum };
		String[] colNames = { "address", "image", "name", "phone" };
		try {
			c = db.query("lbs", colNames, wStr, wherStr, null, null, null);
			// c=db.rawQuery(sql, null);
			//Log.v(TAG, "숫자:" + c.getCount());
			while (c.moveToNext()) {
				title.setText(c.getString(c.getColumnIndex("name")));
				//Log.v(TAG, "숫자:" + c.getString(c.getColumnIndex("name")));
				String image=c.getString(c.getColumnIndex("image"));
				Integer key=map.get(image);
				logo.setBackgroundResource((int)key);
				//holder.frame.setBackgroundResource((int)key);

				address.setText(c.getString(c.getColumnIndex("address")));
				phone.setText(c.getString(c.getColumnIndex("phone")));
			}

		} catch (SQLException e) {
			//Log.v(TAG, "selec error" + e);
		} finally {
			if (c != null) {
				c.close();
			}
		}

	}
	
	public AnimationDrawable frameAnimation;
	void popupImage(Activity context)
	{
		// Create dialog
		 dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.transparentdialog);
		dialog.findViewById(R.id.imageView1);
		dialog.findViewById(R.id.imageView1).setVisibility(ImageView.VISIBLE);
		dialog.findViewById(R.id.imageView1).setBackgroundResource(R.anim.progress);

		 frameAnimation = (AnimationDrawable) dialog.findViewById(R.id.imageView1).getBackground();
		frameAnimation.start();
		//라디오 버튼 
		dialog.show();
	}

private static final String TAG="MainActivity";
	public void selectDb() {// 지역 코드 입력

		openDB();
		// Log.v(TAG,"dbopen");
		// String sql
		// ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c = null;

		String[] colNames = { "wido", "gungdo", "address", "image", "name",
		"phone" };
		// String sql2 = "create table km_lbs(" +
		// "_id integer primary key autoincrement," +
		// "address text not null, image_path not null, name text not null, number text not null, contents text not null); ";
		//
		try {
			c = db.query("lbs", colNames, null, null, null, null, null);
			// c=db.rawQuery(sql, null);
			while (c.moveToNext()) {
				item = new LBSitem();
				item.setWido(c.getDouble(c.getColumnIndex("wido")));
				item.setGungdo(c.getDouble(c.getColumnIndex("gungdo")));
				item.setAddress(c.getString(c.getColumnIndex("address")));
				item.setImage_path(c.getString(c.getColumnIndex("image")));
				item.setName(c.getString(c.getColumnIndex("name")));
				item.setNumber(c.getString(c.getColumnIndex("phone")));
				list.add(item);
				Log.v(TAG, "listsize" + list.size() + "");
				// titem=new TagList();
				/*
				 * titem.setTag_id(c.getString(c.getColumnIndex("tag_id")));
				 * titem.setTag_name(c.getString(c.getColumnIndex("tag_name")));
				 * tlist.add(titem);
				 */

				// Log.v(Debugc.getTaga(), c.getString(0)+ c.getString(1)+
				// c.getString(2)+c.getString(3)+c.getString(4));
				// c.getString(0);
			}
		} catch (SQLException e) {
			Log.v(TAG, "selec error" + e);
		} finally {
			Log.v(TAG, "dbopen3");
			closeDB();
			if (c != null) {
				c.close();
			}
		}
	}

	// String sql2 = "create table km_lbs(" +
	// "_id integer primary key autoincrement," +
	// "address text not null, image_path not null, name text not null, number text not null, contents text not null); ";
	//
	public void insertRec(String address, String image_path, String name,
			String number, String area) {
		// if(selectDb(detail_id)){
		openDB();
		Log.v(TAG, "오픈 디비 ");

		ContentValues values = new ContentValues();
		try {

			values.put("address", address);

			values.put("image_path", image_path);

			values.put("name", name);
			values.put("number", number);

			values.put("area", area);

			long id = db.insert("km_lbs", null, values);

			Log.v(TAG, id > 0 ? "success" : "fail");
		} catch (SQLException e) {
			Log.v(TAG, "insert error " + e);
		}
		closeDB();

	}

	public void openDB() {

		db = myDbHelper.getWritableDatabase();
	}

	// dbClose();
	public void closeDB() {

		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	boolean first = true, animating;

	private void animateMenu(int animationLength, final int visibility) {
		if (!animating) {
			animating = true;
			if (sideMenu.getVisibility() == View.GONE)
				sideMenu.setVisibility(View.VISIBLE);

			sideMenu.animate().translationYBy(animationLength).setDuration(100)
			.setInterpolator(new DecelerateInterpolator())

			.setListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					super.onAnimationStart(animation);
					Log.v(TAG, "start");
					// ((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01_on);
					switch (sideMenu.getVisibility()) {
					case View.VISIBLE:

						break;
					case View.GONE:

						break;
					}
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					Log.v(TAG, "end");
					sideMenu.setVisibility(visibility);
					Log.v(TAG,
							"visible 0=visible 8=gone"
									+ sideMenu.getVisibility() + "");
					switch (sideMenu.getVisibility()) {
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
