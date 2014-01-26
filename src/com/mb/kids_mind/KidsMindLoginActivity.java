package com.mb.kids_mind;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;

public class KidsMindLoginActivity extends Activity {

private AQuery aquery;
private EditText id,pw;
private static final String TAG = "MainActivity";
public static final String EXTRA_MESSAGE = "message";
public static final String PROPERTY_REG_ID = "registration_id";
private static final String PROPERTY_APP_VERSION = "appVersion";
private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

String SENDER_ID = "908544295302";

String regId = "";
TextView mDisplay;

AtomicInteger msgId = new AtomicInteger();
SharedPreferences prefs;
Context context;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialoglogin);
		id=(EditText)findViewById(R.id.editText1);
		pw=(EditText)findViewById(R.id.editText2);
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
			String user_name3 =id.getText().toString();
			String user_pwd3 = pw.getText().toString();
			asyncJoinJson(user_name3, user_pwd3);
			finish();
		}
	});
		regId = getRegistrationId(this);
		if(regId.equals("")){
			Log.v(TAG, "exist");
		//	registerInBackground();
		}else{

			Log.v(TAG, " regId : " + regId);
		}
	    
		aquery = new AQuery(this);
	}
//	private void unRegisterInBackground() {
//	new AsyncTask<Void, Void, String>() {
//		@Override
//		protected String doInBackground(Void... params) {
//			String msg = "";
//			try {
//				if (gcm == null) {
//					gcm = GoogleCloudMessaging.getInstance(MindDrawingLoginActivity.this);
//				}
//				gcm.unregister();
//				Log.v(TAG, "Device unregister, : " + regId);
//				storeRegistrationId(MindDrawingLoginActivity.this, "");
//			} catch (IOException ex) {
//				msg = "Error :" + ex.getMessage();
//				// If there is an error, don't just keep trying to register.
//				// Require the user to click a button again, or perform
//				// exponential back-off.
//				
//			}
//			return "";
//
//		}
//	}.execute();
//}
//private void registerInBackground() {
//
//	new AsyncTask<Void, Void, String>() {
//
//		@Override
//		protected String doInBackground(Void... params) {
//			String msg = "";
//			L
//			try {
//				if (gcm == null) {
//					gcm = GoogleCloudMessaging.getInstance(MindDrawingLoginActivity.this);
//				}
//				
//				regId = gcm.register(SENDER_ID);
//				
//				storeRegistrationId(MindDrawingLoginActivity.this, regId);
//				
//			} catch (IOException ex) {
//				msg = "Error :" + ex.getMessage();
//				// If there is an error, don't just keep trying to register.
//				// Require the user to click a button again, or perform
//				// exponential back-off.
//				Log.v(TAG, msg);
//			}
//			return msg;
//		}
//	}.execute();
//}
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
		Toast.makeText(KidsMindLoginActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	protected void openErrorDialog() { 
		//closeWaitDialog();
		aquery.ajaxCancel();

		openInfoMessageDialogBox("error.");
	}

	// ===========================================

	
	public void asyncNicknameCheckJson(String name) {
		//openWaitDialog();

		String url = Const.NAME_CHECK_PATH + "/" + name;
		aquery.ajax(url, JSONObject.class, this, "jsonNicknameCheckCallback");

		//sendView.setText(url);
	}

	/**
	 * �г��� �˻��Ŀ��� isSucess�� ���
	 */
	String error;
	public void jsonNicknameCheckCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
				

					//	resultView.setText(json.toString());
				} else {
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
					error = json.getString("error");
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
					
					Log.v(Debugc.getTagd(),"user_id"+user_id);
					Log.v(Debugc.getTagd(),"authkey"+authkey);
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("login_check", "checked");
					editor.putString("user_name", user_name);
					editor.putInt("user_id", user_id);
					editor.putString("authkey", authkey);
					editor.commit();
					//asyncAutoLoginJson(user_id, authkey);
					Log.v(TAG,"authkey"+authkey+"user_name"+user_name);
					openInfoMessageDialogBox("로그인 성공");
					//finish();

					//resultView.setText(json.toString());
				} else {
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
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

	/**
	 * �ڵ� �α���
	 * 
	 * @param user_id
	 * @param authkey
	 */
	public void asyncAutoLoginJson(int user_id, String authkey) {
		
		String url = Const.AUTO_LOGIN_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_id", String.valueOf(user_id));
		map.put("authkey", authkey);

		aquery.ajax(url, map, JSONObject.class, this, "jsonAutoLoginCallback");

		//sendView.setText(url);
	}

	/**
	 * �ڵ� �α����� Ȥ�ó� Ȯ�θ�
	 */
	public void jsonAutoLoginCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					int user_id = json.getInt("user_id");
					String user_name = json.getString("user_name");
					String authkey = json.getString("authkey");

				

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
					int user_id = json.getInt("user_id");
					String user_name = json.getString("user_name");
					String authkey = json.getString("authkey");
					String user_name3 =id.getText().toString();
					String user_pwd3 = pw.getText().toString();

					asyncLoginJson(user_name3, user_pwd3);
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();

					editor.putInt("user_id",user_id );
					editor.putString("user_name", user_name);
					editor.putString("authkey", authkey);
					editor.commit();
					//String path=pref.getString("path", null);

					

					//resultView.setText(json.toString());
				} else {
					String error = json.getString("error");
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("acheck", null);
					editor.commit();
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

	/**
	 * �α׾ƿ�
	 * 
	 * @param user_name
	 * @param user_pwd
	 */
	public void asyncLogoutJson(String user_name, String user_pwd) {
		//openWaitDialog();

		String url = Const.LOGOUT_PATH;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		map.put("user_pwd", user_pwd);

		aquery.ajax(url, map, JSONObject.class, this, "jsonLogoutCallback");

		//sendView.setText(url);
	}

	
	public void jsonLogoutCallback(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				aquery.ajaxCancel();
				//closeWaitDialog();

				boolean isSuccess = json.getString("result").equals(Const.SUCCESS);

				if (isSuccess) {
					SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("login_check", "");
					editor.putString("authkey", null);
					editor.commit();
					//resultView.setText(json.toString());
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

}
