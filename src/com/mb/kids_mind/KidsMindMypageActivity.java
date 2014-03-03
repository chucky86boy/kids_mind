package com.mb.kids_mind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.Const;

public class KidsMindMypageActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener {
private static final String TAG ="MainActivity";
TextView name,date;
ImageView sex,profile;
private LoginButton authButton;
private PlusClient mPlusClient;
private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
 private String user_name3,user_pwd3;
  private ProgressDialog mConnectionProgressDialog;
  private AQuery aquery;
  private ConnectionResult mConnectionResult;
private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
/** Called when the activity is first created. */
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
			in.putExtra("user_id", suser_id);
			
			startActivity(in);
			break;
		case R.id.cousel:
			SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
			editor.putString("muser_id", suser_id);
			editor.commit();
							String ch=pref.getString("login_check", "");	
			if(!ch.equals("")){
				//로그인상태
				Log.v(TAG,"1");
				
				Intent in2= new Intent(KidsMindMypageActivity.this,KidsMindAlbumactivity.class);
				in2.putExtra("user_id",suser_id);
				in2.putExtra("where", "advice");
				startActivity(in2);
				
			}else{
				
			Log.v(TAG, "login");
			
			
			String sel=pref.getString("select", "");
			if(sel.equals("")){
				
			Intent intent = new Intent(KidsMindMypageActivity.this,
					KidsMindLoginSelectActivity.class);
			editor.putString("lastresult","on");
			editor.commit();
			startActivityForResult(intent, 30);
			}
			else{
				String loginsel=pref.getString("wlogin", "");
				Log.v(TAG,"로그인 어디서 했니"+loginsel);
				//자동로그인
				if("facebook".equals(loginsel)){
					authButton.performClick();
					editor.putString("auth", "");
					editor.commit();
				}else if("google".equals(loginsel)){
					editor.putString("auth", "");
					editor.commit();

					mPlusClient.connect();
					if (!mPlusClient.isConnected()) {
						if (mConnectionResult == null) {
							Log.v(TAG,"null");
							loading(KidsMindMypageActivity.this);
						} else {
							try {
								mConnectionResult.startResolutionForResult(KidsMindMypageActivity.this, REQUEST_CODE_RESOLVE_ERR);
							} catch (SendIntentException e) {
								// Try connecting again.
								mConnectionResult = null;
								mPlusClient.connect();
							}
						}
					}
			}else{
				Intent intent3 = new Intent(KidsMindMypageActivity.this,
						KidsMindLoginActivity.class);
				startActivityForResult(intent3,13);
				 
			}
			}
			
			
			
		}
			
			
			
		
			
			break;
		case R.id.name:
			break;
		case R.id.date:
			break;
		}
	}
};
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

