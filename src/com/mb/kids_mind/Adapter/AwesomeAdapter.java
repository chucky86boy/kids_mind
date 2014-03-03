package com.mb.kids_mind.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mb.kids_mind.KidsMindAdviceActivity;
import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.CommentList;

/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 *
 */
/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 * 
 */
public class AwesomeAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CommentList> data;

	public ArrayList<CommentList> getData() {
		return data;
	}

	public void setData(ArrayList<CommentList> data) {
		this.data = data;
	}

	public AwesomeAdapter(Context context, ArrayList<CommentList> data) {
		super();
		this.mContext = context;
		this.data = data;
	}

	@Override
	public int getCount() {
	
		if (data.size() == 0) {
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentList item = data.get(position);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.sms_row, parent, false);
			holder.message = (TextView) convertView
					.findViewById(R.id.message_text);
			
//			holder.more=(Button)convertView.findViewById(R.id.more);
//			holder.more.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.message.setText(item.getComment_text());

		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		// check if it is a status message then remove background, and change
		// text color.
		SharedPreferences pref = mContext.getSharedPreferences("pref",
				mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		int user_id = pref.getInt("user_id", 0);

		/*
		 * if(item.getUser_id()!=user_id) {
		 * holder.message.setBackgroundDrawable(null); lp.gravity =
		 * Gravity.LEFT;
		 * 
		 * //holder.message.setTextColor(R.color.textFieldColor); } else {
		 */
		// Check whether message is mine to show green background and align to
		// right
		if (item.getUser_id() != user_id) {
			holder.message.setBackgroundResource(R.drawable.box_doctor_a);
			lp.gravity = Gravity.CENTER_HORIZONTAL;
//			holder.more.setVisibility(View.VISIBLE);
//			holder.more.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Activity activity=(Activity)mContext;
//					((KidsMindAdviceActivity)mContext).list.setVisibility(View.GONE);
//					((KidsMindAdviceActivity)mContext).bg.setVisibility(View.VISIBLE);
//					
//				}
//			});
		}
		// If not mine then it is from sender to show orange background and
		// align to left
		else {
			holder.message.setBackgroundResource(R.drawable.box_user_q);
			lp.gravity = Gravity.CENTER_HORIZONTAL;
			//holder.more.setVisibility(View.GONE);

		}
		holder.message.setLayoutParams(lp);
		// holder.message.setTextColor(R.color.textColor);

		return convertView;
	}

	private static class ViewHolder {
		TextView message;
		//Button more;
	}

	@Override
	public long getItemId(int position) {
		// Unimplemented, because we aren't using Sqlite.
		return 0;
	}

}
