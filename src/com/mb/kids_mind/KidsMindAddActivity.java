package com.mb.kids_mind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mb.kids_mind.Helper.MyHelper;

public class KidsMindAddActivity extends Activity implements
OnDateChangedListener {
	EditText name;
	ImageView boy, girl, birth;
	Button register;
	String sex;
	String birthdate;
	String babyname;
	SQLiteDatabase db;
	MyHelper myhelper;
	Dialog dialog=null; 
	private static final String TAG = "MainActivity";
	SharedPreferences pref;
	View.OnClickListener bHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.birth:
				popupbirth(KidsMindAddActivity.this);
				break;
			case R.id.boy:
				popupImage(KidsMindAddActivity.this);
				sex = "boy";

				break;
			case R.id.girl:

				popupImage(KidsMindAddActivity.this);
				sex = "girl";

				break;
			case R.id.register:
				pref = getSharedPreferences("pref", MODE_PRIVATE);
				pref.getString("image_path", "");
				String imagepath = pref.getString("image_path", "");
				babyname = name.getText().toString();
				if (babyname == null || sex == null || birthdate == null
						|| imagepath.equals("")) {
					Toast.makeText(KidsMindAddActivity.this, "모든항목을  채워 주세요",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.v(TAG,"babyname"+babyname+"birthdate"+birthdate+"sex"+sex+"imagepath"+imagepath);
					String user = "U"+System.currentTimeMillis();
					pref = getSharedPreferences("pref", MODE_PRIVATE);
					SharedPreferences.Editor editor =pref.edit();
					editor.putString("userid", user);
					editor.commit();
					
					String checking=pref.getString("login_check", "");
					String user_name;
					if("".equals(checking)){
						//not login
						 user_name="untitle";
						editor.putString("user_name", user_name);
				        editor.commit();
				        	
					}else{
						//로그인된경우
						 user_name=pref.getString("user_name", "");
						
					}
			        Log.v(TAG," insert username"+user_name);
					insertRec(user_name,user,babyname, birthdate, sex, imagepath);
					KidsMindAddActivity.this.setResult(RESULT_OK);
					finish();
					
				}
				// db insert
				break;
			case R.id.back_btn:
				finish();

			}
		}
	};
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
	RoundedAvatarDrawable profile;
	private Uri mImageCaptureUri;
	private String outFilePath = strFilePath; //

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

			photo = BitmapFactory.decodeFile(outFilePath);
			// RoundedAvatarDrawable profile
			profile = new RoundedAvatarDrawable(photo);
			if ("boy".equals(sex)) {

				boy.setImageDrawable(profile);
				girl.setImageResource(R.drawable.btn_girl);
			} else if ("girl".equals(sex)) {
				girl.setImageDrawable(profile);
				boy.setImageResource(R.drawable.btn_boy);
			}
			if (mImageCaptureUri != null) {
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists()) {
					// f.delete();

					if ("boy".equals(sex)) {

						boy.setImageDrawable(profile);
						girl.setImageResource(R.drawable.btn_girl);
					} else if ("girl".equals(sex)) {
						girl.setImageDrawable(profile);
						boy.setImageResource(R.drawable.btn_boy);
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
				String path = getRealPathFromURI(mImageCaptureUri);
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();

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

					RoundedAvatarDrawable profile = new RoundedAvatarDrawable(
							photo);
					if ("boy".equals(sex)) {

						boy.setImageDrawable(profile);
						girl.setImageResource(R.drawable.btn_girl);
					} else if ("girl".equals(sex)) {
						girl.setImageDrawable(profile);
						boy.setImageResource(R.drawable.btn_boy);
					}
					// profile=new RoundedAvatarDrawable(photo);
					// picture.setImageDrawable(profile);
				} catch (Exception e) {
					return;
				}
			}

			if (photo != null) {
				try {
					FileOutputStream fos = new FileOutputStream(outFilePath);
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

				// startActivityForResult(intent, CROP_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
				Log.e("crop_from_camera", e.toString());
			}
			break;
		}
		}
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

	private DatePicker datePicker;
	private TextView birthtext;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbaby);
		name = (EditText) findViewById(R.id.name);
		birth = (ImageView) findViewById(R.id.birth);
		birthtext=(TextView)findViewById(R.id.birthtext);
		birth.setOnClickListener(bHandler);
		boy = (ImageView) findViewById(R.id.boy);
		boy.setOnClickListener(bHandler);
		findViewById(R.id.back_btn).setOnClickListener(bHandler);
		girl = (ImageView) findViewById(R.id.girl);
		girl.setOnClickListener(bHandler);
		myhelper = new MyHelper(this, "kidsmind.db", null, 1);
		findViewById(R.id.register).setOnClickListener(bHandler);

		// TODO Auto-generated method stub
	}

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

	void popupbirth(Activity context) {
		// Create dialog
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setContentView(R.layout.dialbirth);
		datePicker = (DatePicker) dialog.findViewById(R.id.datePicker1);

		datePicker.init(Calendar.getInstance().get(Calendar.YEAR), (Calendar
				.getInstance().get(Calendar.MONTH)), Calendar.getInstance()
				.get(Calendar.DAY_OF_MONTH), this);
		dialog.findViewById(R.id.reg).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// datapicker에서 받아오는 날짜를 등록
				// date등록
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.can).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void insertRec(String user_name,String user_id,String babyname, String birthdate, String sex,
			String imagepath) {
		// if(selectDb(detail_id)){
		openDB();

		ContentValues values = new ContentValues();
		try {
			values.put("user_name", user_name);
			
			values.put("user_id", user_id);
			values.put("name", babyname);

			values.put("birth", birthdate);

			values.put("sex", sex);
			values.put("image_path", imagepath);
			
			long id = db.insert("km_baby", null, values);

			Log.v(TAG, id > 0 ? "success" : "fail");
		} catch (SQLException e) {

		}
		closeDB();

	}

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

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		birthdate = year + "" + "." + monthOfYear+1 + "" + "." + dayOfMonth + "";
		birthtext.setText(birthdate);
	}
}
