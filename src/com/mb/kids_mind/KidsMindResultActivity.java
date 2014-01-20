package com.mb.kids_mind;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mb.kids_mind.Item.KidsMindResultItem;

public class KidsMindResultActivity extends Activity {
	static final String TAG="TAG";
	
	ArrayList<KidsMindResultItem> siteList = new ArrayList<KidsMindResultItem>();
	ListView siteListView;
   SiteAdapter adapter;
	
	View.OnClickListener bHandler = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.button1:
				Intent in =new Intent(KidsMindResultActivity.this,KidsMindDrawActivity.class);
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
		Intent intent=getIntent();
		//if("1".equals(intent.getStringExtra("where"))){
		// Bitmap bit=intent.getParcelableExtra("img");
		// img.setImageBitmap(bit);
		String image_id=intent.getStringExtra("savename");

		readimage(image_id);
		
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
			bitmap=BitmapFactory.decodeFile(path2);
			Log.v(TAG,"이미지를 읽어오기위한 경로2"+path2);

			if(bitmap!=null){
				Log.v(TAG,"이미지 로딩");
				img.setImageBitmap(bitmap);
			}else{
				Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
			}

		}
	}
	void fillSomeData()
	{		
		
		String[] names = { "나무뿌리", "나무두께", "나무 크기", "모양 기타", "색깔", "나무 줄기", "열매 크기", "나무 위치","나무 구","나무 십" };
		
		for( int i=0; i<names.length; i++)
		{
			KidsMindResultItem site = new KidsMindResultItem();
			site.setTitle(names[i]);
			
			siteList.add(site);
		}

		Log.v(TAG, siteList.toString());
		adapter.notifyDataSetChanged();
	}	
}

