package com.mb.kids_mind;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.fragment.SingleResultSketchMenu;

public class KidsMindTotalResultActivity extends FragmentActivity {
	FragmentManager fm=null;
	public ViewPager pager; 
	public ScreenSlidePagerAdapter mPagerAdapter;
	public SparseArray<WeakReference<SingleResultSketchMenu>> sparseArray = new SparseArray<WeakReference<SingleResultSketchMenu>>();
	
	private ImageView img,drawimage;
	private int currentPage;
	private MyHelper helper;
	private KidsMindDBHelper khelper;
	SQLiteDatabase db,db2;
	private String detail_id;
	private ArrayList<DetailListItem>list=new ArrayList<DetailListItem>() ;
	DetailListItem item;
	TextView contents;
	private String savename;
	boolean mNeedsRedraw = false;
	ImageView icon;
	LinearLayout set;
	TextView title;
	TextView date;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.result_page);
	    View view = findViewById(R.id.menu_pager);
	    img=(ImageView)findViewById(R.id.doctor);
		drawimage=(ImageView)findViewById(R.id.imageView1);
		set=(LinearLayout)findViewById(R.id.set);
		title=(TextView)findViewById(R.id.title);
		date=(TextView)findViewById(R.id.date);
		icon=(ImageView)findViewById(R.id.icon);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent in =new Intent(KidsMindTotalResultActivity.this,KidsMindLastResultActivity.class);
