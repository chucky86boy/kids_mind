package com.skj.kidsmind;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



public class MyFragment5 extends Fragment {
	private static final String TAG="MainActivity";
	Activity activity;
	View convertview;
	LinearLayout linear;

	
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = null;
		
	
		v = inflater.inflate(R.layout.draw,null);
		linear=(LinearLayout)v.findViewById(R.id.linear);
		   LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
			        LinearLayout.LayoutParams.FILL_PARENT, 
			        LinearLayout.LayoutParams.FILL_PARENT); 

			
		   	((KidsMindDrawActivity)activity).board.setLayoutParams(params); 
		   	((KidsMindDrawActivity)activity).board.setPadding(2, 2, 2, 2); 

			linear.addView(((KidsMindDrawActivity)activity).board);
	        		return v;
	}



	@Override
	public void onPause() {
	Log.v(TAG,"onpause����");
	linear.removeView(((KidsMindDrawActivity)activity).board);
		super.onPause();
	}


	@Override
	public void onResume() {
		Log.v(TAG,"on��������");
		super.onResume();
	}
	
}
