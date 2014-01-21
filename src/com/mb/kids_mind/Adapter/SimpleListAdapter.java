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
import java.util.LinkedList;
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
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.KMimageItem;
import com.mb.kids_mind.Item.KidsMindFlagItem;
import com.mb.kids_mind.listener.KidsMindCheckListener;
import com.mb.kids_mind.listener.KidsMindflickingListener;



public class SimpleListAdapter extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<KMimageItem> list;
	ArrayList<KidsMindFlagItem> checkflag;

	public static KidsMindCheckListener checklistener;
	int layout;
	int size;
	String a=null;
	List<Integer> list0=new LinkedList<Integer>();
	List<Integer> list1=new LinkedList<Integer>();
	List<Integer> list2=new LinkedList<Integer>();

	LinkedList<Integer> link0=new LinkedList<Integer>();
			LinkedList<Integer> link1=new LinkedList<Integer>();
					LinkedList<Integer> link2=new LinkedList<Integer>();
					
			public SimpleListAdapter(Context context, int layout,ArrayList<KMimageItem> list,int size) {
		this.mContext = context;
		this.layout = layout;
		this.list=list;
		this.size=size;
		checkflag=new ArrayList<KidsMindFlagItem>();

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
	public View getView(final int position, View cView, ViewGroup parent) {
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
		//	Log.v(TAG,"cvew==null");
		} else {
			
			holder = (ViewHolder) cView.getTag();
		//	Log.v(TAG,"cvew!=null");
		}
		//Log.v(TAG,"카운트"+list.size()+"");
	//	Log.v(TAG,"시작"+contents.getPosition()+"");
		holder.sort.setText(contents.getSort());
		
		holder.frame.setBackgroundResource(contents.getImgres());
		SiteAdapter.flicListener=new KidsMindflickingListener() {
			
			@Override
			public void onIitemSelected() {
				switch(contents.getPosition()){
				case 0:
					selectItem(list0,0);
					
					break;
				case 1:
					selectItem(list1,1);
					
					break;
				case 2:
					selectItem(list2,2);
					
					break;
//				case 3:
//					selectItem(link0,3);
//					
			//		break;
				}
				}
				
			
		};
		holder.title.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View view) {
				contents.setFlag(!contents.isFlag());
				if(contents.isFlag()){
			
					switch (contents.getPosition()){
				case 0:
								
					 int size =list0.size();
						Log.v(TAG,"size"+size+"");
						doSetList(size,list0,position);
						

						
					 
					 
					break;
					
				case 1:
					
						 int size2 =list1.size();
					Log.v(TAG,"size"+size2+"");
					doSetList(size2,list1,position);
			
						
					break;
				case 2:
					 int size3 =list2.size();
						Log.v(TAG,"size"+size3+"");
						doSetList(size3,list2,position);
				
							
					break;
					
				}
					

					//선택되었을시	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				}else{
					switch (contents.getPosition()){
					case 0:
						 
						 int size =list0.size();
							Log.v(TAG,"size2222"+size+"");
					
							doRemoveItem(size,list0,position);
						
						
							
								int size2=list0.size();
								for(int i=0;i<size2;i++){
									Log.v(TAG,"삭제된후에 list0"+list0.get(i).intValue()+"");
								}
							
						break;
						
					case 1:
						 int size3 =list1.size();
							Log.v(TAG,"size2222"+size3+"");
					
							doRemoveItem(size3,list1,position);
														
						break;
					case 2:
						 int size4 =list2.size();
							Log.v(TAG,"size2222"+size4+"");
					
							doRemoveItem(size4,list2,position);
																				
						break;
						
					}	
				}
				notifyDataSetChanged();
			}
		});		
		holder.title.setSelected(contents.isFlag());
	
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
		}
	}
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
	public void	selectItem(List<Integer>link,int position){
    	if (SimpleListAdapter.checklistener != null) {
    		SimpleListAdapter.checklistener.onIitemSelected(link,position);
    	}
    }
	class ViewHolder {
		public FrameLayout frame;
		public Button title;
		public TextView sort;
	}
}
