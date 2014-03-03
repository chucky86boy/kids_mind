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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.mb.kids_mind.CDialog;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.SimilarItem;
import com.mb.kids_mind.fragment.SketchMenu.ScreenSlidePagerAdapter;
import com.mb.kids_mind.task.NetManager;


public class SimilarListAdapterdetail extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<SimilarItem> list;
	public ScreenSlidePagerAdapter mPagerAdapter;
	Hashtable<String,Integer>map=new Hashtable<String,Integer>();
	public int temp,si;
	 private AnimationDrawable loadingViewAnim=null;
	SharedPreferences pref;
	SharedPreferences.Editor editor;


	int layout;
	int size;
	String a=null;
	List<Integer> list0,list1,list2,list3,list4,list5,list6,list7,list8;
	List<String> checklist;
	public SimilarListAdapterdetail(Context context, int layout,ArrayList<SimilarItem> list,int size) {

		this.mContext = context;
		this.layout = layout;
		this.list=list;
		this.size=size;
		
		
		pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		setHashMap();
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
			holder.image=(ImageView)cView.findViewById(R.id.image);
			holder.loading=(ImageView)cView.findViewById(R.id.loadingimg);
			holder.loading.setVisibility(View.GONE);
			holder.loading.setBackgroundResource(R.anim.progress);
			holder.loadinglinear=(LinearLayout)cView.findViewById(R.id.loading);
			holder.loadinglinear.setVisibility(View.GONE);
			loadingViewAnim=(AnimationDrawable)holder.loading.getBackground();
			
			//			holder.question=(TextView)cView.findViewById(R.id.question);
//			holder.date=(TextView)cView.findViewById(R.id.date);
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
//			 holder.image.setImageURI(Uri.fromFile(f1)); 
//			//mLoader.DisplayImage(f1.getAbsolutePath(), holder.imageView);
//
//		} else {
//			new DownTask().execute(
//					(Const.IMAGE_LOAD_URL+"/" + contents.advice_image).trim(),f1,holder.image);
//
//			}
requestMyImage(holder.loading,holder.loadinglinear,holder.image, contents.advice_image);
//		if("Q1".equals(contents.question_id)){
//			holder.question.setText("집 그림 그리기");
//		}else if("Q2".equals(contents.question_id)){
//			holder.question.setText("나무 그림 그리기");
//		}else if("Q3".equals(contents.question_id)){
//			holder.question.setText("사람 그림 그리기");
//		}else if("Q4".equals(contents.question_id)){
//			holder.question.setText("물고기 그림 그리기");
//		}
//		String da=contents.getDate().substring(0, 9);
//		holder.date.setText(da);
		
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
		 ImageView img;

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
		//	img = (ImageView) params[1];
			 img = (ImageView)params[2];
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
					//FileOutputStream fs = new FileOutputStream(ft);
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
	public void doRemoveItem(int size,List<Integer> list,int position){
		if(size!=0){
			//list0=new LinkedList<Integer>();
			for(int i=0;i<size;i++){
				if(list.contains(position)){
					int index=list.indexOf(position);
					list.remove(index);
				}
			}
			Collections.sort(list);
			for(int i=0;i<list.size();i++)
				Log.v(TAG,"삭제후의 값"+list.get(i).intValue()+"");
		}
	}
//	void setHashMap(){
//
//		map.put("d001.png",R.drawable.d001);
//		map.put("d002.png",R.drawable.d002);
//		map.put("d003.png",R.drawable.d003);
//		map.put("d004.png",R.drawable.d004);
//		map.put("d005.png",R.drawable.d005);
//		map.put("d006.png",R.drawable.d006);
//		map.put("d007.png",R.drawable.d007);
//		map.put("d008.png",R.drawable.d008);
//		map.put("d009.png",R.drawable.d009);
//		map.put("d010.png",R.drawable.d010);
//		map.put("d011.png",R.drawable.d011);
//		map.put("d012.png",R.drawable.d012);
//		map.put("d013.png",R.drawable.d013);
//		map.put("d014.png",R.drawable.d014);
//		map.put("d015.png",R.drawable.d015);
//		map.put("d016.png",R.drawable.d016);
//		map.put("d017.png",R.drawable.d017);
//		map.put("d018.png",R.drawable.d018);
//		map.put("d019.png",R.drawable.d019);
//		map.put("d020.png",R.drawable.d020);
//		map.put("d021.png",R.drawable.d021);
//		map.put("d022.png",R.drawable.d022);
//		map.put("d023.png",R.drawable.d023);
//		map.put("d024.png",R.drawable.d024);
//		map.put("d025.png",R.drawable.d025);
//		map.put("d026.png",R.drawable.d026);
//		map.put("d027.png",R.drawable.d027);
//		map.put("d028.png",R.drawable.d028);
//		map.put("d029.png",R.drawable.d029);
//		map.put("d030.png",R.drawable.d030);
//		map.put("d031.png",R.drawable.d031);
//		map.put("d032.png",R.drawable.d032);
//		map.put("d033.png",R.drawable.d033);
//		map.put("d034.png",R.drawable.d034);
//		map.put("d035.png",R.drawable.d035);
//		map.put("d036.png",R.drawable.d036);
//		map.put("d037.png",R.drawable.d037);
//		map.put("d057.png",R.drawable.d057);
//		map.put("d058.png",R.drawable.d058);
//		map.put("d059.png",R.drawable.d059);
//		map.put("d060.png",R.drawable.d060);
//		map.put("d061.png",R.drawable.d061);
//		map.put("d062.png",R.drawable.d062);
//		map.put("d063.png",R.drawable.d063);
//		map.put("d064.png",R.drawable.d064);
//		map.put("d065.png",R.drawable.d065);
//		map.put("d066.png",R.drawable.d066);
//		map.put("d067.png",R.drawable.d067);
//		map.put("d068.png",R.drawable.d068);
//		map.put("d069.png",R.drawable.d069);
//		map.put("d070.png",R.drawable.d070);
//		map.put("d071.png",R.drawable.d071);
//		map.put("d072.png",R.drawable.d072);
//		map.put("d073.png",R.drawable.d073);
//		map.put("d074.png",R.drawable.d074);
//		map.put("d075.png",R.drawable.d075);
//		map.put("d076.png",R.drawable.d076);
//		map.put("d077.png",R.drawable.d077);
//		map.put("d078.png",R.drawable.d078);
//		map.put("d079.png",R.drawable.d079);
//		map.put("d080.png",R.drawable.d080);
//		map.put("d081.png",R.drawable.d081);
//		map.put("d082.png",R.drawable.d082);
//		map.put("d083.png",R.drawable.d083);
//		map.put("d084.png",R.drawable.d084);
//		map.put("d086.png",R.drawable.d086);
//		map.put("d087.png",R.drawable.d087);
//		map.put("d088.png",R.drawable.d088);
//		map.put("d089.png",R.drawable.d089);
//		map.put("d090.png",R.drawable.d090);
//		map.put("d091.png",R.drawable.d091);
//		map.put("d092.png",R.drawable.d092);
//		map.put("d093.png",R.drawable.d093);
//		map.put("d094.png",R.drawable.d094);
//		map.put("d095.png",R.drawable.d095);
//		map.put("d096.png",R.drawable.d096);
//		map.put("d097.png",R.drawable.d097);
//		map.put("d098.png",R.drawable.d098);
//		map.put("d099.png",R.drawable.d099);
//		map.put("d100.png",R.drawable.d100);
//		map.put("d101.png",R.drawable.d101);
//		map.put("d102.png",R.drawable.d102);
//		map.put("d103.png",R.drawable.d103);
//		map.put("d104.png",R.drawable.d104);
//		map.put("d105.png",R.drawable.d105);
//		map.put("d106.png",R.drawable.d106);
//		map.put("d107.png",R.drawable.d107);
//
//
//	}
	protected void openWaitDialog() {
		CDialog.showLoading(mContext);
	}

	protected void closeWaitDialog() {
		CDialog.hideLoading();
	}

	void setHashMap(){

		map.put("d001.png",R.drawable.d001);
		map.put("d002.png",R.drawable.d002);
		map.put("d003.png",R.drawable.d003);
		map.put("d004.png",R.drawable.d004);
		map.put("d005.png",R.drawable.d005);
		map.put("d006.png",R.drawable.d006);
		map.put("d007.png",R.drawable.d007);
		map.put("d008.png",R.drawable.d008);
		map.put("d009.png",R.drawable.d009);
		map.put("d010.png",R.drawable.d010);
		map.put("d011.png",R.drawable.d011);
		map.put("d012.png",R.drawable.d012);
		map.put("d013.png",R.drawable.d013);
		map.put("d014.png",R.drawable.d014);
		map.put("d015.png",R.drawable.d015);
		map.put("d016.png",R.drawable.d016);
		map.put("d017.png",R.drawable.d017);
		map.put("d018.png",R.drawable.d018);
		map.put("d019.png",R.drawable.d019);
		map.put("d020.png",R.drawable.d020);
		map.put("d021.png",R.drawable.d021);
		map.put("d022.png",R.drawable.d022);
		map.put("d023.png",R.drawable.d023);
		map.put("d024.png",R.drawable.d024);
		map.put("d025.png",R.drawable.d025);
		map.put("d026.png",R.drawable.d026);
		map.put("d027.png",R.drawable.d027);
		map.put("d028.png",R.drawable.d028);
		map.put("d029.png",R.drawable.d029);
		map.put("d030.png",R.drawable.d030);
		map.put("d031.png",R.drawable.d031);
		map.put("d032.png",R.drawable.d032);
		map.put("d033.png",R.drawable.d033);
		map.put("d034.png",R.drawable.d034);
		map.put("d035.png",R.drawable.d035);
		map.put("d036.png",R.drawable.d036);
		map.put("d037.png",R.drawable.d037);
		map.put("d057.png",R.drawable.d057);
		map.put("d058.png",R.drawable.d058);
		map.put("d059.png",R.drawable.d059);
		map.put("d060.png",R.drawable.d060);
		map.put("d061.png",R.drawable.d061);
		map.put("d062.png",R.drawable.d062);
		map.put("d063.png",R.drawable.d063);
		map.put("d064.png",R.drawable.d064);
		map.put("d065.png",R.drawable.d065);
		map.put("d066.png",R.drawable.d066);
		map.put("d067.png",R.drawable.d067);
		map.put("d068.png",R.drawable.d068);
		map.put("d069.png",R.drawable.d069);
		map.put("d070.png",R.drawable.d070);
		map.put("d071.png",R.drawable.d071);
		map.put("d072.png",R.drawable.d072);
		map.put("d073.png",R.drawable.d073);
		map.put("d074.png",R.drawable.d074);
		map.put("d075.png",R.drawable.d075);
		map.put("d076.png",R.drawable.d076);
		map.put("d077.png",R.drawable.d077);
		map.put("d078.png",R.drawable.d078);
		map.put("d079.png",R.drawable.d079);
		map.put("d080.png",R.drawable.d080);
		map.put("d081.png",R.drawable.d081);
		map.put("d082.png",R.drawable.d082);
		map.put("d083.png",R.drawable.d083);
		map.put("d084.png",R.drawable.d084);
		map.put("d086.png",R.drawable.d086);
		map.put("d087.png",R.drawable.d087);
		map.put("d088.png",R.drawable.d088);
		map.put("d089.png",R.drawable.d089);
		map.put("d090.png",R.drawable.d090);
		map.put("d091.png",R.drawable.d091);
		map.put("d092.png",R.drawable.d092);
		map.put("d093.png",R.drawable.d093);
		map.put("d094.png",R.drawable.d094);
		map.put("d095.png",R.drawable.d095);
		map.put("d096.png",R.drawable.d096);
		map.put("d097.png",R.drawable.d097);
		map.put("d098.png",R.drawable.d098);
		map.put("d099.png",R.drawable.d099);
		map.put("d100.png",R.drawable.d100);
		map.put("d101.png",R.drawable.d086);
		map.put("d102.png",R.drawable.d087);
		map.put("d103.png",R.drawable.d088);
		map.put("d104.png",R.drawable.d089);
		map.put("d105.png",R.drawable.d090);
		map.put("d106.png",R.drawable.d091);
		map.put("d107.png",R.drawable.d092);
		map.put("d108.png",R.drawable.d093);
		map.put("d109.png",R.drawable.d094);
		map.put("d110.png",R.drawable.d095);
		map.put("d111.png",R.drawable.d096);
		map.put("d112.png",R.drawable.d097);
		map.put("d113.png",R.drawable.d098);
		map.put("d114.png",R.drawable.d099);
		map.put("d115.png",R.drawable.d100);
		map.put("d116.png",R.drawable.d101);
		map.put("d117.png",R.drawable.d102);
		map.put("d118.png",R.drawable.d103);
		map.put("d119.png",R.drawable.d104);
		map.put("d120.png",R.drawable.d105);
		map.put("d121.png",R.drawable.d106);
		map.put("d122.png",R.drawable.d107);


	}
	public void doSetList(int size,List<Integer> list,int position){
		if(size!=0){
			//list0=new LinkedList<Integer>();

			for(int i=1;i<size+1;i++){
				if(!list.contains(position)){
					list.add(position);

				}


			}	
		}else{
			list.add(position);
		}
		Collections.sort(list);
		for(int i=0;i<list.size();i++)
			Log.v(TAG,"int i"+list.get(i).intValue()+"");
	}

	class ViewHolder {
		public ImageView image;
		public LinearLayout loadinglinear;
		public ImageView loading;
		public TextView title;
		public TextView question;
		public TextView date;
		
	}


}
