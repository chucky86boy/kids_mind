package com.mb.kids_mind.Dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.mb.kids_mind.KidsMindAnalyzeActivity;
import com.mb.kids_mind.KidsMindDrawActivity;
import com.mb.kids_mind.R;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MyDialog extends DialogFragment {
	Activity activity;
	String DirPath;
	String savename;
	static Uri uri;
	String bpath;

	View.OnClickListener bHandler =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		switch(v.getId()){
		case R.id.camera:
/*			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			
			//Log.v(d.getTaga(),"�����"+path.getAbsolutePath());
			
			DirPath = Environment.getExternalStorageDirectory().getAbsolutePath();       
			DirPath = DirPath + "/" + "MindDrawing" + "/";	         
			Log.v(d.getTaga(),"�Ƕ���Ǹ��ɵǷ�"+DirPath);

			File cameraDir = new File(DirPath);
			if( !cameraDir.exists() ){
				Log.v(d.getTaga(),"�����"+DirPath);

				cameraDir.mkdirs();
			}
			Log.v(d.getTaga(),"�̹��ִ�"+DirPath);

			
			savename = System.currentTimeMillis()+".jpg";
			
			File save=new File(cameraDir,savename);
			
			 uri = Uri.fromFile(save);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri ) ;
			
			//intent.setAction( android.provider.MediaStore.ACTION_IMAGE_CAPTURE) ; // ��� �ܸ����� �ȵ� �� �ֱ� ������ �����ؾ� ��
			 
			 
			 
			startActivityForResult( intent, 1 ) ;
			*/
			SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			bpath=String.valueOf(System.currentTimeMillis()) + ".jpg";
			editor.putString("bpath", bpath);
			editor.commit();
			String url = "/MindDrawing/" +bpath;
		    uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		 
		    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

		    intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


		    startActivityForResult(intent, 0);
//			Intent intent = new Intent(Intent.ACTION_PICK);
//			intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//			startActivityForResult(intent, 2);

			//dialog.dismiss();
			break;
		case R.id.album:
			doTakeAlbumAction();
			break;
		case R.id.picture:
			Intent i=new Intent(activity,KidsMindDrawActivity.class);
		     
			startActivity(i);
			dismiss();
			break;
		}
			
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = null;
		v = inflater.inflate(R.layout.mydialog, null);
		getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		/*DisplayMetrics displayMetrics = new DisplayMetrics();
		 activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		 int deviceWidth = displayMetrics.widthPixels;
		 int deviceHeight =displayMetrics.heightPixels;
		   LayoutParams params = getDialog().getWindow().getAttributes();    
		   params.width = LayoutParams.MATCH_PARENT;  
		   params.height = LayoutParams.MATCH_PARENT; 
		   getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		   
		*///getDialog().getWindow().setLayout(deviceWidth-100, deviceHeight-500);
		v.findViewById(R.id.camera).setOnClickListener(bHandler);
		v.findViewById(R.id.picture).setOnClickListener(bHandler);
		v.findViewById(R.id.album).setOnClickListener(bHandler);
		return v;
	}
	
	
	 @Override
	public void onPause() {
		// TODO Auto-generated method stub
		 
		super.onPause();
	}
FileOutputStream fos;
File f=null;
Uri imgUri=null;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
	    	super.onActivityResult(requestCode,resultCode,data);
	    	if(resultCode==activity.RESULT_OK){
	    		if(requestCode==0){
	    			 			
	    			//Uri currImageURI=uri;
	    			Intent intent2=new Intent(activity,KidsMindAnalyzeActivity.class);
	    			//intent2.setData(currImageURI);
	    			String where ="2";
	    			intent2.putExtra("where",where);
	    			SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
	    			SharedPreferences.Editor editor=pref.edit();
	    			String bpath2=pref.getString("bpath", "0");
	    			intent2.putExtra("path", bpath2);
	    			startActivity(intent2);
	    			dismiss();
	    		}
	    		else if(requestCode==1){
	    			Uri mImageCaptureUri = data.getData(); // 갤러리에서 선택된 사진의 Uri 리턴
					try {
						Bitmap photo = Images.Media.getBitmap(activity.getContentResolver(), mImageCaptureUri);
						Save(photo,fos);
						Intent intent2=new Intent(activity,KidsMindAnalyzeActivity.class);
		    			//intent2.setData(currImageURI);
		    			String where ="2";
		    			intent2.putExtra("where",where);
		    			SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
		    			SharedPreferences.Editor editor=pref.edit();
		    			String bpath2=pref.getString("bpath", "0");
		    			intent2.putExtra("path", bpath2);
		    			//Log.v(Debugc.getTagd(),"�̹�����δ� 111111"+bpath2);
		    			startActivity(intent2);
		    			dismiss();
		    			
						//Bitmap photo = Images.Media.getBitmap(getContentResolver(), mImageCaptureUri); // Uri로 이미지 가져오기
						//Log.e(TAG, "PICK_FROM_ALBUM : " + photo.getHeight() * photo.getWidth()); // 확인코드
					} catch (Exception e) {
						//Log.e(TAG, "PICK_FROM_ALBUM : " + e.toString());
						return;
					}
				

	    		}
	    		
	    	}
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
			// �ٹ� ȣ��
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
}