//				in.putExtra("savename",savename);
//				in.putExtra("where","1");
//				startActivity(in);
				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
				SharedPreferences.Editor editor =pref.edit();
				editor.putString("whe", "1");
				editor.commit();
				// TODO Auto-generated method stub
			finish();	
			}
		});
		findViewById(R.id.textView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
				SharedPreferences.Editor editor =pref.edit();
				editor.putString("whe", "1");
				editor.commit();
				// TODO Auto-generated method stub
			finish();
			}
		});
	findViewById(R.id.home).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(KidsMindTotalResultActivity.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("check", "1");
			startActivity(intent);
				
		}
	});
		contents=(TextView)findViewById(R.id.contents);
		Intent intent=getIntent();
		//if("1".equals(intent.getStringExtra("where"))){
		// Bitmap bit=intent.getParcelableExtra("img");
		// img.setImageBitmap(bit);
	   khelper=new KidsMindDBHelper(KidsMindTotalResultActivity.this);
		helper = new MyHelper(this, "kidsmind.db", null, 1);
		String image_id=intent.getStringExtra("savename");
		savename=image_id;
		readimage(image_id);
		selectAll(image_id);
		Log.v(TAG,"detail_id"+detail_id);
		String[] detail=detail_id.split(",");
		for(String cha:detail){
			Log.v(TAG,"자른아이디"+cha);
			selectDb(cha);

		}
		item=new DetailListItem();
		item.setDetail_image("1");
		//item.setDetail_tilte("최종 소견서 보러가기");
		item.setDetail_content("최종 소견서 보기");
		list.add(item);
		if(list.size()!=0)
		contents.setText(list.get(0).getDetail_content());
		Log.v(TAG,"here");
		new AsyncTask<View, Void, View>() {
			View view;

			@Override
			protected View doInBackground(View... params) {
				view = params[0];
				while(!view.isShown()){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
				return view;
			}

			@Override
			protected void onPostExecute(View result) {
				pager = (ViewPager) result.findViewById(R.id.menu_pager);

				pager.setOffscreenPageLimit(2);
				mPagerAdapter=new ScreenSlidePagerAdapter(getSupportFragmentManager());
				pager.setAdapter(mPagerAdapter);
				pager.setPageMargin(
						getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
				

				pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

					@Override
					public void onPageSelected(int position) {
						
						View prevView = sparseArray.get(currentPage).get().getView();
						
						View view = sparseArray.get(position).get().getView();
						
						view.animate().scaleX(33f/30f).setDuration(500);
						view.animate().scaleY(33f/30f).setDuration(500);
						
						prevView.animate().scaleX(1f).setDuration(500);
						prevView.animate().scaleY(1f).setDuration(500);
						
						currentPage = position; 
						DetailListItem item=list.get(position);
						contents.setText(item.getDetail_content());
						switch (position%4){
						case 0:
							img.setImageResource(R.drawable.re_dotor1);
							break;
						case 1:
							img.setImageResource(R.drawable.re_dotor2);
							break;
						case 2:
							img.setImageResource(R.drawable.re_dotor3);
							break;
						case 3:
							img.setImageResource(R.drawable.re_dotor4);
							break;
						
						}
					}
					
				});
				
				new AsyncTask<Void, Void, Void>(){
					View view;
					@Override

					protected Void doInBackground(Void... params) {
						try{
						view = pager.getChildAt(0);
						
						while(!view.isShown()){
							SystemClock.sleep(50);

						}
						}catch(NullPointerException e){
							
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						try{
						view.animate().scaleX(33f/30f).setDuration(500);
						view.animate().scaleY(33f/30f).setDuration(500);
						currentPage = 0;
						}catch(NullPointerException e){
							
						}
					}
				}.execute();
				
				super.onPostExecute(result);
			}
		}.execute(view);
		

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
			SharedPreferences.Editor editor =pref.edit();
			editor.putString("whe", "1");
			editor.commit();
			// TODO Auto-generated method stub
		finish();	
	    }
	    return super.onKeyDown(keyCode, event);
	}
	public void selectDb(String cha){

		openDB();
		//Log.v(TAG,"dbopen");
		//String sql ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c=null;
		String wStr="detail_id=?";
		String[] wherStr={cha};
		String[] colNames={"detail_image","detail_title","detail_content"};
		
		try{
			c=db2.query("km_question_detail", colNames, wStr, wherStr, null, null, null);
			//c=db.rawQuery(sql, null);
			while(c.moveToNext()){
				item=new DetailListItem();
				item.setDetail_image(c.getString(c.getColumnIndex("detail_image")));
				item.setDetail_tilte(c.getString(c.getColumnIndex("detail_title")));
				item.setDetail_content(c.getString(c.getColumnIndex("detail_content")));
				list.add(item);
				Log.v(TAG,"listsize"+list.size()+"");
				//titem=new TagList();
				/*titem.setTag_id(c.getString(c.getColumnIndex("tag_id")));
					titem.setTag_name(c.getString(c.getColumnIndex("tag_name")));
					tlist.add(titem);
				 */
				
				//Log.v(Debugc.getTaga(), c.getString(0)+ c.getString(1)+ c.getString(2)+c.getString(3)+c.getString(4));
				//	c.getString(0);
			}
			
		}catch(SQLException e){
			Log.v(TAG,"selec error"+e);
			
		}finally{
			Log.v(TAG,"dbopen3");
			closeDB();
			if(c!=null){
				c.close();
			}
		}
	}

	public void selectAll(String image){
		openDB();
	
		//String sql ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c=null;
		String wStr="fName=?";
		String[] wherStr={image};
		String[] colNames={"fName","detail_id","detail_check"};
		try{
			c=db.query("km_check", colNames, wStr, wherStr, null, null, null);
			while(c.moveToNext()){
				detail_id=c.getString(c.getColumnIndex("detail_id"));
				Log.v(TAG,"detail_id DB"+detail_id);
			}

		}catch(SQLException e){
			Log.v(TAG,"selec error"+e);
		}finally{
			if(c!=null){
				c.close();
			}
		}
		

	}
	public void openDB() {
		// db = openOrCreateDatabase("sample.db", wi, null);
		db2 = khelper.getWritableDatabase();
		db=helper.getWritableDatabase();
	}

	// dbClose();
	public void closeDB() {
		if (db2 != null) {
			if (db2.isOpen()) {
				db2.close();
			}
		}
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	Bitmap bitmap;
	void readimage(String path){
		if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String path2 = Environment.getExternalStorageDirectory()+"/KidsMind/"+path;
			Log.v(TAG,"이미지를 읽어오기위한 경로"+path2);


			//DbUse dbuse=new DbUse(MindDrawingResultActivity.this);
			//insertRec2(path2, "0");
			BitmapFactory.Options options =new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			bitmap=BitmapFactory.decodeFile(path2,options);
			options =getBitmapSize(options);
			bitmap=BitmapFactory.decodeFile(path2,options);
			Log.v(TAG,"이미지를 읽어오기위한 경로2"+path2);

			if(bitmap!=null){
				Log.v(TAG,"이미지 로딩");
				if(bitmap.getWidth()>bitmap.getHeight()){
					set.setVisibility(View.GONE);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.weight=0;
					set.setLayoutParams(params);
					
				}else{
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							0, LayoutParams.WRAP_CONTENT);
					params.weight=1;
					set.setLayoutParams(params);
					set.setVisibility(View.VISIBLE);
					icon.setImageResource(R.drawable.icon_fish);
					SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
					String Q=pref.getString("qposition", "");
					if("Q1".equals(Q)){
						title.setText("물고기 그리기");
						
					}else if("Q2".equals(Q)){
						title.setText("사람 그리기");
						
					}else if("Q3".equals(Q)){
						title.setText("나무 그리기");
						
					}else if("Q4".equals(Q)){
						title.setText("집 그리기");
						
					}

SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd", Locale.KOREA );
Date currentTime = new Date ( );
String mTime = mSimpleDateFormat.format ( currentTime );

					date.setText(mTime);
				}
				drawimage.setImageBitmap(bitmap);
			}else{
				Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
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
	 
	private static final String TAG="MainActivity";
	
	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
        }
	
        @Override
        public int getCount() {
        	//Log.v(TAG,"adapter listsize"+list.size()+"");
            return list.size();
        }

		@Override
		public Fragment getItem(int position) {
			SingleResultSketchMenu frag = new SingleResultSketchMenu();
			frag.setData(list);
			frag.setPosition(position);
			sparseArray.put(position, new WeakReference<SingleResultSketchMenu>(frag));
			return frag;
		}

		
	}
}
