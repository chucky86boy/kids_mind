package com.mb.kids_mind;

import android.app.Activity;
import android.os.Bundle;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KidsMindAnalyzeActivity extends Activity {
	//	  private MatOfInt             mHistSize;
	private int                  mHistSizeNum = 25;
	private ImageView img;
	//private Mat mRgba;
	//private Mat mIntermediateMat;
	//private Mat mGray;
	//private Mat mGray0;
	//private Mat ma;
	private int height;
	//  private Point                mP1;
	//  private Point                mP2;
	private int width;
	String image_id;
	//private Mat ex;
	Bitmap bitmap;
	private float                mBuff[];
	// private Size                 mSize0;
	FrameLayout frambg;
	String bpath;
	// private Mat                  mMat0;
	//	    private MatOfInt             mChannels[];

	//    private MatOfFloat           mRanges;
	//   private Scalar               mColorsRGB[];
	// private Scalar               mColorsHue[];
	//private Scalar               mWhilte;
	Button btn2, btn3, btn4,btn5;
	private static final String TAG="MainActivity";
	//List<MatOfPoint> contours;
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	LinearLayout linear;
	//private Bitmap photo;
	private Uri mImageCaptureUri; // 이미지가 있는 곳의 Uri

	//	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	//		@Override
	//		public void onManagerConnected(int status) {
	//			switch (status) {
	//			case LoaderCallbackInterface.SUCCESS: {
	//				// Log.i(TAG, "OpenCV loaded successfully");
	//				//
	//				// mOpenCvCameraView.enableView();
	//				// mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
	//				System.loadLibrary("opencv_java");
	//
	//
	//
	//			}
	//			break;
	//			default: {
	//				super.onManagerConnected(status);
	//			}
	//			break;
	//			}
	//		}
	//	};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);
		//		if (!OpenCVLoader.initDebug()) {

		//	} else{
		//	mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		DisplayMetrics metrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		width = metrics.widthPixels;

		height = metrics.heightPixels;
		frambg=(FrameLayout)findViewById(R.id.frambg);
		Intent intent=getIntent();
		if("1".equals(intent.getStringExtra("where"))){
			// Bitmap bit=intent.getParcelableExtra("img");
			// img.setImageBitmap(bit);
			image_id=intent.getStringExtra("savename");
			readimage(image_id);
		}else{
			image_id=intent.getStringExtra("path");
			bpath=image_id;
			Log.v(TAG,"이미지 패스"+intent.getStringExtra("path"));
			
			//editor.putString("path", image_id);
			//editor.commit();
			//String path=pref.getString("path", null);
			//Log.v(Debugc.getTaga(),"이미지경로2"+path);
			readimage(bpath);
		}
		//	linear.setBackgroundDrawable(bit);

		//	frambg.setBackgroundResource(R.drawable.image);    
		ImageView imgView = (ImageView) findViewById(R.id.imageView3);
		imgView.setVisibility(ImageView.VISIBLE);
		imgView.setBackgroundResource(R.anim.loading2);		
		AnimationDrawable frameAnimation = (AnimationDrawable) imgView.getBackground();
		frameAnimation.start();
		img=(ImageView)findViewById(R.id.imageView1);


		Animation ani =null;
		ani = new TranslateAnimation(0, width, 0, 0);
		ani.setDuration(2500);

		img.startAnimation(ani);
		Handler handler2=new Handler();
		handler2.postDelayed(new Runnable(){
			Animation ani =null;

			@Override
			public void run(){
				//	doAction(bitmap);
				ani =new TranslateAnimation(width, 0, 0, 0);
				ani.setDuration(3000);
				img.startAnimation(ani);

				//					Rect roi =new Rect(0, 0, ex.cols(),ex.rows() );
				//					Mat mat =ex.submat(roi);
				//					Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
				//
				//					// 2) AdaptiveThreshold -> classify as either black or white
				//
				//					// 3) Invert the image -> so most of the image is black
				//
				//					// 4) Dilate -> fill the image using the MORPH_DILATE
				//					
				//					Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 0);
				//					Imgproc.adaptiveThreshold(mat, mat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);
				//					Core.bitwise_not(mat, mat);
				//					Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(3,3), new Point(2,2));
				//					Imgproc.dilate(mat, mat, kernel);
				//					
				//					//Imgproc.adaptiveThreshold(mat, mat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 4);
				//
				//					mat.convertTo(mat, CvType.CV_8UC1);
				//					//Imgproc.Canny(mat, mat, 150, 150);
				//					List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				//
				//					Imgproc.findContours(mat, contours, new Mat(),
				//							Imgproc.RETR_EXTERNAL,
				//							Imgproc.CHAIN_APPROX_SIMPLE);
				//					Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BayerBG2RGB);
				//					Imgproc.drawContours(mat, contours, -1, new Scalar(0,
				//							255, 0), 1);
				//					// Imgproc.drawContours(ex, contours, -1, new
				//					// Scalar(255,0,0),1);
				//					int a = contours.size();
				//
				//					double d = Imgproc.contourArea(contours.get(0));
				//					// 모멘트 함수
				//
				//					// 외곽선에 따른 중심 좌표 구하는 방법
				//					List<Moments> mu = new ArrayList<Moments>(contours
				//							.size());
				//					for (int i = 0; i < contours.size(); i++) {
				//						mu.add(i, Imgproc.moments(contours.get(i), false));
				//						Moments p = mu.get(i);
				//						int x = (int) (p.get_m10() / p.get_m00());
				//						int y = (int) (p.get_m01() / p.get_m00());
				//
				//						Core.circle(mat, new Point(x, y), 4, new Scalar(255,
				//								49, 0, 255));
				//					}
				//					// double d=Imgproc.matchShapes(contours.get(0),
				//					// contours.get(1), Imgproc.CV_CONTOURS_MATCH_I1, 0.0d);
				//					Toast.makeText(KidsMindAnalActivity.this,
				//							d + "" + "int" + a + "", Toast.LENGTH_SHORT)
				//							.show();
				//					//mat.adjustROI(2, 2, 2, 2);
				//					bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
				//
				//					
				//					Utils.matToBitmap(mat, bitmap);
				//					BitmapDrawable bit =new BitmapDrawable(bitmap);
				//					frambg.setBackgroundDrawable(bit);
				//					mRgba = ex;
				//
				//					
				//					Imgproc.Canny(ex, mIntermediateMat, 80, 100);
				//					Imgproc.cvtColor(mIntermediateMat, mRgba,
				//							Imgproc.COLOR_GRAY2RGBA, 4);
				//					Utils.matToBitmap(mIntermediateMat, bitmap);
				//					BitmapDrawable bit =new BitmapDrawable(bitmap);
				//					frambg.setBackgroundDrawable(bit);
			}
		},2500);

		Handler handler=new Handler();
		handler.postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent =new Intent(KidsMindAnalyzeActivity.this, KidsMindResultActivity.class);
				intent.putExtra("savename", image_id);
				startActivity(intent);
				//getColorRGB();
			}
		},5000);
	}
	// TODO Auto-generated method stub
	//}
	void readimage(String path){
		if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String path2 = Environment.getExternalStorageDirectory()+"/KidsMind/"+path;
			Log.v(TAG,"이미지를 읽어오기위한 경로"+path2);


			//DbUse dbuse=new DbUse(MindDrawingResultActivity.this);
			//insertRec2(path2, "0");
			bitmap=BitmapFactory.decodeFile(path2);
			Log.v(TAG,"이미지를 읽어오기위한 경로2"+path2);

			if(bitmap!=null){
				Log.v(TAG,"이미지 로딩");
				BitmapDrawable bit =new BitmapDrawable(bitmap);
				frambg.setBackgroundDrawable(bit);	//img.setImageBitmap(bitmap);
			}else{
				Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
			}

		}
	}
	//	public void getColorRGB(){
	//		Mat rgba =ex;
	//		Size sizeRgba =ex.size();
	//		
	//		Mat rgbaInnerWindow;
	//		
	//		 int rows = (int) sizeRgba.height;
	//	        int cols = (int) sizeRgba.width;
	//
	//	      
	//		  Mat hist = new Mat();
	//          int thikness = (int) (sizeRgba.width / (mHistSizeNum + 10) / 5);
	//          if(thikness > 5) thikness = 15;
	//     
	//     int offset = (int) ((sizeRgba.width - (5*mHistSizeNum + 4*10)*thikness)/2);
	//     Imgproc.cvtColor(rgba, mIntermediateMat, Imgproc.COLOR_RGB2HSV_FULL);
	//  
	//     // Hue  z
	//     
	//     Imgproc.calcHist(Arrays.asList(mIntermediateMat), mChannels[0], mMat0, hist, mHistSize, mRanges);
	//     Core.normalize(hist, hist, sizeRgba.height/2, 0, Core.NORM_INF);
	//     hist.get(0, 0, mBuff);
	//     
	//     for(int h=0; h<mHistSizeNum; h++) {
	//         mP1.x = mP2.x = offset + (4 * (mHistSizeNum + 10) + h) * thikness;
	//         mP1.y = sizeRgba.height-1;
	//         mP2.y = mP1.y - 2 - (int)mBuff[h];
	//         Log.v(TAG,"mColorsHue["+h+""+"]"+mColorsHue[h]+"");
	//      
	//         Core.line(rgba, mP1, mP2, mColorsHue[h], thikness);
	//     
	//     }
	// 	bitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
	//
	//	
	//	Utils.matToBitmap(rgba, bitmap);
	//	BitmapDrawable bit =new BitmapDrawable(bitmap);
	//	frambg.setBackgroundDrawable(bit);
	//	}
	//	public void doAction(Bitmap bi){
	//		Bitmap bitmap2 = bi.copy(Bitmap.Config.ARGB_8888, true);
	//
	//		ex = new Mat(bitmap2.getWidth(), bitmap2.getHeight(),
	//				CvType.CV_8UC1);
	//		Utils.bitmapToMat(bitmap2, ex);
	//
	//		height = bitmap2.getHeight();
	//		width = bitmap2.getWidth();
	//		mRgba = new Mat(height, width, CvType.CV_8UC1);
	//		mIntermediateMat = new Mat(height, width, CvType.CV_8UC1);
	//		mGray = new Mat(height, width, CvType.CV_8UC1);
	//		mGray0 = new Mat(height, width, CvType.CV_8UC1);
	//		   mChannels = new MatOfInt[] { new MatOfInt(0), new MatOfInt(1), new MatOfInt(2) };
	//	        mBuff = new float[mHistSizeNum];
	//	        mHistSize = new MatOfInt(mHistSizeNum);
	//	        mRanges = new MatOfFloat(0f, 256f);
	//	        mMat0  = new Mat();
	//	        mColorsRGB = new Scalar[] { new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255) };
	//	        mColorsHue = new Scalar[] {
	//	                new Scalar(255, 0, 0, 255),   new Scalar(255, 60, 0, 255),  new Scalar(255, 120, 0, 255), new Scalar(255, 180, 0, 255), new Scalar(255, 240, 0, 255),
	//	                new Scalar(215, 213, 0, 255), new Scalar(150, 255, 0, 255), new Scalar(85, 255, 0, 255),  new Scalar(20, 255, 0, 255),  new Scalar(0, 255, 30, 255),
	//	                new Scalar(0, 255, 85, 255),  new Scalar(0, 255, 150, 255), new Scalar(0, 255, 215, 255), new Scalar(0, 234, 255, 255), new Scalar(0, 170, 255, 255),
	//	                new Scalar(0, 120, 255, 255), new Scalar(0, 60, 255, 255),  new Scalar(0, 0, 255, 255),   new Scalar(64, 0, 255, 255),  new Scalar(120, 0, 255, 255),
	//	                new Scalar(180, 0, 255, 255), new Scalar(255, 0, 255, 255), new Scalar(255, 0, 215, 255), new Scalar(255, 0, 85, 255),  new Scalar(255, 0, 0, 255)
	//	        };
	//	        mWhilte = Scalar.all(255);
	//	        mP1 = new Point();
	//	        mP2 = new Point();
	//
	//	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		if (!OpenCVLoader.initDebug()) {
		//
		//		} else {
		//			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//
		//			mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		//		}
	}

}