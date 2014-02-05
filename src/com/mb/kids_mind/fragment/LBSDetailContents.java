package com.mb.kids_mind.fragment;

import java.util.ArrayList;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.LBSitem;
import com.mb.kids_mind.task.ViewResizeTask;

public class LBSDetailContents extends Fragment{
	ArrayList<LBSitem> dlist;
	private int position;
	Activity activity;
	
	FragmentManager fm;
	//MyDialog dialog=new MyDialog();
	public void setPosition(int position) {
		this.position = position;
	}
	public void setData(ArrayList<LBSitem>list){
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
		View view = inflater.inflate(R.layout.lbsdetail, null);
		View resultContainer = view.findViewById(R.id.resultSketchContainer);
		activity.getFragmentManager();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("unused")
		
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		TextView title=(TextView)view.findViewById(R.id.textView1);
		TextView address=(TextView)view.findViewById(R.id.textView2);
		TextView number=(TextView)view.findViewById(R.id.textView3);
	
		LBSitem item=dlist.get(position);
		title.setText(item.getName());
		address.setText(item.getAddress());
		number.setText(item.getNumber());
//		String image = item.getDetail_image();
//		//String ImageUrl = Const.QUESTION_IMAGE_PATH;
//		Integer key=map.get(image);
		
		@SuppressWarnings("unused")
		LayoutParams layoutParams = imageView.getLayoutParams();
		//layoutParams.height = (int)(size.y * 0.7);
				return view;
	}
		
}
