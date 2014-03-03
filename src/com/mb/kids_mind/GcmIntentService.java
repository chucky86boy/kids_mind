/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mb.kids_mind;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "MainActivity";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.v(TAG, "messageType"+messageType);
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification("Received: " + extras.toString());
            	
                if(PushWakeLock.ScreenOn(this)){
                	Log.v(TAG,"toast");
                	  mHandler.post(new ToastRunnable("",this));
                  
                	
                }else{
                	 PushWakeLock.acquireCpuWakeLock(this);

                	 Bundle bun = new Bundle();
                	 int start=extras.toString().indexOf("adviceId=");
                	 int size=extras.toString().length();
                	 String text=extras.toString().substring(start+9);
                	 //int size=text.length();
                	 text=extras.toString().substring(start+9,size-2);
                	 Log.v(TAG,"text"+text);
                	 bun.putString("notiMessage",text );

                     Intent popupIntent = new Intent(getApplicationContext(), KidsMindNotiActivity.class);
                     popupIntent.putExtras(bun);
                    
                     PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
                     try {
                      pie.send();
                     } catch (CanceledException e) {
                  
                     }
                     Log.v(TAG,"일어나");
                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.launcher)
        .setContentTitle("전문가 알림")
        .setTicker("전문가 메세지 도착")
        .setAutoCancel(true)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText("전문가 메세지 도착");

//        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    boolean flag = false;
  public void ToastAll(Context context,String msg) {


	LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View layout = inflater.inflate(R.layout.toast_layout,
			null);

	Toast mToast = new Toast(context.getApplicationContext());



	mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 2);
	mToast.setDuration(Toast.LENGTH_SHORT);
	mToast.setView(layout);         
	if(flag == false){
		flag = true;
		mToast.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				flag = false;
			}
		}, 2000);//토스트 켜져있는 시간동안 핸들러 지연 대충 숏이 2초 조금 넘는거 같다.
	} else{
		Log.e("","토스트 켜져있음");
	}

}
  private Handler mHandler;

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	mHandler = new Handler();

	return super.onStartCommand(intent, flags, startId);
}
private class ToastRunnable implements Runnable {
    String mText;
    Context context;

    public ToastRunnable(String text,Context context) {
        mText = text;
        this.context=context;
    }

    @Override
    public void run(){
    	ToastAll(context,mText);
    }
}


  
}
