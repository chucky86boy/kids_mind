package com.mb.kids_mind;


import com.mb.kids_mind.R;
import com.mb.kids_mind.fragment.SketchMenu;
import com.mb.kids_mind.listener.MainSideMenuListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;

import com.mb.kids_mind.fragment.SketchMenu;

public class MainActivity extends Activity {
	FragmentManager fm=null;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
        findViewById(R.id.menuToggler).setOnClickListener(new MainSideMenuListener());
        getFragmentManager().beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
