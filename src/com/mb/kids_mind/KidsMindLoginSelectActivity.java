package com.mb.kids_mind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class KidsMindLoginSelectActivity extends Activity {
View.OnClickListener bHandler =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.createaccount://계정생성
			Intent in=new Intent(KidsMindLoginSelectActivity.this,KidsMindLoginActivity.class);
			in.putExtra("wh", "c");
			startActivityForResult(in, 0);
			
			break;
		case R.id.facebook://페이스북 로긴
			
		break;
		case R.id.googleaccount://구글 로긴
			
			break;
		case R.id.loginaccount:
			
			Intent in2=new Intent(KidsMindLoginSelectActivity.this,KidsMindLoginActivity.class);
			in2.putExtra("wh", "l");
			startActivityForResult(in2, 0);
			
			
			break;
		}
	}
};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.loginselect);
	    findViewById(R.id.createaccount).setOnClickListener(bHandler);
	    findViewById(R.id.facebook).setOnClickListener(bHandler);
	    findViewById(R.id.googleaccount).setOnClickListener(bHandler);
	    findViewById(R.id.loginaccount).setOnClickListener(bHandler);
	    // TODO Auto-generated method stub
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(resultCode==RESULT_OK){
			//Log.
			if(requestCode==0){
				KidsMindLoginSelectActivity.this.setResult(RESULT_OK);
				finish();
				
	}
		}
	}

}
