package com.mb.kids_mind.Adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.kids_mind.MainActivity;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Helper.KidsMindDBHelper;
import com.mb.kids_mind.Item.DetailListItem;
import com.mb.kids_mind.Item.KMimageItem;
import com.mb.kids_mind.Item.KidsMindResultItem;
import com.mb.kids_mind.view.TwoWayView;

public class SiteAdapter extends BaseAdapter {
	static final String TAG = "MainActivity";
	SharedPreferences pref;
	SharedPreferences.Editor editor;

	ArrayList<KidsMindResultItem> list;
	List<Integer> list0 ;
	List<Integer> list1 ;
	List<Integer> list2 ;
	List<Integer> list3 ;
	List<Integer> list4 ;
	List<Integer> list5 ;
	List<Integer> list6 ;
	List<Integer> list7 ;
	List<Integer> list8 ;
	
	int numInt;
	int temp;
	DetailListItem ditem;
	SQLiteDatabase db;
	KidsMindDBHelper myhelper;
	Context context;
	int layout;
	boolean flag;
	ArrayList<DetailListItem> dlist;;
	public SiteAdapter(Context context, int layout,
			ArrayList<KidsMindResultItem> list) {
		this.context = context;
		this.layout = layout;
		this.list = list;
		myhelper=new KidsMindDBHelper(context);
		try{
			myhelper.createDataBase();
		}catch(IOException ioe){
			throw new Error("error");
		}
		pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
	}

