package com.mb.kids_mind;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mb.kids_mind.Dialog.MyDialogColor;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.LBSitem;
import com.mb.kids_mind.listener.BackListener;
import com.mb.kids_mind.listener.OnColorSelectedListener;
import com.mb.kids_mind.listener.mapListener;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapData.FindAllPOIListenerCallback;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapMarkerItem2;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;





public class KidsmindMapActivity extends FragmentActivity  implements onLocationChangedCallback  {

//Mapfragment mMapFragment;
//FragmentManager fm;


public TextView text;
private TMapView		mMapView = null;

private FrameLayout 	mMainFrameLayout = null;
private Context 		mContext;
private 	int 		m_nCurrentZoomLevel = 0;
private 	double 		m_Latitude  = 0;
private     double  	m_Longitude = 0;
private 	Boolean 	m_bShowMapIcon;

private 	Boolean 	m_bTrafficeMode;
private 	Boolean 	m_bSightVisible;
private 	Boolean 	m_bTrackingMode;


ArrayList<String>		mArrayID;


ArrayList<String>		mArrayCircleID;
private static 	int 	mCircleID;

ArrayList<String>		mArrayLineID;
private static 	int 	mLineID;

ArrayList<String>		mArrayPolygonID;
private static  int 	mPolygonID;

ArrayList<String>       mArrayMarkerID;
private static int 		mMarkerID;

private TranslateAnimation mShowAnimation;  

TMapGpsManager gps = null;

public static String mApiKey="07492b04-c57a-305a-9307-830a587850ce"; // 발급받은 appKey
public static String mBizAppID; // 발급받은 BizAppID (TMapTapi로 TMap앱 연동을 할 때 BizAppID 꼭 필요)
LBSitem item;
ArrayList<LBSitem> list =new ArrayList<LBSitem>();
SQLiteDatabase db;
private MyHelper helper;
KidsMindDBHelper myDbHelper = null;
private static final String TAG="MainActivity";
double geo_w = 0, geo_g = 0;
private boolean isMapVisible = false;
LocationManager locationMgr;

TextView title, address, phone;

EditText search;

ImageView popup, infopop, logo,loadingimg;
LinearLayout linear,loading;
LinearLayout ser,ser2,ser3;
View sideMenu, hide;
 private AnimationDrawable loadingViewAnim=null;

 private ImageView loadigIcon = null;
 private LinearLayout loadingLayout = null;
 Button btn;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mapfragment);
	    locationMgr = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

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
		myDbHelper = new KidsMindDBHelper(this);
	    helper = new MyHelper(this, "kidsmind.db", null, 1);
		ser = (LinearLayout) findViewById(R.id.ser);
		loading=(LinearLayout)findViewById(R.id.loading);
		
		loadingimg=(ImageView)findViewById(R.id.loadingimg);
		loadingimg.setBackgroundResource(R.anim.progress);
		loading.setVisibility(View.GONE);
		loadingimg.setVisibility(View.GONE);
		loadingViewAnim = (AnimationDrawable) loadingimg.getBackground();
		ser2 = (LinearLayout) findViewById(R.id.ser2);
		ser3 = (LinearLayout) findViewById(R.id.ser3);
		popup = (ImageView) findViewById(R.id.serchpop);
		logo = (ImageView) findViewById(R.id.logo);
		title = (TextView) findViewById(R.id.title);
		address = (TextView) findViewById(R.id.address);
		sideMenu = (LinearLayout) findViewById(R.id.placeinfo);
		hide = (LinearLayout) findViewById(R.id.hide);
		phone = (TextView) findViewById(R.id.phone);
		popup.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		infopop = (ImageView) findViewById(R.id.infopop);
		infopop.setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		search = new EditText(this) {
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

		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		SharedPreferences pref = getSharedPreferences("pref",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
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
						
						Log.v(TAG,"popup"+popup.getVisibility());
						Log.v(TAG,"location"+location);
						new GeocoderTask().execute(location);
						 
						InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

						imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
					
					//	search.setText(location);

					}
					// TODO Auto-generated method stub

				}
				// TODO Auto-generated method stub
				return false;
			}
		});

		btn=(Button)findViewById(R.id.home);

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
				finish();
			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test = "1";
				infopop.setVisibility(View.GONE);
				// TODO Auto-generated method stub
				Log.v(TAG,"onclick");
				popup.setVisibility(View.VISIBLE);

			}
		});
		openMap();
		loadingimg.post(new Starter());
