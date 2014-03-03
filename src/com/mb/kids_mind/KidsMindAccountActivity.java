


package com.mb.kids_mind;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mb.kids_mind.Item.BabyInformationItem;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;

public class KidsMindAccountActivity extends Activity {

	private AQuery aquery;
	private EditText id,pw,cpw;
	private static final String TAG = "MainActivity";
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	String SENDER_ID = "364625981181";
	SharedPreferences pref;
	String regId = "";
	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;
	private String user_name3=null,user_pwd3=null,user_cpwd3=null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			setContentView(R.layout.dialoglaccount);
			
			LinearLayout bg= (LinearLayout)findViewById(R.id.bg);
			
			
			id=(EditText)findViewById(R.id.editText1);
			pw=(EditText)findViewById(R.id.editText2);
			cpw=(EditText)findViewById(R.id.editText3);
			aquery = new AQuery(this);
		
			bg.setBackgroundResource(R.drawable.bg_join);
			pref=getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor=pref.edit();
		
			findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();	
				}
			});
			findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					user_name3 =id.getText().toString();
					user_pwd3 = pw.getText().toString();
					user_cpwd3=cpw.getText().toString();
					Log.v(TAG,"username"+user_name3+"userpwd"+user_pwd3+"user확인"+user_cpwd3);
					if(user_name3.contains("@")&&user_name3.contains(".")&&user_cpwd3.equals(user_pwd3)){
						String auth=pref.getString("authkey", "");
						String first=pref.getString("first", "");
						int user_id=pref.getInt("user_id", 0);
						//if("".equals(auth)){
						Log.v(TAG,"1");
						Log.v(TAG,"가입시user_name3"+user_name3+"user_pwd"+user_pwd3);
						asyncNicknameCheckJson(user_name3);
					}else  if(!user_cpwd3.equals(user_cpwd3)){
						Toast.makeText(KidsMindAccountActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
								
					}else{
						Toast.makeText(KidsMindAccountActivity.this, "유효하지 않은 메일 형식 입니다", Toast.LENGTH_SHORT).show();
					}

				}
			});
			regId = getRegistrationId(this);
			if(regId.equals("")){
				Log.v(TAG, "없어요");
				registerInBackground();
			}else{

				Log.v(TAG, " regId : " + regId);
			}
	

	}
	private void unRegisterInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(KidsMindAccountActivity.this);
					}
					gcm.unregister();
					Log.v(TAG, "Device unregister, : " + regId);
					storeRegistrationId(KidsMindAccountActivity.this, "");
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
						gcm = GoogleCloudMessaging.getInstance(KidsMindAccountActivity.this);
					}

					regId = gcm.register(SENDER_ID);
					Log.v(TAG,regId);
					storeRegistrationId(KidsMindAccountActivity.this, regId);

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
		Toast.makeText(KidsMindAccountActivity.this, message, Toast.LENGTH_SHORT).show();
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
					editor.putString("wlogin", "kidsmind");
					
					editor.putString("authkey", authkey);
					editor.putString("user_pwd", user_pwd3);
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
					pref=getSharedPreferences("pref", MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("select","on");
					editor.putString("wlogin", "account");
					editor.putString("noti", "");
	    		    editor.commit();
					editor.commit();
					
					Intent intent=new Intent(KidsMindAccountActivity.this,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					
					finish();

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
					editor.putString("authkey", authkey);
					editor.putString("noti", "");
	    		    editor.commit();
					editor.commit();
					//String path=pref.getString("path", null);
					String whe=pref.getString("last", "");
					if("".equals(whe)){
					Intent intent=new Intent(KidsMindAccountActivity.this,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					}else{
						KidsMindAccountActivity.this.setResult(RESULT_OK);
							
					}
					
					
					finish();


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
					}
					//openInfoMessageDialogBox(error);
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


}

