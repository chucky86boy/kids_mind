package com.mb.kids_mind.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.LBSitem;
import com.mb.kids_mind.listener.LBSMenuListener;

public class ParentMapfragment extends Fragment implements com.mb.kids_mind.fragment.Mapfragment.OnGoogleMapFragmentListener {
	Activity activity;
	
	ViewPager pager;
	LBSitem item;
	ArrayList<LBSitem> list =new ArrayList<LBSitem>();
	SQLiteDatabase db;
	private MyHelper helper;
	public ScreenSlidePagerAdapter mPagerAdapter;

	private static final String MAP_FRAGMENT_TAG = "map";
	GoogleMap mMap;
	double geo_w = 0,geo_g =0;
	private boolean isMapVisible = false;

	private SupportMapFragment mMapFragment;

	public ParentMapfragment(){
		super();
	}
	private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";

    public static interface OnGoogleMapFragmentListener {
        void onMapReady(GoogleMap map);
    }

  


	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	    this.activity=activity;
	    locationMgr = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		 
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
        helper = new MyHelper(activity, "kidsmind.db", null, 1);
	    }
TextView text;
EditText edit;
MarkerOptions markerOptions;
@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.mapfragment, container,false);
		text=(TextView)v.findViewById(R.id.textView1);
		text.setOnClickListener(new LBSMenuListener());
		edit=(EditText)v.findViewById(R.id.editText1);
		LinearLayout side=(LinearLayout)v.findViewById(R.id.side);
				side.setVisibility(View.GONE);
		 openMap();
		   insertRec("address","image_path",
					"String name", "String number","String contents","1");
		     insertRec("address","image_path",
						"String name", "String number","String contents","1");
		     insertRec("address","image_path",
						"String name", "String number","String contents","1");
		     insertRec("address","image_path",
						"String name", "String number","String contents","1");
		     insertRec("address","image_path",
						"String name", "String number","String contents","1");
		     insertRec("address","image_path",
						"String name", "String number","String contents","1");
		     insertRec("address","image_path",
						"String name", "String number","String contents","2");
		     insertRec("address","image_path",
						"String name", "String number","String contents","2");
		     insertRec("address","image_path",
						"String name", "String number","String contents","2");
		     insertRec("address","image_path",
						"String name", "String number","String contents","2");
		     insertRec("address","image_path",
						"String name", "String number","String contents","2");
		     selectDb("1");
		    pager =(ViewPager)v.findViewById(R.id.menu_pager);
		    pager.setOffscreenPageLimit(5);
		    mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager());
			pager.setAdapter(mPagerAdapter);
		return v ;
	}
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	public SparseArray<Fragment> fragMap = new SparseArray<Fragment>();
	
	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    
	@Override
	public Fragment getItem(int position) {
		LBSDetailContents frag = new LBSDetailContents();
		frag.setData(list);
		frag.setPosition(position);
		return frag;
	}
}

