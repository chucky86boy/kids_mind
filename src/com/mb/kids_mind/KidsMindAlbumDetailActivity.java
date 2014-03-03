package com.mb.kids_mind;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.mb.kids_mind.Adapter.RecommendListAdapter;
import com.mb.kids_mind.Adapter.RecommendListAdapterAlbum;
import com.mb.kids_mind.Adapter.SelectListAdapter;
import com.mb.kids_mind.Adapter.SimilarListAdapter;
import com.mb.kids_mind.Adapter.SimilarListAdapter2;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.RecommendItem;
import com.mb.kids_mind.Item.SItem2;
import com.mb.kids_mind.Item.SimilarItem;
import com.mb.kids_mind.Item.SimilarJsonparser2.SimilarJsonparser;
import com.mb.kids_mind.fragment.LastRecommendImage;
import com.mb.kids_mind.fragment.LastSimilarImage;
import com.mb.kids_mind.fragment.SingleResultSketchAlbumMenu;
import com.mb.kids_mind.task.NetManager;
import com.mb.kids_mind.view.TwoWayView;

public class KidsMindAlbumDetailActivity extends FragmentActivity {
private static final String TAG ="MainActivity";
MyHelper helper;
private AQuery aquery;
SQLiteDatabase db,db2;
private int currentPage;
private KidsMindDBHelper khelper;
SimilarListAdapter2 sAdapter;
RecommendListAdapterAlbum rAdapter;
SelectListAdapter selAdapter;
public ScreenSlidePagerAdapter mPagerAdapter;
public ScreenSlidePagerAdapter2 mPagerAdapter2;
ViewPager pager;
DetailListItem item;
ViewPager listpager;
ArrayList<SimilarItem> slist=new ArrayList<SimilarItem>();
ArrayList<RecommendItem> rlist;
SimilarItem sitem;
RecommendItem ritem;
String savename;
String date;
String where;

private ArrayList<DetailListItem>list=new ArrayList<DetailListItem>() ;
View.OnClickListener bHandler =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back_btn:
			finish();
			break;
		case R.id.home:
			SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		    SharedPreferences.Editor editor=pref.edit();
		    editor.putString("noti", "");
		    editor.commit();
		Intent intent=new Intent(KidsMindAlbumDetailActivity.this,MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("check", "1");
		startActivity(intent);
			break;
		case R.id.face:
			break;
		case R.id.kakao:
			break;
		case R.id.con:
			popupadvice(KidsMindAlbumDetailActivity.this);
			break;
		
		}
	}
};
public SparseArray<WeakReference<SingleResultSketchAlbumMenu>> sparseArray = new SparseArray<WeakReference<SingleResultSketchAlbumMenu>>();
	
ImageView img;
TwoWayView hlview,hlview2,hlview3;
LinearLayout linear;
private String detail_id;
int advice_id;
String questionid;
LinearLayout circle;
//private TextView lastadvice;
@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    DisplayMetrics metrics = new DisplayMetrics(); 
//	    Display mDisplay = KidsMindAlbumDetailActivity.this.getWindowManager() .getDefaultDisplay();
//	    getWindowManager().getDefaultDisplay().getMetrics(metrics); 
////	    float screenDensity = metrics.density;
////	    float screenDensityDPI = metrics.densityDpi; 
//	    int width = mDisplay.getWidth(); 
//	    int Height = mDisplay.getHeight(); 
//	    if(Height<1280){
//	    	setContentView(R.layout.albumdetail2);
//		    
//	    }else{
	    	setContentView(R.layout.albumdetail);
		    	
	    //}
	    View view = findViewById(R.id.menu_pager);
	    khelper=new KidsMindDBHelper(KidsMindAlbumDetailActivity.this);
	    helper=new MyHelper(KidsMindAlbumDetailActivity.this,"kidsmind.db" , null, 1);
	    
	   


	    circle=(LinearLayout)findViewById(R.id.circle);
	    findViewById(R.id.back_btn).setOnClickListener(bHandler);
	    findViewById(R.id.home).setOnClickListener(bHandler);
	    findViewById(R.id.face).setOnClickListener(bHandler);
	    findViewById(R.id.kakao).setOnClickListener(bHandler);
	    findViewById(R.id.con).setOnClickListener(bHandler);
	//    lastadvice=(TextView)findViewById(R.id.lastadvice);
	   
	    img=(ImageView)findViewById(R.id.image);
	    pager=(ViewPager)findViewById(R.id.lastpager);
	    linear=(LinearLayout)findViewById(R.id.pagervi);
		linear.setVisibility(View.GONE);
		//앨범에서 정보 받아오기
		Intent intent=getIntent();
