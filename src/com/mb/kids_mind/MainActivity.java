package com.mb.kids_mind;

<<<<<<< HEAD
=======
import com.mb.kids_mind.R;
import com.mb.kids_mind.fragment.SketchMenu;
import com.mb.kids_mind.listener.MainSideMenuListener;

import android.os.Bundle;
>>>>>>> 017afb6232f5608bff69bda6ebe5cbcf2ca935f6
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;

<<<<<<< HEAD
import com.mb.kids_mind.fragment.SketchMenu;

public class MainActivity extends Activity {
	FragmentManager fm=null;
=======
public class MainActivity extends Activity {
>>>>>>> 017afb6232f5608bff69bda6ebe5cbcf2ca935f6
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
=======
        findViewById(R.id.menuToggler).setOnClickListener(new MainSideMenuListener());
        getFragmentManager().beginTransaction().add(R.id.fragmentHolder,new SketchMenu()).commit();
>>>>>>> 017afb6232f5608bff69bda6ebe5cbcf2ca935f6
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
