package com.mb.kids_mind;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class KidsMindSettingActivity extends Activity {
private static final String TAG="MainActivity";
View.OnClickListener bHandler =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back_btn:
				finish();
				break;
		}
	}
};
CheckBox push,noti,sound;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setting);
	    findViewById(R.id.back_btn).setOnClickListener(bHandler);
	    push=(CheckBox)findViewById(R.id.push);
	    noti=(CheckBox)findViewById(R.id.pop);
	    sound=(CheckBox)findViewById(R.id.sound);
	    push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
				if(isChecked){
					// TODO Auto-generated method stub
				}else{
					
				}
				
			}
		});
sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
				if(isChecked){
					// TODO Auto-generated method stub
				}else{
					
				}
				
			}
		});
noti.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	
		if(isChecked){
			// TODO Auto-generated method stub
		}else{
			
		}
		
	}
});
	    
	    // TODO Auto-generated method stub
	}
	@Override
	protected void onPause() {
		SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
	    SharedPreferences.Editor editor=pref.edit();
	    editor.putString("noti", "");
	    editor.commit();
		super.onPause();
	}

}
