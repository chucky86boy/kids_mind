package com.mb.kids_mind.fragment;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.mb.kids_mind.KidsMindAnalyzeActivity;
import com.mb.kids_mind.R;

import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.SimilarItem;
import com.mb.kids_mind.task.NetManager;
public class LastSimilarImage extends Fragment{
	private static final String TAG="MainActivity";
	public int[] menuImage;
	private int position;
	public LinearLayout img; 
	
	FragmentManager fm;
	Activity activity;
	Dialog dialog;
	TextView title;
	ImageView image;
	TextView contents;
	ArrayList<SimilarItem> list;
	SimilarItem item;
	LinearLayout loadinglinear;
	ImageView loadingimg;
	 private AnimationDrawable loadingViewAnim=null;
	public void setPosition(int position) {
		this.position = position;
	}
	public void setDate(ArrayList<SimilarItem> list){
		this.list=list;
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
		View view = inflater.inflate(R.layout.lastviewpager, null);
		 	title=(TextView)view.findViewById(R.id.title);
		 	image=(ImageView)view.findViewById(R.id.image);
		 	contents=(TextView)view.findViewById(R.id.contents);
		 	loadingimg=(ImageView)view.findViewById(R.id.loadingimg);
		 	loadingimg.setVisibility(View.GONE);
		 	loadingimg.setBackgroundResource(R.anim.progress);
			loadinglinear=(LinearLayout)view.findViewById(R.id.loading);
			loadinglinear.setVisibility(View.GONE);
			loadingViewAnim=(AnimationDrawable)loadingimg.getBackground();
		 	item =new SimilarItem();
		 	item=list.get(position);
		 	if("Q1".equals(item.getQuestion_id())){
		 		title.setText("집 그림 그리기");	
		 	}else if("Q2".equals(item.getQuestion_id())){
		 		title.setText("나무 그림 그리기");
		 	}else if("Q3".equals(item.getQuestion_id())){
		 		title.setText("사람 그림 그리기");
		 	}else if("Q4".equals(item.getQuestion_id())){
		 		title.setText("물고기 그림 그리기");
		 	}
		 	
		 	contents.setText(item.getComment_text());
//			new DownTask().execute(
//					(Const.IMAGE_LOAD_URL+"/" + item.advice_image).trim(),image);
		 	
//		 	title.setText(item.getImage_title());
//		 	//image.setImageBitmap(item.getImage_path());
//		 	image.setBackgroundResource(item.getRes());
//		 	contents.setText(item.getAdvice_contents());
		 	requestMyImage(loadingimg,loadinglinear,image, item.advice_image);
				return view;
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

			img = (ImageView) params[1];
			// img = (ImageView)params[2];
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

//					FileOutputStream fs = new FileOutputStream(ft);
//					fs.write(imgArr);
//					fs.flush();
//					fs.close();

//					bitmap.compress(CompressFormat.PNG, 100,
//							new FileOutputStream((File) params[1]));
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

				 img.setImageBitmap(result);
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

	
	
	FileOutputStream fos;
	File f=null;
	Uri imgUri=null;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(resultCode==activity.RESULT_OK){
			if(requestCode==0){

				//Uri currImageURI=data.getData();
				Intent intent2=new Intent(activity,KidsMindAnalyzeActivity.class);
			//	intent2.setData(currImageURI);
				String where ="2";
				intent2.putExtra("where",where);
				SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				String bpath2=pref.getString("bpath", "0");
				
				intent2.putExtra("path", bpath2);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				dialog.dismiss();
				startActivity(intent2);
				
			}
			else if(requestCode==1){
				Uri mImageCaptureUri = data.getData(); 
				BitmapFactory.Options options =new BitmapFactory.Options(); 
				options.inJustDecodeBounds=true;
				String [] proj={MediaStore.Images.Media.DATA};   

			        Cursor cursor =activity.managedQuery( mImageCaptureUri,   

			                proj, // Which columns to return   

			                null,       // WHERE clause; which rows to return (all rows)   

			                null,       // WHERE clause selection arguments (none)   

			                null); // Order-by clause (ascending by name)   

			        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   

			        cursor.moveToFirst();   

			        

			        mImageCaptureUri = Uri.parse(cursor.getString(column_index)); 

			      String  uploadImagePath = mImageCaptureUri.getEncodedPath(); 

			        Bitmap displayBitmap = BitmapFactory.decodeFile(uploadImagePath, options); 

			      
			          

			        options = getBitmapSize(options); 

			        displayBitmap = BitmapFactory.decodeFile(uploadImagePath, options); 
			        
//					Log.v(TAG,"1");
//					Bitmap photo = Images.Media.getBitmap(activity.getContentResolver(), mImageCaptureUri);
//					Log.v(TAG,"2");
//					Save(photo,fos);
//					Log.v(TAG,"3");
					Intent intent2=new Intent(activity,KidsMindAnalyzeActivity.class);
					//intent2.setData(currImageURI);
					String where ="2";
					intent2.putExtra("where",where);
					SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					String bpath2=pref.getString("bpath", "0");
					intent2.putExtra("path", bpath2);
					dialog.dismiss();

					intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					//Log.v(Debugc.getTagd(),"占싱뱄옙占쏙옙占쏙옙灌占�111111"+bpath2);
					startActivity(intent2);
					dialog.dismiss();

					//Bitmap photo = Images.Media.getBitmap(getContentResolver(), mImageCaptureUri); // Uri濡��대�吏�媛�졇�ㅺ린
					//Log.e(TAG, "PICK_FROM_ALBUM : " + photo.getHeight() * photo.getWidth()); // �뺤씤肄붾뱶
				

			}

		}
	}
	public Options getBitmapSize(Options options){ 

        int targetWidth = 0; 

        int targetHeight = 0; 

          

        if(options.outWidth > options.outHeight){     

            targetWidth = (int)(600 * 1.3); 

            targetHeight = 600; 

        }else{ 

            targetWidth = 600; 

            targetHeight = (int)(600 * 1.3); 

        } 

  

        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth); 

        if(options.outHeight * options.outWidth * 2 >= 16384){ 

            double sampleSize = scaleByHeight 

                ? options.outHeight / targetHeight 

                : options.outWidth / targetWidth; 

            options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize)/Math.log(2d))); 

        } 

        options.inJustDecodeBounds = false; 

        options.inTempStorage = new byte[16*1024]; 

          

        return options; 

    }
	
	
	
	public String getRealPathFromURI(Uri contentUri){
		String[] proj={MediaStore.Images.Media.DATA};
		Cursor cursor=activity.managedQuery(contentUri, proj, null, null, null);
		int column_index =cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
