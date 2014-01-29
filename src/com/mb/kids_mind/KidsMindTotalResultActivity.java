package com.mb.kids_mind;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Intent;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.result_page);
	    View view = findViewById(R.id.menu_pager);
	    img=(ImageView)findViewById(R.id.doctor);
		drawimage=(ImageView)findViewById(R.id.drawimage);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in =new Intent(KidsMindTotalResultActivity.this,KidsMindLastResultActivity.class);
				in.putExtra("savename",savename);
				in.putExtra("where","1");
				startActivity(in);
				// TODO Auto-generated method stub
			finish();	
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
					}
					
				});
				
				new AsyncTask<Void, Void, Void>(){
					View view;
					@Override

					protected Void doInBackground(Void... params) {
						view = pager.getChildAt(0);
						while(!view.isShown()){
							SystemClock.sleep(50);

						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						view.animate().scaleX(33f/30f).setDuration(500);
						view.animate().scaleY(33f/30f).setDuration(500);
						currentPage = 0;
					}
				}.execute();
				
				super.onPostExecute(result);
			}
		}.execute(view);
			
			//pager.setOnPageChangeListener(contai);

		
		
//		pager = (ViewPager) findViewById(R.id.menu_pager);
//		pager.setOffscreenPageLimit(5);
//		mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager());
//		pager.setAdapter(mPagerAdapter);
//		pager.setPageMargin(
//				getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
//		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
//
//			@Override
//			public void onPageScrolled(int position,
//					float positionOffset, int positionOffsetPixels) {
//				
//			}
//
//			@Override
//			public void onPageSelected(int position) {
//				View prevView = pager.getChildAt(currentPage);
//				View view = pager.getChildAt(position);
//				view.animate().scaleX(1.1f).setDuration(500);
//				view.animate().scaleY(1.1f).setDuration(500);
//				prevView.animate().scaleX(10f/11f).setDuration(500);
//				prevView.animate().scaleY(10f/11f).setDuration(500);
//				currentPage = position;
//				try{
//					DetailListItem ditem= list.get(position);
//				contents.setText(ditem.getDetail_content());
//					Log.v(TAG,"position listener"+position);
//					switch(position%2){
//					case 0:
//						img.setImageResource(R.drawable.re_dotor1);
//						break;
//					case 1:img.setImageResource(R.drawable.re_dotor2);
//						break;
//					
//					}
//				}catch(IndexOutOfBoundsException e){
//					
//					Log.v(TAG,"IndexOutofBounds"+e);
//				}
//			}
//			
//		});
		
       // TODO Auto-generated method stub
//	    pager = (ViewPager)findViewById(R.id.menu_pager);
//		pager.setOffscreenPageLimit(5);
//		mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager()); 
//		pager.setAdapter(mPagerAdapter);

		
//		PagerContainer.listene=new PageChagedListener() {
//			
//			@Override
//			public void onPageChange(int position) {
//			try{
//				DetailListItem ditem= list.get(position);
//			contents.setText(ditem.getDetail_content());
//				Log.v(TAG,"position listener"+position);
//				switch(position%2){
//				case 0:
//					img.setImageResource(R.drawable.re_dotor1);
//					break;
//				case 1:img.setImageResource(R.drawable.re_dotor2);
//					break;
//				
//				}
//			}catch(IndexOutOfBoundsException e){
//				
//				Log.v(TAG,"IndexOutofBounds"+e);
//			}
//				}
//			
//		};
		   

		//PagerContainer m=new PagerContainer(KidsMindTotalResultActivity.this,img);
		//pager.setOnPageChangeListener(m);

//		pager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//				switch(position){
//				case 0:
//					img.setImageResource(R.drawable.re_dotor1);
//					break;
//				case 1:img.setImageResource(R.drawable.re_dotor2);
//					break;
//				case 2:
//					img.setImageResource(R.drawable.re_dotor2);
//					break;
//				case 3:
//					img.setImageResource(R.drawable.re_dotor1);
//					break;
//				}
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//				// TODO Auto-generated method stub
//				 mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			    if (mNeedsRedraw)pager.invalidate();
//				
//			}
//			
//			
//		});
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
