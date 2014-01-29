package com.mb.kids_mind;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class KidsMindLoginSelectActivity extends Activity {
View.OnClickListener bHandler =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1://계정생성
			
			break;
		case R.id.button2://페이스북 로긴
			
		break;
		case R.id.button3://구글 로긴
			
			break;
		}
	}
};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.loginselect);
	    findViewById(R.id.button1);
	    findViewById(R.id.button2);
	    findViewById(R.id.button3);
	    
	    // TODO Auto-generated method stub
	}

}
