package com.mb.kids_mind.fragment;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mb.kids_mind.R;

public class SingleSketchMenu extends Fragment{
	private int[] menuImage = {R.drawable.menu_01,R.drawable.menu_02,R.drawable.menu_03,R.drawable.menu_04};
	private int position;
	
	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_sketch, null);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		ImageView imageView = (ImageView) view.findViewById(R.id.singeMenu);
		LayoutParams layoutParams = imageView.getLayoutParams();
		layoutParams.height = (int)(size.y * 0.7);
		imageView.setImageDrawable(getActivity().getResources().getDrawable(menuImage[position]));
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