//	    text.setOnClickListener(new LBSMenuListener());
	    //mMapFragment=MapFragment.newInstance();
//	    FragmentTransaction fragmentTransaction =
//	            getSupportFragmentManager().beginTransaction();
	    
//	    frag=new tmapfragment();
//	    fragmentTransaction.add(R.id.container, frag);
//	    fragmentTransaction.commit();
	    //테스트용
		  MarkerOverlay.listener = new mapListener() {
			
			@Override
			public void onMarkerPressed(String id) {
				// TODO Auto-generated method stub
				Log.v(TAG,"idididi"+id);
				if (id != null){
					Log.v(TAG,"idididi"+id);
					int i=Integer.parseInt(id);
					LBSitem item=list.get(i);
					selectAll(item.getNumber());
				}
				// 셋팅
				switch (sideMenu.getVisibility()) {
				case View.VISIBLE:
					Log.v(TAG, "visible");

					// ((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
					// animateMenu(0, View.GONE);
					// ((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);

					break;
				case View.GONE:
					Log.v(TAG, "Gone");
					hide.setVisibility(View.GONE);

					animateMenu(0, View.VISIBLE);

					break;
				}
			}
		};
		
	}
	private class GeocoderTask extends AsyncTask<String, Void, ArrayList<TMapPOIItem>> {

		@Override
		protected ArrayList<TMapPOIItem> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			 TMapData tmapdata = new TMapData();
			 
			 ArrayList<TMapPOIItem> addresses=null;
		      try {
				addresses= tmapdata.findAllPOI(locationName[0]);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      return addresses;
			    		}

		@Override
		protected void onPostExecute(ArrayList<TMapPOIItem> addresses) {
//			tmapdata.findAllPOI(locationName[0], new FindAllPOIListenerCallback() {
//				
//				@Override
//				public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
//					
//					for(int i = 0; i < poiItem.size(); i++)
//		        	{
//		        		TMapPOIItem  item = poiItem.get(i);
//		        	
//		        		LogManager.printLog("POI Name: " + item.getPOIName().toString() + ", " + 
//		        				            "Address: " + item.getPOIAddress().replace("null", "")  + ", " + 
//		        				            "Point: " + item.getPOIPoint().toString());
//		        		
//		        	}
					Log.v(TAG,"find");
					popup.setVisibility(View.GONE);
					popup.invalidate();		        
					
		

			//Log.v(TAG,"addresssize"+addresses.size()+"");
			
			Log.v(TAG,"addresssizenull"+addresses);
			
			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
			}else{

			// Clears all the existing markers on the map
			// mMap.clear();

			// Adding Markers on Google Map for each matching address
						
			}
		}
	}
	public static BackListener listener;
	public String test;
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
//	    	Log.v(TAG,"test"+test);
//	    	if("1".equals(test)){
//	    		Log.v(TAG,"리스너 들어와");
//	    		
//	    	if (KidsmindMapActivity.listener != null) {
//	    		Log.v(TAG,"리스너");
//	    		KidsmindMapActivity.listener.onBackPressed();
//	    		}
//	    	//do your stuff
//	    } else{
//	    	Log.v(TAG,"test2"+test);
//	    	finish();
//	    }
//	    }
//	    return true;
//	}
	 class Starter implements Runnable {
         
		public void run() {
           //start Asyn Task here   
           new LongOperation().execute();
         }
     }
	  private class LongOperation extends AsyncTask<Void, Void, String> {
		  
	        @Override
	        protected String doInBackground(Void... params) {
	        	
	    	
	    		linear = (LinearLayout) findViewById(R.id.placeinfo);
	    		title = (TextView) findViewById(R.id.title);
	    		address = (TextView) findViewById(R.id.address);
	    		phone = (TextView) findViewById(R.id.phone);
	    		title.setTextColor(0xff000000);
	    		address.setTextColor(0xff000000);
	    		phone.setTextColor(0xff000000);
	    		initView();
	    		findViewById(R.id.connectphone).setOnClickListener(
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
	    		showMarkerPoint2();
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
	public void openMap(){
		mMainFrameLayout =(FrameLayout)findViewById(R.id.contain);
		mMapView =new TMapView(KidsmindMapActivity.this);
		mMainFrameLayout.addView(mMapView);
		configureMapView();
		gps = new TMapGpsManager(KidsmindMapActivity.this);
		gps.setMinTime(1000);
		gps.setMinDistance(5);
		
		gps.setProvider(gps.NETWORK_PROVIDER);
		gps.OpenGps();
		
	}
	MarkerOverlay marker1;
	TMapPoint point;
	public void showMarkerPoint2()
	{	
		/*
		MarkerOverlay marker1 = new MarkerOverlay(this, mMapView);
		marker1.setID("01");
		marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
		marker1.setTMapPoint(new TMapPoint(37.566474, 126.985022) );
		
		ArrayList<Bitmap> list = new ArrayList<Bitmap>();
		list.add(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.map_pin_red));
		list.add(BitmapFactory.decodeResource(mCtx.getResources(),R.drawable.end));
		
		marker1.setAnimationIcons(list);
		marker1.setAniDuration(1000);
		marker1.startAnimation();
		
		mMapView.addMarker2Item("01", marker1);
		
		MarkerOverlay marker2 = new MarkerOverlay(this, mMapView);
		marker2.setID("02");
		marker2.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		marker2.setTMapPoint(new TMapPoint(37.551025, 126.987898) );
		
		ArrayList<Bitmap> list1 = new ArrayList<Bitmap>();
		list1.add(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.map_pin_red));
		list1.add(BitmapFactory.decodeResource(mCtx.getResources(),R.drawable.end));
		
		marker2.setAnimationIcons(list1);
		marker2.startAnimation();
		
		mMapView.addMarker2Item("02", marker2);
		*/
		setHashMap();
	    
		marker1 = new MarkerOverlay(this, mMapView);
		marker1.setID("me");
		marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.btn_me));
		point =new TMapPoint(geo_w, geo_g);
		marker1.setTMapPoint(point);
		mMapView.addMarkerItem2("0", marker1);
		for(int i = 0; i < list.size(); i++) {
			LBSitem item = list.get(i);
			
			marker1 = new MarkerOverlay(this, mMapView);
			
			String strID = String.format("%02d", i+1);
			
			marker1.setID(strID);
			marker1.balloonView.allPath.setText("클릭");
			marker1.balloonView.sectionPath.setText("해주세요");
			
//			Integer key=map.get(item.getImage_path());
//			Log.v(TAG,""+item.getImage_path());
//			marker1.balloonView.clickImage.setImageResource((int)key);
			marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.btn_pin));
			
		    point =new TMapPoint(item.getWido(), item.getGungdo());
			marker1.setTMapPoint(point);
			
			ArrayList<Bitmap> list = new ArrayList<Bitmap>();
			list.add(BitmapFactory.decodeResource(getResources(), R.drawable.btn_pin));
			//list.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end));
			
			marker1.setAnimationIcons(list);
			//marker1.setAniDuration(1000);
			
			//marker1.startAnimation();
			mMapView.addMarkerItem2(strID, marker1);
			
			
		}
		
		
		mMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
			
			@Override
			public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
				
				LogManager.printLog("ClickEvent " + " id " + id + " " + markerItem2.latitude + " " +  markerItem2.longitude);
				
				String strMessage = "ClickEvent " + " id " + id + " " + markerItem2.latitude + " " +  markerItem2.longitude;
				
				//Common.showAlertDialog(KidsmindMapActivity.this, "TMapMarker2", strMessage);	
				}
		});
		
		
		
		
		
	}