public void selectDb(String cha){//지역 코드 입력

	openDB();
	//Log.v(TAG,"dbopen");
	//String sql ="select tag_name from md_question_tag where question_id=Q"+po+";";
	Cursor c=null;
	String wStr="area=?";
	String[] wherStr={cha};
	String[] colNames={"address","image_path","name","number","contents"};
//	String sql2 = "create table km_lbs(" +
//			"_id integer primary key autoincrement," +
//			"address text not null, image_path not null, name text not null, number text not null, contents text not null); ";
//	
	try{
		c=db.query("km_lbs", colNames, wStr, wherStr, null, null, null);
		//c=db.rawQuery(sql, null);
		while(c.moveToNext()){
			item=new LBSitem();
			item.setAddress(c.getString(c.getColumnIndex("address")));
			item.setContents(c.getString(c.getColumnIndex("contents")));
			item.setImage_path(c.getString(c.getColumnIndex("image_path")));
			item.setName(c.getString(c.getColumnIndex("name")));
			item.setNumber(c.getString(c.getColumnIndex("number")));
			list.add(item);
			Log.v(TAG,"listsize"+list.size()+"");
			//titem=new TagList();
			/*titem.setTag_id(c.getString(c.getColumnIndex("tag_id")));
				titem.setTag_name(c.getString(c.getColumnIndex("tag_name")));
				tlist.add(titem);
			 */
			
			//Log.v(Debugc.getTaga(), c.getString(0)+ c.getString(1)+ c.getString(2)+c.getString(3)+c.getString(4));
			//	c.getString(0);
		}
	}catch(SQLException e){
		Log.v(TAG,"selec error"+e);
	}finally{
		Log.v(TAG,"dbopen3");
		closeDB();
		if(c!=null){
			c.close();
		}
	}
}
//String sql2 = "create table km_lbs(" +
//		"_id integer primary key autoincrement," +
//		"address text not null, image_path not null, name text not null, number text not null, contents text not null); ";
//
public void insertRec(String address, String image_path,
		String name, String number,String contents,String area) {
	// if(selectDb(detail_id)){
	openDB();
	Log.v(TAG,"오픈 디비 ");
	
	
	
	ContentValues values = new ContentValues();
	try {

		values.put("address", address);
		
		values.put("image_path", image_path);
		
		values.put("name", name);
		values.put("number", number);
		values.put("contents", contents);
		values.put("area",area);
		
		long id = db.insert("km_lbs", null, values);
		
		Log.v(TAG, id > 0 ? "success" : "fail");
	} catch (SQLException e) {
		Log.v(TAG, "insert error " + e);
	}
	closeDB();
	

}
public void openDB() {
	
	
	db=helper.getWritableDatabase();
}

// dbClose();
public void closeDB() {

	if (db != null) {
		if (db.isOpen()) {
			db.close();
		}
	}
}

//	@Override
//	public void onStart() {
//	    super.onStart();
//	    text.setOnClickListener(new View.OnClickListener() {
//	        @Override
//	        public void onClick(View v) {
//	            if (!isMapVisible) {
//	            	 openMap();
//	            	 
//
//	            	
//	            } else {
//	               // closeMap();
//	            }
//	            isMapVisible = !isMapVisible;
//	        }
//	    });
//	}
	 LatLng latLng;
	  private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
		  
	        @Override
	        protected List<Address> doInBackground(String... locationName) {
	            // Creating an instance of Geocoder class
	            Geocoder geocoder = new Geocoder(activity.getBaseContext());
	            List<Address> addresses = null;
	 
	            try {
	                // Getting a maximum of 3 Address that matches the input text
	                addresses = geocoder.getFromLocationName(locationName[0], 3);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return addresses;
	        }
	 
	        @Override
	        protected void onPostExecute(List<Address> addresses) {
	 
	            if(addresses==null || addresses.size()==0){
	                Toast.makeText(activity.getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
	            }
	 
	            // Clears all the existing markers on the map
	            mMap.clear();
	 
	            // Adding Markers on Google Map for each matching address
	            for(int i=0;i<addresses.size();i++){
	 
	                Address address = (Address) addresses.get(i);
	 
	                // Creating an instance of GeoPoint, to display in Google Map
	                latLng = new LatLng(address.getLatitude(), address.getLongitude());
	 
	                String addressText = String.format("%s, %s",
	                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                address.getCountryName());
	 
	                markerOptions = new MarkerOptions();
	                markerOptions.position(latLng);
	                markerOptions.title(addressText);
	 
	                mMap.addMarker(markerOptions);
	 
	                // Locate the first location
	                if(i==0)
	                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	            }
	        }
	  }
	
	private void openMap() {
		
	    // Creates initial configuration for the map
//	    GoogleMapOptions options = new GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(new LatLng(37.4005502611301, -5.98233461380005), 16))
//	            .compassEnabled(false).mapType(GoogleMap.MAP_TYPE_NORMAL).rotateGesturesEnabled(false).scrollGesturesEnabled(false).tiltGesturesEnabled(false)
//	            .zoomControlsEnabled(false).zoomGesturesEnabled(false)A;
		GoogleMapOptions options = new GoogleMapOptions();
		options.camera(CameraPosition.fromLatLngZoom(new LatLng(geo_w, geo_g), 16))
		.mapType(GoogleMap.MAP_TYPE_NORMAL)
	    .compassEnabled(false)
	    .rotateGesturesEnabled(false)
	    .tiltGesturesEnabled(false);
	    // Modified from the sample code:
	    // It isn't possible to set a fragment's id programmatically so we set a
	    // tag instead and search for it using that.
	    mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);

	    // We only create a fragment if it doesn't already exist.
	    if (mMapFragment == null) {
	    	Log.v(TAG,"mMap");
	        // To programmatically add the map, we first create a
	        // SupportMapFragment.
	        mMapFragment = Mapfragment.newInstance(options);
	        // Then we add it using a FragmentTransaction.
	        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
	        fragmentTransaction.add(R.id.contain, mMapFragment, MAP_FRAGMENT_TAG);
	        fragmentTransaction.commit();
	    }
	    // We can't be guaranteed that the map is available because Google Play
	    // services might not be available.
	     //XXX Here, getMap() returns null so  the Marker can't be added
	    // The map is shown with the previous options.
	}

	private void closeMap() {
	    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
	    fragmentTransaction.remove(mMapFragment);
	    fragmentTransaction.commit();
	}
