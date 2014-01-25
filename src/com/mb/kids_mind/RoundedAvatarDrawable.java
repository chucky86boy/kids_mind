package com.mb.kids_mind;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class RoundedAvatarDrawable extends Drawable {

   private final Bitmap mBitmap;
   private final Paint mPaint;
   private final RectF mRectF;
   private final int mBitmapWidth;
   private final int mBitmapHeight;


   public static final int COLOR_FACEBOOK = Color.rgb(59, 89, 152);
   public static final int COLOR_TWITTER = Color.rgb(64, 153, 255);

   public RoundedAvatarDrawable(Bitmap bitmap) {
      mBitmap = bitmap;
      mRectF = new RectF();
      mPaint = new Paint();
      mPaint.setAntiAlias(true);
      mPaint.setDither(true);

      final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      mPaint.setShader(shader);

      mBitmapWidth = mBitmap.getWidth();
      mBitmapHeight = mBitmap.getHeight();
      
   }


   @Override
   public void draw(Canvas canvas) {
      // TODO Auto-generated method stub
      canvas.drawOval(mRectF, mPaint);

   }

   @Override
   public int getOpacity() {
      // TODO Auto-generated method stub
      return PixelFormat.TRANSLUCENT;
   }

   @Override
   public void setAlpha(int alpha) {
      // TODO Auto-generated method stub
      if (mPaint.getAlpha() != alpha) {
         mPaint.setAlpha(alpha);
         invalidateSelf();
      }
   }

   @Override
   protected void onBoundsChange(Rect bounds) {
      // TODO Auto-generated method stub
      super.onBoundsChange(bounds);
      mRectF.set(bounds);

   }


   @Override
   public void setColorFilter(ColorFilter cf) {
      // TODO Auto-generated method stub
      mPaint.setColorFilter(cf);
   }


   @Override
   public int getIntrinsicHeight() {
      // TODO Auto-generated method stub

      return mBitmapHeight;
   }


   @Override
   public int getIntrinsicWidth() {
      // TODO Auto-generated method stub
      return mBitmapWidth;
   }


   @Override
   public void setDither(boolean dither) {
      // TODO Auto-generated method stub
      super.setDither(dither);
   }
   public void setAntiAlias(boolean aa) {
      mPaint.setAntiAlias(aa);
      invalidateSelf();
   }

   @Override
   public void setFilterBitmap(boolean filter) {
      // TODO Auto-generated method stub
      mPaint.setFilterBitmap(filter);
      invalidateSelf();
   }

   public Bitmap getBitmap() {
      return mBitmap;
   }


}