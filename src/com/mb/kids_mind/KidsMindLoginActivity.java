package com.mb.kids_mind;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class KidsMindLoginActivity extends Activity {
EditText id,pw;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialoglogin);
		id=(EditText)findViewById(R.id.editText1);
		pw=(EditText)findViewById(R.id.editText2);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//로그인 시도
			finish();
		}
	});
	    // TODO Auto-generated method stub
	}

}
