package com.mb.kids_mind.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class Mapfragment extends SupportMapFragment {
	Activity activity;
	private LatLng mPosfija;
	public Mapfragment(){
		super();
	}
	  private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";

	public static Mapfragment newInstance(GoogleMapOptions options) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, options);

        Mapfragment fragment = new Mapfragment();
        fragment.setArguments(arguments);
        return fragment;
    }

	public static Mapfragment newInstance(){
		Mapfragment frag = new Mapfragment();
		
	
	    return frag;
	}

	GoogleMap mMap;
	@Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater,container,savedInstanceState);
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnGoogleMapFragmentListener) {
            ((OnGoogleMapFragmentListener) fragment).onMapReady(getMap());
        }

		
		return v ;
	}
	
	
	public static interface OnGoogleMapFragmentListener {

        void onMapReady(GoogleMap map);
    }

	
}
