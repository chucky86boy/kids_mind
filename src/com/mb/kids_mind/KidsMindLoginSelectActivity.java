package com.mb.kids_mind;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusClient;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;

public class KidsMindLoginSelectActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener {
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	private LoginButton authButton;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	private AQuery aquery;
	Context context;
	private String user_name3=null,user_pwd3=null;
	String SENDER_ID = "364625981181";
	String regId = "";
	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	View.OnClickListener bHandler =new OnClickListener() {

		@Override
		public void onClick(View v) {

			SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
			switch(v.getId()){
			case R.id.createaccount://계정생성

				editor.putString("auth", "");
				editor.commit();
				Intent in=new Intent(KidsMindLoginSelectActivity.this,KidsMindAccountActivity.class);
				in.putExtra("wh", "c");
				startActivityForResult(in, 1);

				break;
			case R.id.facebook://페이스북 로긴
				authButton.performClick();
				editor.putString("auth", "");
				editor.commit();
				//		        if (mPlusClient.isConnected()) {
				//		            mPlusClient.clearDefaultAccount();
				//		            mPlusClient.disconnect();
				//		            mPlusClient.connect();
				//		            Toast.makeText(getApplicationContext(), "로그아웃",Toast.LENGTH_SHORT).show();
				//		        }

				break;
			case R.id.googleaccount://구글 로긴
				editor.putString("auth", "");
				editor.commit();

				mPlusClient.connect();
				if (!mPlusClient.isConnected()) {
					if (mConnectionResult == null) {
						// mConnectionProgressDialog.show();
					} else {
						try {
							mConnectionResult.startResolutionForResult(KidsMindLoginSelectActivity.this, REQUEST_CODE_RESOLVE_ERR);
						} catch (SendIntentException e) {
							// Try connecting again.
							mConnectionResult = null;
							mPlusClient.connect();
						}
					}
				}
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
		Button btn=(Button)findViewById(R.id.facebook);
		btn.setVisibility(View.GONE);
		findViewById(R.id.googleaccount).setOnClickListener(bHandler);

		// TODO Auto-generated method stub
		
		aquery = new AQuery(this);
		regId = getRegistrationId(this);
		if(regId.equals("")){
			Log.v(TAG, "없어요");
			registerInBackground();
		}else{

			Log.v(TAG, " regId : " + regId);
		}
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
								asyncNicknameCheckJson(user_name3);
								SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
								SharedPreferences.Editor editor= pref.edit();
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

	private static final String TAG="MainActivity";
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

		asyncNicknameCheckJson(accountName);
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



	@Override
	public void onDisconnected() {
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(Session.getActiveSession()!=null)
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

		if(resultCode==RESULT_OK){
			//Log.
			if(requestCode==1){
				KidsMindLoginSelectActivity.this.setResult(RESULT_OK);
				finish();

			}
			else if(requestCode ==REQUEST_CODE_RESOLVE_ERR){
				mConnectionResult = null;
				mPlusClient.connect();
			}
		}
	}

	@Override
	protected void onResume() {
		SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		String a=pref.getString("auth", "");
		Log.v(TAG,"onresume"+a);
		if("auth".equals(a)){
			authButton.performClick();
		}
		super.onResume();
	}
	private void unRegisterInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(KidsMindLoginSelectActivity.this);
					}
					gcm.unregister();
					Log.v(TAG, "Device unregister, : " + regId);
					storeRegistrationId(KidsMindLoginSelectActivity.this, "");
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.

				}
				return "";

			}
		}.execute();
	}

	private void registerInBackground() {

		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String msg = "";

				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(KidsMindLoginSelectActivity.this);
					}

					regId = gcm.register(SENDER_ID);
					Log.v(TAG,regId);
					storeRegistrationId(KidsMindLoginSelectActivity.this, regId);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
					Log.v(TAG, msg);
				}
				return msg;
			}
		}.execute();
	}
	public void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences("pref", 0);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("regId", regId);
		editor.commit();
	}
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences("pref", 0);
		String registrationId = prefs.getString("regId", "");
		if (registrationId.isEmpty()) {
			Log.v(TAG, "Registration not found.");
			return "";
		}
		return registrationId;
	}
	private AlertDialog messageDialog;

	public void openInfoMessageDialogBox(String message) {
		Toast.makeText(KidsMindLoginSelectActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	protected void openErrorDialog() { 
		//closeWaitDialog();
		aquery.ajaxCancel();

		openInfoMessageDialogBox("error.");
	}

	// ===========================================


	public void asyncNicknameCheckJson(String name) {
		//openWaitDialog();

		//String url = "http://localhost:3083/namecheck" + "/" + name;
		String url=Const.NAME_CHECK_PATH+"/"+name;
		Log.v(TAG,"URL"+url);
		aquery.ajax(url, JSONObject.class, this, "jsonNicknameCheckCallback");

		//sendView.setText(url);
	}


	String error;
	public void jsonNicknameCheckCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					Log.v(TAG,"닉네임중복 없음");
					asyncJoinJson(user_name3, user_pwd3);


					//	resultView.setText(json.toString());
				} else {
					Log.v(TAG,"중복있음");
					asyncLoginJson(user_name3, user_pwd3);
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					error = json.getString("error");
					openInfoMessageDialogBox(error);
				}
			} catch (JSONException e) {
				Log.v(TAG,"에러");
				openErrorDialog();
				e.printStackTrace();
			}
		} else {
			Log.v(TAG,"에러2");
			openErrorDialog();
		}
	}
	public void asyncJoinJson(String user_name, String user_pwd) {

		//		openWaitDialog();

		String url = Const.JOIN_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		map.put("user_pwd", user_pwd);

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

					int user_id = json.getInt("user_id");
					String user_name = json.getString("user_name");
					String authkey = json.getString("authkey");

					Log.v(TAG,"z키즈마인드user_id"+user_id);
					Log.v(Debugc.getTagd(),"authkey"+authkey);
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("first", "first");
					editor.putString("login_check", "checked");
					editor.putString("user_name", user_name);
					editor.putInt("user_id", user_id);
				

					editor.putString("authkey", authkey);

					editor.commit();

					asyncAutoLoginJson(user_id,regId);
					Log.v(TAG,"authkey"+authkey+"user_name"+user_name+"user_id"+user_id+"");
					openInfoMessageDialogBox("로그인 성공");

					//					KidsMindLoginActivity.this.setResult(RESULT_OK);
					//					finish();


					//resultView.setText(json.toString());
				} else {
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					openInfoMessageDialogBox("error");
				}
			} catch (JSONException e) {
				openErrorDialog();
				e.printStackTrace();
			}
		} else {
			openErrorDialog();
		}
	}

	/**
	 * �ڵ� �α���
	 * 
	 * @param user_id
	 * @param authkey
	 */
	public void asyncAutoLoginJson(int user_id, String authkey) {

		String url = Const.GCM;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_id", String.valueOf(user_id));
		map.put("regId", authkey);

		aquery.ajax(url, map, JSONObject.class, this, "jsonAutoLoginCallback");

		//sendView.setText(url);
	}


	public void jsonAutoLoginCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();


				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					//					int user_id = json.getInt("user_id");
					//					String user_name = json.getString("user_name");
					//					String authkey = json.getString("authkey");
					Log.v(TAG,"gcm_등록");
					//					Intent intent=new Intent(KidsMindLoginActivity.this,MainActivity.class);
					//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//					startActivity(intent);
					//				finish();
					SharedPreferences pref =getSharedPreferences("pref", MODE_PRIVATE);
					SharedPreferences.Editor editor =pref.edit();
					editor.putString("select", "on");
					editor.commit();
					String wh=pref.getString("doctor", "");
					String wha=pref.getString("lastresult", "");
					if(!"".equals(wh)||!"".equals(wha)){
						KidsMindLoginSelectActivity.this.setResult(RESULT_OK);
						finish();
					}else{
						editor.putString("noti", "");
		    		    editor.commit();
					Intent intent=new Intent(KidsMindLoginSelectActivity.this,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					}
					

					//	resultView.setText(json.toString());
				} else {
					String error = json.getString("error");
					openInfoMessageDialogBox(error);
				}
			} catch (JSONException e) {
				openErrorDialog();
				e.printStackTrace();
			}
		} else {
			openErrorDialog();
		}
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

				if (isSuccess) {
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
					editor.putString("user_pwd", user_pwd3);
					editor.putString("authkey", authkey);
					editor.putString("select", "on");
					editor.commit();
					//String path=pref.getString("path", null);
					String whe=pref.getString("last", "");
					String wh=pref.getString("doctor", "");
//					if("".equals(whe)){
//						Intent intent=new Intent(KidsMindLoginSelectActivity.this,MainActivity.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);
//					}else{
//						KidsMindLoginSelectActivity.this.setResult(RESULT_OK);
//
//					}
				
					Log.v(TAG,"doctor"+wh);
					if(!"".equals(wh)){
						KidsMindLoginSelectActivity.this.setResult(RESULT_OK);
						finish();
					}else{
						editor.putString("noti", "");
		    		    editor.commit();
					Intent intent=new Intent(KidsMindLoginSelectActivity.this,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					}

					


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
						openInfoMessageDialogBox(error+"또는 E-Mail로 가입하셨습니다");

						if (mPlusClient.isConnected()) {
							mPlusClient.clearDefaultAccount();
							mPlusClient.disconnect();
							mPlusClient.connect();
						}
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


	private AbstractGetNameTask getTask(
			KidsMindLoginSelectActivity activity, String email, String scope) {


		return new GetNameInBackgroundWithSync(activity, email, scope);


	}
}