	public void setList(ArrayList<KidsMindResultItem> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View cView, ViewGroup parent) {
		Holder holder;

		if (cView == null) {
			// Inflate the view since it does not exist
			cView = View.inflate(context, layout, null);

			// Create and save off the holder in the tag so we get quick access
			// to inner fields
			// This must be done for performance reasons
			holder = new Holder();
			holder.title = (TextView) cView.findViewById(R.id.textView1);
			holder.title.setTextColor(0xff888991);
			// holder.siteImage = (ImageView)
			// cView.findViewById(R.id.siteImage);
			holder.hlView = (TwoWayView) cView.findViewById(R.id.list);
			holder.hlView.setItemMargin(10);
			holder.hlView.setLongClickable(true);
			holder.itemList = new ArrayList<DetailListItem>();

			holder.itemAdapter = new SimpleListAdapter(context,
					R.layout.item_view, holder.itemList, list.size());

			// Assign adapter to HorizontalListView
			holder.hlView.setAdapter(holder.itemAdapter);

			cView.setTag(holder);
		} else {

			holder = (Holder) cView.getTag();
		}
		SharedPreferences.Editor editor = pref.edit();
		Log.v(TAG, "position 맞냐" + pos + "");


		KidsMindResultItem site = list.get(pos);
		// Populate the text
		holder.title.setText(site.getTitle());
		// holder.siteImage.setImageResource(site.getImage());
		holder.itemList.clear();
		// ToDo : 아이템 리스트 채우고 다시 데이터를 가져온다.

		switch (pos) {

		case 0:
	//	case 1:
			Log.v(TAG, "포지션 0인데");
			
			String str = pref.getString("list", "");
			//holder.itemAdapter.list0.clear();
			//list0.clear();
		list0 = new ArrayList<Integer>();
			String[] detail = str.split(",");
			for (String cha : detail) {
				Log.v(TAG, "자른아이디0" + cha);
				try {
					numInt = Integer.parseInt(cha);
					list0.add(numInt);
				} catch (Exception e) {
					list0.removeAll(list0);
				}
				

			}


			break;
		case 1:
			Log.v(TAG, "포지션 1인데");
		//case 3:
			String str2 = pref.getString("list2", "");
			//holder.itemAdapter.list0.clear();
			//list0.clear();
		list1 = new ArrayList<Integer>();
			String[] detail2 = str2.split(",");
			for (String cha : detail2) {
				Log.v(TAG, "자른아이디1" + cha);
				try {
					int num;
					num= Integer.parseInt(cha);
					list1.add(num);
				} catch (Exception e) {
					list1.removeAll(list1);
				}
				

			}
			break;
		case 2:
	
			//	case 1:
					Log.v(TAG, "포지션 2인데");
					
					String str3 = pref.getString("list3", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list2 = new ArrayList<Integer>();
					String[] detail3 = str3.split(",");
					for (String cha : detail3) {
						Log.v(TAG, "자른아이디2" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list2.add(numInt);
						} catch (Exception e) {
							list2.removeAll(list2);
						}
						

					}
			break;
		case 3:
		
			//	case 1:
					Log.v(TAG, "포지션 3인데");
					
					String str4 = pref.getString("list4", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list3 = new ArrayList<Integer>();
					String[] detail4 = str4.split(",");
					for (String cha : detail4) {
						Log.v(TAG, "자른아이디3" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list3.add(numInt);
						} catch (Exception e) {
							list3.removeAll(list3);
						}
						

					}
			break;
		case 4:
	
			//	case 1:
					Log.v(TAG, "포지션 4인데");
					
					String str5 = pref.getString("list5", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list4 = new ArrayList<Integer>();
					String[] detail5 = str5.split(",");
					for (String cha : detail5) {
						Log.v(TAG, "자른아이디4" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list4.add(numInt);
						} catch (Exception e) {
							list4.removeAll(list4);
						}
						

					}
			break;
		case 5:
		
			//	case 1:
					Log.v(TAG, "포지션 5인데");
					
					String str6 = pref.getString("list6", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list5 = new ArrayList<Integer>();
					String[] detail6 = str6.split(",");
					for (String cha : detail6) {
						Log.v(TAG, "자른아이디5" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list5.add(numInt);
						} catch (Exception e) {
							list5.removeAll(list5);
						}
						

					}
			break;
		case 6:
		
			//	case 1:
					Log.v(TAG, "포지션 6인데");
					
					String str7 = pref.getString("list7", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list6 = new ArrayList<Integer>();
					String[] detail7 = str7.split(",");
					for (String cha : detail7) {
						Log.v(TAG, "자른아이디6" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list6.add(numInt);
						} catch (Exception e) {
							list6.removeAll(list6);
						}
						

					}
			break;
		case 7:
			//	case 1:
					Log.v(TAG, "포지션7인데");
					
					String str8 = pref.getString("list8", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list7 = new ArrayList<Integer>();
					String[] detail8 = str8.split(",");
					for (String cha : detail8) {
						Log.v(TAG, "자른아이디7" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list7.add(numInt);
						} catch (Exception e) {
							list7.removeAll(list7);
						}
						

					}
			break;
		case 8:
			//	case 1:
					Log.v(TAG, "포지션 8인데");
					
					String str9 = pref.getString("list9", "");
					//holder.itemAdapter.list0.clear();
					//list0.clear();
				list8 = new ArrayList<Integer>();
					String[] detail9 = str9.split(",");
					for (String cha : detail9) {
						Log.v(TAG, "자른아이디7" + cha);
						try {
							numInt = Integer.parseInt(cha);
							list8.add(numInt);
						} catch (Exception e) {
							list8.removeAll(list8);
						}
						

					}
			break;
		
		}


		downloadImages(holder.itemList, pos, flag);
		
		//holder.itemAdapter.setList(holder.itemList);
		holder.itemAdapter.notifyDataSetChanged();
		return cView;
	}

	private class Holder {
		public TextView title;
		// public ImageView siteImage;
		public TwoWayView hlView;
		SimpleListAdapter itemAdapter;
		ArrayList<DetailListItem> itemList;
	}

	void downloadImages(ArrayList<DetailListItem> ilist, int pos, boolean fl) {
		SharedPreferences pref = context.getSharedPreferences("pref",
				context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		KidsMindResultItem site = list.get(pos);
		String tag_id=site.getTag_id();
	Log.v(TAG,"TAGID"+tag_id);
				
		
		switch (pos) {
		case 0:
		if("T01".equals(tag_id)){
			selectTab("T1",ilist,list0,0);
			}else if("T07".equals(tag_id)){
				selectTab("T7",ilist,list0,0);
				}else if("T16".equals(tag_id)){
					selectTab(tag_id,ilist,list0,0);
							}else if("T23".equals(tag_id)){
								selectTab(tag_id,ilist,list0,0);
								}

//			int[] drawables = { R.drawable.box_thumbnail,
//					R.drawable.box_thumbnail, R.drawable.box_thumbnail,
//					R.drawable.box_thumbnail, R.drawable.box_thumbnail,
//					R.drawable.box_thumbnail, R.drawable.box_thumbnail,
//					R.drawable.box_thumbnail };
//			// Log.v(TAG,"두개나와야되"+drawables.length+"");
//
//			// if(checked){
//			for (int i = 0; i < drawables.length; i++) {
//				KMimageItem item = new KMimageItem();
//				String name = "image" + (i + 1);
//				item.setSort(name);
//				item.setPosition(0);
//				// 체크된거 몇번째 포지션인지 매칭해서 true 해준다.
//
//				int size = list0.size();
//				Log.v(TAG, "플릭킹 size" + size + "");
//				if (size != 0) {
//					for (int j = 0; j < list0.size(); j++) {
//
//						Log.v(TAG, "position" + pos + "" + "list0의"
//								+ list0.get(j).intValue() + "");
//						if (i == list0.get(j).intValue()) {
//							item.setFlag(true);
//							break;
//						}
//					}
//
//				}
//
//				item.setImgres(drawables[i]);
//				ilist.add(item);
//			}
			break;
		case 1:
//			int[] drawables2 = { R.drawable.d009, R.drawable.d010,
//					R.drawable.d011, R.drawable.d012, R.drawable.d013,
//					R.drawable.d014, R.drawable.d015, R.drawable.d016 };
//			Log.v(TAG, "두개나와야되" + drawables2.length + "");
//			for (int i = 0; i < drawables2.length; i++) {
//				KMimageItem item = new KMimageItem();
//				String name = "image" + (i + 1);
//				item.setSort(name);
//				item.setPosition(1);
//
//				int size = list1.size();
//				Log.v(TAG, "플릭킹 sizelist1" + size + "");
//				if (size != 0) {
//					for (int j = 0; j < list1.size(); j++) {
//						Log.v(TAG, "position" + pos + "" + "list1의"
//								+ list1.get(j).intValue() + "");
//						if (i == list1.get(j).intValue()) {
//							item.setFlag(true);
//							break;
//						}
//					}
//
//				}
//
//				item.setImgres(drawables2[i]);
//				list.add(item);
//			}
			if("T02".equals(tag_id)){
				selectTab("T2",ilist,list1,1);
				}else if("T08".equals(tag_id)){
					selectTab("T8",ilist,list1,1);
					}else if("T17".equals(tag_id)){
						selectTab(tag_id,ilist,list1,1);
								}else if("T24".equals(tag_id)){
									selectTab(tag_id,ilist,list1,1);
									}
			break;
		case 2:
			if("T03".equals(tag_id)){
				selectTab("T3",ilist,list2,2);
				}else if("T09".equals(tag_id)){
					selectTab("T9",ilist,list2,2);
					}else if("T18".equals(tag_id)){
						selectTab(tag_id,ilist,list2,2);
								}else if("T25".equals(tag_id)){
									selectTab(tag_id,ilist,list2,2);
									}
			break;
		case 3:
			if("T04".equals(tag_id)){
				selectTab("T4",ilist,list3,3);
				}else if("T10".equals(tag_id)){
					selectTab(tag_id,ilist,list3,3);
					}else if("T19".equals(tag_id)){
						selectTab(tag_id,ilist,list3,3);
								}else if("T26".equals(tag_id)){
									selectTab(tag_id,ilist,list3,3);
									}
			break;
		case 4:
			if("T05".equals(tag_id)){
				selectTab("T5",ilist,list4,4);
				}else if("T11".equals(tag_id)){
					selectTab(tag_id,ilist,list4,4);
					}else if("T20".equals(tag_id)){
						selectTab(tag_id,ilist,list4,4);
								}else if("T27".equals(tag_id)){
									selectTab(tag_id,ilist,list4,4);
									}
			break;
		case 5:
			if("T06".equals(tag_id)){
				selectTab("T6",ilist,list5,5);
				}else if("T12".equals(tag_id)){
					selectTab(tag_id,ilist,list5,5);
					}else if("T21".equals(tag_id)){
						selectTab(tag_id,ilist,list5,5);
								}else if("T28".equals(tag_id)){
									selectTab(tag_id,ilist,list5,5);
									}
			break;
		case 6:
			 if("T13".equals(tag_id)){
					selectTab(tag_id,ilist,list6,6);
					}else if("T22".equals(tag_id)){
						selectTab(tag_id,ilist,list6,6);
								}else if("T28".equals(tag_id)){
									selectTab(tag_id,ilist,list6,6);
									}
			break;
		case 7:
			if("T14".equals(tag_id)){
				selectTab(tag_id,ilist,list7,7);
				}
			break;
		case 8:
			if("T15".equals(tag_id)){
				selectTab(tag_id,ilist,list8,8);
				}	
			break;
			
		}

	}

	Bitmap makeThumbnail(Bitmap src, int w, int h) {
		return ThumbnailUtils.extractThumbnail(src, w, h);
	}
	public void selectTab(String tag,ArrayList<DetailListItem> ilist,List<Integer> listinteger,int position ) {
		openDB();
		Log.v(TAG, "탭 디비 시작");

		Cursor c = null;
		String wStr = "tag_id=?";
		String[] wherStr = { tag };
		String[] colNames = { "detail_id", "detail_title", "detail_content",
				"detail_image", "tag_id" };
		try {
			c = db.query("km_question_detail", colNames, wStr, wherStr, null,
					null, null);
			int i=0;
			Log.v(TAG, "숫자:" + c.getCount());
			while (c.moveToNext()) {
				
				ditem = new DetailListItem();
				ditem.setPosition(position);
				ditem.setDetail_id(c.getString(0));
				ditem.setDetail_tilte(c.getString(1));
				// ditem.setDetail_content(c.getString(2));
				ditem.setDetail_image(c.getString(3));
				int size = listinteger.size();

				if(size !=0){
					for(int j=0;j<listinteger.size();j++){
						if (i == listinteger.get(j).intValue()) {
							ditem.setFlag(true);
							break;
						}
					}
				}
				i++;
//				if (size != 0) {
//					for (int j = 0; j < list0.size(); j++) {
//
//								
//						if (i == list0.get(j).intValue()) {
//							ditem.setFlag(true);
//							break;
//						}
//					}
//
//				}

				ditem.setTag_id(c.getString(4));
				ilist.add(ditem);
				Log.v(TAG,
						"인자값은" + c.getString(c.getColumnIndex("detail_title")));
				// Log.v(Debugc.getTaga(), c.getString(0)+ c.getString(1)+
				// c.getString(2)+c.getString(3)+c.getString(4));
				// c.getString(0);
			}

		} catch (SQLException e) {
			Log.v(TAG, "selec error" + e);
		} finally {
			if (c != null) {
				c.close();
			}
		}

	}
	public void openDB() {
		// db = openOrCreateDatabase("sample.db", wi, null);
		db =  myhelper.getWritableDatabase();
	}

	// dbClose();
	public void closeDB() {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	public String writeBitmap(Bitmap bitmap, String saveName) {
		FileOutputStream out = null;
		File f = null;

		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath();

			f = new File(path, saveName);

			out = new FileOutputStream(f);

			bitmap.compress(CompressFormat.JPEG, 90, out);

			Log.w(TAG, "save Success :  " + saveName);

			return f.getAbsolutePath();

		} catch (Exception e) {
			Log.w(TAG, "error : " + e);
			return null;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

}
