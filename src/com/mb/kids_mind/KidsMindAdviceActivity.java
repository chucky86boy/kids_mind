package com.mb.kids_mind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.mb.kids_mind.Adapter.AwesomeAdapter;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.AdviceItem2;
import com.mb.kids_mind.Item.CommentList;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;
import com.mb.kids_mind.Item.MyJsonparser3.MyJsonParser3;
import com.mb.kids_mind.task.NetManager;

public class KidsMindAdviceActivity extends Activity {
	Button trans;
	private static final String TAG = "MainActivity";
	private AQuery aquery;
	private String imagePath;
	MyHelper helper;
	String bpath;
	static Uri uri;
	String DirPath;
	SQLiteDatabase db;
	private Uri mImageCaptureUri;
	private Bitmap photo;
	String detail_id;
	ArrayList<CommentList> data = new ArrayList<CommentList>();
	AdviceItem2 advice;

	View.OnClickListener bHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				finish();
				break;
			case R.id.home:
				SharedPreferences pref2 =getSharedPreferences("pref", MODE_PRIVATE);
    		    SharedPreferences.Editor editor=pref2.edit();
    		    editor.putString("noti", "");
    		    editor.commit();
				Intent intent=new Intent(KidsMindAdviceActivity.this,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("check", "1");
				startActivity(intent);
				break;
			case R.id.trans:
				// 첫번째만
				loading(KidsMindAdviceActivity.this);
				//bg.setVisibility(View.GONE);
				//list.setVisibility(View.VISIBLE);
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				SharedPreferences.Editor editor3=pref.edit();
				String message = msg.getText().toString();
				int che = -1;
				if ("direct".equals(where)) {
					String check = pref.getString("direct", "");
					if ("".equals(check)) {
						Toast.makeText(KidsMindAdviceActivity.this,
								"이미지를 추가해 주세요", Toast.LENGTH_SHORT).show();
					} else {
						String savename = pref.getString("bpath", "");
						che = selectAll(savename);
						if (che == -1) {
							asyncImageUploadJson(message);

						} else {
							//asynccommentJson(message);

						}

					}
				} else {
					Log.v(TAG, "a");
					String savename=pref.getString("savename", "");
					che = selectAll(savename);
				
					editor3.putInt("advice_id", che);
					editor3.commit();
					if (che == -1) {
						asyncImageUploadJson(message);

					} else {
						asynccommentJson(message,che);

					}

				}
				Log.v(TAG, "checkadvice_id" + che + "");
				msg.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(msg.getWindowToken(), 0);
			
				break;
			case R.id.imageView1:
				break;
			case R.id.button1:// 이미지 업로드

				break;
			case R.id.button2:
				String message2 = msg.getText().toString();
				Log.v(TAG, "message" + message2);

				break;

			case R.id.camera:
				// SharedPreferences
				// pref=getSharedPreferences("pref",MODE_PRIVATE);
				// SharedPreferences.Editor editor=pref.edit();
				//
				// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// bpath=String.valueOf(System.currentTimeMillis()) + ".jpg";
				// editor.putString("bpath", bpath);
				// editor.commit();
				// String url = "/KidsMind/" +bpath;
				// uri = Uri.fromFile(new
				// File(Environment.getExternalStorageDirectory(), url));
				//
				// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				// uri);
				//
				// intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				//
				//
				// startActivityForResult(intent, 0);
				// // Intent intent = new Intent(Intent.ACTION_PICK);
				// //
				// intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				// // startActivityForResult(intent, 2);

				doTakePhotoAction();
				dialog.dismiss();
				break;
			case R.id.album:
				doTakeAlbumAction();
				dialog.dismiss();
				break;
			case R.id.picture:

				dialog.dismiss();
				break;
			case R.id.more:
				//list.setVisibility(View.GONE);
				bottom.setVisibility(View.VISIBLE);
			//	bg.setVisibility(View.VISIBLE);
				break;
			}
		}
	};
	String savename;
	TextView title, date;
	String where;
	EditText msg;
	ImageView icon, imageView1;
	public LinearLayout bg, bg2, set,morebutton,bottom;
	public ListView list;
	AwesomeAdapter adapter;
	public Button more;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advice);
		findViewById(R.id.back_btn).setOnClickListener(bHandler);
		findViewById(R.id.home).setOnClickListener(bHandler);
		more=(Button)findViewById(R.id.more);
				more.setOnClickListener(bHandler);
				morebutton=(LinearLayout)findViewById(R.id.morebutton);
				morebutton.setVisibility(View.GONE);
				
		trans = (Button) findViewById(R.id.trans);
		trans.setOnClickListener(bHandler);
		//trans.setVisibility(View.GONE);
		msg=(EditText)findViewById(R.id.text);
		
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("direct", "");
		editor.commit();
		
		title = (TextView) findViewById(R.id.title);
		date = (TextView) findViewById(R.id.date);
		icon = (ImageView) findViewById(R.id.icon);
		// msg=(EditText)findViewById(R.id.editText1);

		list = (ListView) findViewById(R.id.listView1);
		// int
		// height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*data.size();
		// LayoutParams lp=(LayoutParams) list.getLayoutParams();
		// lp.height=height;
		// list.setLayoutParams(lp);
		adapter = new AwesomeAdapter(KidsMindAdviceActivity.this, data);
		list.setAdapter(adapter);
		aquery = new AQuery(this);
		Intent in = getIntent();
		
		savename = in.getStringExtra("savename");

		date.setText(in.getStringExtra("date"));

		editor.putString("savename", savename);
		editor.commit();
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		bottom = (LinearLayout) findViewById(R.id.bottom_write_bar);
		bottom.setVisibility(View.GONE);
		//		
		//		bg = (LinearLayout) findViewById(R.id.bg);
