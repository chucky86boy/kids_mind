package com.mb.kids_mind.fragment;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.Item.DetailListItem;

public class SingleResultSketchMenu extends Fragment{
	ArrayList<DetailListItem> dlist;
	Hashtable<String,Integer>map=new Hashtable<String,Integer>();
	
	int[] menuImage={R.drawable.d001,R.drawable.d002,R.drawable.d004,R.drawable.d003};
	private int position;
	Activity activity;
	
	FragmentManager fm;
	//MyDialog dialog=new MyDialog();
	public void setPosition(int position) {
		this.position = position;
	}
	public void setData(ArrayList<DetailListItem>list){
		this.dlist=list;
	}
	 @Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.result_sketch, null);
		activity.getFragmentManager();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		ImageView imageView = (ImageView) view.findViewById(R.id.singeMenu);
		TextView title=(TextView)view.findViewById(R.id.resulttitle);
		
		setHashMap();
		DetailListItem item=dlist.get(position);
		title.setText(item.getDetail_tilte());
		String image = item.getDetail_image();
		//String ImageUrl = Const.QUESTION_IMAGE_PATH;
		Integer key=map.get(image);
		
		LayoutParams layoutParams = imageView.getLayoutParams();
		//layoutParams.height = (int)(size.y * 0.7);
		if(key!=null)
		imageView.setImageResource(key);
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:

					//doAction(v);
					break;
				case MotionEvent.ACTION_UP:
					switch (position)
					{
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					
					break;
				
			}
				return false;
			}
		});
		return view;
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
	
}
