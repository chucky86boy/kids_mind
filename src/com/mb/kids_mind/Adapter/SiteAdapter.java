package com.mb.kids_mind.Adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.kids_mind.R;
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
	int numInt;
	int temp;
	Context context;
	int layout;
	boolean flag;

	public SiteAdapter(Context context, int layout,
			ArrayList<KidsMindResultItem> list) {
		this.context = context;
		this.layout = layout;
		this.list = list;
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
			// holder.siteImage = (ImageView)
			// cView.findViewById(R.id.siteImage);
			holder.hlView = (TwoWayView) cView.findViewById(R.id.list);
			holder.hlView.setItemMargin(10);
			holder.hlView.setLongClickable(true);
			holder.itemList = new ArrayList<KMimageItem>();

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
				Log.v(TAG, "자른아이디" + cha);
				try {
					numInt = Integer.parseInt(cha);
					list0.add(numInt);
				} catch (Exception e) {
					list0.removeAll(list0);
				}
				

			}

//			Log.v(TAG,
//					"holder에서 불러와 사이즈가 얼마냐" + holder.itemAdapter.list0.size()
//					+ "");
//
//			int abs = pref.getInt("abs", 0);
//			Log.v(TAG,"abs"+abs+"");
//			if (abs <= 1) {
//				if (holder.itemAdapter.list0.size() != 0) {
//					for (int i = 0; i < holder.itemAdapter.list0.size(); i++) {
//						if (!list0.contains(holder.itemAdapter.list0.get(i))) {
//							list0.add(holder.itemAdapter.list0.get(i));
//
//						}
//						Log.v(TAG, "어댑터에서 불러와" + list0.get(i).intValue() + "");
//					}
//				}
//			} 
			break;
		case 1:
		//case 3:
			String str2 = pref.getString("list2", "");
			//holder.itemAdapter.list0.clear();
			//list0.clear();
		list1 = new ArrayList<Integer>();
			String[] detail2 = str2.split(",");
			for (String cha : detail2) {
				Log.v(TAG, "자른아이디" + cha);
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
		//case 5:


			for (int i = 0; i < holder.itemAdapter.list2.size(); i++) {
				if (!list2.contains(holder.itemAdapter.list2.get(i))) {
					list2.add(holder.itemAdapter.list2.get(i));

				}
			}
			break;
		default:
			Log.v(TAG, "들어오기는 하니");
		}

//		
//
//		temp = pref.getInt("temp", 0);
//
//		Log.v(TAG, "siteAdapter의 리스트의 크기" + list0.size() + "" + "temp" + temp
//				+ "");
//		if (temp > 1) {
//			Log.v(TAG, " 리스트의 크기" + list0.size() + "" + "temp" + temp + "");
//			Log.v(TAG, "절대값" + Math.abs(temp - list0.size()) + "" + "temp"
//					+ temp + "");
//			if (Math.abs(temp - list0.size()) > 1) {
//				editor.putInt("abs", Math.abs(temp - list0.size()));
//				editor.commit();
//				Log.v(TAG, "절대값" + "" + "temp" + temp + "");
//
//			} 
////			else {
////			String str = "";
////			for (int i = 0; i < list0.size(); i++) {
////				Log.v(TAG, "평상시" + list0.get(i).intValue() + "");
////				str += list0.get(i).intValue() + "" + ",";
////			}
////			Log.v(TAG, "strstr" + str);
////			editor.putString("list", str);
////			editor.commit();
////		}
//
//		// for(int i=0;i<list0.size();i++){
//		// if(!tempList.contains(list0.get(i))){
//		// tempList.add(list0
//		// .get(i));
//		//
//		// }
//		// // Log.v(TAG,"어댑터에서 불러와"+list0.get(i).intValue()+"");
//		// }
//		//
//		}
//		temp = list0.size();
//		editor = pref.edit();
//		editor.putInt("temp", temp);
//		editor.commit();
//		
//		// holder.itemAdapter.setList(holder.itemList);
//		int abs = pref.getInt("abs", 0);
//		Log.v(TAG,"abs"+abs+"");
//		//if (abs <= 1) {
		downloadImages(holder.itemList, pos, flag);
		

		holder.itemAdapter.notifyDataSetChanged();
		return cView;
	}

	private class Holder {
		public TextView title;
		// public ImageView siteImage;
		public TwoWayView hlView;
		SimpleListAdapter itemAdapter;
		ArrayList<KMimageItem> itemList;
	}

	void downloadImages(ArrayList<KMimageItem> list, int pos, boolean fl) {
		SharedPreferences pref = context.getSharedPreferences("pref",
				context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		switch (pos) {
		case 0:

			Boolean checked = pref.getBoolean("checked", false);
			int[] drawables = { R.drawable.box_thumbnail,
					R.drawable.box_thumbnail, R.drawable.box_thumbnail,
					R.drawable.box_thumbnail, R.drawable.box_thumbnail,
					R.drawable.box_thumbnail, R.drawable.box_thumbnail,
					R.drawable.box_thumbnail };
			// Log.v(TAG,"두개나와야되"+drawables.length+"");

			// if(checked){
			for (int i = 0; i < drawables.length; i++) {
				KMimageItem item = new KMimageItem();
				String name = "image" + (i + 1);
				item.setSort(name);
				item.setPosition(0);
				// 체크된거 몇번째 포지션인지 매칭해서 true 해준다.

				int size = list0.size();
				Log.v(TAG, "플릭킹 size" + size + "");
				if (size != 0) {
					for (int j = 0; j < list0.size(); j++) {

						Log.v(TAG, "position" + pos + "" + "list0의"
								+ list0.get(j).intValue() + "");
						if (i == list0.get(j).intValue()) {
							item.setFlag(true);
							break;
						}
					}

				}

				item.setImgres(drawables[i]);
				list.add(item);
			}
			break;
		case 1:
			int[] drawables2 = { R.drawable.d009, R.drawable.d010,
					R.drawable.d011, R.drawable.d012, R.drawable.d013,
					R.drawable.d014, R.drawable.d015, R.drawable.d016 };
			Log.v(TAG, "두개나와야되" + drawables2.length + "");
			for (int i = 0; i < drawables2.length; i++) {
				KMimageItem item = new KMimageItem();
				String name = "image" + (i + 1);
				item.setSort(name);
				item.setPosition(1);

				int size = list1.size();
				Log.v(TAG, "플릭킹 sizelist1" + size + "");
				if (size != 0) {
					for (int j = 0; j < list1.size(); j++) {
						Log.v(TAG, "position" + pos + "" + "list1의"
								+ list1.get(j).intValue() + "");
						if (i == list1.get(j).intValue()) {
							item.setFlag(true);
							break;
						}
					}

				}

				item.setImgres(drawables2[i]);
				list.add(item);
			}
			break;

		}

	}

	Bitmap makeThumbnail(Bitmap src, int w, int h) {
		return ThumbnailUtils.extractThumbnail(src, w, h);
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
