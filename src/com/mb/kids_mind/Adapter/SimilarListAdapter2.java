/*
 * Copyright (C) 2012 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mb.kids_mind.Adapter;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.SimilarItem;
import com.mb.kids_mind.fragment.SketchMenu.ScreenSlidePagerAdapter;
import com.mb.kids_mind.task.NetManager;


public class SimilarListAdapter2 extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<SimilarItem> list;
	public ScreenSlidePagerAdapter mPagerAdapter;
	Hashtable<String,Integer>map=new Hashtable<String,Integer>();
	public int temp,si;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	 private AnimationDrawable loadingViewAnim=null;

	int layout;
	int size;
	String a=null;
	List<Integer> list0,list1,list2,list3,list4,list5,list6,list7,list8;
	List<String> checklist;
	public SimilarListAdapter2(Context context, int layout,ArrayList<SimilarItem> list,int size) {

		this.mContext = context;
		this.layout = layout;
		this.list=list;
		this.size=size;
		
		
		pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		
	}







	public ArrayList<SimilarItem> getList() {
		return list;

	}



	public void setList(ArrayList<SimilarItem> list) {
	
		this.list = list;
	}



	@Override
	public int getCount() {
		return list.size();

	}

	@Override
	public Integer getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View cView, ViewGroup parent) {
		ViewHolder holder = null;
		final SimilarItem contents=list.get(position);

		final SharedPreferences pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		final SharedPreferences.Editor editor=pref.edit();
		if (cView == null) {
			//cView=View.inflate(mContext, layout,null);
			cView=LayoutInflater.from(mContext).inflate(layout, parent,false);
			holder = new ViewHolder();
			holder.linear=(ImageView)cView.findViewById(R.id.image);
			holder.li=(LinearLayout)cView.findViewById(R.id.LinearLayout1);
			holder.loading=(ImageView)cView.findViewById(R.id.loadingimg);
			holder.loading.setVisibility(View.GONE);
			holder.loading.setBackgroundResource(R.anim.progress);
			holder.loadinglinear=(LinearLayout)cView.findViewById(R.id.loading);
			holder.loadinglinear.setVisibility(View.GONE);
			loadingViewAnim=(AnimationDrawable)holder.loading.getBackground();
			cView.setTag(holder);
			//	Log.v(TAG,"cvew==null");
		} else {
		
			holder = (ViewHolder) cView.getTag();
			//	Log.v(TAG,"cvew!=null");
		}
	
		//holder.image.setImageResource(contents.getRes());
//		String DirPath = Environment.getExternalStorageDirectory()
//				.getAbsolutePath();
//		DirPath = DirPath + "/" + "KidsMind2" + "/";
//
//		File cameraDir = new File(DirPath);
//		if (!cameraDir.exists()) {
//
//			cameraDir.mkdirs();
//		}
//		File f1 = new File(cameraDir, contents.advice_image);
//		if (f1.exists()) {
//			BitmapFactory.Options opt=new BitmapFactory.Options();
//			opt.inSampleSize=2;
//			Bitmap bm=BitmapFactory.decodeFile(f1.toString(),opt);
//			 //bm=Bitmap.createScaledBitmap(bm,  101,69, true);
//			 BitmapDrawable draw=new BitmapDrawable(bm);
//			 holder.linear.setBackgroundDrawable(draw);
////			 holder.linear.setImageURI(Uri.fromFile(f1)); 
//			//mLoader.DisplayImage(f1.getAbsolutePath(), holder.imageView);
//
//		} else {
//			new DownTask().execute(
//					(Const.IMAGE_LOAD_URL+"/" + contents.advice_image).trim(),f1,holder.linear);
//
//			}
	
		
		requestMyImage(holder.loading,holder.loadinglinear,holder.linear, contents.advice_image);
		
		return cView;
	}
	public void requestMyImage(final ImageView loading,final LinearLayout loadinglinear,ImageView image, String userImagePath) {
		AQuery aq = new AQuery(image);
loading.setVisibility(View.VISIBLE);
loadinglinear.setVisibility(View.VISIBLE);
loadingViewAnim.start();
		if (userImagePath.equals("")) {
			// aq.id(R.id.my_image).image(R.drawable.photo1); ����Ʈ �̹���
		} else {
			String url = Const.IMAGE_LOAD_URL + "/" + userImagePath;
			url=url.trim();
			aq.image(url, false, false, 0, 0, new BitmapAjaxCallback() { // my_image <== ImageView
						@Override
						public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
							
							
							if (bm != null) {
								loading.setVisibility(View.GONE);
								loadinglinear.setVisibility(View.GONE);
								loadingViewAnim.stop();
								iv.setImageBitmap(bm);
							}
						}
					});
		}
	}
	class DownTask extends AsyncTask<Object, Void, Bitmap> {
		 LinearLayout img;

		@Override
		protected Bitmap doInBackground(Object... params) {

			Bitmap bitmap = null;
			File ft;
			HttpClient client = null;
			HttpGet request = null;
			HttpResponse response = null;
			int code = 0;
			InputStream is = null;
			int length = 0;
			byte[] imgArr = null;
ft=(File)params[1];
			//img = (ImageView) params[1];
			 img = (LinearLayout)params[2];
			Log.v(TAG, "add : " + params[0]);
			try {
				client = NetManager.getHttpClient();
				request = NetManager.getGet(params[0].toString());
				response = client.execute(request);
				code = response.getStatusLine().getStatusCode();
				switch (code) {
				case 200:
					is = response.getEntity().getContent();

					length = (int) response.getEntity().getContentLength();
					if (length != -1) {
						int iData = 0;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						while ((iData = is.read()) != -1) {
							baos.write(iData);
						}
						imgArr = baos.toByteArray();
						bitmap = BitmapFactory.decodeByteArray(imgArr, 0,
								(int) imgArr.length);

						Log.v(TAG, "loading success: "
								+ imgArr.length);
					} else {
						imgArr = new byte[length];
						DataInputStream dis = new DataInputStream(is);
						dis.readFully(imgArr, 0, length);
						bitmap = BitmapFactory.decodeByteArray(imgArr, 0,
								(int) imgArr.length);
					}

					FileOutputStream fs =  mContext.openFileOutput(ft.toString(), mContext.MODE_PRIVATE);
					fs.write(imgArr);
					fs.flush();
					fs.close();

					bitmap.compress(CompressFormat.PNG, 100,
						fs);
					Log.v(TAG, "Image Loading success! ");
					break;
				}
			} catch (Exception e) {
				Log.v(TAG, "Image Load error : " + e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {

					}
				}

			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {

			if (result != null) {
//				Drawable drawable = new BitmapDrawable(result);
//				 img.setBackgroundDrawable(drawable);
			}

			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			// pDialog = ProgressDialog.show(MainActivity.this, "",
			// "Loading...");
			super.onPreExecute();
		}

	}

	
	Dialog dialog =null;
//	void popupImage(Context mContext2,int position)
//	{
//		// Create dialog
//		Activity activity=(Activity)mContext2;
//		 dialog = new Dialog(activity);
//		dialog.getWindow().setBackgroundDrawable
//
//		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		
//		dialog.setContentView(R.layout.lastpopup);
//		ViewPager pager=(ViewPager)dialog.findViewById(R.id.lastpager);
//		
//		mPagerAdapter=new ScreenSlidePagerAdapter(((KidsMindLastResultActivity)mContext2).getSupportFragmentManager());
//		pager.setAdapter(mPagerAdapter);
//		
//		//라디오 버튼 
//		dialog.show();
//	}
	



	class ViewHolder {
		public ImageView linear;
		public LinearLayout li;
		public LinearLayout loadinglinear;
		public ImageView loading;
	}


}