String suser_id,image_path,babyname,babydate,babysex;
MyHelper myhelper;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mypage);
	    aquery = new AQuery(this);
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
	    suser_id=in.getStringExtra("user_id");
	    
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
	    authButton = (LoginButton)findViewById(R.id.authButton);
		authButton.setVisibility(View.GONE);

		authButton.setOnErrorListener(new OnErrorListener() {      
			@Override
			public void onError(FacebookException error) {
				Log.v(TAG, "로그인 에러 : " + error);
			}
		});
		//        shareButton.setVisibility(View.VISIBLE);
		authButton.setReadPermissions(Arrays.asList("basic_info","email"));


		authButton.setSessionStatusCallback(new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) { 
				if (session.isOpened()) {



					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,Response response) {    
							if (user != null) { 

								// 로그인 성공 (user에 정보가 들어있음.)
								Log.v(TAG,"User ID "+ user.getId());
								Log.v(TAG,"Email "+ user.asMap().get("email"));
								Log.v(TAG,"name "+ user.asMap().get("name"));
								user_name3= user.asMap().get("email").toString();
								user_pwd3="Qce5cPoBrUhZu5LF5UFADzGUno";
								Log.v(TAG,"facebook23");
								//asyncLoginJson(user_name3,user_pwd3);
								SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
								SharedPreferences.Editor editor= pref.edit();
//								editor.putString("where", "indirect");
//								editor.putString("date", date);
//								editor.putString("savename", savename);
//								editor.putString("advice_type", advice_type);
					
								editor.putString("lfacebook", "onefef");
								editor.putString("auth","auth");
								editor.putString("wlogin", "facebook");
								editor.putString("user_name", user_name3);
								editor.putString("user_pwd", user_pwd3);
								editor.commit();

							}
						}
					});
				}else{

				}
			}
		});
		mPlusClient = new PlusClient.Builder(this, this, this)
		.setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
		.setScopes(Scopes.PLUS_LOGIN)  // recommended login scope for social features
		// .setScopes("profile")       // alternative basic login scope
		.build();
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
		super.onActivityResult(requestCode,resultCode,data);
		if(Session.getActiveSession()!=null)
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			if(requestCode==30||requestCode==13){
				
				
				Intent in2= new Intent(KidsMindMypageActivity.this,KidsMindAlbumactivity.class);
				in2.putExtra("user_id", suser_id);
				in2.putExtra("where", "advice");
				startActivity(in2);
				//startActivity(intent);
			}		
		}else {
			 if(requestCode ==REQUEST_CODE_RESOLVE_ERR){
				mConnectionResult = null;
				mPlusClient.connect();
			}
		}
		// 크롭 사용안함

		switch (requestCode) {
		case CROP_FROM_CAMERA: {
			//updateinsertRec(user_name,user,babyname, birthdate, sex, imagepath);
			SharedPreferences pref = getSharedPreferences("pref",
					MODE_PRIVATE);
			
			photo = BitmapFactory.decodeFile(outFilePath);
			// RoundedAvatarDrawable profile
		
			int wi=pref.getInt("bwidth", 0);
			int he=pref.getInt("bheight", 0);
			photo = getBitmapResizePrc(photo, 234, 234);
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
				
			
				updateRec(suser_id,path);
				Log.v(TAG,"앨범에서"+path);
				editor.putString("mImage_path", path);
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
				
					int wi=pref.getInt("bwidth", 0);
					int he=pref.getInt("bheight", 0);
					photo = getBitmapResizePrc(photo, 234, 234);
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

//			try {
//				// Launch picker to choose photo for selected contact
//				Intent intent = new Intent("com.android.camera.action.CROP");
//				intent.setDataAndType(getTempUri(), "image/*");
//				intent.putExtra("crop", "true");
//				intent.putExtra("outputX", 320);
//				intent.putExtra("outputY", 320);
//				intent.putExtra("aspectX", 320);
//				intent.putExtra("aspectY", 320);
//				intent.putExtra("scale", true);
//				intent.putExtra("return-data", true);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
//				intent.putExtra("outputFormat",
//						Bitmap.CompressFormat.JPEG.toString());
//				intent.putExtra("noFaceDetection", true);
//				intent.putExtra("circleCrop", false);
//
//				 startActivityForResult(intent, CROP_FROM_CAMERA);
//			} catch (ActivityNotFoundException e) {
//				Log.e("crop_from_camera", e.toString());
//			}
			break;
		}
		}
	}
	@Override
	protected void onResume() {
		
		super.onResume();
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
			SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE);
			int wi=pref.getInt("bwidth", 0);
			int he=pref.getInt("bheight", 0);
			bitmap = getBitmapResizePrc(bitmap, 234, 234);
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
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		//  mPlusClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		//	      if (mConnectionProgressDialog.isShowing()) {
		// The user clicked the sign-in button already. Start to resolve
		// connection errors. Wait until onConnected() to dismiss the
		// connection dialog.
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			} catch (SendIntentException e) {
				mPlusClient.connect();
			}
		}
		//	      }
		// Save the result and resolve the connection failure upon a user click.
		mConnectionResult = result;
	}


	String accountName;
	@Override
	public void onConnected(Bundle connectionHint) {
		accountName = mPlusClient.getAccountName();
		Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
		user_name3=accountName;
		user_pwd3="AIzaSyAlkq6NLiwn";

		SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor= pref.edit();
		editor.putString("wlogin", "google");
		editor.putString("user_pwd", user_pwd3);
		editor.putString("auth","");
		editor.commit();

		asyncLoginJson(user_name3, user_pwd3);
		//	        
		//	        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
		//	            @Override
		//	            protected String doInBackground(Void... params) {
		//	                String token = null;
		//
		//	                try {
		//	                    token = GoogleAuthUtil.getToken(
		//	                            KidsMindLoginSelectActivity.this,
		//	                            mPlusClient.getAccountName(),
		//	                            SCOPE);
		//	                } catch (IOException transientEx) {
		//	                    // Network or server error, try later
		//	                    Log.e(TAG, transientEx.toString());
		//	                } catch (UserRecoverableAuthException e) {
		//	                    // Recover (with e.getIntent())
		//	                    Log.e(TAG, e.toString());
		//	                    Intent recover = e.getIntent();
		//	                   // startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
		//	                } catch (GoogleAuthException authEx) {
		//	                    // The call is not ever expected to succeed
		//	                    // assuming you have already verified that 
		//	                    // Google Play services is installed.
		//	                    Log.e(TAG, authEx.toString());
		//	                }
		//
		//	                return token;
		//	            }
		//
		//	            @Override
		//	            protected void onPostExecute(String token) {
		//	                Log.i(TAG, "Access token retrieved:" + token);
		//	            }
		//
		//	        };
		//	        task.execute();

		//getTask(KidsMindLoginSelectActivity.this, accountName, SCOPE).execute();

	}

	public void asyncLoginJson(String user_name, String user_pwd) {
		//openWaitDialog();

		String url = Const.LOGIN_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		map.put("user_pwd", user_pwd);

		aquery.ajax(url, map, JSONObject.class, this, "jsonLoginCallback");

		//sendView.setText(url);
	}

	public void jsonLoginCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();
				dialog2.dismiss();
				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					
					Log.v(TAG,"재로그인 성공");
					int user_id = json.getInt("user_id");
					String user_name = json.getString("user_name");
					String authkey = json.getString("authkey");

					//asyncLoginJson(user_name3, user_pwd3);
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("login_check", "checked");
					editor.putInt("user_id",user_id );
					editor.putString("user_name", user_name);
					editor.putString("authkey", authkey);
					editor.commit();
					//String path=pref.getString("path", null);
