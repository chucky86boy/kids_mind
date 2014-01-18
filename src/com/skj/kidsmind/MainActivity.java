package com.skj.kidsmind;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.skj.kidsmind.Adapter.SiteAdapter;
import com.skj.kidsmind.Item.KidsMindResultItem;

public class MainActivity extends Activity {
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
				Intent in =new Intent(MainActivity.this,KidsMindDrawActivity.class);
				startActivity(in);
			break;
			case R.id.button2:
				
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(bHandler);
		siteListView = (ListView) findViewById(R.id.listView1);
		siteListView.setDivider(null);
        adapter =new SiteAdapter(MainActivity.this, R.layout.resultlist, siteList);
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

