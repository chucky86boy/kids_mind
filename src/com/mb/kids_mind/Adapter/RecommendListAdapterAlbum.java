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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Adapter.SimilarListAdapter.ViewHolder;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.KidsMindFlagItem;
import com.mb.kids_mind.Item.RecommendItem;


public class RecommendListAdapterAlbum extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<RecommendItem> list;
	
	public int temp,si;
	SharedPreferences pref;
	SharedPreferences.Editor editor;


	int layout;
	int size;
	String a=null;
	
	public RecommendListAdapterAlbum(Context context, int layout,ArrayList<RecommendItem> list,int size) {

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
			
			holder.linear=(LinearLayout)cView.findViewById(R.id.image);
			
			cView.setTag(holder);
			//	Log.v(TAG,"cvew==null");
		} else {
		
			holder = (ViewHolder) cView.getTag();
			//	Log.v(TAG,"cvew!=null");
		}
	
		holder.linear.setBackgroundResource(contents.getRes());
//		if(layout==R.layout.similarlistview){
//	
//		holder.question.setText(contents.getContents());
//		holder.date.setText(contents.getTitle());
//		}
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
//

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
		LinearLayout linear;
	}
}
