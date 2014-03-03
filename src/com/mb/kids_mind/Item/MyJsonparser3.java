package com.mb.kids_mind.Item;

	import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;





public class MyJsonparser3 {

		private static final String TAG = "MainActivity";
		
		
		public static class MyJsonParser3 {
			private static final String TAG = "MainActivity";
				public static AdviceItem2 parse(String data)
					{
					Log.v(Debugc.getTagd(),"data"+data);
				
				AdviceItem2 list = null;
				JSONObject object = null;
				JSONArray jcomment=null;
				try {
					object = new JSONObject(data);
				
					list=new AdviceItem2();
					list.setResult(object.getString("result"));
					
					try{
					//if(object.getString("advice_user_name")!=null)
					list.setAdvice_user_name(object.getString("advice_user_name"));
					}catch(JSONException e){
						Log.v(TAG,"존재하지 않습니다");
					}
					list.setUser_name(object.getString("user_name"));
					
					list.setAdvice_date(object.getString("advice_date"));
				
					jcomment = object.getJSONArray("commentlist");
					
					ArrayList<CommentList> cList = list.getData();
					CommentList comment;
					JSONObject jtemp = null;
					for(int i=0;i<jcomment.length();i++){
						comment = new CommentList();
						jtemp = jcomment.getJSONObject(i);
						comment.setUser_id(jtemp.getInt("user_id"));
						comment.setComment_text(jtemp.getString("comment_text"));
						comment.setComment_date(jtemp.getString("comment_date"));
						
						cList.add(comment);
					}				

				} catch (Exception e) {
					Log.v(TAG, "json parse error : " + e);
					// TODO: handle exception
				}finally{
					
				}
				return list;
			}
		}
}



