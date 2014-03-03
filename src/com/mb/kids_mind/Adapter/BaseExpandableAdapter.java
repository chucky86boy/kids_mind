package com.mb.kids_mind.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.ConsultChildItem;
import com.mb.kids_mind.Item.ConsultItem;


public class BaseExpandableAdapter extends BaseExpandableListAdapter{
	
	private ArrayList<ConsultItem> groupList = null;
	private ArrayList<ArrayList<ConsultChildItem>> childList = null;
	
	private ConsultItem item;
	private ConsultChildItem item2;
	private LayoutInflater inflater = null;
	private ViewHolder holder = null;
	
	public BaseExpandableAdapter(Context c, ArrayList<ConsultItem> groupList,ArrayList<ArrayList<ConsultChildItem>> childlist
			){
		super();
		this.inflater = LayoutInflater.from(c);
		this.groupList = groupList;
		this.childList=childlist;
	}
	
	// 그룹 포지션을 반환한다.
	@Override
	public ConsultItem getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	// 그룹 사이즈를 반환한다.
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	// 그룹 ID를 반환한다.
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 그룹뷰 각각의 ROW 
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		View v = convertView;
		ConsultItem item=groupList.get(groupPosition);
		if(v == null){
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.consultlistadpater, parent, false);
			holder.name = (TextView) v.findViewById(R.id.name);
			holder.image = (ImageView) v.findViewById(R.id.image);
			holder.address=(TextView)v.findViewById(R.id.address);
			holder.title=(TextView)v.findViewById(R.id.title);
			holder.center=(TextView)v.findViewById(R.id.center);
			holder.check=(ImageView)v.findViewById(R.id.checkBox1);
			v.setTag(holder);
		}else{
			holder = (ViewHolder)v.getTag();
		}
		
		// 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
		if(isExpanded){
			holder.check.setBackgroundResource(R.drawable.btn_folding_push);
		}else{
			holder.check.setBackgroundResource(R.drawable.btn_folding);		}
		holder.center.setText(item.getCenter());
		holder.name.setText(item.getName());
		holder.title.setText(item.getTitle());
		holder.address.setText(item.getAddress());
		holder.image.setImageResource(R.drawable.btn_doctor);
		
		return v;
	}
	
	// 차일드뷰를 반환한다.
	@Override
	public ConsultChildItem getChild(int groupPosition, int childPosition) {
		
		return childList.get(childPosition).get(groupPosition);
			}
	
	// 차일드뷰 사이즈를 반환한다.
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	// 차일드뷰 ID를 반환한다.
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
private static final String TAG="MainActivity";
	// 차일드뷰 각각의 ROW
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v = convertView;
		 ConsultChildItem item2=childList.get(0).get(groupPosition);
		 Log.v(TAG,"gPosition"+groupPosition+"child"+childPosition);
		Log.v(TAG,"item"+item2.getText());
			
		if(v == null){
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.consultchild, null);
			holder.child = (TextView) v.findViewById(R.id.contents);
			holder.child.setTextColor(0xfffefefe);
			holder.button=(Button)v.findViewById(R.id.button1);
			v.setTag(holder);
		}else{
			holder = (ViewHolder)v.getTag();
		}
		//for(int i=0;i<groupList.size();i++){
		//if(groupPosition==childPosition){
		Log.v(TAG,"childlist"+childList.size());
		//item2=getChild(groupPosition, childPosition);
		
		holder.child.setText(item2.getText());
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	//	}
		
	//}
		return v;
	}
	@Override
	public boolean hasStableIds() {	return true; }

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
	
	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView title;
		public TextView center;
		public TextView address;
		public ImageView check;
		public TextView child;
		public Button button;
	}

}




