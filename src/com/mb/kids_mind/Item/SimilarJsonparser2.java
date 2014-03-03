package com.mb.kids_mind.Item;

	import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;





public class SimilarJsonparser2 {

	private static final String TAG = "MainActivity";
	
	
	public static class SimilarJsonparser {
			private static final String TAG = "MainActivity";
				public static SItem2 parse(String data)
					{
					Log.v(Debugc.getTagd(),"data"+data);
				
					SItem2 list = null;
				JSONObject object = null;
				JSONArray jsimilar=null;
				try {
					object = new JSONObject(data);
				
					list=new SItem2();
					
					
					
					jsimilar = object.getJSONArray("similarlist");
					
					ArrayList<SimilarItem> cList = list.getData();
					SimilarItem similaritem;
					JSONObject jtemp = null;
					for(int i=0;i<jsimilar.length();i++){
						similaritem = new SimilarItem();
						jtemp = jsimilar.getJSONObject(i);
						similaritem.setDate(jtemp.getString("advice_date"));
						similaritem.setQuestion_id(jtemp.getString("question_id"));
						similaritem.setAdvice_id(jtemp.getInt("user_id"));
						similaritem.setAdvice_user_id(jtemp.getInt("advice_user_id"));
						similaritem.setAdvice_image(jtemp.getString("adivce_image"));
						similaritem.setDetail_id(jtemp.getString("detail_id"));
						similaritem.setComment_text(jtemp.getString("comment_text"));
						cList.add(similaritem);
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



