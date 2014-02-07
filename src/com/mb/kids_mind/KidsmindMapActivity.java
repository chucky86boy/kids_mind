package com.mb.kids_mind;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.LBSitem;
import com.mb.kids_mind.fragment.ParentMapfragment;
import com.mb.kids_mind.listener.BackListener;


public class KidsmindMapActivity extends FragmentActivity {
GoogleMap mMap;
//Mapfragment mMapFragment;
//FragmentManager fm;
MapFragment mMapFragment;
ParentMapfragment frag;
public TextView text;
ViewPager pager;
LBSitem item;
ArrayList<LBSitem> list =new ArrayList<LBSitem>();
SQLiteDatabase db;
private MyHelper helper;

private static final String TAG="MainActivity";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	  
	    helper = new MyHelper(this, "kidsmind.db", null, 1);
//	    text.setOnClickListener(new LBSMenuListener());
	    mMapFragment=MapFragment.newInstance();
	    FragmentTransaction fragmentTransaction =
	            getSupportFragmentManager().beginTransaction();
	    
	    frag=new ParentMapfragment();
	    fragmentTransaction.add(R.id.container, frag);
	    fragmentTransaction.commit();
	    //테스트용
	  
		
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
}
