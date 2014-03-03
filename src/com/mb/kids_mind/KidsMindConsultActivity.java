package com.mb.kids_mind;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.mb.kids_mind.Adapter.BaseExpandableAdapter;
import com.mb.kids_mind.Item.ConsultChildItem;
import com.mb.kids_mind.Item.ConsultItem;



public class KidsMindConsultActivity extends Activity {
	private ArrayList<ConsultItem> mGroupList = null;
	private ArrayList<ArrayList<ConsultChildItem>> mChildList=null;
	private ArrayList<ConsultChildItem> mChildListContent = null;
	private ExpandableListView mListView;
	//private 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.cousultlist);
	    findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    mListView = (ExpandableListView) findViewById(R.id.elv_list);
	    mGroupList = new ArrayList<ConsultItem>();
	    mChildList = new ArrayList<ArrayList<ConsultChildItem>>();
	    mChildListContent = new ArrayList<ConsultChildItem>();
	    for(int i=0;i<4;i++){
	    	ConsultItem item= new ConsultItem();
	    	item.setAddress("/서울시 용산구 원효로 3가 ");
	    	item.setCenter("WeakBox Center");
	    	item.setRes(R.drawable.btn_doctor);
	    	item.setName("유지민");
	    	item.setTitle("|독서 치료 심리 상담 전문가");
	    	
	    		
	    	mGroupList.add(item);
	    	
	    }
	    ConsultChildItem item1=new ConsultChildItem();
    	
	    item1.setText(" -고신대학교 기독교 교육상담\n -고신대학교 대학원 박사 과정 \n -프랑스 몽펠리에 3대학 음악치료사 \n -중리 초등학교 전문 상담사 \n -보훈 요양원 집단 프로그램 컨설팅 및 전문 상담사");
    	mChildListContent.add(item1);
    	mChildList.add(mChildListContent);
    	ConsultChildItem item2=new ConsultChildItem();
    	
    	item2.setText(" -고신대학교 기독교 교육상담\n -고신대학교 대학원 박사 과정 \n -프랑스 몽펠리에 3대학 음악치료사 \n -중리 초등학교 전문 상담사 \n -보훈 요양원 집단 프로그램 컨설팅 및 전문 상담사");
    	mChildListContent.add(item2);
    	mChildList.add(mChildListContent)
    	;
    	ConsultChildItem item3=new ConsultChildItem();
    	
    	item3.setText(" -고신대학교 기독교 교육상담\n -고신대학교 대학원 박사 과정 \n -프랑스 몽펠리에 3대학 음악치료사 \n -중리 초등학교 전문 상담사 \n -보훈 요양원 집단 프로그램 컨설팅 및 전문 상담사");
    	mChildListContent.add(item3);
    	mChildList.add(mChildListContent);
    	
    	ConsultChildItem item4=new ConsultChildItem();
    	
    	item4.setText(" -고신대학교 기독교 교육상담\n -고신대학교 대학원 박사 과정 \n -프랑스 몽펠리에 3대학 음악치료사 \n -중리 초등학교 전문 상담사 \n -보훈 요양원 집단 프로그램 컨설팅 및 전문 상담사");
    	mChildListContent.add(item4);
    	mChildList.add(mChildListContent);
    	    mListView.setAdapter(new BaseExpandableAdapter(this, mGroupList,mChildList));
	    
	mListView.setOnGroupClickListener(new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
//			Toast.makeText(getApplicationContext(), "g click = " + groupPosition, 
//					Toast.LENGTH_SHORT).show();
			return false;
		}
	});
	
	// 차일드 클릭 했을 경우 이벤트
	mListView.setOnChildClickListener(new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Toast.makeText(getApplicationContext(), "c click = " + childPosition, 
					Toast.LENGTH_SHORT).show();
			return false;
		}
	});
	
	// 그룹이 닫힐 경우 이벤트
	mListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
		@Override
		public void onGroupCollapse(int groupPosition) {
//			Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition, 
//					Toast.LENGTH_SHORT).show();
		}
	});
	
	// 그룹이 열릴 경우 이벤트
	mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
		@Override
		public void onGroupExpand(int groupPosition) {
//			Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition, 
//					Toast.LENGTH_SHORT).show();
		}
	});
	}

}