LocationManager locationMgr;
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the
	    // map.
		String location = edit.getText().toString();
   	 
//        if(location!=null && !location.equals("")){
//       	
//       	 new GeocoderTask().execute(location);
//        }
		Log.v(TAG,"mMap"+mMap.toString());
//		GeoPoint location=findGeoPoint("용산구청");
//		double g=location.getLatitudeE6();
//		double w=location.getLongitudeE6();
	    //	Log.v(TAG,"mMap null"+g+""+w+"");
	        // Try to obtain the map from the SupportMapFragment.
	        
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	        	Log.v(TAG,"marker");
	        	mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					
					@Override
					public View getInfoWindow(Marker marker) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public View getInfoContents(Marker marker) {
//					// Getting view from the layout file info_window_layout
		                View v = activity.getLayoutInflater().inflate(R.layout.info_window_layout, null);
		                
		                // Getting the position from the marker
		                LatLng latLng = marker.getPosition();
		 
		                // Getting reference to the TextView to set latitude
		                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
		 
		                // Getting reference to the TextView to set longitude
		                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
		 
		                // Setting the latitude
		                tvLat.setText("Latitude:" + latLng.latitude);
		 
		                // Setting the longitude
		                tvLng.setText("Longitude:"+ latLng.longitude);
		 
		                // Returning the view containing InfoWindow contents
		                
						return v;


					}
				});
	        	   mMap.setOnMapClickListener(new OnMapClickListener() {
	        		   
	                   @Override
	                   public void onMapClick(LatLng arg0) {
	                       // Clears any existing markers from the GoogleMap
	                       mMap.clear();
	        
	                       // Creating an instance of MarkerOptions to set position
	                       MarkerOptions markerOptions = new MarkerOptions();
	        
	                       // Setting position on the MarkerOptions
	                       markerOptions.position(arg0);
	        
	                       // Animating to the currently touched position
	                       mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
	        
	                       // Adding marker on the GoogleMap
	                       Marker marker = mMap.addMarker(markerOptions);
	        
	                       // Showing InfoWindow on the GoogleMap
	                       marker.showInfoWindow();
	        
	                   }
	               });

	         //   mMap.addMarker(new MarkerOptions().position(new LatLng(34.7517273, 127.72628889)).title("Marker"));
	        }
	    
	}


	
	private OnGoogleMapFragmentListener mCallback;
	private static final String TAG="MainActivity";
	@Override
	public void onMapReady(GoogleMap map) {
		Log.v(TAG,"onMapREADy");
		 mMap = map;
		 setUpMapIfNeeded();
		
	}
	private GeoPoint findGeoPoint(String address) {
        Geocoder geocoder = new Geocoder(activity);
        Address addr;
        GeoPoint location = null;
        try {
            List<Address> listAddress = geocoder.getFromLocationName(address, 1);
            if (listAddress.size() > 0) { // 주소값이 존재 하면
               addr = listAddress.get(0); // Address형태로
               int lat = (int) (addr.getLatitude() * 1E6);
                int lng = (int) (addr.getLongitude() * 1E6);
                location = new GeoPoint(lat, lng);
                
                Log.d(TAG, "주소로부터 취득한 위도 : " + lat + ", 경도 : " + lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    } 
	
}
