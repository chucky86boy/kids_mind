package com.mb.kids_mind;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.mb.kids_mind.Adapter.RecommendListAdapter;
import com.mb.kids_mind.Adapter.SimilarListAdapter;
import com.mb.kids_mind.Adapter.SimpleListAdapter;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.RecommendItem;
import com.mb.kids_mind.Item.SimilarItem;
import com.mb.kids_mind.view.TwoWayView;

public class KidsMindLastResultActivity extends Activity {
	private static final String TAG="MainActivity";
	private AQuery aquery;
	private String imagePath;
	MyHelper helper;
	TwoWayView hlview,hlview2;
	SQLiteDatabase db;
	SimilarListAdapter sAdapter;
	RecommendListAdapter rAdapter;

	ArrayList<SimilarItem> slist;
	ArrayList<RecommendItem> rlist;
	SimilarItem sitem;
	RecommendItem ritem;
	String savename;
	EditText msg;
	ImageView total;
	CheckBox similar,recommend;
	TextView sichat;
	TextView rechat;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.totallastview);
		total=(ImageView)findViewById(R.id.imageView1);
		total.setImageResource(R.drawable.cap);
		//total set image 리절트에 따른 이미지 셋팅
		sichat=(TextView)findViewById(R.id.chat);
		sichat.setVisibility(View.GONE);
		rechat=(TextView)findViewById(R.id.recochat);
		rechat.setVisibility(View.GONE);
		similar=(CheckBox)findViewById(R.id.similar);
		similar.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					sichat.setVisibility(View.VISIBLE);
				}else{
					sichat.setVisibility(View.GONE);	
				}

			}
		});
		recommend=(CheckBox)findViewById(R.id.recomment);
		recommend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rechat.setVisibility(View.VISIBLE);
				}else{
					rechat.setVisibility(View.GONE);	
				}	
			}
		});

		hlview=(TwoWayView)findViewById(R.id.list);
		hlview.setItemMargin(10);
		hlview.setLongClickable(true);
		//itemList = new ArrayList<DetailListItem>();
		//selectdb();
		
		//테스트
		
		slist=new ArrayList<SimilarItem>();
		for(int i=0 ; i<10 ;i++){
			sitem= new SimilarItem();
			sitem.setAdvice_contents("이아이의 그림은 정말 좋아요");
			sitem.setDetail_id("M!");
			sitem.setRes(R.drawable.d002);
			sitem.setImage_title("나무그리기");
			sitem.setUser_age("10세 남아");
			
			slist.add(sitem);
		}
		sAdapter = new SimilarListAdapter(KidsMindLastResultActivity.this,
				R.layout.similarlistview, slist, slist.size());

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
		
		rAdapter = new RecommendListAdapter(KidsMindLastResultActivity.this,
				R.layout.similarlistview, rlist, rlist.size());

		// Assign adapter to HorizontalListView
		hlview2.setAdapter(rAdapter);







		findViewById(R.id.advice).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//의뢰페이지로 전달

			}
		});
		aquery = new AQuery(this);
		Intent in=getIntent();
		savename=in.getStringExtra("savename");
		helper=new MyHelper(KidsMindLastResultActivity.this,"kidsmind.db" , null, 1);
		// TODO Auto-generated method stub
	}
	private File uploadFile ;
	private void asyncImageUploadJson(String msg) {






		// uploadFileExecute(uploadFile);
		SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();

		int userId = pref.getInt("user_id", 0);
		String questionid=pref.getString("qposition", "");
		Map<String, Object> params = new HashMap<String, Object>();
		uploadFile= new File(Environment.getExternalStorageDirectory(), "/KidsMind/"+savename);
		Log.v(TAG,"파일 용량 =>"+ String.valueOf(uploadFile.length()));
		params.put("image", uploadFile);
		params.put("message",msg);

		aquery.ajax(Const.IMAGE_CHANGE_URL + "/" + userId+"/"+questionid, params, JSONObject.class, this, "jsonImageUploadCallback");
	}

	public void jsonImageUploadCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					int advice_id = json.getInt("advice_id");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putInt("advice_id", advice_id);
					editor.commit();
					Log.v(Debugc.getTagd(),"advice_id"+advice_id);
					//사
					updateRec(savename,advice_id);
					//openInfoMessageDialogBox("상담 요청 하였습니다.");

				} else {
					String error = json.getString("error");
					//openInfoMessageDialogBox(error);
				}
			} catch (JSONException e) {
				//openErrorDialog();
				e.printStackTrace();
			}
		} else {
			//openErrorDialog();
		}
	}
	public void asynccommentJson(String msg) {

		//	openWaitDialog();

		String url = Const.USER_COMMENT;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("comment", msg);
		SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		int userId = pref.getInt("user_id", 0);
		int adviceId=pref.getInt("advice_id", 0);
		Log.v(TAG,"userid"+userId+""+adviceId+"");
		String userid=userId+"";
		String adviceid=adviceId+"";
		url=url+userid+"/"+adviceid;
		Log.v(TAG,"댓글url"+url);
		aquery.ajax(url, map, JSONObject.class, this, "jsonJoinCallback");

		//sendView.setText(url);
	}


	public void jsonJoinCallback(String url, JSONObject json, AjaxStatus status) {

		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					Log.v(TAG,"댓글달림");
					//				int user_id = json.getInt("user_id");
					//				String user_name = json.getString("user_name");
					//				String authkey = json.getString("authkey");
					//				
					//				Log.v(Debugc.getTagd(),"user_id"+user_id);
					//				Log.v(Debugc.getTagd(),"authkey"+authkey);
					//				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					//				SharedPreferences.Editor editor=pref.edit();
					//				editor.putString("first", "first");
					//				editor.putString("login_check", "checked");
					//				editor.putString("user_name", user_name);
					//				editor.putInt("user_id", user_id);
					//				editor.putString("authkey", authkey);
					//				editor.putString("user_pwd", user_pwd3);
					//				editor.commit();
					//				
					//asyncAutoLoginJson(user_id, authkey);
					//	Log.v(TAG,"authkey"+authkey+"user_name"+user_name+"user_id"+user_id+"");
					//openInfoMessageDialogBox("로그인 성공");
					//	KidsMindLoginActivity.this.setResult(RESULT_OK);
					finish();


					//resultView.setText(json.toString());
				} else {
					Log.v(TAG,"에러");
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					//		openInfoMessageDialogBox("error");
				}
			} catch (JSONException e) {
				//		openErrorDialog();
				Log.v(TAG,"e"+e);
				e.printStackTrace();
			}
		} else {
			Log.v(TAG,"json null");
			//		openErrorDialog();
		}
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
		//db = openOrCreateDatabase("sample.db", wi, null);
		db = helper.getWritableDatabase();
	}
	// dbClose();
	void closeDB(){
		if(db != null){
			if(db.isOpen()){
				db.close();
			}
		}
	}
}