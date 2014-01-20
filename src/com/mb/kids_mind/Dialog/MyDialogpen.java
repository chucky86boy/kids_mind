package com.mb.kids_mind.Dialog;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mb.kids_mind.R;
import com.mb.kids_mind.listener.OnColorSelectedListener;
import com.mb.kids_mind.listener.OnPenSelectedListener;
import com.mb.kids_mind.listener.OnPenSelectedsortListener;


public class MyDialogpen extends DialogFragment  {
	Activity activity;
	CheckBox prb1,prb2,prb3,prb4;
	CheckBox srb1,srb2,srb3,srb4;
	public static OnPenSelectedListener listener;
	public static OnPenSelectedsortListener listener2;
	public static OnColorSelectedListener listener3;
	void selectcolor(int color){
	if (MyDialogErager.listener != null) {
		MyDialogErager.listener.onColorSelected(color);
	
	}
	}
void selectPen(int pen){
	if (MyDialogpen.listener != null) {
		MyDialogpen.listener.onPenSelected(pen);
	}
}
public void	selectPensort(int pen){
	if (MyDialogpen.listener2 != null) {
		MyDialogpen.listener2.onPenSelected(pen);
	}
}

View.OnClickListener bHandler =new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
	switch(v.getId()){
	
	case R.id.first1:
		selectPen(40);
	
		dismiss();
		break;
	case R.id.second1:
		selectPen(30);
		
		dismiss();
		break;
	case R.id.third1:
		selectPen(20);
		
		dismiss();
		break;
	case R.id.forth1:
		selectPen(10);
		
		dismiss();
		break;
	}
		
	}
};
/*	public void doAction(){
//     eraserSelected = !eraserSelected; 
	  
     if (eraserSelected) { 
         colorBtn.setEnabled(false); 
         penBtn.setEnabled(false); 
         undoBtn.setEnabled(false); 

         colorBtn.invalidate(); 
         penBtn.invalidate(); 
         undoBtn.invalidate(); 

         oldColor = mColor; 
         oldSize = mSize; 

         mColor = Color.WHITE; 
         mSize = 15; 

         board.updatePaintProperty(mColor, mSize); 
         displayPaintProperty(); 

     } else { 
         colorBtn.setEnabled(true); 
         penBtn.setEnabled(true); 
         undoBtn.setEnabled(true); 

         colorBtn.invalidate(); 
         penBtn.invalidate(); 
         undoBtn.invalidate(); 

         mColor = oldColor; 
         mSize = oldSize; 

         board.updatePaintProperty(mColor, mSize); 
         displayPaintProperty(); 

     } 

 } 
}); 

}*/
@Override
public void onAttach(Activity activity) {
	this.activity = activity;
	super.onAttach(activity);
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	View v = null;
	v = inflater.inflate(R.layout.eraser, null);
	getDialog().getWindow().requestFeature(STYLE_NO_TITLE);
	getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	
	v.findViewById(R.id.first1).setOnClickListener(bHandler);
	v.findViewById(R.id.second1).setOnClickListener(bHandler);
	v.findViewById(R.id.third1).setOnClickListener(bHandler);
	v.findViewById(R.id.forth1).setOnClickListener(bHandler);
	
	
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


