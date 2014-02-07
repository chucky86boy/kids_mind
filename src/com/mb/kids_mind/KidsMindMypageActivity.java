package com.mb.kids_mind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.kids_mind.Helper.MyHelper;

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
			popupImage(KidsMindMypageActivity.this);
			
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
MyHelper myhelper;
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
		myhelper = new MyHelper(this, "kidsmind.db", null, 1);
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
	    		profile.setImageResource(R.drawable.pic_mypage_girl);
				
	    	
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
	Dialog dialog=null; 
	void popupImage(Activity context) {
		// Create dialog
		 dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.profile);
		dialog.findViewById(R.id.regpic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 사진 등록
						doTakeAlbumAction();
						dialog.dismiss();
					}
				});
		dialog.findViewById(R.id.canpic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		dialog.show();
	}
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private void doTakeAlbumAction() {
		// �ٹ� ȣ��
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putString("where", "album");
		editor.commit();
	}

	public static String strFilePath = Environment
			.getExternalStorageDirectory() + "/tmp.jpg";

	private Bitmap photo;
	RoundedAvatarDrawable profile3;
	private Uri mImageCaptureUri;
	private String outFilePath ; //
	SQLiteDatabase db;
	public void openDB() {
		// db = openOrCreateDatabase("sample.db", wi, null);
		db = myhelper.getWritableDatabase();
	}

	// dbClose();
	public void closeDB() {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}

	}
	void updateRec(String user_id, String imagepath){
		openDB();
		Log.v(TAG,"userid"+user_id+"imagepath"+imagepath);
		//String sql="update tmember set age =32 where fname like '%k%';";
		ContentValues values = new ContentValues();
		values.put("image_path", imagepath);
		String whereClause="user_id like ?";
		String[] whereArgs={"%" + user_id + "%"};
		
		try{
		Log.v(TAG,"시작");
			int cnt=db.update("km_baby", values, whereClause, whereArgs);
	Log.v(TAG,"pass/fail"+cnt+"");
	Log.v(TAG,"성공");
		
		}catch(SQLException e){
			
		}
		
		closeDB();
	}
	@Override
	// 카메라 또는 앨범
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // ����ó��
			if (data == null) {

				if (requestCode == PICK_FROM_CAMERA && mImageCaptureUri != null) {
					File f = new File(mImageCaptureUri.getPath());
					if (f.exists()) {
						// f.delete();
					}
				}
			}
			return;
		}
		// 크롭 사용안함

		switch (requestCode) {
		case CROP_FROM_CAMERA: {
			//updateinsertRec(user_name,user,babyname, birthdate, sex, imagepath);
			SharedPreferences pref = getSharedPreferences("pref",
					MODE_PRIVATE);
			
			photo = BitmapFactory.decodeFile(outFilePath);
			// RoundedAvatarDrawable profile
			profile3 = new RoundedAvatarDrawable(photo);
			if ("boy".equals(babysex)) {

				profile.setImageDrawable(profile3);
			} else if ("girl".equals(babysex)) {
				profile.setImageDrawable(profile3);
			}
			if (mImageCaptureUri != null) {
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists()) {
					// f.delete();

					if ("boy".equals(babysex)) {

						profile.setImageDrawable(profile3);
					} else if ("girl".equals(babysex)) {
						profile.setImageDrawable(profile3);
					}
				}
			}

			break;
		}

		case PICK_FROM_ALBUM: {
			Bitmap photo = null;
			if (requestCode == PICK_FROM_ALBUM) {
				mImageCaptureUri = data.getData(); // ���������� ���õ� ������
				// Uri ����
				SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
				SharedPreferences.Editor editor =pref.edit();
				String path = getRealPathFromURI(mImageCaptureUri);
				
			
				updateRec(user_id,path);
				Log.v(TAG,"앨범에서"+path);
				editor.putString("image_path", path);
				editor.commit();
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					String[] proj = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(mImageCaptureUri,

							proj, // Which columns to return

							null, // WHERE clause; which rows to return (all
							// rows)

							null, // WHERE clause selection arguments (none)

							null); // Order-by clause (ascending by name)

					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

					cursor.moveToFirst();

					mImageCaptureUri = Uri
							.parse(cursor.getString(column_index));

					String uploadImagePath = mImageCaptureUri.getEncodedPath();

					photo = BitmapFactory.decodeFile(uploadImagePath, options);

					options = getBitmapSize(options);

					photo = BitmapFactory.decodeFile(uploadImagePath, options);// Uri
					// 비트맵으로
					// 바꾸기
					photo = getBitmapResizePrc(photo, 150, 150);
					pref.getInt("exifD", 0);
					if ("camera".equals(pref.getString("where", "error")))
						photo = rotate(photo, pref.getInt("exifD", 0));

					RoundedAvatarDrawable profile3 = new RoundedAvatarDrawable(
							photo);
					if ("boy".equals(babysex)) {

						profile.setImageDrawable(profile3);
					} else if ("girl".equals(babysex)) {
						profile.setImageDrawable(profile3);
					}
					// profile=new RoundedAvatarDrawable(photo);
					// picture.setImageDrawable(profile);
				} catch (Exception e) {
					return;
				}
			}

			if (photo != null) {
				try {
					outFilePath=Environment
							.getExternalStorageDirectory()+"/KidsMind/"+System.currentTimeMillis()+".jpg";
					FileOutputStream fos = new FileOutputStream(outFilePath);
					Log.v(TAG,"imagepath"+outFilePath);
					photo.compress(CompressFormat.JPEG, 100, fos); // �̹��� ����
					fos.flush();
					fos.close();
				} catch (Exception e) {
					Log.e(TAG, "" + requestCode + " : " + e.toString());
					return;
				}
			} else {
				Log.e(TAG, "Bitmap is null"); // ����ó��
				return;
			}

			try {
				// Launch picker to choose photo for selected contact
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(getTempUri(), "image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra("aspectX", 320);
				intent.putExtra("aspectY", 320);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				intent.putExtra("noFaceDetection", true);
				intent.putExtra("circleCrop", false);

				 startActivityForResult(intent, CROP_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
				Log.e("crop_from_camera", e.toString());
			}
			break;
		}
		}
	}
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	public Options getBitmapSize(Options options) {

		int targetWidth = 0;

		int targetHeight = 0;

		if (options.outWidth > options.outHeight) {

			targetWidth = (int) (600 * 1.3);

			targetHeight = 600;

		} else {

			targetWidth = 600;

			targetHeight = (int) (600 * 1.3);

		}

		Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
				.abs(options.outWidth - targetWidth);

		if (options.outHeight * options.outWidth * 2 >= 16384) {

			double sampleSize = scaleByHeight

					? options.outHeight / targetHeight

							: options.outWidth / targetWidth;

			options.inSampleSize = (int) Math.pow(2d,
					Math.floor(Math.log(sampleSize) / Math.log(2d)));

		}

		options.inJustDecodeBounds = false;

		options.inTempStorage = new byte[16 * 1024];

		return options;

	}

	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	public Bitmap rotate(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);

			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != converted) {
					bitmap.recycle();
					bitmap = converted;
				}
			} catch (OutOfMemoryError ex) {
				// �޸𸮰� �����Ͽ� ȸ���� ��Ű�� ���� ��� �׳� ���� ��ȯ�մϴ�.
			}
		}
		return bitmap;
	}

	private File getTempFile() {
		if (isSDCARDMounted()) {
			File f = new File(outFilePath);
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
			return f;
		} else {
			return null;
		}
	}

	private boolean isSDCARDMounted() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public Bitmap getBitmapResizePrc(Bitmap Src, int newHeight, int newWidth) {
		BitmapDrawable result = null;
		int width, height;
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
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
	
}
