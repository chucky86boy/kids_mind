package com.mb.kids_mind.task;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ViewResizeTask extends AsyncTask<Void, Void, Void>{
	private ImageView view;
	private Fragment fragment;
	private float newHeightProportion, newWidthProportion;
	public ViewResizeTask(){}



	public ViewResizeTask(ImageView view, 
			float newHeightProportion, float newWidthProportion, Fragment fragment) {
		super();
		this.view = view;
		this.fragment = fragment;
		this.newHeightProportion = newHeightProportion;
		this.newWidthProportion = newWidthProportion;
	}

	@Override
	protected Void doInBackground(Void... params) {
		while(!fragment.isVisible()){
			SystemClock.sleep(100);
		}
		return null;
	}



	@Override
	protected void onPostExecute(Void result) {		
		int width = fragment.getView().getWidth();
		int height = fragment.getView().getHeight();
		int newHeight = (int) (height * newHeightProportion);
		int newWidth = (int)(width* newWidthProportion);
		LayoutParams lp = new LinearLayout.LayoutParams(newWidth, newHeight);
		view.setLayoutParams(lp);
		view.setVisibility(View.VISIBLE);
		view.invalidate();
	}
}