private void configureMapView() {
		
		new Thread() {
			@Override
			public void run() {
				mMapView.setSKPMapApiKey(mApiKey);

				mMapView.setLanguage(mMapView.LANGUAGE_KOREAN);	
				
				mMapView.setSKPMapBizappId(mBizAppID);
				
			}
		}.start();
		
		
	}
private void initView()
{	
	mMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {

		@Override
		public void onEnableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
			
			LogManager.printLog("MainActivity onEnableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
		}
		
	});

	
	mMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {

		@Override
		public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
			
			LogManager.printLog("MainActivity onDisableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
		}
		
	});
	
	mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
		
		@Override
		public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
			switch (sideMenu.getVisibility()) {
			case View.VISIBLE:
				Log.v(TAG, "visible");

				// ((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);
				animateMenu(0, View.GONE);
				// ((MainActivity)activity).toggle.setImageResource(R.drawable.navi_btn01);

				break;
			case View.GONE:
				Log.v(TAG, "Gone");
				// hide.setVisibility(View.GONE);

				// animateMenu(0, View.VISIBLE);

				break;
			}
			LogManager.printLog("MainActivity onPressUpEvent " + markerlist.size());
			
			return false;
		}
		
		@Override
		public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
		
			LogManager.printLog("MainActivity onPressEvent " + markerlist.size());

			for(int i = 0; i < markerlist.size(); i++)
			{
				TMapMarkerItem item = markerlist.get(i);
				
				LogManager.printLog("MainActivity onPressEvent " + item.getName() + " " + item.getTMapPoint().getLatitude() + " " + item.getTMapPoint().getLongitude());
			}
			
			return false;
		}
		
	});

	
	mMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
		
		@Override
		public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point) 
		{
			LogManager.printLog("MainActivity onLongPressEvent " + markerlist.size());
		}
	});
	
	
	mMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
		
		@Override
		public void onCalloutRightButton(TMapMarkerItem markerItem) {
			
			String strMessage = "";
			strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
			
			Common.showAlertDialog(KidsmindMapActivity.this, "Callout Right Button", strMessage);
			
		}
	});
	
	m_nCurrentZoomLevel = -1;
	m_bShowMapIcon = false;
	m_bTrafficeMode = false;
	m_bSightVisible = false;
	m_bTrackingMode = false;	
	
}
	public void selectAll(String phonenum) {
		openDB();

		Log.v(TAG, "탭 디비 시작");
		setHashMap();
		// ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c = null;
		String wStr = "phone=?";
		String[] wherStr = { phonenum };
		String[] colNames = { "address", "image", "name", "phone" };
		try {
			c = db.query("lbs", colNames, wStr, wherStr, null, null, null);
			// c=db.rawQuery(sql, null);
			Log.v(TAG, "숫자:" + c.getCount());
			while (c.moveToNext()) {
				title.setText(c.getString(c.getColumnIndex("name")));
				Log.v(TAG, "숫자:" + c.getString(c.getColumnIndex("name")));
				String image=c.getString(c.getColumnIndex("image"));
				Integer key=map.get(image);
				logo.setBackgroundResource((int)key);
				//holder.frame.setBackgroundResource((int)key);

				address.setText(c.getString(c.getColumnIndex("address")));
				phone.setText(c.getString(c.getColumnIndex("phone")));
			}

		} catch (SQLException e) {
			Log.v(TAG, "selec error" + e);
		} finally {
			if (c != null) {
				c.close();
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
	public Dialog dialog=null;
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
	@Override
	public void onLocationChange(Location location) {
		LogManager.printLog("onLocationChange " + location.getLatitude() +  " " + location.getLongitude());
		if(m_bTrackingMode) {

			mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
		}
		
	}

}
