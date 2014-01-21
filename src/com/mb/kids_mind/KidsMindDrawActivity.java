package com.mb.kids_mind;


import java.io.FileOutputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mb.kids_mind.Dialog.MyDialogColor;
import com.mb.kids_mind.Dialog.MyDialogErager;
import com.mb.kids_mind.Dialog.MyDialogpen;
import com.mb.kids_mind.fragment.DrawFragment;
import com.mb.kids_mind.listener.OnColorSelectedListener;
import com.mb.kids_mind.listener.OnPenSelectedListener;
import com.mb.kids_mind.listener.OnPenSelectedsortListener;
import com.mb.kids_mind.view.BestPaintBoard;
public class KidsMindDrawActivity extends FragmentActivity {
	private static final String TAG="MainActivity";
	 public	BestPaintBoard board; 
	    Button colorBtn; 
	    Button penBtn; 
	    Button eraserBtn; 
	    Button undoBtn;
	    Button trashBtn;
	    Button newBtn;
	    Button completeBtn;
	    FragmentManager fm=null;
	    DrawFragment frag5;
	    //Debugc d=new Debugc();
	    FrameLayout fram=null;
	    String savename;
	LinearLayout linear;
	View.OnClickListener bHandler=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.colorBtn:
				
                MyDialogColor.listener = new OnColorSelectedListener() { 
                    public void onColorSelected(int color) { 
                    	//Log.v(d.getTaga(),"������ ���� Į��"+color+"");
                        mColor = color; 
                        linear.setBackgroundColor(color);
                        board.updatePaintProperty(mColor, mSize); 
                      //  displayPaintProperty(); 
                    } 
                }; 
                MyDialogColor dialog=new MyDialogColor();
				dialog.show(fm, "aaa");
  
  
				
				break;
			case R.id.penBtn:
				MyDialogpen dialog2=new MyDialogpen();
				dialog2.show(fm, "bbb");
   
              MyDialogpen.listener = new OnPenSelectedListener() { 
                    public void onPenSelected(int size) { 
                        mSize = size; 
                        board.updatePaintProperty(mColor, mSize); 
                       // displayPaintProperty(); 
                    } 
                };
                MyDialogpen.listener3 = new OnColorSelectedListener() { 
                    public void onColorSelected(int size) { 
                        mColor = size; 
                        board.updatePaintProperty(mColor, mSize); 
                       // displayPaintProperty(); 
                    } 
                };
                MyDialogpen.listener2 = new OnPenSelectedsortListener() { 
                    

					@Override
					public void onPenSelected(int size) {
						// TODO Auto-generated method stub
						    
						 board.updatePaintProperty2(size); 
	                       
	                        
					} 
                }; 
  
  
                
				break;
			case R.id.eraserBtn:
				MyDialogErager dialog3=new MyDialogErager();
				dialog3.show(fm, "ccc");
				 MyDialogErager.listener2 = new OnPenSelectedListener() { 
	                    public void onPenSelected(int size) { 
	                        mSize = size; 
	                        board.updatePaintProperty(mColor, mSize); 
	                    } 
	                }; 
	                MyDialogErager.listener = new OnColorSelectedListener() { 
	                    public void onColorSelected(int color) { 
	                        mColor = color; 
	                        board.updatePaintProperty(mColor, mSize); 
	                      //  displayPaintProperty(); 
	                    } 
	                }; 
				/*   eraserSelected = !eraserSelected; 
				   
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
	  
	                }*/ 
				break;
			case R.id.newpage2://���� �Է����� �ٲ�
				board.clear();
				break;
			case R.id.undoBtn://���
				   board.undo(); 
				break;
		
				
				
