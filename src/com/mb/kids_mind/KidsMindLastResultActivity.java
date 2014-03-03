package com.mb.kids_mind;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.mb.kids_mind.Adapter.RecommendListAdapter;
import com.mb.kids_mind.Adapter.SimilarListAdapterdetail;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;
import com.mb.kids_mind.Item.RecommendItem;
import com.mb.kids_mind.Item.SItem2;
import com.mb.kids_mind.Item.SimilarItem;
import com.mb.kids_mind.Item.SimilarJsonparser2.SimilarJsonparser;
import com.mb.kids_mind.fragment.LastRecommendImage;
import com.mb.kids_mind.fragment.LastSimilarImage;
import com.mb.kids_mind.task.NetManager;
import com.mb.kids_mind.view.TwoWayView;

public class KidsMindLastResultActivity extends FragmentActivity implements
ConnectionCallbacks, OnConnectionFailedListener {
	@Override
	protected void onDestroy() {
		//  adView.destroy();

		super.onDestroy();
	}
	@Override
	protected void onPause() {
		//adView.pause();

		super.onPause();
	}
	@Override
	protected void onResume() {
		//adView.resume();

		super.onResume();
	}
	AdView adView;
	private static final String TAG="MainActivity";
	private AQuery aquery;
	private String imagePath;
	MyHelper helper;
	TwoWayView hlview,hlview2;
	SQLiteDatabase db;
	SimilarListAdapterdetail sAdapter;
	RecommendListAdapter rAdapter;
	public ScreenSlidePagerAdapter mPagerAdapter;
	ViewPager pager;
	ArrayList<SimilarItem> slist=new ArrayList<SimilarItem>();
	ArrayList<RecommendItem> rlist;
	SimilarItem sitem;
	RecommendItem ritem;
	String savename;
	EditText msg;
	TextView total;
	String date;
	CheckBox similar,recommend;
	TextView sichat;
	TextView rechat;
	FragmentManager fm=null;
	LinearLayout linear;
	String advice_type,advice_talk;
	String where;
	String popwhere;
	LinearLayout advicepop,pic;
	Button send,cancel;
	private LoginButton authButton;
	 private PlusClient mPlusClient;
	 private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	  private String user_name3,user_pwd3;
	   private ProgressDialog mConnectionProgressDialog;



	   private ConnectionResult mConnectionResult;
	 private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.totallastview);
		linear=(LinearLayout)findViewById(R.id.pagervi);
		linear.setVisibility(View.GONE);
		pic=(LinearLayout)findViewById(R.id.pic);
		

	    
		total=(TextView)findViewById(R.id.imageView1);
		advicepop=(LinearLayout)findViewById(R.id.advicepop);
		advicepop.setVisibility(View.GONE);
		 send=(Button)findViewById(R.id.button1);
		cancel=(Button)findViewById(R.id.button2);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
								String ch=pref.getString("login_check", "");	
				if(!ch.equals("")){
					//로그인상태
					Log.v(TAG,"1");
					advicepop.setVisibility(View.GONE);
					Intent intent= new Intent(KidsMindLastResultActivity.this,KidsMindAdviceActivity.class);
					intent.putExtra("where", "indirect");
					intent.putExtra("date", date);
					intent.putExtra("savename", savename);
					intent.putExtra("advice_type", advice_type);
					startActivity(intent);
					
				}else{
					
				Log.v(TAG, "login");
				
				
				String sel=pref.getString("select", "");
				if(sel.equals("")){
					
				Intent intent = new Intent(KidsMindLastResultActivity.this,
						KidsMindLoginSelectActivity.class);
				editor.putString("lastresult","on");
				editor.commit();
				startActivityForResult(intent, 30);
				}
				else{
					String loginsel=pref.getString("wlogin", "");
					Log.v(TAG,"로그인 어디서 했니"+loginsel);
					//자동로그인
					if("facebook".equals(loginsel)){
						authButton.performClick();
						editor.putString("auth", "");
						editor.commit();
					}else if("google".equals(loginsel)){
						editor.putString("auth", "");
						editor.commit();

						mPlusClient.connect();
						if (!mPlusClient.isConnected()) {
							if (mConnectionResult == null) {
								popupImage(KidsMindLastResultActivity.this);
								loading(KidsMindLastResultActivity.this);
							} else {
								try {
									mConnectionResult.startResolutionForResult(KidsMindLastResultActivity.this, REQUEST_CODE_RESOLVE_ERR);
								} catch (SendIntentException e) {
									// Try connecting again.
									mConnectionResult = null;
									mPlusClient.connect();
								}
							}
						}
				}else{
					Intent intent3 = new Intent(KidsMindLastResultActivity.this,
							KidsMindLoginActivity.class);
					startActivityForResult(intent3,13);
					 
				}
				}
				
				
				
			}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
    		    SharedPreferences.Editor editor=pref.edit();
    		    editor.putString("noti", "");
    		    editor.commit();
				Intent intent=new Intent(KidsMindLastResultActivity.this,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("check", "1");
				startActivity(intent);
				
			}
		});
		//total set image 리절트에 따른 이미지 셋팅
		sichat=(TextView)findViewById(R.id.chat);
		sichat.setVisibility(View.GONE);
		rechat=(TextView)findViewById(R.id.recochat);
		rechat.setVisibility(View.GONE);
		similar=(CheckBox)findViewById(R.id.similar);
	 fm = getSupportFragmentManager();
	 
	 pager=(ViewPager)findViewById(R.id.lastpager);
	 
	
		similar.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					sichat.setVisibility(View.VISIBLE);
				}else{
					sichat.setVisibility(View.GONE);	
				}

			}
		});
		recommend=(CheckBox)findViewById(R.id.recomment);
		recommend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rechat.setVisibility(View.VISIBLE);
				}else{
					rechat.setVisibility(View.GONE);	
				}	
			}
		});

		

		// Assign adapter to HorizontalListView
		


		hlview2=(TwoWayView)findViewById(R.id.list2);
		hlview2.setItemMargin(10);
		hlview2.setLongClickable(true);
		//itemList = new ArrayList<DetailListItem>();

		//selectdb();
		//테스트
		rlist=new ArrayList<RecommendItem>();
		for(int i=0 ; i<10 ;i++){
			ritem= new RecommendItem();
			ritem.setContents("난화 그리기\n"+


"준비물: 화절(8절), 크레파스\n"+


"1.아이가 관심을 가지고 집중할 수 있도록 분위기를 잘 조성하여주세요\n"+

"2.종이 전체에 선을 자유롭게 그려주세요. 단 너무 복잡하게 그리지는 마세요\n"+

"3.다양한 선 안에 무엇이 있을지 연상되는 것들을 찾아보세요. \n"+

"4.다른 종이에 아동이 직접 선을 그어보게 한 후 연상되는 것들을 찾게 해주세요. 예시로 하나를 찾아서 먼저 보여주세요\n"+

"5.여러장을 만들어 아동이 찾아내기 어렵지 않고 지루함을 느끼지 않도록 배려해주세요\n"+


"이런효과가 있어요\n"+

"-정형화되지 않은 선의 조합에서 형태를 찾아내는 활동은 집중력을 길러주고 융통성과 문제 해결 능력을 키워줍니다.\n"+

"-형식과 틀을 배제하고 결과보다는 과정을 중요시하는 단순한 미술활동은 소극적인 아동의 호기심과 흥미를 자극해 참여도를 높여주며 무의식을 표출하고 상상력과 창의력의 발달을 도와줍니다.\n");
			switch(i){
			case 0:
				ritem.setRes(R.drawable.s01);
				break;
			case 1:
				ritem.setRes(R.drawable.s02);
				break;
			case 2:
				ritem.setRes(R.drawable.s03);
				break;
			case 3:
				ritem.setRes(R.drawable.s04);
				break;
			case 4:
				ritem.setRes(R.drawable.s05);
				break;
			case 5:
				ritem.setRes(R.drawable.s05);
			
				break;
				default:
					ritem.setRes(R.drawable.s05);
					break;
			
			}
		
			ritem.setTitle("대인관계");
			rlist.add(ritem);
		}
		
		rAdapter = new RecommendListAdapter(KidsMindLastResultActivity.this,
				R.layout.recolistview, rlist, rlist.size());

		// Assign adapter to HorizontalListView
		hlview2.setAdapter(rAdapter);







		findViewById(R.id.advice).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popwhere="2";
				//의뢰페이지로 전달
				advicepop.setVisibility(View.VISIBLE);
				//popupadvice(KidsMindLastResultActivity.this);
			}
		});
		aquery = new AQuery(this);
		Intent in=getIntent();
		savename=in.getStringExtra("savename");
		SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		
		date=in.getStringExtra("date");
		helper=new MyHelper(KidsMindLastResultActivity.this,"kidsmind.db" , null, 1);
		selectAll(savename);
		editor.putString("fname", savename);
		editor.putString("tdate", date);
		editor.putString("advice_type", advice_type);
		editor.commit();
		
		hlview=(TwoWayView)findViewById(R.id.list);
		hlview.setItemMargin(10);
		hlview.setLongClickable(true);
		//itemList = new ArrayList<DetailListItem>();
		//selectdb();
		String url2 = Const.SIMILAR + questionid +"/"+detail_id ;
		Log.v(TAG, "유사이미지 url" + url2);

		new JobTask().execute(url2);
		authButton = (LoginButton)findViewById(R.id.authButton);
		authButton.setVisibility(View.GONE);

		authButton.setOnErrorListener(new OnErrorListener() {      
			@Override
			public void onError(FacebookException error) {
				Log.v(TAG, "로그인 에러 : " + error);
			}
		});
		//        shareButton.setVisibility(View.VISIBLE);
		authButton.setReadPermissions(Arrays.asList("basic_info","email"));


		authButton.setSessionStatusCallback(new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) { 
				if (session.isOpened()) {



					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,Response response) {    
							if (user != null) { 

								// 로그인 성공 (user에 정보가 들어있음.)
								Log.v(TAG,"User ID "+ user.getId());
								Log.v(TAG,"Email "+ user.asMap().get("email"));
								Log.v(TAG,"name "+ user.asMap().get("name"));
								user_name3= user.asMap().get("email").toString();
								user_pwd3="Qce5cPoBrUhZu5LF5UFADzGUno";
								Log.v(TAG,"facebook23");
								//asyncLoginJson(user_name3,user_pwd3);
								SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
								SharedPreferences.Editor editor= pref.edit();
								editor.putString("where", "indirect");
								editor.putString("date", date);
								editor.putString("savename", savename);
								editor.putString("advice_type", advice_type);
								advicepop.setVisibility(View.GONE);
								editor.putString("lfacebook", "on");
								editor.putString("auth","auth");
								editor.putString("wlogin", "facebook");
								editor.putString("user_name", user_name3);
								editor.putString("user_pwd", user_pwd3);
								editor.commit();

							}
						}
					});
				}else{

				}
			}
		});
		mPlusClient = new PlusClient.Builder(this, this, this)
		.setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
		.setScopes(Scopes.PLUS_LOGIN)  // recommended login scope for social features
		// .setScopes("profile")       // alternative basic login scope
		.build();
		// Progress bar to be displayed if the connection failure is not resolved.
		//mConnectionProgressDialog = new ProgressDialog(this);
		//mConnectionProgressDialog.setMessage("Signing in...");
		//테스트
		
