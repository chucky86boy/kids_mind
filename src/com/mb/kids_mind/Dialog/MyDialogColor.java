package com.mb.kids_mind.Dialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mb.kids_mind.R;
import com.mb.kids_mind.listener.OnColorSelectedListener;



public class MyDialogColor extends DialogFragment {
	Activity activity;
	//Debugc d=new Debugc();
	public static OnColorSelectedListener listener;
	
	public static final int [] colors = new int[] {
		0xff000000,
		0xffff0000,
		0xffffff00,
		0xff66ffff,
		0xffff0099,
		0xff009900,
		0xffff6600,
		0xff9900ff,
		0xff009966,
		0xffffffff};
	
	
	public void	selectcolor(int color){
		if (MyDialogColor.listener != null) {
		//	Log.v(d.getTaga(),"������ Į��"+color+"");
			MyDialogColor.listener.onColorSelected(color);
		}
	}
		View.OnClickListener bHandler =new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					SharedPreferences pref=activity.getSharedPreferences("pref",activity.MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
				
				switch(v.getId()){
							
			case R.id.first:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
			//	Log.v(d.getTaga(),"ù��° Į��"+colors[0]+"");
				break;
			case R.id.second:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				//Log.v(d.getTaga(),"ù��° Į��"+colors[1]+"");
				break;
			case R.id.third:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			case R.id.forth:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			case R.id.fifth:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			
			case R.id.first1:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			case R.id.second1:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			case R.id.third1:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			case R.id.forth1:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
			case R.id.fifth1:
				selectcolor(((Integer)v.getTag()).intValue());
				editor.putInt("color",((Integer)v.getTag()).intValue());
				editor.commit();
				dismiss();
				break;
	
			}
				
			}
		};
		
		@Override
		public void onAttach(Activity activity) {
			this.activity = activity;
			super.onAttach(activity);
		}
Button first,second,third,forth,fifth,first1,second1,third1,forth1,fifth1;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			//Log.v(d.getTaga(),"���̾�α׽���");
			View v = null;
			v = inflater.inflate(R.layout.color, null);
			getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
			first=(Button)v.findViewById(R.id.first);
			first.setTag(colors[0]);
			first.setOnClickListener(bHandler);
			second=(Button)v.findViewById(R.id.second);
			second.setTag(colors[1]);
			second.setOnClickListener(bHandler);
			third=(Button)v.findViewById(R.id.third);
			third.setTag(colors[2]);
			third.setOnClickListener(bHandler);
			forth=(Button)v.findViewById(R.id.forth);
			forth.setTag(colors[3]);
			forth.setOnClickListener(bHandler);
			fifth=(Button)v.findViewById(R.id.fifth);
			fifth.setTag(colors[4]);
			fifth.setOnClickListener(bHandler);
			first1=(Button)v.findViewById(R.id.first1);
			first1.setTag(colors[5]);
			first1.setOnClickListener(bHandler);
			second1=(Button)v.findViewById(R.id.second1);
			second1.setTag(colors[6]);
			second1.setOnClickListener(bHandler);
			third1=(Button)v.findViewById(R.id.third1);
			third1.setTag(colors[7]);
			third1.setOnClickListener(bHandler);
			forth1=(Button)v.findViewById(R.id.forth1);
			forth1.setTag(colors[8]);
			forth1.setOnClickListener(bHandler);
			fifth1=(Button)v.findViewById(R.id.fifth1);
			fifth1.setTag(colors[9]);
			fifth1.setOnClickListener(bHandler);
			
			
			
			/*DisplayMetrics displayMetrics = new DisplayMetrics();
			 activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			 int deviceWidth = displayMetrics.widthPixels;
			 int deviceHeight =displayMetrics.heightPixels;
			   LayoutParams params = getDialog().getWindow().getAttributes();    
			   params.width = LayoutParams.MATCH_PARENT;  
			   params.height = LayoutParams.MATCH_PARENT; 
			   getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
			   
			*///getDialog().getWindow().setLayout(deviceWidth-100, deviceHeight-500);
			
			return v;
		}
		
		
		 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			 
			super.onPause();
		}

		/*@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data){
		    	super.onActivityResult(requestCode,resultCode,data);
		    	if(resultCode==activity.RESULT_OK){
		    		if(requestCode==1){
		    			Uri currImageURI=data.getData();
		    			Intent intent2=new Intent(activity,MindDrawingResultActivity.class);
		    			intent2.setData(currImageURI);
		    			intent2.putExtra("path", getRealPathFromURI(currImageURI));
		    			startActivity(intent2);
		    			dismiss();
		    		}
		    	}
		    }
		    public String getRealPathFromURI(Uri contentUri){
		    	String[] proj={MediaStore.Images.Media.DATA};
		    	Cursor cursor=activity.managedQuery(contentUri, proj, null, null, null);
		    	int column_index =cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    	cursor.moveToFirst();
		    	return cursor.getString(column_index);
		    }*/
	}

