package com.mb.kids_mind;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.mb.kids_mind.Adapter.AddBabyAdater;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.AlbumItem;
import com.mb.kids_mind.Item.BabyInformationItem;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.fragment.SketchMenu;
import com.mb.kids_mind.listener.BackListener;
import com.mb.kids_mind.listener.MainSideMenuListener;

public class MainActivity extends FragmentActivity {
	public FragmentManager fm = null;
	private AQuery aquery;
	private static final String TAG = "MainActivity";
	ArrayList<BabyInformationItem> babyitem;
	ArrayList<AlbumItem> albumitem;
	View.OnClickListener bHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mypage:
				// my page

				break;
			case R.id.add:
				Intent in2 = new Intent(MainActivity.this,
						KidsMindAddActivity.class);
				startActivityForResult(in2, 0);
				// adapter notifydatachaged 해주기
				break;
			case R.id.notic:// 이벤트 공지사항
				break;
			case R.id.setting://
				break;
			case R.id.mapbtn:
				Intent intent = new Intent(MainActivity.this,
						KidsmindMapActivity.class);
				startActivity(intent);
				break;
			case R.id.myalbum:
				Intent in = new Intent(MainActivity.this,
						KidsMindMypageActivity.class);
				in.putExtra("user_id", mUser_id);
				in.putExtra("image_path", mImage_path);
				in.putExtra("name", mName);
				in.putExtra("date", mDate);
				in.putExtra("sex", mSex);
			
				startActivity(in);
				break;
			// case R.id.login:
			// popuplogin(MainActivity.this);
			//
			// break;
			}
		}
	};
	SQLiteDatabase db, db2;
	MyHelper helper;
	TextView name;

	ListView addlist;
	public ImageView mypage, login;
	AddBabyAdater adapter;
	ImageView myalbum;
	public ImageView toggle;
	public LinearLayout info;
	public String test;