//		slist=new ArrayList<SimilarItem>();
//		for(int i=0 ; i<10 ;i++){
//			sitem= new SimilarItem();
////			sitem.setAdvice_contents("이아이의 그림은 정말 좋아요");
////			sitem.setDetail_id("M!");
////			sitem.setRes(R.drawable.d002);
////			sitem.setImage_title("나무그리기");
////			sitem.setUser_age("10세 남아");
//			
//			slist.add(sitem);
//		}
		sAdapter = new SimilarListAdapterdetail(KidsMindLastResultActivity.this,
				R.layout.similarlistview, slist, slist.size());
		hlview.setAdapter(sAdapter);
		total.setText(advice_talk);
//		mPagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());
//		pager.setAdapter(mPagerAdapter);// TODO Auto-generated method stub
		hlview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				pic.setVisibility(View.GONE);
				popwhere="1";
				where="1";
				linear.setVisibility(View.VISIBLE);
				mPagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());
				pager.setAdapter(mPagerAdapter);// TODO Auto-generated method stub
				
			}
		});
		hlview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				pic.setVisibility(View.VISIBLE);
				popwhere="1";
				where="2";
				linear.setVisibility(View.VISIBLE);
				mPagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());
				pager.setAdapter(mPagerAdapter);// TODO Auto-generated method stub
				
			}
		});
	}
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		//  mPlusClient.connect();
	}
	Dialog dialog =null;
	void popupImage(Activity context)
	{
		// Create dialog
		 dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.progress);
		dialog.findViewById(R.id.imageView1);
		dialog.findViewById(R.id.imageView1).setVisibility(ImageView.VISIBLE);
		dialog.findViewById(R.id.imageView1).setBackgroundResource(R.anim.progress);

		AnimationDrawable frameAnimation = (AnimationDrawable) dialog.findViewById(R.id.imageView1).getBackground();
		frameAnimation.start();
		//라디오 버튼 
		dialog.show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		//	      if (mConnectionProgressDialog.isShowing()) {
		// The user clicked the sign-in button already. Start to resolve
		// connection errors. Wait until onConnected() to dismiss the
		// connection dialog.
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			} catch (SendIntentException e) {
				mPlusClient.connect();
			}
		}
		//	      }
		// Save the result and resolve the connection failure upon a user click.
		mConnectionResult = result;
	}


	String accountName;
	@Override
	public void onConnected(Bundle connectionHint) {
		accountName = mPlusClient.getAccountName();
		Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
		user_name3=accountName;
		user_pwd3="AIzaSyAlkq6NLiwn";

		SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor= pref.edit();
		editor.putString("wlogin", "google");
		editor.putString("user_pwd", user_pwd3);
		editor.putString("auth","");
		editor.commit();

		asyncLoginJson(user_name3, user_pwd3);
		//	        
		//	        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
		//	            @Override
		//	            protected String doInBackground(Void... params) {
		//	                String token = null;
		//
		//	                try {
		//	                    token = GoogleAuthUtil.getToken(
		//	                            KidsMindLoginSelectActivity.this,
		//	                            mPlusClient.getAccountName(),
		//	                            SCOPE);
		//	                } catch (IOException transientEx) {
		//	                    // Network or server error, try later
		//	                    Log.e(TAG, transientEx.toString());
		//	                } catch (UserRecoverableAuthException e) {
		//	                    // Recover (with e.getIntent())
		//	                    Log.e(TAG, e.toString());
		//	                    Intent recover = e.getIntent();
		//	                   // startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
		//	                } catch (GoogleAuthException authEx) {
		//	                    // The call is not ever expected to succeed
		//	                    // assuming you have already verified that 
		//	                    // Google Play services is installed.
		//	                    Log.e(TAG, authEx.toString());
		//	                }
		//
		//	                return token;
		//	            }
		//
		//	            @Override
		//	            protected void onPostExecute(String token) {
		//	                Log.i(TAG, "Access token retrieved:" + token);
		//	            }
		//
		//	        };
		//	        task.execute();

		//getTask(KidsMindLoginSelectActivity.this, accountName, SCOPE).execute();

	}

	public void asyncLoginJson(String user_name, String user_pwd) {
		//openWaitDialog();

		String url = Const.LOGIN_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		map.put("user_pwd", user_pwd);

		aquery.ajax(url, map, JSONObject.class, this, "jsonLoginCallback");

		//sendView.setText(url);
	}

	public void jsonLoginCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);
				ldialog.dismiss();
				
				if (isSuccess) {
					dialog.dismiss();
					Log.v(TAG,"재로그인 성공");
					int user_id = json.getInt("user_id");
					String user_name = json.getString("user_name");
					String authkey = json.getString("authkey");

					//asyncLoginJson(user_name3, user_pwd3);
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("login_check", "checked");
					editor.putInt("user_id",user_id );
					editor.putString("user_name", user_name);
					editor.putString("authkey", authkey);
					editor.commit();
					//String path=pref.getString("path", null);
//					String whe=pref.getString("last", "");
//					if("".equals(whe)){
//					Intent intent=new Intent(KidsMindLoginActivity.this,MainActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					}else{
//						KidsMindLoginActivity.this.setResult(RESULT_OK);
//							
//					}
					Log.v(TAG,"2");
					Intent intent= new Intent(KidsMindLastResultActivity.this,KidsMindAdviceActivity.class);
					intent.putExtra("where", "indirect");
					intent.putExtra("date", date);
					intent.putExtra("savename", savename);
					intent.putExtra("advice_type", advice_type);
					startActivity(intent);
					


					//resultView.setText(json.toString());
				} else {
					Log.v(TAG,"재로그인 실패");
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					if(error.contains("이미")){
						asyncLogoutJson(user_name3, user_pwd3);
					}else{
						openInfoMessageDialogBox(error);
						
					}
					
				}
			} catch (JSONException e) {
				openErrorDialog();
				e.printStackTrace();
				Log.v(TAG,"json execption");
			}
		} else {
			Log.v(TAG,"json null");
			openErrorDialog();
		}
	}
	public void openInfoMessageDialogBox(String message) {
		Toast.makeText(KidsMindLastResultActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	protected void openErrorDialog() { 
		//closeWaitDialog();
		aquery.ajaxCancel();

		openInfoMessageDialogBox("error.");
	}
	public void asyncLogoutJson(String user_name, String user_pwd) {
		// openWaitDialog();

		String url = Const.LOGOUT_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		map.put("user_pwd", user_pwd);

		aquery.ajax(url, map, JSONObject.class, this, "jsonLogoutCallback");

		// sendView.setText(url);
	}

	public void jsonLogoutCallback(String url, JSONObject json,
			AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				// closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(
						Const.SUCCESS);

				if (isSuccess) {
					
					Log.v(TAG, "logout성공");
					asyncLoginJson(user_name3, user_pwd3);
					
				} else {
					String error = json.getString("error");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
		}
	}
	Dialog ldialog=null;
	void loading(Activity context)
	{
		// Create dialog
		 ldialog = new Dialog(context);
		ldialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		ldialog.setCancelable(false);
		ldialog.setCanceledOnTouchOutside(false);
		ldialog.setContentView(R.layout.progress);
		ldialog.findViewById(R.id.imageView1);
		ldialog.findViewById(R.id.imageView1).setVisibility(ImageView.VISIBLE);
		ldialog.findViewById(R.id.imageView1).setBackgroundResource(R.anim.progress);

		AnimationDrawable frameAnimation = (AnimationDrawable) ldialog.findViewById(R.id.imageView1).getBackground();
		frameAnimation.start();
		//라디오 버튼 
		ldialog.show();
	}

	void popupadvice(Activity context) {
		// Create dialog
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable

		(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		dialog.setContentView(R.layout.advicedialog);
		dialog.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				
//				String ch=pref.getString("login_check", "");	
//				if(!ch.equals("")){
//					//로그인상태
//					Intent intent= new Intent(KidsMindLastResultActivity.this,KidsMindAdviceActivity.class);
//					intent.putExtra("where", "indirect");
//					intent.putExtra("date", date);
//					intent.putExtra("savename", savename);
//					intent.putExtra("advice_type", advice_type);
//					startActivity(intent);
//					
//				}else{
//					Intent in=new Intent(KidsMindLastResultActivity.this,KidsMindLoginSelectActivity.class);
//						SharedPreferences.Editor editor=pref.edit();
//									editor.putString("last", "last");
//									editor.commit();
//									
//					startActivityForResult(in, 0);
//				}
//				dialog.dismiss();
	
				
			}
		});
		dialog.findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(Session.getActiveSession()!=null)
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			//Log.
			if(requestCode==30||requestCode==13){
				SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
				SharedPreferences.Editor editor=pref.edit();
				
				savename=pref.getString("fname", "");
				date=pref.getString("date", "");
				advice_type=pref.getString("advice_type", "");
				
				Intent intent= new Intent(KidsMindLastResultActivity.this,KidsMindAdviceActivity.class);
				Log.v(TAG,"3");
				intent.putExtra("where", "indirect");
				intent.putExtra("date", date);
				intent.putExtra("savename", savename);
				intent.putExtra("advice_type", advice_type);
				startActivity(intent);
				//startActivity(intent);
			}	else if(requestCode ==REQUEST_CODE_RESOLVE_ERR){
				mConnectionResult = null;
				mPlusClient.connect();
			}
		}
	}
	private File uploadFile ;
	private void asyncImageUploadJson(String msg) {






		// uploadFileExecute(uploadFile);
		SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();

		int userId = pref.getInt("user_id", 0);
		String questionid=pref.getString("qposition", "");
		Map<String, Object> params = new HashMap<String, Object>();
		uploadFile= new File(Environment.getExternalStorageDirectory(), "/KidsMind/"+savename);
		Log.v(TAG,"파일 용량 =>"+ String.valueOf(uploadFile.length()));
		params.put("image", uploadFile);
		params.put("message",msg);

		aquery.ajax(Const.IMAGE_CHANGE_URL + "/" + userId+"/"+questionid, params, JSONObject.class, this, "jsonImageUploadCallback");
	}

	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public SparseArray<Fragment> fragMap = new SparseArray<Fragment>();
		
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
        }
	
        @Override
        public int getCount() {
        	if("1".equals(where)){
            return slist.size();
        	}else{
        		return rlist.size();
        	}
        }
        
		@Override
		public Fragment getItem(int position) {
			if("1".equals(where)){
		        
				LastSimilarImage frag = new LastSimilarImage();
				frag.setPosition(position);
				frag.setDate(slist);
				return frag;
				}else{
					LastRecommendImage frag =new LastRecommendImage();
					frag.setPosition(position);
					frag.setDate(rlist);
					return frag;
				}
		}
	}

	public void jsonImageUploadCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					int advice_id = json.getInt("advice_id");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putInt("advice_id", advice_id);
					editor.commit();
					Log.v(Debugc.getTagd(),"advice_id"+advice_id);
					//사
					updateRec(savename,advice_id);
					//openInfoMessageDialogBox("상담 요청 하였습니다.");

				} else {
					String error = json.getString("error");
					//openInfoMessageDialogBox(error);
				}
			} catch (JSONException e) {
				//openErrorDialog();
				e.printStackTrace();
			}
		} else {
			//openErrorDialog();
		}
	}
	public void asynccommentJson(String msg) {

		//	openWaitDialog();

		String url = Const.USER_COMMENT;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("comment", msg);
		SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		int userId = pref.getInt("user_id", 0);
		int adviceId=pref.getInt("advice_id", 0);
		Log.v(TAG,"userid"+userId+""+adviceId+"");
		String userid=userId+"";
		String adviceid=adviceId+"";
		url=url+userid+"/"+adviceid;
		Log.v(TAG,"댓글url"+url);
		aquery.ajax(url, map, JSONObject.class, this, "jsonJoinCallback");

		//sendView.setText(url);
	}


	public void jsonJoinCallback(String url, JSONObject json, AjaxStatus status) {

		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					Log.v(TAG,"댓글달림");
					//				int user_id = json.getInt("user_id");
					//				String user_name = json.getString("user_name");
					//				String authkey = json.getString("authkey");
					//				
					//				Log.v(Debugc.getTagd(),"user_id"+user_id);
					//				Log.v(Debugc.getTagd(),"authkey"+authkey);
					//				SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					//				SharedPreferences.Editor editor=pref.edit();
					//				editor.putString("first", "first");
					//				editor.putString("login_check", "checked");
					//				editor.putString("user_name", user_name);
					//				editor.putInt("user_id", user_id);
					//				editor.putString("authkey", authkey);
					//				editor.putString("user_pwd", user_pwd3);
					//				editor.commit();
					//				
					//asyncAutoLoginJson(user_id, authkey);
					//	Log.v(TAG,"authkey"+authkey+"user_name"+user_name+"user_id"+user_id+"");
					//openInfoMessageDialogBox("로그인 성공");
					//	KidsMindLoginActivity.this.setResult(RESULT_OK);
					finish();


					//resultView.setText(json.toString());
				} else {
					Log.v(TAG,"에러");
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					//		openInfoMessageDialogBox("error");
				}
			} catch (JSONException e) {
				//		openErrorDialog();
				Log.v(TAG,"e"+e);
				e.printStackTrace();
			}
		} else {
			Log.v(TAG,"json null");
			//		openErrorDialog();
		}
	}
	String questionid,detail_id;
	public void selectAll(String image){
		openDB();
	
		//String sql ="select tag_name from md_question_tag where question_id=Q"+po+";";
		Cursor c=null;
		String wStr="fName=?";
		String[] wherStr={image};
		String[] colNames={"question_id","detail_id","advice_type","advice_talk"};
		try{
			c=db.query("km_check", colNames, wStr, wherStr, null, null, null);
			while(c.moveToNext()){
				questionid=c.getString(c.getColumnIndex("question_id"));
				detail_id=c.getString(c.getColumnIndex("detail_id"));
				advice_talk=c.getString(c.getColumnIndex("advice_talk"));
				Log.v(TAG,"advice_talk DB"+advice_talk);
				advice_type=c.getString(c.getColumnIndex("advice_type"));
				Log.v(TAG,"advice_type DB"+advice_type);
			}

		}catch(SQLException e){
			Log.v(TAG,"selec error"+e);
		}finally{
			if(c!=null){
				c.close();
			}
		}
		

	}
	void updateRec(String wStr,  int newLName){
		openDB();

		//String sql="update tmember set age =32 where fname like '%k%';";
		ContentValues values = new ContentValues();
		values.put("advice_id", newLName);

		String whereClause="fName like ?";
		String[] whereArgs={"%" + wStr + "%"};
		try{

			int cnt=db.update("km_check", values, whereClause, whereArgs);


			Log.v(Debugc.getTagad(),"advice_id업데이트중"+cnt+"");
		}catch(SQLException e){
			Log.v(Debugc.getTagad(),"update error"+e);
		}

		closeDB();
	}
	
