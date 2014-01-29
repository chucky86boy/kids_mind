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
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.KidsMindFlagItem;


public class SimpleListAdapter extends BaseAdapter {
	private static final String TAG="MainActivity";
	private final Context mContext;
	ArrayList<DetailListItem> list;
	ArrayList<KidsMindFlagItem> checkflag;
	Hashtable<String,Integer>map=new Hashtable<String,Integer>();
	public int temp,si;
	SharedPreferences pref;
	SharedPreferences.Editor editor;


	int layout;
	int size;
	String a=null;
	List<Integer> list0,list1,list2,list3,list4,list5,list6,list7,list8;
	List<String> checklist;
	public SimpleListAdapter(Context context, int layout,ArrayList<DetailListItem> list,int size) {

		this.mContext = context;
		this.layout = layout;
		this.list=list;
		this.size=size;
		checkflag=new ArrayList<KidsMindFlagItem>();
		list0=new ArrayList<Integer>();
		list1=new ArrayList<Integer>();
		list2=new ArrayList<Integer>();
		list3=new ArrayList<Integer>();
		list4=new ArrayList<Integer>();
		list5=new ArrayList<Integer>();
		list6=new ArrayList<Integer>();
		list7=new ArrayList<Integer>();
		list8=new ArrayList<Integer>();
		
		pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		setHashMap();
	}







	public ArrayList<DetailListItem> getList() {
		return list;

	}



	public void setList(ArrayList<DetailListItem> list) {
		Log.v(TAG,"setlist"+list0.size()+"");
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
		final DetailListItem contents=list.get(position);

		final SharedPreferences pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
		final SharedPreferences.Editor editor=pref.edit();
		if (cView == null) {
			//cView=View.inflate(mContext, layout,null);
			cView=LayoutInflater.from(mContext).inflate(layout, parent,false);
			holder = new ViewHolder();
			holder.frame=(FrameLayout)cView.findViewById(R.id.frame);
			holder.title=(Button)cView.findViewById(R.id.checkBox1);
			holder.sort=(TextView)cView.findViewById(R.id.textView1);
			holder.sort.setTextColor(0xff000000);
			holder.image=(ImageView)cView.findViewById(R.id.image);
			cView.setTag(holder);
			//	Log.v(TAG,"cvew==null");
		} else {

			holder = (ViewHolder) cView.getTag();
			//	Log.v(TAG,"cvew!=null");
		}
		if(contents.isFlag()){
			holder.sort.setTextColor(0xffffffff);
			//holder.frame.setBackgroundColor(0xff000080);
		}else{
			holder.sort.setTextColor(0xff000000);
			//holder.frame.setBackgroundColor(0xffffffff);
		}

		holder.sort.setText(contents.getDetail_tilte());
		String image = contents.getDetail_image();
		//String ImageUrl = Const.QUESTION_IMAGE_PATH;
		Integer key=map.get(image);
		if(key!=null){
			 holder.image.setBackgroundResource((int)key);
			//holder.frame.setBackgroundResource((int)key);

		}else
			holder.image.setBackgroundResource(R.drawable.btn_doctor);
			//holder.frame.setBackgroundResource(R.drawable.box_thumbnail);
		//		holder.frame.setBackgroundResource(contents.getImgres());


		holder.title.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View view) {
				contents.setFlag(!contents.isFlag());
				if(contents.isFlag()){
					//먼저 불러와서 시작
					String checkedlist = pref.getString("checked", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				checklist = new ArrayList<String>();
					String[] detail = checkedlist.split(",");
					for (String cha : detail) {
						Log.v(TAG, "자른아이디0" + cha);
						try {
							
							checklist.add(cha);
						} catch (Exception e) {
							checklist.removeAll(checklist);
						}
						

					}
					
					
					
					
					int checksize=checklist.size();
					
					
					if(checksize!=0){
						for(int i=1;i<size+1;i++){
							if(!checklist.contains(contents.getDetail_id())){
								checklist.add(contents.getDetail_id());

							}


						}	
					}else{
						checklist.add(contents.getDetail_id());
					}

					String checked = "";
					for (int i = 0; i < checklist.size(); i++) {
						Log.v(TAG, "체크된 리스트 봅아준다" + checklist.get(i));
						checked += checklist.get(i) + ",";
					}
					Log.v(TAG, "checked" + checked);
					editor.putString("checked", checked);
					editor.commit();

					switch (contents.getPosition()){
					case 0:

						int size =list0.size();
						Log.v(TAG,"size"+size+"");
						//doSetList(size,list0,position);
						if(size!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size+1;i++){
								if(!list0.contains(position)){
									list0.add(position);

								}


							}	
						}else{
							list0.add(position);
						}
						Collections.sort(list0);
						//				else {
						String str = "";
						for (int i = 0; i < list0.size(); i++) {
							Log.v(TAG, "평상시" + list0.get(i).intValue() + "");
							str += list0.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str);
						editor.putString("list", str);
						editor.commit();
						//			}



						break;

					case 1:

						int size2 =list1.size();
						Log.v(TAG,"size"+size2+"");
						//doSetList(size,list0,position);
						if(size2!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size2+1;i++){
								if(!list1.contains(position)){
									list1.add(position);

								}


							}	
						}else{
							list1.add(position);
						}
						Collections.sort(list1);
						//				else {
						String str2 = "";
						for (int i = 0; i < list1.size(); i++) {
							Log.v(TAG, "평상시" + list1.get(i).intValue() + "");
							str2 += list1.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str2);
						editor.putString("list2", str2);
						editor.commit();
						//			}

						break;
					case 2:

						int size3 =list2.size();
						Log.v(TAG,"size"+size3+"");
						//doSetList(size,list0,position);
						if(size3!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size3+1;i++){
								if(!list2.contains(position)){
									list2.add(position);

								}


							}	
						}else{
							list2.add(position);
						}
						Collections.sort(list2);
						//				else {
						String str3 = "";
						for (int i = 0; i < list2.size(); i++) {
							Log.v(TAG, "평상시" + list2.get(i).intValue() + "");
							str3 += list2.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str3);
						editor.putString("list3", str3);
						editor.commit();
						//			}


						break;

					case 3:
						int size4 =list3.size();
						Log.v(TAG,"size"+size4+"");
						//doSetList(size,list0,position);
						if(size4!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size4+1;i++){
								if(!list3.contains(position)){
									list3.add(position);

								}


							}	
						}else{
							list3.add(position);
						}
						Collections.sort(list3);
						//				else {
						String str4 = "";
						for (int i = 0; i < list3.size(); i++) {
							Log.v(TAG, "평상시" + list3.get(i).intValue() + "");
							str4 += list3.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str4);
						editor.putString("list4", str4);
						editor.commit();
						//			}


