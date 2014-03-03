package com.mb.kids_mind.Helper;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHelper extends SQLiteOpenHelper {
	private static final String TAG = "MainActivity";
	Context context;
	
	public MyHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.v(TAG, "DB open");
		super.onOpen(db);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
//		Log.v(TAG, "DB onCreate");
//		String sql = "create table md_question(" +
//				
//				"question_id char(5) primary key  , question_title text not null, question_content text not null,  question_message text not null); ";
//		String sql2 = "create table md_question_tag(" +
//				
//				"tag_id char(5) primary key  , tag_name text not null, question_id char(5) not null); ";
		String sql2 = "create table km_lbs(" +
				"_id integer primary key autoincrement," +
				"address text not null, image_path text not null, name text not null, number text not null, area text); ";
		
		String sql3 = "create table km_baby(" +
				"_id integer primary key autoincrement," +
				"user_name text not null, user_id text not null, name text not null, birth text not null, sex text not null,  image_path text not null); ";
		String sql4 = "create table km_check(" +
				"_id integer primary key autoincrement," +
				"fName text not null, detail_id text, detail_check text, advice_id integer ,question_id text, user_id text not null, question_title text, date text not null,advice_talk text,advice_type text not null); ";
		//km_check에서 user_id즉 아기 아이디 U커런트 밀리초 이아이디를 집어 넣어준다. insert시
		String sql5 = "create table km_image(" +
				"_id integer primary key autoincrement," +
				"fName text not null, checked char(5) not null); ";
		
		try{
			Log.v(TAG,"DB만들어졌어요");
			db.execSQL(sql3);
			db.execSQL(sql4);
			db.execSQL(sql5);
			db.execSQL(sql2);
		}catch(SQLException e){
			Log.v(TAG, "create erroor : " + e );
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TAG, String.format("update oldversion %d newVersion %d "
					, oldVersion,newVersion));
		try{
			db.execSQL("drop table if exists tMember");
			onCreate(db);
		}catch(SQLException e){}

	}

}