//		in.putExtra("savename", info.getImage_path());
//		in.putExtra("date", info.getDate());
//		in.putExtra("advice_id", info.getAdvice_id());
//		in.putExtra("qposition", info.getQuestioin());
		advice_id=intent.getIntExtra("advice_id", -1);
		date=intent.getStringExtra("date");
		savename=intent.getStringExtra("savename");
		Log.v(TAG,"advice_id"+advice_id+""+date+savename);
		readimage(savename);
		selectAll(savename);
		SharedPreferences pref = getSharedPreferences("pref",
				MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putString("qposition", questionid);
		editor.commit();
		//lastadvice.setText(advice_talk);
		Log.v(TAG,"detail_id"+detail_id);
		String[] detail=detail_id.split(",");
		for(String cha:detail){
			Log.v(TAG,"자른아이디"+cha);
			selectDb(cha);

		}
		
		
	    hlview=(TwoWayView)findViewById(R.id.list);
		hlview.setItemMargin(10);
		hlview.setLongClickable(true);
		String url2 = Const.SIMILAR + questionid +"/"+detail_id ;
		Log.v(TAG, "유사이미지 url" + url2);

		new JobTask().execute(url2);
		
		
		sAdapter = new SimilarListAdapter2(KidsMindAlbumDetailActivity.this,
				R.layout.similarlistview2, slist, slist.size());

		// Assign adapter to HorizontalListView
		hlview.setAdapter(sAdapter);
		
		hlview2=(TwoWayView)findViewById(R.id.list2);
		hlview2.setItemMargin(10);
		hlview2.setLongClickable(true);
		//itemList = new ArrayList<DetailListItem>();

		//selectdb();
		//테스트
		rlist=new ArrayList<RecommendItem>();
		for(int i=0 ; i<10 ;i++){
			ritem= new RecommendItem();
			ritem.setContents("놀이치료는 어쩌고저쩌고");
			ritem.setRes(R.drawable.d001);
			ritem.setTitle("대인관계");
			rlist.add(ritem);
		}
		
		rAdapter = new RecommendListAdapterAlbum(KidsMindAlbumDetailActivity.this,
				R.layout.similarlistviewalbum, rlist, rlist.size());

		// Assign adapter to HorizontalListView
		hlview2.setAdapter(rAdapter);
		

		hlview3=(TwoWayView)findViewById(R.id.list3);
		hlview3.setItemMargin(30);
		//itemList = new ArrayList<DetailListItem>();

		//selectdb();
		//테스트
		
		
		selAdapter = new SelectListAdapter(KidsMindAlbumDetailActivity.this,
				R.layout.result_sketch_album, list, list.size());

		// Assign adapter to HorizontalListView
		hlview3.setAdapter(selAdapter);
	    // TODO Auto-generated method stub
		aquery = new AQuery(this);
		hlview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				circle.setVisibility(View.GONE);
				where="1";
				linear.setVisibility(View.VISIBLE);
				mPagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());
				pager.setAdapter(mPagerAdapter);// TODO Auto-generated method stub
				
			}
		});
		hlview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				circle.setVisibility(View.VISIBLE);
				where="2";
				linear.setVisibility(View.VISIBLE);
				mPagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());
				pager.setAdapter(mPagerAdapter);// TODO Auto-generated method stub
				
			}
		});
	
//				listpager = (ViewPager)findViewById(R.id.menu_pager);
//
//				listpager.setOffscreenPageLimit(2);
//				mPagerAdapter2=new ScreenSlidePagerAdapter2(getSupportFragmentManager());
//				listpager.setAdapter(mPagerAdapter2);
//				listpager.setPageMargin(
//						getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
//				
//
//				listpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
//
//					@Override
//					public void onPageSelected(int position) {
//					
//						
//					}
//					
//				});
				
		
}
void popupImage(Activity context, Bitmap bitmap)
{
	// Create dialog
	Dialog dialog = new Dialog(context);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.imagedialog);

	ImageView image = (ImageView) dialog.findViewById(R.id.imageView1);

	// !!! Do here setBackground() instead of setImageDrawable() !!! //
	image.setImageBitmap(bitmap);

	// Without this line there is a very small border around the image (1px)
	// In my opinion it looks much better without it, so the choice is up to you.
	dialog.getWindow().setBackgroundDrawable(null);

	// Show the dialog
	dialog.show();    
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
			
			img.setImageBitmap(bitmap);
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupImage(KidsMindAlbumDetailActivity.this, bitmap);
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
		}

	}
}
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	public SparseArray<Fragment> fragMap = new SparseArray<Fragment>();
	
	public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
    }

    @Override
    public int getCount() {
    	if("1".equals(where)){
        return slist.size();
    	}else{
    		return rlist.size();
    	}
    }
    
	@Override
	public Fragment getItem(int position) {
		if("1".equals(where)){
	        
		LastSimilarImage frag = new LastSimilarImage();
		frag.setPosition(position);
		frag.setDate(slist);
		return frag;
		}else{
			LastRecommendImage frag =new LastRecommendImage();
			frag.setPosition(position);
			frag.setDate(rlist);
			return frag;
		}
		
	}
}

