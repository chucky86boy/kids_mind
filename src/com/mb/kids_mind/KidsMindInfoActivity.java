package com.mb.kids_mind;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;


public class KidsMindInfoActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.infodetail);
		overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
			editor.putString("infocheck", "on");
			editor.commit();
				finish();
			}
		});
		//linear.setBackgroundColor(Color.TRANSPARENT);
		/*Paint paint = new Paint();
	
	 paint.setColor(Color.BLACK);

	 paint.setAlpha(70);


	linear.setBackgroundColor(paint.getColor());*/
		/*Window window = getWindow();
	 WindowManager.LayoutParams windowParams = window.getAttributes();
	 windowParams.dimAmount = 0.50f;
	 windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
	 window.setAttributes(windowParams);*/
		// TODO Auto-generated method stub
	}
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);	super.onPause();
	}

}
