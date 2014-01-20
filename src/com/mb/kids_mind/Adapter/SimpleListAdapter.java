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
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.KMimageItem;



public class SimpleListAdapter extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<KMimageItem> list;
	int layout;

	public SimpleListAdapter(Context context, int layout,ArrayList<KMimageItem> list) {
		this.mContext = context;
		this.layout = layout;
		this.list=list;
		
	}

	

	public ArrayList<KMimageItem> getList() {
		return list;
		
	}



	public void setList(ArrayList<KMimageItem> list) {
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
	public View getView(int position, View cView, ViewGroup parent) {
	    ViewHolder holder = null;
		final KMimageItem contents=list.get(position);
		
		SharedPreferences pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		final SharedPreferences.Editor editor=pref.edit();
		if (cView == null) {
			//cView=View.inflate(mContext, layout,null);
			cView=LayoutInflater.from(mContext).inflate(layout, parent,false);
			holder = new ViewHolder();
			holder.frame=(FrameLayout)cView.findViewById(R.id.frame);
			holder.title=(Button)cView.findViewById(R.id.checkBox1);
			holder.sort=(TextView)cView.findViewById(R.id.textView1);
			cView.setTag(holder);
		} else {
		    holder = (ViewHolder) cView.getTag();
		}
		Log.v(TAG,"카운트"+list.size()+"");
Log.v(TAG,"시작");
		holder.sort.setText(contents.getSort());
		holder.frame.setBackgroundResource(contents.getImgres());
		holder.title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				contents.setFlag(!contents.isFlag());
				if(contents.isFlag()){
					editor.putBoolean("check", true);
					
					editor.commit();
				//선택되었을시	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				}else{
				editor.putBoolean("check", false);
				editor.commit();                                                                                                                                                                                           
				}
				notifyDataSetChanged();
			}
		});		
		holder.title.setSelected(contents.isFlag());

		return cView;
	}

	class ViewHolder {
		public FrameLayout frame;
	    public Button title;
	    public TextView sort;
	}
}