class JobTask extends AsyncTask<String, Void, SItem2> {

	@Override
	protected SItem2 doInBackground(String... params) {
		SItem2 advice = null;
		String url = params[0];

		Log.v(Debugc.getTagd(), "urlurl" + url);
		HttpClient client = null;
		HttpGet request = null; // go catch
		HttpResponse response = null;
		BufferedReader br = null;
		int code = 0;
		try {
			client = NetManager.getHttpClient();
			request = NetManager.getGet(url);
			response = client.execute(request);
			code = response.getStatusLine().getStatusCode();
			switch (code) {
			case 200:
				br = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				String sTemp = "";
				while ((sTemp = br.readLine()) != null) {
					// Log.v(TAG, "aa");
					sb.append(sTemp).append("\n");
				}

				advice = SimilarJsonparser.parse(sb.toString());
				// adapter.setData(data);

				// data = sb.toString();

				Log.v(Debugc.getTaga(),
						"=======================================================================================");
				Log.v(Debugc.getTaga(), "comment : " + advice.toString());
				Log.v(Debugc.getTaga(),
						"===============================================================================================");

				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}

		return advice;
	}

	@Override
	protected void onPostExecute(SItem2 result) {
		// if(CDialog dialog != null)
		// {
		// pDialog.cancel();
		// }

		if (result != null) {

			slist = result.getData();
			int cnt = slist.size();
			

			sAdapter.setList(slist);
			sAdapter.notifyDataSetChanged();
			// int
			// height=(getResources().getDimensionPixelSize(R.dimen.list_item_size)+1)*data.size();
			// LayoutParams lp=(LayoutParams) list.getLayoutParams();
			// lp.height=height;
			// list.setLayoutParams(lp);

		}
		super.onPostExecute(result);
	}

}

	void openDB(){
		//db = openOrCreateDatabase("sample.db", wi, null);
		db = helper.getWritableDatabase();
	}
	// dbClose();
	void closeDB(){
		if(db != null){
			if(db.isOpen()){
				db.close();
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	if("1".equals(popwhere)){
	    	switch(linear.getVisibility()){
	    	case View.GONE:
	    		
	    		finish();
	    		break;
	    	case View.VISIBLE:
	    		linear.setVisibility(View.GONE);
	    		
	    		break;
	    	}
	    	}else{
	    		switch (advicepop.getVisibility()){
	    		case View.GONE:
	    			finish();
	    			break;
	    		case View.VISIBLE:
	    			advicepop.setVisibility(View.GONE);
	    			break;
	    		}
	    	}
	    }
	    return true;
	}
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
}