//	in.putExtra("user_id", info.getUser_id());
//	in.putExtra("image_path", info.getImage_path());
//	in.putExtra("name", info.getName());
//	in.putExtra("date", info.getBirth());
//	in.putExtra("sex", info.getSex());
	public String mUser_id,mImage_path,mName,mDate,mSex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		findViewById(R.id.mapbtn).setOnClickListener(bHandler);
		info = (LinearLayout) findViewById(R.id.info);
		info.setVisibility(View.GONE);
		myalbum=(ImageView)findViewById(R.id.myalbum);
		myalbum.setOnClickListener(bHandler);
		fm = getSupportFragmentManager();

		fm.beginTransaction().add(R.id.fragmentHolder, new SketchMenu())
				.commit();
		name = (TextView) findViewById(R.id.name);
		helper = new MyHelper(this, "kidsmind.db", null, 1);

		addlist = (ListView) findViewById(R.id.listView1);
		addlist.setDivider(null);

		selectAll();
		if(babyitem.size()!=0){
			String check=pref.getString("item", "");
			
			if("".equals(check)){
		BabyInformationItem info = babyitem.get(0);
		mUser_id=info.getUser_id();
		mName=info.getName();
		mDate=info.getBirth();
		mSex=info.getSex();
		mImage_path=info.getImage_path();
		if("none".equals(mImage_path)){
			if("boy".equals(mSex)){
				myalbum.setImageResource(R.drawable.btn_boy_push);	
			}else if("girl".equals(mSex)){
				myalbum.setImageResource(R.drawable.btn_girl_push);
				
			}
			
		}else{
		readimage(mImage_path, myalbum);
		//holder.image.setImageBitmap(bm);
		}
			}else{
		
				
				mUser_id=pref.getString("mUser_id", "");
				mName=pref.getString("mName", "");
				mDate=pref.getString("mDate", "");
				mSex=pref.getString("mSex", "");
				mImage_path=pref.getString("mImage_path", "");
				
			
				if("none".equals(mImage_path)){
					if("boy".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_boy_push);	
					}else if("girl".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_girl_push);
						
					}
					
				}else{
				readimage(mImage_path, myalbum);
				//holder.image.setImageBitmap(bm);
				}
				
			}
		}
		int height = (getResources().getDimensionPixelSize(
				R.dimen.list_item_size) + 1)
				* babyitem.size();
		LayoutParams lp = (LayoutParams) addlist.getLayoutParams();
		lp.height = height;
		addlist.setLayoutParams(lp);
		// editor.putString("login_check", "checked");

		adapter = new AddBabyAdater(MainActivity.this, R.layout.babyadapter,
				babyitem);

		addlist.setAdapter(adapter);
		addlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				ArrayList<BabyInformationItem> list = new ArrayList<BabyInformationItem>();
				list = adapter.getList();
				BabyInformationItem info = list.get(position);
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("item", "on");
				editor.putString("userid", info.getUser_id());
				editor.commit();
			
				mUser_id=info.getUser_id();
				mName=info.getName();
				mDate=info.getBirth();
				mSex=info.getSex();
				mImage_path=info.getImage_path();
				Log.v(TAG,"mImage_path"+mImage_path);
				editor.putString("mUser_id", mUser_id);
				editor.putString("mName", mName);
				editor.putString("mDate", mDate);
				editor.putString("mSex", mSex);
				editor.putString("mImage_path", mImage_path);
				editor.commit();
				if("none".equals(mImage_path)){
					if("boy".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_boy_push);	
					}else if("girl".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_girl_push);
						
					}
					
				}else{
				readimage(mImage_path, myalbum);
				//holder.image.setImageBitmap(bm);
				}
				Log.v(TAG, "userid" + info.getUser_id());
				// 시작 앨범으로 시작~~ 그해당 userid를 통해 km_check 테이블에서 해당 image_id뽑아내고
				// 그다음 image_id를 통해 detail_id를 이용해서 선택항목 추출

			}
		});
		aquery = new AQuery(this);
		// 리스트에 들어가는 부분에서 선택하면 user id를 쉐어드로 넣어준다.
		login = (ImageView) findViewById(R.id.login);
		login.setImageResource(R.drawable.btn_login);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v(TAG, "onclick");
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);

				String ch = pref.getString("login_check", "");
				if (!ch.equals("")) {
					login.setImageResource(R.drawable.btn_login);
					String user_name = pref.getString("user_name", "");
					String user_pwd = pref.getString("user_pwd", "");
					Log.v(TAG, "logout");
					name.setText("로그인을 해주세요");
					asyncLogoutJson(user_name, user_pwd);
				} else {
					Log.v(TAG, "login");

					Intent intent = new Intent(MainActivity.this,
							KidsMindLoginSelectActivity.class);
					startActivityForResult(intent, 1);
					// startActivity(intent);
				}
			}
		});
		toggle = (ImageView) findViewById(R.id.menuToggler);

		toggle.setOnClickListener(new MainSideMenuListener());

		toggle.setOnClickListener(new MainSideMenuListener());
		findViewById(R.id.add).setOnClickListener(bHandler);
		mypage = (ImageView) findViewById(R.id.mypage);
		mypage.setOnClickListener(bHandler);
		findViewById(R.id.notic).setOnClickListener(bHandler);
		findViewById(R.id.setting).setOnClickListener(bHandler);

		// findViewById(R.id.linear).setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// switch(event.getAction()){
		// case MotionEvent.ACTION_DOWN:
		//
		// //doAction(v);
		// break;
		// case MotionEvent.ACTION_UP:
		//
		// break;
		// case MotionEvent.ACTION_MOVE:
		//
		// break;
		//
		// }
		//
		//
		// return false;
		// }
		// });
		editor.putInt("abs", 0);
		editor.putString("list", "");
		editor.putInt("temp", 0);
		editor.commit();
		String ch = pref.getString("just", "");
		if ("".equals(ch)) {

			Intent in = new Intent(MainActivity.this, KidsMindAddActivity.class);
			startActivityForResult(in, 0);
		}

	}
	Bitmap bitmap;

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
			profile=new RoundedAvatarDrawable(bitmap);
			img.setImageDrawable(profile);
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
	RoundedAvatarDrawable profile;
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

	public static BackListener listener2;

	@Override
	protected void onResume() {
		selectAll();
		SharedPreferences pref = getSharedPreferences("pref",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		if(babyitem.size()!=0){
			String check=pref.getString("item", "");
			Log.v(TAG,"체크아이템onresum"+check);
			if("".equals(check)){
		BabyInformationItem info = babyitem.get(0);
		mUser_id=info.getUser_id();
		mName=info.getName();
		mDate=info.getBirth();
		mSex=info.getSex();
		mImage_path=info.getImage_path();
		if("none".equals(mImage_path)){
			if("boy".equals(mSex)){
				myalbum.setImageResource(R.drawable.btn_boy_push);	
			}else if("girl".equals(mSex)){
				myalbum.setImageResource(R.drawable.btn_girl_push);
				
			}
			
		}else{
		readimage(mImage_path, myalbum);
		//holder.image.setImageBitmap(bm);
		}
			}else{
		
				
				mUser_id=pref.getString("mUser_id", "");
				mName=pref.getString("mName", "");
				mDate=pref.getString("mDate", "");
				mSex=pref.getString("mSex", "");
				mImage_path=pref.getString("mImage_path", "");
				Log.v(TAG,"mImage_pathonresum"+mImage_path);
				
			
				if("none".equals(mImage_path)){
					if("boy".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_boy_push);	
					}else if("girl".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_girl_push);
						
					}
					
				}else{
				readimage(mImage_path, myalbum);
				//holder.image.setImageBitmap(bm);
				}
				
			}
		}
		Log.v(TAG, "size" + babyitem.size() + "");
		int height = (getResources().getDimensionPixelSize(
				R.dimen.list_item_size) + 1)
				* babyitem.size();
		LayoutParams lp = (LayoutParams) addlist.getLayoutParams();
		lp.height = height;
		addlist.setLayoutParams(lp);
		adapter.setList(babyitem);
		adapter.notifyDataSetChanged();
		Log.v(TAG, "리절트로 왔어요");
		super.onResume();
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
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();

					editor.putString("login_check", "");
					editor.commit();
					Log.v(TAG, "logout성공");
					selectAll();
					BabyInformationItem info = babyitem.get(0);
					mUser_id=info.getUser_id();
					mName=info.getName();
					mDate=info.getBirth();
					mSex=info.getSex();
					mImage_path=info.getImage_path();
					if("none".equals(mImage_path)){
						if("boy".equals(mSex)){
							myalbum.setImageResource(R.drawable.btn_boy_push);	
						}else if("girl".equals(mSex)){
							myalbum.setImageResource(R.drawable.btn_girl_push);
							
						}
						
					}else{
					readimage(mImage_path, myalbum);
					//holder.image.setImageBitmap(bm);
					}
					
					Log.v(TAG, "size" + babyitem.size() + "");
					int height = (getResources().getDimensionPixelSize(
							R.dimen.list_item_size) + 1)
							* babyitem.size();
					LayoutParams lp = (LayoutParams) addlist.getLayoutParams();
					lp.height = height;
					addlist.setLayoutParams(lp);
					adapter.setList(babyitem);
					adapter.notifyDataSetChanged();
					Log.v(TAG, "리절트로 왔어요");
					// resultView.setText(json.toString());
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// Log.
			if (requestCode == 0) {
				Log.v(TAG, "리절트 왔어요");
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				
				selectAll();
				editor.putString("item", "");
				BabyInformationItem info = babyitem.get(0);
				mUser_id=info.getUser_id();
				editor.putString("userid", info.getUser_id());
				editor.commit();
				mName=info.getName();
				mDate=info.getBirth();
				mSex=info.getSex();
				mImage_path=info.getImage_path();
				if("none".equals(mImage_path)){
					if("boy".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_boy_push);	
					}else if("girl".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_girl_push);
						
					}
					
				}else{
				readimage(mImage_path, myalbum);
				//holder.image.setImageBitmap(bm);
				}
				
				Log.v(TAG, "size" + babyitem.size() + "");
				int height = (getResources().getDimensionPixelSize(
						R.dimen.list_item_size) + 1)
						* babyitem.size();
				LayoutParams lp = (LayoutParams) addlist.getLayoutParams();
				lp.height = height;
				addlist.setLayoutParams(lp);
				adapter.setList(babyitem);
				adapter.notifyDataSetChanged();
				Log.v(TAG, "리절트로 왔어요");
				// 리스트뷰 어댑터에 이미지를 추가해 주고 setdata -> notifydata changed
			} else if (requestCode == 1) {
				// 로그인완료
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				selectAll();
				editor.putString("item", "");
				BabyInformationItem info = babyitem.get(0);
				editor.putString("userid", info.getUser_id());
				editor.commit();
				mUser_id=info.getUser_id();
				mName=info.getName();
				mDate=info.getBirth();
				mSex=info.getSex();
				mImage_path=info.getImage_path();
				if("none".equals(mImage_path)){
					if("boy".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_boy_push);	
					}else if("girl".equals(mSex)){
						myalbum.setImageResource(R.drawable.btn_girl_push);
						
					}
					
				}else{
				readimage(mImage_path, myalbum);
				//holder.image.setImageBitmap(bm);
				}
				
				login.setImageResource(R.drawable.btn_logout);
				Log.v(TAG, "size" + babyitem.size() + "");
				int height = (getResources().getDimensionPixelSize(
						R.dimen.list_item_size) + 1)
						* babyitem.size();
				LayoutParams lp = (LayoutParams) addlist.getLayoutParams();
				lp.height = height;

				addlist.setLayoutParams(lp);
				adapter.setList(babyitem);
				adapter.notifyDataSetChanged();

			}
		}
	}

	void selectAll() {
		openDB();
		Log.v(TAG, "탭 디비 시작");
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		String checking = pref.getString("login_check", "");
		String user_name;
		if ("".equals(checking)) {
			// not login
			user_name = "untitle";
			// name.setText(user_name);
			editor.putString("user_name", user_name);
			editor.commit();

		} else {
			// 로그인된경우
			user_name = pref.getString("user_name", "");
			name.setText(user_name);
		}
		Log.v(TAG, "username" + user_name);
		// String sql
		// ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c = null;
		babyitem = new ArrayList<BabyInformationItem>();

		String wStr = "user_name=?";
		String[] wherStr = { user_name };

		String[] colNames = { "user_id", "name", "birth", "sex", "image_path" };
		try {
			c = db2.query("km_baby", colNames, wStr, wherStr, null, null,
					"_id desc");

			//
			// String sql
			// ="select * from km_baby where "+user_name"+ order by _id DESC;";
			// Cursor c=null;
			Log.v(TAG, "select db");
			// try{
			// Log.v(TAG,"select db");
			//
			// c=db2.rawQuery(sql, null);
			Log.v(TAG, "숫자:" + c.getCount());
			editor.putInt("babycount", c.getCount());
			editor.commit();
			while (c.moveToNext()) {
				BabyInformationItem item = new BabyInformationItem();
				item.setUser_id(c.getString(c.getColumnIndex("user_id")));
				item.setName(c.getString(c.getColumnIndex("name")));
				item.setBirth(c.getString(c.getColumnIndex("birth")));
				item.setSex(c.getString(c.getColumnIndex("sex")));
				item.setImage_path(c.getString(c.getColumnIndex("image_path")));
				babyitem.add(item);
				// c.getString(c.getColumnIndex("question_id"));
				// Log.v(TAG, c.getString(0));
				// c.getString(0);
			}

		} catch (SQLException e) {
			Log.v(TAG, "selec error" + e);
		} finally {
			if (c != null) {
				c.close();
			}
		}

	}

	EditText id, pw;

	void popuplogin(Activity context) {
		// Create dialog
		Log.v(TAG, "1");
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialoglogin);
		id = (EditText) dialog.findViewById(R.id.editText1);
		pw = (EditText) dialog.findViewById(R.id.editText2);
		Log.v(TAG, "2");
		dialog.findViewById(R.id.button1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 로그인 시도
						Log.v(TAG, "3");
						login.setImageResource(R.drawable.btn_logout);
						dialog.dismiss();
					}
				});
		Log.v(TAG, "4");
	}

	void openDB() {
		Log.v(TAG, "opendb");

		// db = openOrCreateDatabase("sample.db", wi, null);
		db2 = helper.getWritableDatabase();

	}

	// dbClose();
	void closeDB() {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.v(TAG, "test" + test);
			if ("1".equals(test)) {
				Log.v(TAG, "리스너 들어와");
				Log.v(TAG, "리스너 뭐야" + MainActivity.listener2);

				if (MainActivity.listener2 != null) {
					Log.v(TAG, "리스너");
					MainActivity.listener2.onBackPressed();
				}
				// do your stuff
			} else {
				Log.v(TAG, "test2" + test);
				finish();
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