//		bg2 = (LinearLayout) findViewById(R.id.bg2);
//		bg.setBackgroundColor(0x00000000);

		set = (LinearLayout) findViewById(R.id.set);
		helper = new MyHelper(KidsMindAdviceActivity.this, "kidsmind.db", null,
				1);
		
//		msg = new EditText(this) {
//			@Override
//			public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//
//					switch (trans.getVisibility()) {
//					case View.GONE:
//						Log.v(TAG, "gone");
//						// finish();
//						break;
//					case View.VISIBLE:
//						bg.setBackgroundColor(0x00000000);
//						trans.setVisibility(View.GONE);
//						break;
//					}
//				}
//
//				return super.onKeyPreIme(keyCode, event);
//			}
//		};
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		((View) msg).setPadding(50, 50, 0, 0);
//		msg.setBackgroundResource(R.drawable.box_text);
//		msg.setTextColor(0xff000000);
//		msg.setHint("내용을 입력해 주세요");
//		msg.setGravity(Gravity.TOP);
//		msg.setHintTextColor(0xff000000);
//		params.setMargins(10, 10, 10, 10);
//		msg.setLayoutParams(params);
//		bg2.addView(msg);
//		msg.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (hasFocus) {
//					bg.setBackgroundResource(R.drawable.bg_text_box);
//					trans.setVisibility(View.VISIBLE);
//
//				}
//			}
//		});
//		msg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Log.v(TAG, "msgclick");
//				bg.setBackgroundResource(R.drawable.bg_text_box);
//				trans.setVisibility(View.VISIBLE);
//			}
//		});
		
		where = in.getStringExtra("where");
		editor.putString("whereintent", where);
		editor.commit();
		if ("direct".equals(where)) {
			imageView1.setImageResource(R.drawable.btn_doctor);
			imageView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popupImage(KidsMindAdviceActivity.this);
					// 앨범 사진 으로 전송
				}
			});
		} else if("album".equals(where)){
			loading(KidsMindAdviceActivity.this);
			readimage(savename);
			int advc = selectAll(savename);
			
			String url2 = Const.ADVICE_COMMENT_LIST + advc + "";
			Log.v(TAG, "상담리스트 url" + url2);

			new JobTask().execute(url2);

		}else if("albumdetail".equals(where)){
			loading(KidsMindAdviceActivity.this);
			readimage(savename);
			int advc = selectAll(savename);
			
			String url2 = Const.ADVICE_COMMENT_LIST + advc + "";
			Log.v(TAG, "상담리스트 url" + url2);

			new JobTask().execute(url2);

		}
		else {
			readimage(savename);
			int advc = selectAll(savename);

			String url2 = Const.ADVICE_COMMENT_LIST + advc + "";
			Log.v(TAG, "상담리스트 url" + url2);

			new JobTask().execute(url2);
		}

		// TODO Auto-generated method stub
	}
	Dialog dialog2=null;
	void loading(Activity context)
	{
		// Create dialog
		 dialog2 = new Dialog(context);
		dialog2.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog2.setCancelable(false);
		dialog2.setCanceledOnTouchOutside(false);
		dialog2.setContentView(R.layout.progress);
		dialog2.findViewById(R.id.imageView1);
		dialog2.findViewById(R.id.imageView1).setVisibility(ImageView.VISIBLE);
		dialog2.findViewById(R.id.imageView1).setBackgroundResource(R.anim.progress);

		AnimationDrawable frameAnimation = (AnimationDrawable) dialog2.findViewById(R.id.imageView1).getBackground();
		frameAnimation.start();
		//라디오 버튼 
		dialog2.show();
	}
	public float iscale;

	public void scaleImage(float scale) {
		// TODO Auto-generated method stub
		Log.v(TAG, "scale" + scale + "");
		this.imageView1.setScaleX(scale);
		iscale = scale;
	}

	@Override
	protected void onResume() {
		helper = new MyHelper(KidsMindAdviceActivity.this, "kidsmind.db", null,
				1);
		super.onResume();
	}

	Dialog dialog = null;

	void popupImage(Activity context) {
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.mydialog);
		dialog.findViewById(R.id.camera).setOnClickListener(bHandler);
		dialog.findViewById(R.id.picture).setOnClickListener(bHandler);
		dialog.findViewById(R.id.album).setOnClickListener(bHandler);
		dialog.show();
	}

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
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

	private void doTakePhotoAction() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			String url = "tmp_" + String.valueOf(System.currentTimeMillis())
					+ ".png";
			mImageCaptureUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), url));

			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PICK_FROM_CAMERA);

		} catch (ActivityNotFoundException anfe) {

		}
	}

	public static String strFilePath = Environment
			.getExternalStorageDirectory() + "/tmp.jpg";
	private String outFilePath = strFilePath;
	FileOutputStream fos;
	File f = null;

	private void doTakeAlbumAction() {

		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			if (data == null) {
				Log.e(TAG, "resultCode : " + resultCode + " Error!!! ");

				if (requestCode == PICK_FROM_CAMERA && mImageCaptureUri != null) {
					File f = new File(mImageCaptureUri.getPath());
					if (f.exists()) {
						f.delete();
					}
				}
			}
			return;
		}

		switch (requestCode) {
		case CROP_FROM_CAMERA: {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			photo = BitmapFactory.decodeFile(outFilePath, options);
			options = getBitmapSize(options);

			photo = BitmapFactory.decodeFile(outFilePath, options);
			Save(photo, fos);
			SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("direct", "1");
			editor.commit();
			String userid = pref.getString("userid", "");
			insertRec(bpath, "", "0", -1, "Q1", userid);
			if (photo.getWidth() > photo.getHeight()) {
				set.setVisibility(View.GONE);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.weight = 0;
				set.setLayoutParams(params);

			} else {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT);
				params.weight = 1;
				set.setLayoutParams(params);
				set.setVisibility(View.VISIBLE);

			}

			imageView1.setImageBitmap(photo);
			// asyncImageUploadJson(photo);

			if (mImageCaptureUri != null) {
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists()) {
					f.delete();
				}
			}

			break;
		}

		case PICK_FROM_ALBUM:
		case PICK_FROM_CAMERA: {
			Bitmap photo = null;
			if (requestCode == PICK_FROM_ALBUM) {
				mImageCaptureUri = data.getData();
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

					photo = BitmapFactory.decodeFile(uploadImagePath, options);

					Log.e(TAG,
							"PICK_FROM_ALBUM : " + photo.getHeight()
									* photo.getWidth());
				} catch (Exception e) {
					Log.e(TAG, "PICK_FROM_ALBUM : " + e.toString());
					return;
				}
			}

			if (requestCode == PICK_FROM_CAMERA) {
				if (data == null) {
					try {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;
						// String [] proj={MediaStore.Images.Media.DATA};
						//
						// Cursor cursor =managedQuery( mImageCaptureUri,
						//
						// proj, // Which columns to return
						//
						// null, // WHERE clause; which rows to return (all
						// rows)
						//
						// null, // WHERE clause selection arguments (none)
						//
						// null); // Order-by clause (ascending by name)
						//
						// int column_index =
						// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						//
						// cursor.moveToFirst();
						//
						//
						//
						// mImageCaptureUri =
						// Uri.parse(cursor.getString(column_index));

						String uploadImagePath = mImageCaptureUri
								.getEncodedPath();

						photo = BitmapFactory.decodeFile(uploadImagePath,
								options);

						options = getBitmapSize(options);

						photo = BitmapFactory.decodeFile(uploadImagePath,
								options);

						ExifInterface exif = new ExifInterface(
								mImageCaptureUri.getPath());
						int exifOrientation = exif.getAttributeInt(
								ExifInterface.TAG_ORIENTATION,
								ExifInterface.ORIENTATION_NORMAL);
						int exifDegree = exifOrientationToDegrees(exifOrientation);

						photo = rotate(photo, exifDegree);
					} catch (FileNotFoundException e) {
						Log.e(TAG, "PICK_FROM_CAMERA : " + e.toString());
						return;
					} catch (IOException e) {
						Log.e(TAG, "PICK_FROM_CAMERA : " + e.toString());
						return;
					}
				} else {
					Bundle extras = data.getExtras();
					photo = extras.getParcelable("data");
				}
				Log.e(TAG, "PICK_FROM_CAMERA size : " + photo.getHeight()
						* photo.getWidth());
			}

			if (photo != null) {
				try {
					FileOutputStream fos = new FileOutputStream(outFilePath);
					photo.compress(CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					Log.e(TAG, "" + requestCode + " : " + e.toString());
					return;
				}
			} else {
				Log.e(TAG, "Bitmap is null");
				return;
			}

			try {
				// Launch picker to choose photo for selected contact
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(getTempUri(), "image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("outputX", 700);
				intent.putExtra("outputY", 500);
				intent.putExtra("aspectX", 700);
				intent.putExtra("aspectY", 500);
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

	public int exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
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

			}
		}
		return bitmap;
	}

	public void Save(Bitmap mBitmap, OutputStream outstream) {
		try {

			DirPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			DirPath = DirPath + "/" + "KidsMind" + "/";

			File cameraDir = new File(DirPath);
			if (!cameraDir.exists()) {

				cameraDir.mkdirs();
			}
			SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();

			bpath = String.valueOf(System.currentTimeMillis()) + ".jpg";
			editor.putString("bpath", bpath);
			editor.commit();
			// insertRec(getDate(),saveName);
			f = new File(cameraDir, bpath);

			outstream = new FileOutputStream(f);

			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);

		} catch (Exception e) {
			// Log.v(d.getTaga(),"erroer"+e);
		}
	}

	@SuppressWarnings("deprecation")
	public BitmapDrawable getBitmapResizePrc(Bitmap Src, int newHeight,
			int newWidth) {
		BitmapDrawable result = null;

		int width = Src.getWidth();
		int height = Src.getHeight();

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

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
		result = new BitmapDrawable(resizedBitmap);

		return result;
	}

	public void doSaveFile(Bitmap photo, File file) {
		OutputStream out = null;
		try {
			if (file.exists() == false) {
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			photo.compress(CompressFormat.PNG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File uploadFile;

	private void asyncImageUploadJson(Bitmap bitmap) {
		doSaveFile(bitmap, uploadFile);

		if (uploadFile.exists() == false || uploadFile.length() > 1048576 * 2L) {

			return;
		}

		// uploadFileExecute(uploadFile);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		int userId = pref.getInt("user_id", 0);
		String questionid = pref.getString("qposition", "");
		Map<String, Object> params = new HashMap<String, Object>();
		String messege = msg.getText().toString();
		params.put("image", uploadFile);
		params.put("message", messege);

		aquery.ajax(Const.IMAGE_CHANGE_URL + "/" + userId + "/" + questionid,
				params, JSONObject.class, this, "jsonImageUploadCallback");
	}

	private void asyncImageUploadJson(String msg) {

		// uploadFileExecute(uploadFile);
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		int userId = pref.getInt("user_id", 0);
		String questionid = pref.getString("qposition", "");
		Map<String, Object> params = new HashMap<String, Object>();
		editor.putString("whereintent", where);
		editor.commit();
		String ch = pref.getString("whereintent", "");
		if ("direct".equals(ch)) {
			String bpath = pref.getString("bpath", "");
			uploadFile = new File(Environment.getExternalStorageDirectory(),
					"/KidsMind/" + bpath);

		} else {
			String savename = pref.getString("savename", "");
			uploadFile = new File(Environment.getExternalStorageDirectory(),
					"/KidsMind/" + savename);

		}
		Log.v(TAG, "파일 용량 =>" + String.valueOf(uploadFile.length()));
		params.put("image", uploadFile);
		params.put("message", msg);
		
		aquery.ajax(Const.IMAGE_CHANGE_URL + "/" + userId + "/" + questionid+"/" + detail_id,
				params, JSONObject.class, this, "jsonImageUploadCallback");
	}

	public void jsonImageUploadCallback(String url, JSONObject json,
			AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();

				boolean isSuccess = json.getString("result").equals(
						Const.SUCCESS);

				if (isSuccess) {
					
					int advice_id = json.getInt("advice_id");
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putInt("advice_id", advice_id);
					editor.commit();
					String url3 = Const.ADVICE_COMMENT_LIST + advice_id + "";

					new JobTask().execute(url3);
					Log.v(Debugc.getTagd(), "advice_id" + advice_id);
					// 사
					String ch = pref.getString("whereintent", "");
					if ("direct".equals(ch)) {
						String bpath = pref.getString("bpath", "");
						updateRec(bpath, advice_id);

					} else {
						String savename = pref.getString("savename", "");
						updateRec(savename, advice_id);

					}
					// openInfoMessageDialogBox("상담 요청 하였습니다.");

				} else {
					String error = json.getString("error");
					// openInfoMessageDialogBox(error);
				}
			} catch (JSONException e) {
				// openErrorDialog();
				e.printStackTrace();
			}
		} else {
			// openErrorDialog();
		}
	}

	public void insertRec(String image_id, String detail_id,
			String detail_check, int advice_id, String question_id,
			String user_id) {
		// if(selectDb(detail_id)){
		openDB();
		Log.v(TAG, "오픈 디비 ");
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd",
				Locale.KOREA);
		Date currentTime = new Date();

		String date = mSimpleDateFormat.format(currentTime);
		Log.v(TAG,
				"image_id,detail_id,detail_check,advice_id,question_id,user_id,date"
						+ image_id + detail_id + detail_check + question_id
						+ user_id);

		Log.v(TAG, "DATe" + date);

		ContentValues values = new ContentValues();
		try {

			values.put("fName", image_id);

			values.put("detail_id", detail_id);

			values.put("detail_check", detail_check);
			values.put("advice_id", advice_id);
			values.put("question_id", question_id);
			values.put("user_id", user_id);
			values.put("date", date);
			long id = db.insert("km_check", null, values);

			Log.v(TAG, id > 0 ? "success" : "fail");
		} catch (SQLException e) {
			Log.v(TAG, "insert error " + e);
		}
		closeDB();

	}

	Bitmap bitmap = null;

	void readimage(String path) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path2 = Environment.getExternalStorageDirectory()
					+ "/KidsMind/" + path;
			Log.v(TAG, "이미지를 읽어오기위한 경로" + path2);

			// DbUse dbuse=new DbUse(MindDrawingResultActivity.this);
			// insertRec2(path2, "0");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(path2, options);
			options = getBitmapSize(options);
			bitmap = BitmapFactory.decodeFile(path2, options);
			Log.v(TAG, "이미지를 읽어오기위한 경로2" + path2);

			if (bitmap != null) {
				Log.v(TAG, "이미지 로딩");
				if (bitmap.getWidth() > bitmap.getHeight()) {
					set.setVisibility(View.GONE);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.weight = 0;
					set.setLayoutParams(params);

				} else {
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							0, LayoutParams.WRAP_CONTENT);
					params.weight = 1;
					set.setLayoutParams(params);
					set.setVisibility(View.VISIBLE);
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					String Q = pref.getString("qposition", "");
					if ("Q4".equals(Q)) {
						title.setText("물고기 그리기");
						icon.setImageResource(R.drawable.icon_fish);

					} else if ("Q3".equals(Q)) {
						title.setText("사람 그리기");
						icon.setImageResource(R.drawable.icon_human);

					} else if ("Q2".equals(Q)) {
						title.setText("나무 그리기");
						icon.setImageResource(R.drawable.icon_tree);

					} else if ("Q1".equals(Q)) {
						title.setText("집 그리기");
						icon.setImageResource(R.drawable.icon_home);

					}

					SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
							"yyyy.MM.dd", Locale.KOREA);
					Date currentTime = new Date();
					String mTime = mSimpleDateFormat.format(currentTime);

					date.setText(mTime);
				}
				imageView1.setImageBitmap(bitmap);
			} else {
				Toast.makeText(getApplicationContext(), "에러",
						Toast.LENGTH_SHORT).show();
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

	public void asynccommentJson(String msg,int adviceId) {

		// openWaitDialog();

		String url = Const.USER_COMMENT;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("comment", msg);
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		int userId = pref.getInt("user_id", 0);
		//int adviceId = pref.getInt("advice_id", 0);
		Log.v(TAG, "userid" + userId + "" + adviceId+"");
		String userid = userId + "";
	
		url = url + userid + "/" + adviceId+"";
		Log.v(TAG, "댓글url" + url);
		aquery.ajax(url, map, JSONObject.class, this, "jsonJoinCallback");

		// sendView.setText(url);
	}

	public void jsonJoinCallback(String url, JSONObject json, AjaxStatus status) {

		if (json != null) {
			try {
				aquery.ajaxCancel();
				// closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(
						Const.SUCCESS);

				if (isSuccess) {
					Log.v(TAG, "댓글달림");
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					int adviceId = pref.getInt("advice_id", 0);
					String url3 = Const.ADVICE_COMMENT_LIST + adviceId + "";

					new JobTask().execute(url3);

					// resultView.setText(json.toString());
				} else {
					Log.v(TAG, "에러");
					String error = json.getString("error");
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					// openInfoMessageDialogBox("error");
				}
			} catch (JSONException e) {
				// openErrorDialog();
				Log.v(TAG, "e" + e);
				e.printStackTrace();
			}
		} else {
			Log.v(TAG, "json null");
			// openErrorDialog();
		}
	}

	public int selectAll(String savename) {
		openDB();
		Log.v(TAG, "탭 디비 시작");
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		Cursor c = null;
		int adviceid = 0;
		String wStr = "fName=?";
		String[] wherStr = { savename };

		String[] colNames = { "advice_id","detail_id" };
		
			
			try {
			c = db.query("km_check", colNames, wStr, wherStr, null, null,
					"_id desc");
			while (c.moveToNext()) {
				// c.getString(c.getColumnIndex("question_id"));
				adviceid = c.getInt(c.getColumnIndex("advice_id"));
				detail_id=c.getString(c.getColumnIndex("detail_id"));
			}

		} catch (SQLException e) {
			Log.v(TAG, "selec error" + e);
		} catch (IllegalArgumentException e){
			Log.v(TAG,"illagal"+e);
		}finally {
		
			if (c != null) {
				c.close();
			}
		}
		return adviceid;

	}

	class JobTask extends AsyncTask<String, Void, AdviceItem2> {

		@Override
		protected AdviceItem2 doInBackground(String... params) {
			AdviceItem2 advice = null;
			String url = params[0];

			Log.v(Debugc.getTagd(), "urlurl" + url);
			HttpClient client = null;
			HttpGet request = null; // go catch
			HttpResponse response = null;
			BufferedReader br = null;
			int code = 0;
			try {
				client = NetManager.getHttpClient();
				request = NetManager.getGet(url);
				response = client.execute(request);
				code = response.getStatusLine().getStatusCode();
				switch (code) {
				case 200:
					br = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent()));
					StringBuilder sb = new StringBuilder();
					String sTemp = "";
					while ((sTemp = br.readLine()) != null) {
						// Log.v(TAG, "aa");
						sb.append(sTemp).append("\n");
					}

					advice = MyJsonParser3.parse(sb.toString());
					// adapter.setData(data);

					// data = sb.toString();

					Log.v(Debugc.getTaga(),
							"=======================================================================================");
					Log.v(Debugc.getTaga(), "comment : " + advice.toString());
					Log.v(Debugc.getTaga(),
							"===============================================================================================");

					break;
				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {

			}

			return advice;
		}

		@Override
		protected void onPostExecute(AdviceItem2 result) {
			// if(CDialog dialog != null)
			// {
			// pDialog.cancel();
			// }

			if (result != null) {

				data = result.getData();
				int cnt = data.size();
				if (cnt == 0) {
					//bg.setVisibility(View.VISIBLE);

					bottom.setVisibility(View.VISIBLE);
				} else {
					////
					CommentList item=data.get(cnt-1);
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					int user_id = pref.getInt("user_id", 0);

					/*
					 * if(item.getUser_id()!=user_id) {
					 * holder.message.setBackgroundDrawable(null); lp.gravity =
					 * Gravity.LEFT;
					 * 
					 * //holder.message.setTextColor(R.color.textFieldColor); } else {
					 */
					// Check whether message is mine to show green background and align to
					// right
					if (item.getUser_id() != user_id) {
						morebutton.setVisibility(View.VISIBLE);
						
					}else{
						morebutton.setVisibility(View.GONE);
						bottom.setVisibility(View.GONE);
					}
				
					
					
					//bg.setVisibility(View.GONE);

				}

				adapter.setData(data);
				adapter.notifyDataSetChanged();
				if(dialog2!=null)
				dialog2.dismiss();
				// int
				// height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*data.size();
				// LayoutParams lp=(LayoutParams) list.getLayoutParams();
				// lp.height=height;
				// list.setLayoutParams(lp);

			}
			super.onPostExecute(result);
		}

	}

	void updateRec(String wStr, int newLName) {
		openDB();

		// String sql="update tmember set age =32 where fname like '%k%';";
		ContentValues values = new ContentValues();
		values.put("advice_id", newLName);
		values.put("detail_check", "1");
		String whereClause = "fName like ?";
		String[] whereArgs = { "%" + wStr + "%" };
		try {

			int cnt = db.update("km_check", values, whereClause, whereArgs);

			Log.v(Debugc.getTagad(), "advice_id업데이트중" + cnt + "");
		} catch (SQLException e) {
			Log.v(Debugc.getTagad(), "update error" + e);
		}

		closeDB();
	}

	void openDB() {
		// db = openOrCreateDatabase("sample.db", wi, null);
		if(helper!=null){
			db = helper.getWritableDatabase();
				
		}else{
			helper = new MyHelper(KidsMindAdviceActivity.this, "kidsmind.db", null,
					1);
			db = helper.getWritableDatabase();
			
		}
	}

	// dbClose();
	void closeDB() {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK ) {
	// switch(trans.getVisibility()){
	// case View.GONE:
	//
	// finish();
	// break;
	// case View.VISIBLE:
	// bg.setBackgroundColor(0x00000000);
	// trans.setVisibility(View.GONE);
	// break;
	// }
	// }
	// return true;
	// }

}