						break;
					case 4:
						int size5 =list4.size();
						Log.v(TAG,"size"+size5+"");
						//doSetList(size,list0,position);
						if(size5!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size5+1;i++){
								if(!list4.contains(position)){
									list4.add(position);

								}


							}	
						}else{
							list4.add(position);
						}
						Collections.sort(list4);
						//				else {
						String str5 = "";
						for (int i = 0; i < list4.size(); i++) {
							Log.v(TAG, "평상시" + list4.get(i).intValue() + "");
							str5 += list4.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str5);
						editor.putString("list5", str5);
						editor.commit();
						//			}


						break;
					case 5:
						int size6 =list5.size();
						Log.v(TAG,"size"+size6+"");
						//doSetList(size,list0,position);
						if(size6!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size6+1;i++){
								if(!list5.contains(position)){
									list5.add(position);

								}


							}	
						}else{
							list5.add(position);
						}
						Collections.sort(list5);
						//				else {
						String str6 = "";
						for (int i = 0; i < list5.size(); i++) {
							Log.v(TAG, "평상시" + list5.get(i).intValue() + "");
							str6 += list5.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str6);
						editor.putString("list6", str6);
						editor.commit();
						//			}


						break;
					case 6:
						int size7 =list6.size();
						Log.v(TAG,"size"+size7+"");
						//doSetList(size,list0,position);
						if(size7!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size7+1;i++){
								if(!list6.contains(position)){
									list6.add(position);

								}


							}	
						}else{
							list6.add(position);
						}
						Collections.sort(list6);
						//				else {
						String str7 = "";
						for (int i = 0; i < list6.size(); i++) {
							Log.v(TAG, "평상시" + list6.get(i).intValue() + "");
							str7 += list6.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str7);
						editor.putString("list7", str7);
						editor.commit();
						//			}


						break;
					case 7:
						int size8 =list7.size();
						Log.v(TAG,"size"+size8+"");
						//doSetList(size,list0,position);
						if(size8!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size8+1;i++){
								if(!list7.contains(position)){
									list7.add(position);

								}


							}	
						}else{
							list7.add(position);
						}
						Collections.sort(list7);
						//				else {
						String str8 = "";
						for (int i = 0; i < list7.size(); i++) {
							Log.v(TAG, "평상시" + list7.get(i).intValue() + "");
							str8 += list7.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str8);
						editor.putString("list8", str8);
						editor.commit();
						//			}

						break;
					case 8:
						int size9 =list8.size();
						Log.v(TAG,"size"+size9+"");
						//doSetList(size,list0,position);
						if(size9!=0){
							//list0=new LinkedList<Integer>();

							for(int i=1;i<size9+1;i++){
								if(!list8.contains(position)){
									list8.add(position);

								}


							}	
						}else{
							list8.add(position);
						}
						Collections.sort(list8);
						//				else {
						String str9 = "";
						for (int i = 0; i < list8.size(); i++) {
							Log.v(TAG, "평상시" + list8.get(i).intValue() + "");
							str9 += list8.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str9);
						editor.putString("list9", str9);
						editor.commit();
						//			}


						break;

					}


					//선택되었을시	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				}else{
					//먼저 불러와서 시작
					String checkedlist = pref.getString("checked", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				checklist = new ArrayList<String>();
					String[] detail = checkedlist.split(",");
					for (String cha : detail) {
						Log.v(TAG, "자른아이디0" + cha);
						try {
							
							checklist.add(cha);
						} catch (Exception e) {
							checklist.removeAll(checklist);
						}
						

					}
					int checksize =checklist.size();
					Log.v(TAG,"삭제후"+checksize+"");
					if(checksize!=0){
						//list0=new LinkedList<Integer>();
						for(int i=0;i<checksize;i++){
							if(checklist.contains(contents.getDetail_id())){
								int index=checklist.indexOf(contents.getDetail_id());
								checklist.remove(index);
							}
						}
						String checked = "";
						for (int i = 0; i < checklist.size(); i++) {
							checked += checklist.get(i) + ",";
						}
						Log.v(TAG, "strstr" + checked);
						editor.putString("checked", checked);
						editor.commit();
					}
					switch (contents.getPosition()){
					case 0:

						int size =list0.size();
						Log.v(TAG,"size2222"+size+"");

						doRemoveItem(size,list0,position);

						String str = "";
						for (int i = 0; i < list0.size(); i++) {
							
							str += list0.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr" + str);
						editor.putString("list", str);
						editor.commit();


						//								int size2=list0.size();
						//								for(int i=0;i<size2;i++){
						//									Log.v(TAG,"삭제된후에 list0"+list0.get(i).intValue()+"");
						//								}

						break;

					case 1:
						int size2 =list1.size();
						Log.v(TAG,"size2222"+size2+"");

						doRemoveItem(size2,list1,position);
						String str2 = "";
						for (int i = 0; i < list1.size(); i++) {
							
							str2 += list1.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str2);
						editor.putString("list2", str2);
						editor.commit();



						break;
					case 2:
						int size3 =list2.size();
						Log.v(TAG,"size2222"+size3+"");

						doRemoveItem(size3,list2,position);
						String str3 = "";
						for (int i = 0; i < list2.size(); i++) {
						
							str3 += list2.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str3);
						editor.putString("list3", str3);
						editor.commit();


						break;
					case 3:
						int size4 =list3.size();
						Log.v(TAG,"size2222"+size4+"");

						doRemoveItem(size4,list3,position);
						String str4 = "";
						for (int i = 0; i < list3.size(); i++) {
							
							str4 += list3.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str4);
						editor.putString("list4", str4);
						editor.commit();

						break;
					case 4:
						int size5 =list4.size();
						Log.v(TAG,"size2222"+size5+"");

						doRemoveItem(size5,list4,position);
						String str5 = "";
						for (int i = 0; i < list4.size(); i++) {
							
							str5 += list4.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str5);
						editor.putString("list5", str5);
						editor.commit();

						break;
					case 5:
						int size6 =list5.size();

						doRemoveItem(size6,list5,position);
						String str6 = "";
						for (int i = 0; i < list5.size(); i++) {
							
							str6 += list5.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str6);
						editor.putString("list6", str6);
						editor.commit();

						break;
					case 6:
						int size7 =list6.size();
						Log.v(TAG,"size2222"+size7+"");

						doRemoveItem(size7,list6,position);
						String str7 = "";
						for (int i = 0; i < list6.size(); i++) {
							
							str7 += list6.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str7);
						editor.putString("list7", str7);
						editor.commit();

						break;
					case 7:
						int size8 =list7.size();
						Log.v(TAG,"size2222"+size8+"");

						doRemoveItem(size8,list7,position);
						String str8 = "";
						for (int i = 0; i < list7.size(); i++) {
						
							str8 += list7.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str8);
						editor.putString("list8", str8);
						editor.commit();

						break;
					case 8:
						int size9 =list8.size();
						Log.v(TAG,"size2222"+size9+"");

						doRemoveItem(size9,list8,position);
						String str9 = "";
						for (int i = 0; i < list8.size(); i++) {
							
							str9 += list8.get(i).intValue() + "" + ",";
						}
						Log.v(TAG, "strstr2" + str9);
						editor.putString("list9", str9);
						editor.commit();

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
			for(int i=0;i<list.size();i++)
				Log.v(TAG,"삭제후의 값"+list.get(i).intValue()+"");
		}
	}
	void setHashMap(){

		map.put("d001.png",R.drawable.d001);
		map.put("d002.png",R.drawable.d002);
		map.put("d003.png",R.drawable.d003);
		map.put("d004.png",R.drawable.d004);
		map.put("d005.png",R.drawable.d005);
		map.put("d006.png",R.drawable.d006);
		map.put("d007.png",R.drawable.d007);
		map.put("d008.png",R.drawable.d008);
		map.put("d009.png",R.drawable.d009);
		map.put("d010.png",R.drawable.d010);
		map.put("d011.png",R.drawable.d011);
		map.put("d012.png",R.drawable.d012);
		map.put("d013.png",R.drawable.d013);
		map.put("d014.png",R.drawable.d014);
		map.put("d015.png",R.drawable.d015);
		map.put("d016.png",R.drawable.d016);
		map.put("d017.png",R.drawable.d017);
		map.put("d018.png",R.drawable.d018);
		map.put("d019.png",R.drawable.d019);
		map.put("d020.png",R.drawable.d020);
		map.put("d021.png",R.drawable.d021);
		map.put("d022.png",R.drawable.d022);
		map.put("d023.png",R.drawable.d023);
		map.put("d024.png",R.drawable.d024);
		map.put("d025.png",R.drawable.d025);
		map.put("d026.png",R.drawable.d026);
		map.put("d027.png",R.drawable.d027);
		map.put("d028.png",R.drawable.d028);
		map.put("d029.png",R.drawable.d029);
		map.put("d030.png",R.drawable.d030);
		map.put("d031.png",R.drawable.d031);
		map.put("d032.png",R.drawable.d032);
		map.put("d033.png",R.drawable.d033);
		map.put("d034.png",R.drawable.d034);
		map.put("d035.png",R.drawable.d035);
		map.put("d036.png",R.drawable.d036);
		map.put("d037.png",R.drawable.d037);
		map.put("d057.png",R.drawable.d057);
		map.put("d058.png",R.drawable.d058);
		map.put("d059.png",R.drawable.d059);
		map.put("d060.png",R.drawable.d060);
		map.put("d061.png",R.drawable.d061);
		map.put("d062.png",R.drawable.d062);
		map.put("d063.png",R.drawable.d063);
		map.put("d064.png",R.drawable.d064);
		map.put("d065.png",R.drawable.d065);
		map.put("d066.png",R.drawable.d066);
		map.put("d067.png",R.drawable.d067);
		map.put("d068.png",R.drawable.d068);
		map.put("d069.png",R.drawable.d069);
		map.put("d070.png",R.drawable.d070);
		map.put("d071.png",R.drawable.d071);
		map.put("d072.png",R.drawable.d072);
		map.put("d073.png",R.drawable.d073);
		map.put("d074.png",R.drawable.d074);
		map.put("d075.png",R.drawable.d075);
		map.put("d076.png",R.drawable.d076);
		map.put("d077.png",R.drawable.d077);
		map.put("d078.png",R.drawable.d078);
		map.put("d079.png",R.drawable.d079);
		map.put("d080.png",R.drawable.d080);
		map.put("d081.png",R.drawable.d081);
		map.put("d082.png",R.drawable.d082);
		map.put("d083.png",R.drawable.d083);
		map.put("d084.png",R.drawable.d084);
		map.put("d086.png",R.drawable.d086);
		map.put("d087.png",R.drawable.d087);
		map.put("d088.png",R.drawable.d088);
		map.put("d089.png",R.drawable.d089);
		map.put("d090.png",R.drawable.d090);
		map.put("d091.png",R.drawable.d091);
		map.put("d092.png",R.drawable.d092);
		map.put("d093.png",R.drawable.d093);
		map.put("d094.png",R.drawable.d094);
		map.put("d095.png",R.drawable.d095);
		map.put("d096.png",R.drawable.d096);
		map.put("d097.png",R.drawable.d097);
		map.put("d098.png",R.drawable.d098);
		map.put("d099.png",R.drawable.d099);
		map.put("d100.png",R.drawable.d100);
		map.put("d101.png",R.drawable.d101);
		map.put("d102.png",R.drawable.d102);
		map.put("d103.png",R.drawable.d103);
		map.put("d104.png",R.drawable.d104);
		map.put("d105.png",R.drawable.d105);
		map.put("d106.png",R.drawable.d106);
		map.put("d107.png",R.drawable.d107);


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

	class ViewHolder {
		public ImageView image;
		public FrameLayout frame;
		public Button title;
		public TextView sort;
	}
}
