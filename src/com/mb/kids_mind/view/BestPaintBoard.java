package com.mb.kids_mind.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * ����Ʈ���忡 ��� �߰�
 * Path�� ����ϴ� ������� ����
 * 
 * @author Mike
 *
 */
public class BestPaintBoard extends View {
private static final String TAG="MainActivity";
	/**
	 * Undo data
	 */
	Stack undos = new Stack();

	/**
	 * Maximum Undos
	 */
	public static int maxUndos = 10;

	/**
	 * Changed flag
	 */
	public boolean changed = false;	
	
	/**
	 * Canvas instance
	 */
	Canvas mCanvas;
	
	/**
	 * Bitmap for double buffering
	 */
	Bitmap mBitmap;
	
	/**
	 * Paint instance
	 */
	final Paint mPaint;
	
	/**
	 * X coordinate
	 */
	float lastX;
	
	/**
	 * Y coordinate
	 */
	float lastY;

	
    private final Path mPath = new Path();

    private float mCurveEndX;
    private float mCurveEndY;

    private int mInvalidateExtraBorder = 10;
    
    static final float TOUCH_TOLERANCE = 8;

    private static final boolean RENDERING_ANTIALIAS = true;
    private static final boolean DITHER_FLAG = true;

    private int mCertainColor = 0xFF000000;
    private float mStrokeWidth = 2.0f;
	
	/**
	 * Initialize paint object and coordinates
	 * 
	 * @param c
	 */
	public BestPaintBoard(Context context) {
		super(context);
		
		// create a new paint object
		mPaint = new Paint();
		mPaint.setAntiAlias(RENDERING_ANTIALIAS);
		mPaint.setColor(mCertainColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setDither(DITHER_FLAG);

		
		lastX = -1;
		lastY = -1;

		Log.i("GoodPaintBoard", "initialized.");
		
	}

	/**
	 * Clear undo
	 */
	public void clearUndo()
	{
		while(true) {
			Bitmap prev = (Bitmap)undos.pop();
			if (prev == null) return;
			
			prev.recycle();
		}
	}	
public void clear(){
	mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
}

	
	/**
	 * Save undo
	 */
	public void saveUndo()
	{
		if (mBitmap == null) return;
		
		while (undos.size() >= maxUndos){
			Bitmap i = (Bitmap)undos.get(undos.size()-1);
			i.recycle();
			undos.remove(i);
		}
		
		Bitmap img = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		
		undos.push(img);
		
		Log.i("GoodPaintBoard", "saveUndo() called.");
	}
	
	/**
	 * Undo
	 */
	public void undo()
	{
		Bitmap prev = null;
		try {
			prev = (Bitmap)undos.pop();
		} catch(Exception ex) {
			Log.e("GoodPaintBoard", "Exception : " + ex.getMessage());
		}
		
		if (prev != null){
			drawBackground(mCanvas);
			mCanvas.drawBitmap(prev, 0, 0, mPaint);
			invalidate();
			
			prev.recycle();
		}
		
		Log.i("GoodPaintBoard", "undo() called.");
	}	
	
	/**
	 * Paint background
	 * 
	 * @param g
	 * @param w
	 * @param h
	 */
	public void drawBackground(Canvas canvas)
	{
		if (canvas != null) {
			canvas.drawColor(Color.WHITE);
		}
	}	
	
	/**
	 * Update paint properties
	 * 
	 * @param canvas
	 */
	public void updatePaintProperty(int color, int size)
	{
		mPaint.setColor(color);
		mPaint.setStrokeWidth(size);
	}
	public void updatePaintProperty2(int sort)
	{
		switch(sort){
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
			
		}
		/*mPaint.setColor(color);
		mPaint.setStrokeWidth(size);*/
	}
	
	/**
	 * Create a new image
	 */
	public void newImage(int width, int height)
	{
		Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);
		
		mBitmap = img;
		mCanvas = canvas;

		drawBackground(mCanvas);
		
		changed = false;
		invalidate();
	}	
	
	/**
	 * Set image
	 * 
	 * @param newImage
	 */
	public void setImage(Bitmap newImage)
	{
		changed = false;
		
		setImageSize(newImage.getWidth(),newImage.getHeight(),newImage);
		invalidate();
	}	
	