public class ScreenSlidePagerAdapter2 extends FragmentStatePagerAdapter {
	public ScreenSlidePagerAdapter2(FragmentManager fm) {
		super(fm);
    }

    @Override
    public int getCount() {
    	//Log.v(TAG,"adapter listsize"+list.size()+"");
        return list.size();
    }

	@Override
	public Fragment getItem(int position) {
		SingleResultSketchAlbumMenu frag = new SingleResultSketchAlbumMenu();
		frag.setData(list);
		frag.setPosition(position);
//		sparseArray.put(position, new WeakReference<SingleResultSketchAlbumMenu>(frag));
		return frag;
	}
}
	
void popupadvice(Activity context) {
	// Create dialog
	final Dialog dialog = new Dialog(context);
	dialog.getWindow().setBackgroundDrawable

	(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	dialog.setCanceledOnTouchOutside(false);

	dialog.setContentView(R.layout.advicedialog);
	dialog.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
			String ch=pref.getString("login_check", "");	
			if(!ch.equals("")){
				//로그인상태
				Intent intent= new Intent(KidsMindAlbumDetailActivity.this,KidsMindAdviceActivity.class);
				intent.putExtra("where", "albumdetail");
				intent.putExtra("date", date);
				intent.putExtra("savename", savename);
				startActivity(intent);
				
			}else{
				Intent in=new Intent(KidsMindAlbumDetailActivity.this,KidsMindLoginSelectActivity.class);
					SharedPreferences.Editor editor=pref.edit();
								editor.putString("last", "last");
								editor.commit();
								
				startActivityForResult(in, 0);
			}
			dialog.dismiss();
		}
	});
	dialog.findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});

	dialog.show();
}
void updateRec(String wStr,  int newLName){
	openDB();

	//String sql="update tmember set age =32 where fname like '%k%';";
	ContentValues values = new ContentValues();
	values.put("advice_id", newLName);

	String whereClause="fName like ?";
	String[] whereArgs={"%" + wStr + "%"};
	try{

		int cnt=db.update("km_check", values, whereClause, whereArgs);


		Log.v(Debugc.getTagad(),"advice_id업데이트중"+cnt+"");
	}catch(SQLException e){
		Log.v(Debugc.getTagad(),"update error"+e);
	}

	closeDB();
}
void openDB(){
	db2 = khelper.getWritableDatabase();
	db = helper.getWritableDatabase();
}
// dbClose();
void closeDB(){
	if (db2 != null) {
		if (db2.isOpen()) {
			db2.close();
		}
	}
	if(db != null){
		if(db.isOpen()){
			db.close();
		}
	}
}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK ) {
    	switch(linear.getVisibility()){
    	case View.GONE:
    		
    		finish();
    		break;
    	case View.VISIBLE:
    		linear.setVisibility(View.GONE);
    		
    		break;
    	}
    	
    }
    return true;
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
class JobTask extends AsyncTask<String, Void, SItem2> {

	@Override
	protected SItem2 doInBackground(String... params) {
		SItem2 advice = null;
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

				advice = SimilarJsonparser.parse(sb.toString());
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
	protected void onPostExecute(SItem2 result) {
		// if(CDialog dialog != null)
		// {
		// pDialog.cancel();
		// }

		if (result != null) {

			slist = result.getData();
			int cnt = slist.size();
			

			sAdapter.setList(slist);
			sAdapter.notifyDataSetChanged();
			// int
			// height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*data.size();
			// LayoutParams lp=(LayoutParams) list.getLayoutParams();
			// lp.height=height;
			// list.setLayoutParams(lp);

		}
		super.onPostExecute(result);
	}

}

String advice_talk;
public void selectAll(String image){
	openDB();

	//String sql ="select tag_name from md_question_tag where question_id=Q"+po+";";
	Cursor c=null;
	String wStr="fName=?";
	String[] wherStr={image};
	String[] colNames={"fName","detail_id","detail_check","question_id","advice_talk"};
	try{
		c=db.query("km_check", colNames, wStr, wherStr, null, null, null);
		while(c.moveToNext()){
			detail_id=c.getString(c.getColumnIndex("detail_id"));
			advice_talk=c.getString(c.getColumnIndex("advice_talk"));
			questionid=c.getString(c.getColumnIndex("question_id"));
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
}
