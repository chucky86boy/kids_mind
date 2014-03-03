package com.mb.kids_mind.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mb.kids_mind.KidsmindMapActivity;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Item.LBSitem;

public class ParentMapfragment extends Fragment implements
com.mb.kids_mind.fragment.Mapfragment.OnGoogleMapFragmentListener {
	Activity activity;

	LBSitem item;
	ArrayList<LBSitem> list = new ArrayList<LBSitem>();
	SQLiteDatabase db;
	// private MyHelper helper;
	KidsMindDBHelper myDbHelper = null;

	private static final String MAP_FRAGMENT_TAG = "map";
	GoogleMap mMap;
	double geo_w = 0, geo_g = 0;
	private boolean isMapVisible = false;

	private SupportMapFragment mMapFragment;

	public ParentMapfragment() {
		super();
	}

	private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";

	public static interface OnGoogleMapFragmentListener {
		void onMapReady(GoogleMap map);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
     Button btn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mapfragment, container, false);
	
		// search=(EditText)v.findViewById(R.id.serch);
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
		SharedPreferences pref = activity.getSharedPreferences("pref",
				activity.MODE_PRIVATE);
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
				Log.v(TAG,"onclick");
				popup.setVisibility(View.VISIBLE);

			}
		});
		loadingimg.post(new Starter(v));


	
//		popupImage(activity);
	