	/**
	 * Set image size
	 * 
	 * @param width
	 * @param height
	 * @param newImage
	 */
	public void setImageSize(int width, int height, Bitmap newImage)
	{
		if (mBitmap != null){
			if (width < mBitmap.getWidth()) width = mBitmap.getWidth();
			if (height < mBitmap.getHeight()) height = mBitmap.getHeight();
		}
		
		if (width < 1 || height < 1) return;
		
		Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		drawBackground(canvas);
		
		if (newImage != null) {
			canvas.setBitmap(newImage);
		}
		
		if (mBitmap != null) {
			mBitmap.recycle();
			mCanvas.restore();
		}

		mBitmap = img;
		mCanvas = canvas;
		
		clearUndo();
	}
	
	
	
	/**
	 * onSizeChanged
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w > 0 && h > 0) {
			newImage(w, h);
		}
	}

	/**
	 * Draw the bitmap
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}

	}

	/**
	 * Handles touch event, UP, DOWN and MOVE
	 */
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:
				changed = true;
				
				Rect rect = touchUp(event, false);
				if (rect != null) {
                    invalidate(rect);
                }

		        mPath.rewind();
		        
                return true;
                
			case MotionEvent.ACTION_DOWN:
				saveUndo();
				
				rect = touchDown(event);
				if (rect != null) {
                    invalidate(rect);
                }
                
				return true;
                
			case MotionEvent.ACTION_MOVE:
				rect = touchMove(event);
                if (rect != null) {
                    invalidate(rect);
                }

                return true;
		}

		return false;
	}

	/**
	 * Process event for touch down
	 * 
	 * @param event
	 * @return
	 */
    private Rect touchDown(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        lastX = x;
        lastY = y;

        Rect mInvalidRect = new Rect();
        mPath.moveTo(x, y);

        final int border = mInvalidateExtraBorder;
        mInvalidRect.set((int) x - border, (int) y - border, (int) x + border, (int) y + border);

        mCurveEndX = x;
        mCurveEndY = y;

        mCanvas.drawPath(mPath, mPaint);
        
        return mInvalidRect;
    }
	
    
    /**
     * Process event for touch move
     * 
     * @param event
     * @return
     */
    private Rect touchMove(MotionEvent event) {
        Rect rect = processMove(event);
        
        return rect;
    }	

    private Rect touchUp(MotionEvent event, boolean cancel) {
    	Rect rect = processMove(event);

        return rect;
    }

    /**
     * Process Move Coordinates
     * 
     * @param x
     * @param y
     * @param dx
     * @param dy
     * @return
     */
    private Rect processMove(MotionEvent event) {

    	final float x = event.getX();
        final float y = event.getY();

        final float dx = Math.abs(x - lastX);
        final float dy = Math.abs(y - lastY);

        Rect mInvalidRect = new Rect();
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            final int border = mInvalidateExtraBorder;
            mInvalidRect.set((int) mCurveEndX - border, (int) mCurveEndY - border,
                    (int) mCurveEndX + border, (int) mCurveEndY + border);

            float cX = mCurveEndX = (x + lastX) / 2;
            float cY = mCurveEndY = (y + lastY) / 2;

            mPath.quadTo(lastX, lastY, cX, cY);

            // union with the control point of the new curve
            mInvalidRect.union((int) lastX - border, (int) lastY - border,
                    (int) lastX + border, (int) lastY + border);

            // union with the end point of the new curve
            mInvalidRect.union((int) cX - border, (int) cY - border,
                    (int) cX + border, (int) cY + border);

            lastX = x;
            lastY = y;

            mCanvas.drawPath(mPath, mPaint);
        }

        return mInvalidRect;
    }
	
	/**
	 * Save this contents into a Jpeg image
	 * 
	 * @param outstream
	 * @return
	 */
    String DirPath;
    String savename;
    File f=null;
    public Bitmap Save(OutputStream outstream) {
		try {
			
			DirPath = Environment.getExternalStorageDirectory().getAbsolutePath();       
			DirPath = DirPath + "/" + "KidsMind" + "/";	         

			File cameraDir = new File(DirPath);
			if( !cameraDir.exists() ){

				Log.v(TAG,"1");
				cameraDir.mkdirs();
			}
			Log.v(TAG,"2");
			/*File cameraDir1 = new File(cameraDir, "Petsitter");
			if( !cameraDir1.exists() ){
				cameraDir1.mkdirs();
			}*/
			Log.v(TAG,"3");
			savename = System.currentTimeMillis()+".jpg";
			//insertRec(getDate(),saveName);
			f = new File(cameraDir, savename);
			Log.v(TAG,"4");
			outstream = new FileOutputStream( f );
			Log.v(TAG,"5");


			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
			Log.v(TAG,"6");
			invalidate();
			
			return mBitmap;
		} catch (Exception e) {
			return null;
		}
	}
	public String startActivity(){
		return savename;
	}
	
}
