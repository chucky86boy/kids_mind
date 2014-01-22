package com.mb.kids_mind;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mb.kids_mind.Adapter.SiteAdapter;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.KidsMindResultItem;
import com.mb.kids_mind.Item.TagList;


public class KidsMindResultActivity extends Activity {
	static final String TAG="TAG";
	SharedPreferences pref;
	ArrayList<KidsMindResultItem> siteList = new ArrayList<KidsMindResultItem>();
	ListView siteListView;
   SiteAdapter adapter;
   String savename;
   SQLiteDatabase db;
   TagList titem;
   KidsMindDBHelper myhelper;
	ArrayList<TagList> tlist = new ArrayList<TagList>();
	ArrayList<DetailListItem>dlist=new ArrayList<DetailListItem>();
	View.OnClickListener bHandler = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.button1:
				Intent in =new Intent(KidsMindResultActivity.this,KidsMindTotalResultActivity.class);
				in.putExtra("savename",savename);
				in.putExtra("where","1");
				startActivity(in);
			break;
			case R.id.button2:
				
			}
		}
	};
	ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		Button btn=(Button)findViewById(R.id.button1);
		 img=(ImageView)findViewById(R.id.imageView1);
		 pref=getSharedPreferences("pref",MODE_PRIVATE);
		 myhelper=new KidsMindDBHelper(KidsMindResultActivity.this);
		 try{
				myhelper.createDataBase();
			}catch(IOException ioe){
				throw new Error("error");
			}
		 Intent intent=getIntent();
		
		//if("1".equals(intent.getStringExtra("where"))){
		// Bitmap bit=intent.getParcelableExtra("img");
		// img.setImageBitmap(bit);
		savename=intent.getStringExtra("savename");

		readimage(savename);
		
		btn.setOnClickListener(bHandler);
		siteListView = (ListView) findViewById(R.id.listView1);
		siteListView.setDivider(null);
		
        adapter =new SiteAdapter(KidsMindResultActivity.this, R.layout.resultlist, siteList);
        // Assign adapter to HorizontalListView
        siteListView.setAdapter(adapter);
        
        fillSomeData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	void fillSomeData()
	{		
		selectAll();
		
		for( int i=0; i<tlist.size(); i++)
		{
			KidsMindResultItem site = new KidsMindResultItem();
			TagList item = tlist.get(i);
			site.setTitle(item.getTag_name());
			site.setTag_id(item.getTag_id());
			
			siteList.add(site);
		}

		Log.v(TAG, siteList.toString());
		adapter.notifyDataSetChanged();
	}	
	public void selectAll() {
		openDB();
		// 諛⑸쾿 1
		Log.v(TAG, "탭 디비 시작");

		String question=pref.getString("qposition", "");
		Log.v(TAG,"questiong_id"+question);
		// String sql
		// ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c = null;
		String wStr = "question_id=?";
		String[] wherStr = { question };
		String[] colNames = { "tag_id", "tag_name" };
		try {
			c = db.query("km_question_tag", colNames, wStr, wherStr, null,
					null, null);
			// c=db.rawQuery(sql, null);
			Log.v(TAG, "숫자:" + c.getCount());
			while (c.moveToNext()) {
				titem = new TagList();
				titem.setTag_id(c.getString(c.getColumnIndex("tag_id")));
				titem.setTag_name(c.getString(c.getColumnIndex("tag_name")));
				tlist.add(titem);
				Log.v(TAG,
						"탭 아이디값은" + c.getString(c.getColumnIndex("tag_name")));
				// Log.v(Debugc.getTaga(), c.getString(0)+ c.getString(1)+
				// c.getString(2)+c.getString(3)+c.getString(4));
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
}

