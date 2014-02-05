package com.mb.kids_mind;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.LBSitem;
import com.mb.kids_mind.fragment.LBSDetailContents;
import com.mb.kids_mind.fragment.ParentMapfragment;
import com.mb.kids_mind.fragment.SingleSketchMenu;
import com.mb.kids_mind.listener.LBSMenuListener;


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
	    text=(TextView)findViewById(R.id.textView1);
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
	
}
