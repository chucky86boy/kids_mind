package com.mb.kids_mind.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mb.kids_mind.R;

public class SingleResultSketchMenu extends Fragment{
	
	int[] menuImage;
	private int position;
	Activity activity;
	
	FragmentManager fm;
	//MyDialog dialog=new MyDialog();
	public void setPosition(int position) {
		this.position = position;
	}
	 @Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_sketch, null);
		activity.getFragmentManager();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		ImageView imageView = (ImageView) view.findViewById(R.id.singeMenu);
		LayoutParams layoutParams = imageView.getLayoutParams();
		layoutParams.height = (int)(size.y * 0.7);
		imageView.setImageDrawable(getActivity().getResources().getDrawable(menuImage[position]));
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:

					//doAction(v);
					break;
				case MotionEvent.ACTION_UP:
					switch (position)
					{
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					
					break;
				
			}
				return false;
			}
		});
		return view;
	}
	
//	private class imageSizeAdjustTask extends AsyncTask<View, Void, ImageView>{
//		View view;
//		protected ImageView doInBackground(View... params) {
//			view = params[1];
//			while(!SingleSketchMenu.this.isVisible()){
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {}
//			}
//			return (ImageView) params[0];
//		}
//		@Override
//		protected void onPostExecute(ImageView result) {
//			LayoutParams lp = view.getLayoutParams();
//			result.getLayoutParams().height = (int) (lp.height * 0.7);
//			result.setVisibility(View.VISIBLE);
//			super.onPostExecute(result);
//		}
//	}
}
