package com.mb.kids_mind;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.mb.kids_mind.fragment.SketchMenu;
import com.mb.kids_mind.listener.MainSideMenuListener;

public class MainActivity extends Activity {
	FragmentManager fm=null;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
        findViewById(R.id.menuToggler).setOnClickListener(new MainSideMenuListener());
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putInt("abs", 0);
		editor.putString("list", "");
		editor.putInt("temp", 0);
		editor.commit();
		
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