			case R.id.transfor://����Ʈ �ѱ��
				Bitmap bitmap2=board.Save(fos);
				savename=board.startActivity();
				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				editor.putString("savename", savename);
				editor.commit();
				Intent i=new Intent(KidsMindDrawActivity.this,KidsMindAnalyzeActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				
				i.putExtra("savename",savename);
				i.putExtra("where","1");
				//i.putExtra("img",bitmap2);
				startActivity(i);
			
				
				
				break;
				
			}
		}
	};
   
    // RadioButton added 
   
    LinearLayout addedLayout; 
    Button colorLegendBtn; 
    TextView sizeLegendTxt; 
  
    int mColor = 0xff000000; 
    int mSize = 2; 
    int oldColor; 
    int oldSize;
    int width;
    int height;
    boolean eraserSelected = false; 
    FileOutputStream fos;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        this.overridePendingTransition(0, 0);

        setContentView(R.layout.drawmain); 
        fm=getSupportFragmentManager();
        //LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.toolsLayout); 
       // LinearLayout boardLayout = (LinearLayout) findViewById(R.id.boardLayout); 
        fram=(FrameLayout)findViewById(R.id.boardLayout);
        width=fram.getWidth();
        height=fram.getHeight();
        colorBtn = (Button) findViewById(R.id.colorBtn); 
        colorBtn.setOnClickListener(bHandler);
        penBtn = (Button) findViewById(R.id.penBtn);
        penBtn.setOnClickListener(bHandler);
        eraserBtn = (Button) findViewById(R.id.eraserBtn); 
        eraserBtn.setOnClickListener(bHandler);
        undoBtn = (Button) findViewById(R.id.undoBtn);
        undoBtn.setOnClickListener(bHandler);
      
        newBtn=(Button)findViewById(R.id.newpage2);
        newBtn.setOnClickListener(bHandler);
        completeBtn=(Button)findViewById(R.id.transfor);
        completeBtn.setOnClickListener(bHandler);
        linear=(LinearLayout)findViewById(R.id.colorlinear);
        // RadioButton referenced 
        frag5=new DrawFragment();
        board=new BestPaintBoard(this);
        
        FragmentTransaction ft =fm.beginTransaction();
        ft.add(R.id.boardLayout, frag5);
        ft.commit();
        
        /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
                LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.FILL_PARENT); 
  
        board = new BestPaintBoard(this); 
        board.setLayoutParams(params); 
        board.setPadding(2, 2, 2, 2); 
  
        boardLayout.addView(board); 
  
        // add legend buttons 
        LinearLayout.LayoutParams addedParams = new LinearLayout.LayoutParams( 
                LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.FILL_PARENT); 
  
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams( 
                LinearLayout.LayoutParams.FILL_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT); 
        addedLayout = new LinearLayout(this); 
        addedLayout.setLayoutParams(addedParams); 
        addedLayout.setOrientation(LinearLayout.VERTICAL); 
        addedLayout.setPadding(8,8,8,8); 
  
        LinearLayout outlineLayout = new LinearLayout(this); 
        outlineLayout.setLayoutParams(buttonParams); 
        outlineLayout.setOrientation(LinearLayout.VERTICAL); 
        outlineLayout.setBackgroundColor(Color.LTGRAY); 
        outlineLayout.setPadding(1,1,1,1); 
  
        colorLegendBtn = new Button(this); 
        colorLegendBtn.setLayoutParams(buttonParams); 
        colorLegendBtn.setText(" "); 
        colorLegendBtn.setBackgroundColor(mColor); 
        colorLegendBtn.setHeight(20); 
        outlineLayout.addView(colorLegendBtn); 
        addedLayout.addView(outlineLayout); 
  
        sizeLegendTxt = new TextView(this); 
        sizeLegendTxt.setLayoutParams(buttonParams); 
        sizeLegendTxt.setText("Size : " + mSize); 
        sizeLegendTxt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL); 
        sizeLegendTxt.setTextSize(16); 
        sizeLegendTxt.setTextColor(Color.BLACK); 
        addedLayout.addView(sizeLegendTxt); 
  
        toolsLayout.addView(addedLayout); */
  
    }
  
  
     
  
  
    public int getChosenColor() { 
        return mColor; 
    } 
  
    public int getPenThickness() { 
        return mSize; 
    } 
  
  /*  private void displayPaintProperty() { 
        colorLegendBtn.setBackgroundColor(mColor); 
        sizeLegendTxt.setText("Size : " + mSize); 
  
        addedLayout.invalidate(); 
    }*/ 
  
}
/*



*//**
 * �ձ۾��� ���ų� ��ġ�� �׸��� �׸� �� �ִ� ����Ʈ���带 ����� ��� ���� �� �� �ֽ��ϴ�.
 * �ڵ�� �ܰ躰�� �����˴ϴ�. ���� ���� ��Ƽ��Ƽ�� �ܰ躰�� �����غ� �� �ִ� ��ư�� �ֽ��ϴ�.
 * 
 * @author Mike
 *
 *//*
public class BestPaintBoardActivity extends Activity {
 
	 */


