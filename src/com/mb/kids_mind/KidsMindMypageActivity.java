package com.mb.kids_mind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class KidsMindMypageActivity extends Activity {
private static final String TAG ="MainActivity";
TextView name,date;
ImageView sex,profile;
View.OnClickListener bHandler =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.home:
			finish();
			break;
		case R.id.profile:
			break;
		case R.id.sex:
		break;
		case R.id.myalbum:
			Intent in= new Intent(KidsMindMypageActivity.this,KidsMindAlbumactivity.class);
			in.putExtra("user_id", user_id);
			startActivity(in);
			break;
		case R.id.cousel:
			Intent in2= new Intent(KidsMindMypageActivity.this,KidsMindAlbumactivity.class);
			in2.putExtra("user_id", user_id);
			startActivity(in2);
			break;
		case R.id.name:
			break;
		case R.id.date:
			break;
		}
	}
};
String user_id,image_path,babyname,babydate,babysex;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mypage);
	    findViewById(R.id.home).setOnClickListener(bHandler);
	    findViewById(R.id.myalbum).setOnClickListener(bHandler);
	    findViewById(R.id.cousel).setOnClickListener(bHandler);
	    sex =(ImageView)findViewById(R.id.sex);
	    sex.setOnClickListener(bHandler);
	    profile =(ImageView)findViewById(R.id.profile);
	    profile.setOnClickListener(bHandler);
	    
	    name=(TextView)findViewById(R.id.name);
	    name.setOnClickListener(bHandler);
	    date=(TextView)findViewById(R.id.date);
	    date.setOnClickListener(bHandler);
	    Intent in= getIntent();
	    user_id=in.getStringExtra("user_id");
	    image_path=in.getStringExtra("image_path");
	    babyname=in.getStringExtra("name");
	    babydate=in.getStringExtra("date");
	    babysex=in.getStringExtra("sex");
	    Log.v(TAG,"image_path"+image_path);
	    if(!"none".equals(image_path)){
	    readimage(image_path,profile);
	    }else{
	    	if("boy".equals(babysex)){
				profile.setImageResource(R.drawable.pic_mypage_boy);
			}else if("girl".equals(babysex)){
				sex.setImageResource(R.drawable.pic_mypage_girl);
				
			}
	    }
	    
	    name.setText(babyname);
	    date.setText(babydate);
	    
	    if("boy".equals(babysex)){
			sex.setImageResource(R.drawable.icon_boyl);
		}else if("girl".equals(babysex)){
			sex.setImageResource(R.drawable.icon_girl);
			
		}

	}
    Bitmap bitmap;
    RoundedAvatarDrawable profile2;

	void readimage(String path,ImageView img){
		if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

try{
			//DbUse dbuse=new DbUse(MindDrawingResultActivity.this);
			//insertRec2(path2, "0");
			BitmapFactory.Options options =new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			bitmap=BitmapFactory.decodeFile(path,options);
			options =getBitmapSize(options);
			bitmap=BitmapFactory.decodeFile(path,options);
			Log.v(TAG,"이미지를 읽어오기위한 경로2"+path);
			bitmap = getBitmapResizePrc(bitmap, 150, 150);
			profile2=new RoundedAvatarDrawable(bitmap);
			img.setImageDrawable(profile2);
}catch(OutOfMemoryError e){
	Log.v(TAG,"outofmemoryerror");
}
			//						if(bitmap!=null){
//				Log.v(TAG,"이미지 로딩");
//				img.setImageBitmap(bitmap);
//			}else{
//			}

		}
	}
	public Bitmap getBitmapResizePrc(Bitmap Src, int newHeight, int newWidth) {
		BitmapDrawable result = null;
		int width, height;
		SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		if (Src != null) {
			width = Src.getWidth();
			height = Src.getHeight();
			editor.putInt("width", width);
			editor.putInt("height", height);
			editor.commit();
		} else {
			width = pref.getInt("width", 0);
			height = pref.getInt("height", 0);

		}

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

		// rotate the Bitmap ȸ�� ��Ű���� �ּ� ����!
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(Src, 0, 0, width, height,
				matrix, true);

		// check
		width = resizedBitmap.getWidth();
		height = resizedBitmap.getHeight();

		Log.i("ImageResize",
				"Image Resize Result : "
						+ Boolean.toString((newHeight == height)
								&& (newWidth == width)));

		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever

		return resizedBitmap;
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
}
