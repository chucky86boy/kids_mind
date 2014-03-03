package com.mb.kids_mind.fragment;

import java.util.ArrayList;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mb.kids_mind.KidsMindLastResultActivity;
import com.mb.kids_mind.KidsMindTotalResultActivity;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.task.ViewResizeTask;

public class SingleResultSketchAlbumMenu extends Fragment{
	ArrayList<DetailListItem> dlist;
	Hashtable<String,Integer>map=new Hashtable<String,Integer>();
	
	int[] menuImage={R.drawable.d001,R.drawable.d002,R.drawable.d004,R.drawable.d003};
	private int position;
	Activity activity;
	private static final String TAG="MainActivity";
	FragmentManager fm;
	//MyDialog dialog=new MyDialog();
	public void setPosition(int position) {
		this.position = position;
	}
	public void setData(ArrayList<DetailListItem>list){
		this.dlist=list;
	}
	 @Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.result_sketch_album, null);
		View resultContainer = view.findViewById(R.id.resultSketchContainer);
		
		activity.getFragmentManager();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("unused")
		int height = 0;
		if((android.os.Build.VERSION.SDK_INT >= 13)){
			Point size = new Point();
			display.getSize(size);
			height = size.y;
		}else{
			DisplayMetrics metrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
			height = metrics.heightPixels;
		}
		
		TextView contents = (TextView) view.findViewById(R.id.singeMenu);
		TextView title=(TextView)view.findViewById(R.id.resulttitle);
		Log.v(TAG,"position"+position+"dlist.size()"+dlist.size());
		
//		if(position==(dlist.size()-1)){
//			Log.v(TAG,"참");
//			
//			Button btn2=new Button(activity);
//			btn2.setText("결과보기");
//			btn2.setWidth(66);
//			btn2.setId(position);
//			btn.addView(btn2);
//			btn2.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//				
//					Intent in =new Intent (activity,KidsMindLastResultActivity.class);
//					startActivity(in);
//				}
//			});
//		}
		
		new ViewResizeTask(resultContainer, 0.7f, 0.7f,this).execute();
		//setHashMap();
		DetailListItem item=dlist.get(position);
		title.setText(item.getDetail_tilte());
		contents.setText(item.getDetail_content());
		
		
		
//		imageView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch(event.getAction()){
//				case MotionEvent.ACTION_DOWN:
//
//					//doAction(v);
//					break;
//				case MotionEvent.ACTION_UP:
//					Log.v(TAG,"참"+position);
//					if(position==(dlist.size()-1)){
//						Log.v(TAG,"참");
//						
//							
//								Intent in =new Intent (activity,KidsMindLastResultActivity.class);
//								startActivity(in);
//							
//					}
//					break;	
//				case MotionEvent.ACTION_MOVE:
//					
//					break;
//				
//			}
//				return false;
//			}
//		});
		return view;
	}
	
}
