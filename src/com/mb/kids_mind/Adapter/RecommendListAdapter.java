/*
 * Copyright (C) 2012 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mb.kids_mind.Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Adapter.SimilarListAdapter.ViewHolder;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.KidsMindFlagItem;
import com.mb.kids_mind.Item.RecommendItem;


public class RecommendListAdapter extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<RecommendItem> list;
	
	public int temp,si;
	SharedPreferences pref;
	SharedPreferences.Editor editor;


	int layout;
	int size;
	String a=null;
	
	public RecommendListAdapter(Context context, int layout,ArrayList<RecommendItem> list,int size) {

		this.mContext = context;
		this.layout = layout;
		this.list=list;
		this.size=size;
	
		
		pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		//setHashMap();
	}







	public ArrayList<RecommendItem> getList() {
		return list;

	}



	public void setList(ArrayList<RecommendItem> list) {
		
		this.list = list;
	}



	@Override
	public int getCount() {
		return list.size();

	}

	@Override
	public Integer getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View cView, ViewGroup parent) {
		ViewHolder holder = null;
		final RecommendItem contents=list.get(position);

		final SharedPreferences pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		final SharedPreferences.Editor editor=pref.edit();
		if (cView == null) {
			//cView=View.inflate(mContext, layout,null);
			cView=LayoutInflater.from(mContext).inflate(layout, parent,false);
			holder = new ViewHolder();
			cView=LayoutInflater.from(mContext).inflate(layout, parent,false);
			holder = new ViewHolder();
			holder.image=(ImageView)cView.findViewById(R.id.image);
			holder.title=(TextView)cView.findViewById(R.id.title);
			holder.question=(TextView)cView.findViewById(R.id.question);
			holder.date=(TextView)cView.findViewById(R.id.date);
			cView.setTag(holder);
			//	Log.v(TAG,"cvew==null");
		} else {
		
			holder = (ViewHolder) cView.getTag();
			//	Log.v(TAG,"cvew!=null");
		}
	
		holder.image.setImageResource(contents.getRes());
		holder.title.setText(contents.getTitle());
		holder.question.setText(contents.getContents());
		holder.date.setText(contents.getTitle());
		return cView;
	}

	public void doRemoveItem(int size,List<Integer> list,int position){
		if(size!=0){
			//list0=new LinkedList<Integer>();
			for(int i=0;i<size;i++){
				if(list.contains(position)){
					int index=list.indexOf(position);
					list.remove(index);
				}
			}
			Collections.sort(list);
			for(int i=0;i<list.size();i++)
				Log.v(TAG,"삭제후의 값"+list.get(i).intValue()+"");
		}
	}
//	void setHashMap(){
//
//		map.put("d001.png",R.drawable.d001);
//		map.put("d002.png",R.drawable.d002);
//		map.put("d003.png",R.drawable.d003);
//		map.put("d004.png",R.drawable.d004);
//		map.put("d005.png",R.drawable.d005);
//		map.put("d006.png",R.drawable.d006);
//		map.put("d007.png",R.drawable.d007);
//		map.put("d008.png",R.drawable.d008);
//		map.put("d009.png",R.drawable.d009);
//		map.put("d010.png",R.drawable.d010);
//		map.put("d011.png",R.drawable.d011);
//		map.put("d012.png",R.drawable.d012);
//		map.put("d013.png",R.drawable.d013);
//		map.put("d014.png",R.drawable.d014);
//		map.put("d015.png",R.drawable.d015);
//		map.put("d016.png",R.drawable.d016);
//		map.put("d017.png",R.drawable.d017);
//		map.put("d018.png",R.drawable.d018);
//		map.put("d019.png",R.drawable.d019);
//		map.put("d020.png",R.drawable.d020);
//		map.put("d021.png",R.drawable.d021);
//		map.put("d022.png",R.drawable.d022);
//		map.put("d023.png",R.drawable.d023);
//		map.put("d024.png",R.drawable.d024);
//		map.put("d025.png",R.drawable.d025);
//		map.put("d026.png",R.drawable.d026);
//		map.put("d027.png",R.drawable.d027);
//		map.put("d028.png",R.drawable.d028);
//		map.put("d029.png",R.drawable.d029);
//		map.put("d030.png",R.drawable.d030);
//		map.put("d031.png",R.drawable.d031);
//		map.put("d032.png",R.drawable.d032);
//		map.put("d033.png",R.drawable.d033);
//		map.put("d034.png",R.drawable.d034);
//		map.put("d035.png",R.drawable.d035);
//		map.put("d036.png",R.drawable.d036);
//		map.put("d037.png",R.drawable.d037);
//		map.put("d057.png",R.drawable.d057);
//		map.put("d058.png",R.drawable.d058);
//		map.put("d059.png",R.drawable.d059);
//		map.put("d060.png",R.drawable.d060);
//		map.put("d061.png",R.drawable.d061);
//		map.put("d062.png",R.drawable.d062);
//		map.put("d063.png",R.drawable.d063);
//		map.put("d064.png",R.drawable.d064);
//		map.put("d065.png",R.drawable.d065);
//		map.put("d066.png",R.drawable.d066);
//		map.put("d067.png",R.drawable.d067);
//		map.put("d068.png",R.drawable.d068);
//		map.put("d069.png",R.drawable.d069);
//		map.put("d070.png",R.drawable.d070);
//		map.put("d071.png",R.drawable.d071);
//		map.put("d072.png",R.drawable.d072);
//		map.put("d073.png",R.drawable.d073);
//		map.put("d074.png",R.drawable.d074);
//		map.put("d075.png",R.drawable.d075);
//		map.put("d076.png",R.drawable.d076);
//		map.put("d077.png",R.drawable.d077);
//		map.put("d078.png",R.drawable.d078);
//		map.put("d079.png",R.drawable.d079);
//		map.put("d080.png",R.drawable.d080);
//		map.put("d081.png",R.drawable.d081);
//		map.put("d082.png",R.drawable.d082);
//		map.put("d083.png",R.drawable.d083);
//		map.put("d084.png",R.drawable.d084);
//		map.put("d086.png",R.drawable.d086);
//		map.put("d087.png",R.drawable.d087);
//		map.put("d088.png",R.drawable.d088);
//		map.put("d089.png",R.drawable.d089);
//		map.put("d090.png",R.drawable.d090);
//		map.put("d091.png",R.drawable.d091);
//		map.put("d092.png",R.drawable.d092);
//		map.put("d093.png",R.drawable.d093);
//		map.put("d094.png",R.drawable.d094);
//		map.put("d095.png",R.drawable.d095);
//		map.put("d096.png",R.drawable.d096);
//		map.put("d097.png",R.drawable.d097);
//		map.put("d098.png",R.drawable.d098);
//		map.put("d099.png",R.drawable.d099);
//		map.put("d100.png",R.drawable.d100);
//		map.put("d101.png",R.drawable.d101);
//		map.put("d102.png",R.drawable.d102);
//		map.put("d103.png",R.drawable.d103);
//		map.put("d104.png",R.drawable.d104);
//		map.put("d105.png",R.drawable.d105);
//		map.put("d106.png",R.drawable.d106);
//		map.put("d107.png",R.drawable.d107);
//
//
//	}

	public void doSetList(int size,List<Integer> list,int position){
		if(size!=0){
			//list0=new LinkedList<Integer>();

			for(int i=1;i<size+1;i++){
				if(!list.contains(position)){
					list.add(position);

				}


			}	
		}else{
			list.add(position);
		}
		Collections.sort(list);
		for(int i=0;i<list.size();i++)
			Log.v(TAG,"int i"+list.get(i).intValue()+"");
	}

	class ViewHolder {
		public ImageView image;
		public ImageView newmessage;
		public TextView title;
		public TextView question;
		public TextView date;
	}
}
