package com.mb.kids_mind.Adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
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
	static final String TAG="MainActivity";
	
    ArrayList<KidsMindResultItem> list;
    Context context;
    int		layout;
    boolean flag;

	public SiteAdapter( Context context, int layout, ArrayList<KidsMindResultItem> list)
    {
    	this.context = context;
    	this.layout = layout;
    	this.list = list;     	
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

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.title = (TextView) cView.findViewById(R.id.textView1);
           // holder.siteImage = (ImageView) cView.findViewById(R.id.siteImage);
            holder.hlView = (TwoWayView) cView.findViewById(R.id.list);
            holder.hlView.setItemMargin(10);
            holder.hlView.setLongClickable(true);
            holder.itemList = new ArrayList<KMimageItem>();
            holder.itemAdapter = new SimpleListAdapter(context, R.layout.item_view, holder.itemList);

            // Assign adapter to HorizontalListView
            holder.hlView.setAdapter(holder.itemAdapter);
            
            cView.setTag(holder);
        } else {
            holder = (Holder) cView.getTag();
        }

        KidsMindResultItem site = list.get(pos);
        // Populate the text
        holder.title.setText( site.getTitle());
        //holder.siteImage.setImageResource(site.getImage());
        holder.itemList.clear();
        // ToDo : 아이템 리스트 채우고 다시 데이터를 가져온다.
    	
        downloadImages(holder.itemList, pos,flag );
        
        holder.itemAdapter.setList(holder.itemList);
        holder.itemAdapter.notifyDataSetChanged();
         
        Log.v(TAG, list.get(pos).toString() );
		return cView;
	}

    private class Holder {
        public TextView title;
        //public ImageView siteImage;
        public TwoWayView hlView;
        SimpleListAdapter itemAdapter;
        ArrayList<KMimageItem> itemList;
    }

    // 사이트에서 Item을 다운로드하고 리스트에 추가
    void downloadImages(  ArrayList<KMimageItem> list,int pos,boolean fl)
    {
    
    	switch(pos){
    	case 0:
    		
    		int[] drawables = {R.drawable.box_thumbnail,R.drawable.box_thumbnail,R.drawable.box_thumbnail,R.drawable.box_thumbnail,R.drawable.box_thumbnail,R.drawable.box_thumbnail,R.drawable.box_thumbnail,R.drawable.box_thumbnail};
    		Log.v(TAG,"두개나와야되"+drawables.length+"");
        	for(int i=0; i<drawables.length; i++)
        	{
    			KMimageItem item = new KMimageItem();
    			String name = "image" + (i+1);
    			item.setSort( name);
    			
//    			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//    			String savedName = path + File.separator + name+".jpg";
//    			if( !new File(savedName).exists() )
//    			{
//    				Bitmap src = BitmapFactory.decodeResource(context.getResources(), drawables[i]);
//    				Bitmap thumb = makeThumbnail( src, 120, 80 );
//    				savedName = writeBitmap(thumb, name+".jpg");
//    			}
    //
//    			item.setThumb(savedName);
//    			item.setSiteId( drawables[i] );

    			item.setImgres(drawables[i]);
    			list.add(item);    		
        	}        	
    		break;
    	case 1:
       		int[] drawables2 = {R.drawable.d009,R.drawable.d010,R.drawable.d011,R.drawable.d012,R.drawable.d013,R.drawable.d014,R.drawable.d015,R.drawable.d016};
    		Log.v(TAG,"두개나와야되"+drawables2.length+"");
        	for(int i=0; i<drawables2.length; i++)
        	{
    			KMimageItem item = new KMimageItem();
    			String name = "image" + (i+1);
    			item.setSort( name);
    			
//    			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//    			String savedName = path + File.separator + name+".jpg";
//    			if( !new File(savedName).exists() )
//    			{
//    				Bitmap src = BitmapFactory.decodeResource(context.getResources(), drawables[i]);
//    				Bitmap thumb = makeThumbnail( src, 120, 80 );
//    				savedName = writeBitmap(thumb, name+".jpg");
//    			}
    //
//    			item.setThumb(savedName);
//    			item.setSiteId( drawables[i] );

    			item.setImgres(drawables2[i]);
    			list.add(item);    		
        	}
        	break;
    	
        	
    	}

    }
    
	Bitmap makeThumbnail(Bitmap src, int w, int h)
	{
		return ThumbnailUtils.extractThumbnail(src, w, h);		
	}
	
	public String writeBitmap(Bitmap bitmap, String saveName) {
		FileOutputStream out = null;
		File f = null;

		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();

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