//					String whe=pref.getString("last", "");
//					if("".equals(whe)){
//					Intent intent=new Intent(KidsMindLoginActivity.this,MainActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					}else{
//						KidsMindLoginActivity.this.setResult(RESULT_OK);
//							
//					}
					Intent in2= new Intent(KidsMindMypageActivity.this,KidsMindAlbumactivity.class);
					in2.putExtra("user_id", suser_id);
					in2.putExtra("where", "advice");
					startActivity(in2);
//					Log.v(TAG,"2");
//					Intent intent= new Intent(KidsMindMypageActivity.this,KidsMindAdviceActivity.class);
//					intent.putExtra("where", "indirect");
//					intent.putExtra("date", date);
//					intent.putExtra("savename", savename);
//					intent.putExtra("advice_type", advice_type);
//					startActivity(intent);
					


					//resultView.setText(json.toString());
				} else {
					Log.v(TAG,"재로그인 실패");
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					if(error.contains("이미")){
						asyncLogoutJson(user_name3, user_pwd3);
					}else{
						openInfoMessageDialogBox(error);
						
					}
					
				}
			} catch (JSONException e) {
				openErrorDialog();
				e.printStackTrace();
				Log.v(TAG,"json execption");
			}
		} else {
			Log.v(TAG,"json null");
			openErrorDialog();
		}
	}
	public void openInfoMessageDialogBox(String message) {
		Toast.makeText(KidsMindMypageActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	protected void openErrorDialog() { 
		//closeWaitDialog();
		aquery.ajaxCancel();

		openInfoMessageDialogBox("error.");
	}
	public void asyncLogoutJson(String user_name, String user_pwd) {
		// openWaitDialog();

		String url = Const.LOGOUT_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		map.put("user_pwd", user_pwd);

		aquery.ajax(url, map, JSONObject.class, this, "jsonLogoutCallback");

		// sendView.setText(url);
	}

	public void jsonLogoutCallback(String url, JSONObject json,
			AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				// closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(
						Const.SUCCESS);

				if (isSuccess) {
					
					Log.v(TAG, "logout성공");
					asyncLoginJson(user_name3, user_pwd3);
					
				} else {
					String error = json.getString("error");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data){
//		
//		if(resultCode==RESULT_OK){
//			//Log.
//			if(requestCode==30||requestCode==13){
//				SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
//				SharedPreferences.Editor editor=pref.edit();
//				
//				savename=pref.getString("fname", "");
//				date=pref.getString("date", "");
//				advice_type=pref.getString("advice_type", "");
//				
//				Intent intent= new Intent(KidsMindLastResultActivity.this,KidsMindAdviceActivity.class);
//				Log.v(TAG,"3");
//				intent.putExtra("where", "indirect");
//				intent.putExtra("date", date);
//				intent.putExtra("savename", savename);
//				intent.putExtra("advice_type", advice_type);
//				startActivity(intent);
//				//startActivity(intent);
//			}	else if(requestCode ==REQUEST_CODE_RESOLVE_ERR){
//				mConnectionResult = null;
//				mPlusClient.connect();
//			}
//		}
//	}
}
