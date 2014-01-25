package com.mb.kids_mind.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

import com.mb.kids_mind.KidsMindAnalyzeActivity;
import com.mb.kids_mind.KidsMindDrawActivity;
import com.mb.kids_mind.R;

public class SingleSketchMenu extends Fragment{
private static final String TAG="MainActivity";
	int[] menuImage;
	private int position;
	public ImageView img; 

	FragmentManager fm;
	Activity activity;
	String DirPath;
	String savename;
	static Uri uri;
	String bpath;
	Dialog dialog;
	public float iscale;
	View.OnClickListener bHandler =new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.camera:
				SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				bpath=String.valueOf(System.currentTimeMillis()) + ".jpg";
				editor.putString("bpath", bpath);
				editor.commit();
				String url = "/KidsMind/" +bpath;
				uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

				intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


				startActivityForResult(intent, 0);
				//			Intent intent = new Intent(Intent.ACTION_PICK);
				//			intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				//			startActivityForResult(intent, 2);

				dialog.dismiss();
				break;
			case R.id.album:
				doTakeAlbumAction();

				break;
			case R.id.picture:
				
				Intent i=new Intent(activity,KidsMindDrawActivity.class);

				startActivity(i);
				dialog.dismiss();
				break;
			}

		}
	};
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		dialog=new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		super.onAttach(activity);
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_sketch, null);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
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
		
		img = (ImageView) view.findViewById(R.id.singeMenu);
		Log.v(TAG,"iscale"+iscale+"");
		//img.setScaleX(iscale);
		LayoutParams layoutParams = img.getLayoutParams();
		layoutParams.height = (int)(height * 0.7);
		img.setImageDrawable(getActivity().getResources().getDrawable(menuImage[position]));
		img.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					//doAction(v);
					break;
				case MotionEvent.ACTION_UP:
					SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putInt("qposition", position);
					editor.putInt("dbpath", position+1);//0遺�꽣�ㅼ뼱媛꾨떎
					editor.commit();

					switch (position)
					{
					case 0:
						popupImage(activity);

						//Toast.makeText(activity, "position"+position+"", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						popupImage(activity);
						//Toast.makeText(activity, "position"+position+"", Toast.LENGTH_SHORT).show();

						break;
					case 2:
						popupImage(activity);
						//Toast.makeText(activity, "position"+position+"", Toast.LENGTH_SHORT).show();

						break;
					case 3:
						popupImage(activity);
						//Toast.makeText(activity, "position"+position+"", Toast.LENGTH_SHORT).show();

						break;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					break;

				}
				return true;
			}
		});
		return view;
	}
	void popupImage(Activity context)
	{
		// Create dialog
		//	final Dialog dialog = new Dialog(context);

		dialog.setContentView(R.layout.mydialog);
		dialog.findViewById(R.id.camera).setOnClickListener(bHandler);
		dialog.findViewById(R.id.picture).setOnClickListener(bHandler);
		dialog.findViewById(R.id.album).setOnClickListener(bHandler);
		//�쇰뵒��踰꾪듉 
		dialog.show();
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
				
				startActivity(intent2);
				dialog.dismiss();
			}
			else if(requestCode==1){
				Uri mImageCaptureUri = data.getData(); // 媛ㅻ윭由ъ뿉���좏깮���ъ쭊��Uri 由ы꽩
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
			        Save(displayBitmap,fos);
			        
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
	public void scaleImage(float scale) {
		// TODO Auto-generated method stub
		Log.v(TAG,"scale"+scale+"");
		this.img.setScaleX(scale);
		iscale=scale;
	}

	public void Save(Bitmap mBitmap,OutputStream outstream) {
		try {

			DirPath = Environment.getExternalStorageDirectory().getAbsolutePath();       
			DirPath = DirPath + "/" + "KidsMind" + "/";	         

			File cameraDir = new File(DirPath);
			if( !cameraDir.exists() ){

				cameraDir.mkdirs();
			}
			SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
			/*File cameraDir1 = new File(cameraDir, "Petsitter");
					if( !cameraDir1.exists() ){
						cameraDir1.mkdirs();
					}*/


			bpath=String.valueOf(System.currentTimeMillis()) + ".jpg";
			editor.putString("bpath", bpath);
			editor.commit();
			//insertRec(getDate(),saveName);
			f = new File(cameraDir, bpath);

			outstream = new FileOutputStream( f );



			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);

		} catch (Exception e) {
			//Log.v(d.getTaga(),"erroer"+e);
		}
	}
	private void doTakeAlbumAction() {
		// 占쌕뱄옙 호占쏙옙
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, 1);
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
