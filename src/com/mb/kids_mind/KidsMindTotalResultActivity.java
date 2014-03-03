package com.mb.kids_mind;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
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
import android.widget.LinearLayout.LayoutParams;
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
	public String savename;
	boolean mNeedsRedraw = false;
	ImageView icon;
	LinearLayout set;
	TextView title;
	TextView date;
	String first="M113, M068";
	String second="M008, M009, M011, M012, M014, M017, M113";
	String third="M043, M050,M082,M088";
	String forth="M102,";
	String first2="그림에서 근거해서 보면 아동이 소극적이고 자신감이 부족한 것으로 보입니다. 문제가 있다고 보기는 어렵지만 소극적인 아동은 평소에는 조용하고 순한 아동으로 보여져 방치되다가 후에 사회성에 문제가 생길 수 있습니다. 만약 부모님께서 아이에게 필요한 것을 모두 해주시거나 혹은 아이의 행동을 간섭하고 질책하신다면 아이를 더욱 소극적으로 만들 수 있습니다. 그러니 평소에 아동이 자신의 주장을 잘 할 수 있도록 도와주고 안정감을 가질 수 있도록 격려해주셔서 아이의 자신감을 키워주시는 것이 중요합니다";	
	String second2="문제가 있다고 보기는 어렵지만, 그림에 근거해서 보면 아동에게 사회성이 부족할 수 있습니다. 이러한 경우 아동이 지나치게 소극적이거나 과격해서 상호작용이 어렵거나 혹은 자기자신에게만 몰두해 주변 상황에 무관심한 경우가 많습니다. 부모님께서 아동을 잘 관찰하실 필요가 있으며, 아동이 소극적이거나 자기 자신에게만 몰두하는 경우 부모님께서 아이가 다양한 사람들을 만날 수 있는 경험을 제공해주시고 다른 사람들 앞에서 많은 칭찬을 하여 자신감을 갖도록 하는 것이 중요합니다.";
	String third2="혹시 아동이 평소에 신경질적이거나 공격적인 행동을 보이지는 않나요? 문제가 크지는 않지만 신경질적인 아동은 상처를 쉽게 받고 사소한 일에도 투정과 짜증을 내어서 사회성에 문제가 생길 수 있습니다. 이런 아동은 보통 과잉보호나 애정결핍으로 인한 심리적 불안정이 성격에 영향을 끼친 경우가 많습니다. 부모님의 양육방식에 대한 점검이 필요하며, 아이가 안정된 환경에서 밝은 기분을 가질 수 있도록 만드는 것이 중요합니다. 아이에게 도움이 되는 놀이치료 방법은 아래의 추천하는 놀이를 참고해주시기 바랍니다.";
	String forth2="그림에 근거해서 보면 아동이 현재 안정감을 충분히 느끼지 못하는 것으로 보입니다. 아이가불안과 불안정감을 계속 느끼면 감정이 외면화되어 공격적이거나 여러 심리적 문제가 발생할 수 있으니 부모님의 세심한 배려가 필요합니다. 부모님께서는 충분한 관심과 사랑, 정서적인 지지를 주어야 하며 아이와의 안정 애착을 형성하는데는 신체적 접촉이 이루어지는 놀이가 도움이 되니 아래의 추천하는 놀이치료를 참고해주시기 바랍니다.";
	String fifth="그림분석결과 특이사항이 나타나지 않았으며 아이가 안정된 상태로 보입니다. 그러나 한 장의 그림으로는 심리를 정확하게 분석하기 어려우니 여러 그림을 지속적으로 분석하여 아이의 심리상태를 잘 파악하는 것이 중요합니다.";
	public String idate;
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
			SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		    SharedPreferences.Editor editor=pref.edit();
		    editor.putString("noti", "");
		    editor.commit();
			Intent intent=new Intent(KidsMindTotalResultActivity.this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("check", "1");
			startActivity(intent);
				
		}
	});
		contents=(TextView)findViewById(R.id.contents);
		Intent intent=getIntent();
		idate=intent.getStringExtra("date");
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
		String[] last=first.split(",");
		int cnt=0;
		for(String f:last){
		
			if(detail_id.contains(f)){
			Log.v(TAG,"first"+cnt+"");
			if(cnt==0)	
			updateRec(savename,"1",first2);
			++cnt;
			}
			
		}
		String[] last2=second.split(",");
		int cnt2=0;
		for(String f:last2){
		
			if(detail_id.contains(f)){
			Log.v(TAG,"last2"+cnt+"");
			if(cnt2==0)	
			updateRec(savename,"1",second2);
			++cnt2;
			}
			
		}
		String[] last3=third.split(",");
		int cnt3=0;
		for(String f:last3){
		
			if(detail_id.contains(f)){
			Log.v(TAG,"last3"+cnt3+"");
			if(cnt3==0)
				updateRec(savename,"1",third2);
			++cnt3;
			}
			
		}
		String[] last4=forth.split(",");
		int cnt4=0;
		for(String f:last4){
		
			if(detail_id.contains(f)){
			Log.v(TAG,"last4"+cnt4+"");
			if(cnt4==0)	
			updateRec(savename,"1",forth2);
			++cnt4;
			}
			
		}
		if(cnt==0&&cnt2==0&&cnt3==0&&cnt4==0){
			updateRec(savename,"5",fifth);
			
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
	void updateRec(String image_id, String advice_type,String advice_talk){
		openDB();
		//String sql="update tmember set age =32 where fname like '%k%';";
		Log.v(TAG,"updatestart"+advice_talk+advice_type);
		ContentValues values = new ContentValues();
		values.put("advice_talk", advice_talk);
		
		values.put("advice_type", advice_type);
		String whereClause="fName like ?";
		String[] whereArgs={"%" + image_id + "%"};
		
		try{
		Log.v(TAG,"시작");
			int cnt=db.update("km_check", values, whereClause, whereArgs);
	Log.v(TAG,"pass/fail"+cnt+"");
	Log.v(TAG,"성공");
		
		}catch(SQLException e){
			
		}
		
		closeDB();
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
		String[] colNames={"detail_image","detail_title","detail_contents"};
		
		try{
			c=db2.query("km_question_detail", colNames, wStr, wherStr, null, null, null);
			//c=db.rawQuery(sql, null);
			while(c.moveToNext()){
				item=new DetailListItem();
				item.setDetail_image(c.getString(c.getColumnIndex("detail_image")));
				item.setDetail_tilte(c.getString(c.getColumnIndex("detail_title")));
				item.setDetail_content(c.getString(c.getColumnIndex("detail_contents")));
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
					if("Q4".equals(Q)){
						title.setText("물고기 그리기");
						
					}else if("Q3".equals(Q)){
						title.setText("사람 그리기");
						
					}else if("Q2".equals(Q)){
						title.setText("나무 그리기");
						
					}else if("Q1".equals(Q)){
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