//		dialog.dismiss();
		
		return v;
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
	        	
	    		openMap();
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

	// @Override
	// public void onStart() {
	// super.onStart();
	// text.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (!isMapVisible) {
	// openMap();
	//
	//
	//
	// } else {
	// // closeMap();
	// }
	// isMapVisible = !isMapVisible;
	// }
	// });
	// }
	LatLng latLng;

	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(activity.getBaseContext());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
				Log.v(TAG,"address"+addresses);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {
			popup.setVisibility(View.GONE);
			popup.invalidate();
			//Log.v(TAG,"addresssize"+addresses.size()+"");
			
			Log.v(TAG,"addresssizenull"+addresses);
			
			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(activity.getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
			}else{

			// Clears all the existing markers on the map
			// mMap.clear();

			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {

				Address address = (Address) addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());
				// mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

				// markerOptions = new MarkerOptions();
				// markerOptions.position(latLng);
				// markerOptions.title(addressText);
				//
				// mMap.addMarker(markerOptions);

				// Locate the first location
				if (i == 0)
					mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
			}
		}
	}

	private void openMap() {

		// Creates initial configuration for the map
		// GoogleMapOptions options = new
		// GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(new
		// LatLng(37.4005502611301, -5.98233461380005), 16))
		// .compassEnabled(false).mapType(GoogleMap.MAP_TYPE_NORMAL).rotateGesturesEnabled(false).scrollGesturesEnabled(false).tiltGesturesEnabled(false)
		// .zoomControlsEnabled(false).zoomGesturesEnabled(false)A;
		GoogleMapOptions options = new GoogleMapOptions();
		options.camera(
				CameraPosition.fromLatLngZoom(new LatLng(geo_w, geo_g), 16))
				.mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(false)
				.rotateGesturesEnabled(false)

				.tiltGesturesEnabled(false);

		// Modified from the sample code:
		// It isn't possible to set a fragment's id programmatically so we set a
		// tag instead and search for it using that.
		mMapFragment = (SupportMapFragment) getChildFragmentManager()
				.findFragmentByTag(MAP_FRAGMENT_TAG);

		// We only create a fragment if it doesn't already exist.
		if (mMapFragment == null) {
			Log.v(TAG, "mMap");
			// To programmatically add the map, we first create a
			// SupportMapFragment.
			mMapFragment = Mapfragment.newInstance(options);
			// Then we add it using a FragmentTransaction.
			FragmentTransaction fragmentTransaction = getChildFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(R.id.contain, mMapFragment,
					MAP_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

		// We can't be guaranteed that the map is available because Google Play
		// services might not be available.
		// XXX Here, getMap() returns null so the Marker can't be added
		// The map is shown with the previous options.
	}

	private void closeMap() {
		FragmentTransaction fragmentTransaction = getChildFragmentManager()
				.beginTransaction();
		fragmentTransaction.remove(mMapFragment);
		fragmentTransaction.commit();
	}

	LocationManager locationMgr;
	private static LatLng MELBOURNE = null;
	private Marker melbourne;
	String titlename;

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.

		Log.v(TAG, "mMap" + mMap.toString());
		// GeoPoint location=findGeoPoint("용산구청");
		// double g=location.getLatitudeE6();
		// double w=location.getLongitudeE6();
		// Log.v(TAG,"mMap null"+g+""+w+"");
		// Try to obtain the map from the SupportMapFragment.

		// Check if we were successful in obtaining the map.
		if (mMap != null) {
			// mMap.getUiSettings();
			// mMap.setMyLocationEnabled(true)isMyLocationEnabled();

			Log.v(TAG, "marker");

			MELBOURNE = new LatLng(geo_w, geo_g);
			melbourne = mMap.addMarker(new MarkerOptions()
			.position(MELBOURNE)
			.title("현재위치")
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.btn_me)));
			// selectDb("1");
			latLng = new LatLng(geo_w, geo_g);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
			for (int i = 0; i < list.size(); i++) {
				LBSitem item = list.get(i);
				MELBOURNE = new LatLng(item.getWido(), item.getGungdo());
				melbourne = mMap.addMarker(new MarkerOptions()
				.position(MELBOURNE)
				.title(item.getName())
				.snippet(item.getNumber())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.btn_pin)));

			}

			// mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			//
			// @Override
			// public View getInfoWindow(Marker marker) {
			// // TODO Auto-generated method stub
			// return null;
			// }
			//
			// @Override
			// public View getInfoContents(Marker marker) {
			// // // Getting view from the layout file info_window_layout
			// View v =
			// activity.getLayoutInflater().inflate(R.layout.info_window_layout,
			// null);
			//
			// // Getting the position from the marker
			// LatLng latLng = marker.getPosition();
			//
			//
			// // Getting reference to the TextView to set latitude
			// TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
			//
			// // Getting reference to the TextView to set longitude
			// TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
			//
			// // Setting the latitude
			// tvLat.setText("Latitude:" + latLng.latitude);
			//
			// // Setting the longitude
			// tvLng.setText("Longitude:"+ latLng.longitude);
			//
			// // Returning the view containing InfoWindow contents
			//
			// return v;
			//
			//
			// }
			// });

			mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					titlename = marker.getSnippet();
					if (titlename != null)
						selectAll(titlename);
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
					// selectdb(titlename);
					return false;
				}
			});

			mMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng latLng) {
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
					// Clears any existing markers from the GoogleMap
					// mMap.clear();
					//
					// // Creating an instance of MarkerOptions to set position
					// MarkerOptions markerOptions = new MarkerOptions();
					//
					// // Setting position on the MarkerOptions
					// markerOptions.position(arg0);
					//
					// // Animating to the currently touched position
					// mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
					//
					// // Adding marker on the GoogleMap
					// Marker marker = mMap.addMarker(markerOptions);
					//
					// // Showing InfoWindow on the GoogleMap
					// marker.showInfoWindow();

				}
			});

			// mMap.addMarker(new MarkerOptions().position(new
			// LatLng(34.7517273, 127.72628889)).title("Marker"));
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
	void setHashMap(){

		map.put("01.png",R.drawable.l001);
		map.put("02.png",R.drawable.l002);
		map.put("03.png",R.drawable.l003);
	}
	private OnGoogleMapFragmentListener mCallback;
	private static final String TAG = "MainActivity";

	@Override
	public void onMapReady(GoogleMap map) {
		Log.v(TAG, "onMapREADy");
		mMap = map;
		setUpMapIfNeeded();

	}

	//	private GeoPoint findGeoPoint(String address) {
	//		Geocoder geocoder = new Geocoder(activity);
	//		Address addr;
	//		GeoPoint location = null;
	//		try {
	//			List<Address> listAddress = geocoder
	//					.getFromLocationName(address, 1);
	//			if (listAddress.size() > 0) { // 주소값이 존재 하면
	//				addr = listAddress.get(0); // Address형태로
	//				int lat = (int) (addr.getLatitude() * 1E6);
	//				int lng = (int) (addr.getLongitude() * 1E6);
	//				location = new GeoPoint(lat, lng);
	//
	//				Log.d(TAG, "주소로부터 취득한 위도 : " + lat + ", 경도 : " + lng);
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		return location;
	//	}

}
