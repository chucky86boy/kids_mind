package com.mb.kids_mind;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.mb.kids_mind.Dialog.MyDialogColor;
import com.mb.kids_mind.fragment.SingleResultSketchMenu;
import com.mb.kids_mind.fragment.SingleSketchMenu;
import com.mb.kids_mind.listener.OnColorSelectedListener;
import com.mb.kids_mind.listener.PageChagedListener;
import com.mb.kids_mind.view.PagerContainer;

public class KidsMindTotalResultActivity extends Activity {
	FragmentManager fm=null;
	public ViewPager pager; 
	public ScreenSlidePagerAdapter mPagerAdapter;
	private ImageView img,drawimage;
	private int currentPage;
	boolean mNeedsRedraw = false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.result_page);
	    img=(ImageView)findViewById(R.id.doctor);
		drawimage=(ImageView)findViewById(R.id.drawimage);
	    Intent intent=getIntent();
		//if("1".equals(intent.getStringExtra("where"))){
		// Bitmap bit=intent.getParcelableExtra("img");
		// img.setImageBitmap(bit);
		String image_id=intent.getStringExtra("savename");

		readimage(image_id);
       // TODO Auto-generated method stub
	    pager = (ViewPager)findViewById(R.id.menu_pager);
		pager.setOffscreenPageLimit(5);
		mPagerAdapter=new ScreenSlidePagerAdapter(getFragmentManager()); 
		pager.setAdapter(mPagerAdapter);

		
		PagerContainer.listener=new PageChagedListener() {
			
			@Override
			public void onPageChange(int position) {
			
				Log.v(TAG,"position listener"+position);
				switch(position){
				case 0:
					img.setImageResource(R.drawable.re_dotor1);
					break;
				case 1:img.setImageResource(R.drawable.re_dotor2);
					break;
				case 2:
					img.setImageResource(R.drawable.re_dotor2);
					break;
				case 3:
					img.setImageResource(R.drawable.re_dotor1);
					break;
				}
		
				}
			
		};
		   

		//PagerContainer m=new PagerContainer(KidsMindTotalResultActivity.this,img);
		//pager.setOnPageChangeListener(m);

//		pager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//				switch(position){
//				case 0:
//					img.setImageResource(R.drawable.re_dotor1);
//					break;
//				case 1:img.setImageResource(R.drawable.re_dotor2);
//					break;
//				case 2:
//					img.setImageResource(R.drawable.re_dotor2);
//					break;
//				case 3:
//					img.setImageResource(R.drawable.re_dotor1);
//					break;
//				}
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//				// TODO Auto-generated method stub
//				 mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			    if (mNeedsRedraw)pager.invalidate();
//				
//			}
//			
//			
//		});
	}
	Bitmap bitmap;
	void readimage(String path){
		if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String path2 = Environment.getExternalStorageDirectory()+"/KidsMind/"+path;
			Log.v(TAG,"이미지를 읽어오기위한 경로"+path2);


			//DbUse dbuse=new DbUse(MindDrawingResultActivity.this);
			//insertRec2(path2, "0");
			BitmapFactory.Options options =new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			bitmap=BitmapFactory.decodeFile(path2,options);
			options =getBitmapSize(options);
			bitmap=BitmapFactory.decodeFile(path2,options);
			Log.v(TAG,"이미지를 읽어오기위한 경로2"+path2);

			if(bitmap!=null){
				Log.v(TAG,"이미지 로딩");
				drawimage.setImageBitmap(bitmap);
			}else{
				Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
			}

		}
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
	private static final String TAG="MainActivity";
	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ViewPager pager2; 
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
        }
	
        @Override
        public int getCount() {
            return 4;
        }

		@Override
		public Fragment getItem(int position) {
		
		
			SingleResultSketchMenu frag = new SingleResultSketchMenu();
			
			frag.setPosition(position);
			
			return frag;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			Log.v(TAG,"getItemposition"+super.getItemPosition(object)+"");
			return super.getItemPosition(object);
		}
	}
}
