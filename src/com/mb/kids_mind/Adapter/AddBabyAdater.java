package com.mb.kids_mind.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mb.kids_mind.R;
import com.mb.kids_mind.RoundedAvatarDrawable;
import com.mb.kids_mind.Item.BabyInformationItem;

public class AddBabyAdater extends BaseAdapter {
	Context context;
	ArrayList<BabyInformationItem> list;
	int layout;
	RoundedAvatarDrawable profile;
	public AddBabyAdater(Context context, int layout,ArrayList<BabyInformationItem> list) {

		this.context = context;
		this.layout = layout;
		this.list=list;
		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public ArrayList<BabyInformationItem> getList() {
		return list;
	}
	public void setList(ArrayList<BabyInformationItem> list) {
		this.list = list;
	}
	@Override
	public Integer getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		ViewHolder holder = null;
		final BabyInformationItem contents=list.get(position);

//		final SharedPreferences pref=mContext.getSharedPreferences("pref",mContext.MODE_PRIVATE);
//		final SharedPreferences.Editor editor=pref.edit();
		if (cView == null) {
			//cView=View.inflate(mContext, layout,null);
			cView=LayoutInflater.from(context).inflate(layout, parent,false);
			holder = new ViewHolder();
			holder.image=(ImageView)cView.findViewById(R.id.imageView1);
			holder.sex=(ImageView)cView.findViewById(R.id.sex);
			holder.name=(TextView)cView.findViewById(R.id.name);
			holder.name.setTextColor(0xff000000);
			cView.setTag(holder);
			//	Log.v(TAG,"cvew==null");
		} else {

			holder = (ViewHolder) cView.getTag();
			//	Log.v(TAG,"cvew!=null");
		}
		holder.name.setText(contents.getName());
		String se=contents.getSex();
		if("boy".equals(se)){
			holder.sex.setImageResource(R.drawable.gender_boy);
		}else if("girl".equals(se)){
			holder.sex.setImageResource(R.drawable.gender_girl);
			
		}
		String image_path=contents.getImage_path();
		readimage(image_path, holder.image);
		//holder.image.setImageBitmap(bm);
		
		
		return cView;
	}
	Bitmap bitmap;
	private static final String TAG="MainActivity";
	void readimage(String path,ImageView img){
		if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

try{
			//DbUse dbuse=new DbUse(MindDrawingResultActivity.this);
			//insertRec2(path2, "0");
			BitmapFactory.Options options =new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			bitmap=BitmapFactory.decodeFile(path,options);
			options =getBitmapSize(options);
			bitmap=BitmapFactory.decodeFile(path,options);
			Log.v(TAG,"이미지를 읽어오기위한 경로2"+path);
			bitmap = getBitmapResizePrc(bitmap, 150, 150);
			profile=new RoundedAvatarDrawable(bitmap);
			img.setImageDrawable(profile);
}catch(OutOfMemoryError e){
	Log.v(TAG,"outofmemoryerror");
}
			//						if(bitmap!=null){
//				Log.v(TAG,"이미지 로딩");
//				img.setImageBitmap(bitmap);
//			}else{
//			}

		}
	}
	public Bitmap getBitmapResizePrc(Bitmap Src, int newHeight, int newWidth) {
		BitmapDrawable result = null;
		int width, height;
		SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		if (Src != null) {
			width = Src.getWidth();
			height = Src.getHeight();
			editor.putInt("width", width);
			editor.putInt("height", height);
			editor.commit();
		} else {
			width = pref.getInt("width", 0);
			height = pref.getInt("height", 0);

		}

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

		// rotate the Bitmap ȸ�� ��Ű���� �ּ� ����!
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(Src, 0, 0, width, height,
				matrix, true);

		// check
		width = resizedBitmap.getWidth();
		height = resizedBitmap.getHeight();

		Log.i("ImageResize",
				"Image Resize Result : "
						+ Boolean.toString((newHeight == height)
								&& (newWidth == width)));

		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever

		return resizedBitmap;
	}
	 public Options getBitmapSize(Options options){ 

	        int targetWidth = 0; 

	        int targetHeight = 0; 

	          

	        if(options.outWidth > options.outHeight){     

	            targetWidth = (int)(600 * 1.3); 

	            targetHeight = 600; 

	        }else{ 

	            targetWidth = 600; 

	            targetHeight = (int)(600 * 1.3); 

	        } 

	  

	        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth); 

	        if(options.outHeight * options.outWidth * 2 >= 16384){ 

	            double sampleSize = scaleByHeight 

	                ? options.outHeight / targetHeight 

	                : options.outWidth / targetWidth; 

	            options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize)/Math.log(2d))); 

	        } 

	        options.inJustDecodeBounds = false; 

	        options.inTempStorage = new byte[16*1024]; 

	          

	        return options; 

	    }
	class ViewHolder {
		public ImageView image;
		public ImageView sex;
		public TextView name;
		
	}
}
