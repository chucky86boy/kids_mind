package com.mb.kids_mind;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.mb.kids_mind.Helper.MyHelper;
import com.mb.kids_mind.Item.AdviceItem2;
import com.mb.kids_mind.Item.CommentList;
import com.mb.kids_mind.Item.Const;
import com.mb.kids_mind.Item.Debugc;

public class KidsMindNoticeActivity extends Activity {
	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	 private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	 private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	 private MediaRecorder recorder = null;
	 private int currentFormat = 0;
	 private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
	 private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
	 private VideoView vi;
	 Button trans;
		private static final String TAG = "MainActivity";
		private AQuery aquery;
		private String imagePath;
		MyHelper helper;
		String bpath;
		static Uri uri;
		String DirPath;
		SQLiteDatabase db;
		private Uri mImageCaptureUri;
		private Bitmap photo;
		String detail_id;
		ArrayList<CommentList> data = new ArrayList<CommentList>();
		AdviceItem2 advice;
	    MediaController mc=null;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.audio);
	    aquery = new AQuery(this);
	    vi=(VideoView)findViewById(R.id.videoView1);
	    setButtonHandlers();
	    enableButtons(false);
	 mc = new MediaController(this);

	    setFormatButtonCaption();
	 }

	 private void setButtonHandlers() {
	     ((Button) findViewById(R.id.button1)).setOnClickListener(btnClick);
	    ((Button) findViewById(R.id.button2)).setOnClickListener(btnClick);
	    ((Button) findViewById(R.id.button3)).setOnClickListener(btnClick);
	    ((Button) findViewById(R.id.button4)).setOnClickListener(btnClick);
	 }

	 private void enableButton(int id, boolean isEnable) {
	    ((Button) findViewById(id)).setEnabled(isEnable);
	 }

	 private void enableButtons(boolean isRecording) {
	    enableButton(R.id.button1, !isRecording);
	    enableButton(R.id.button2, isRecording);
	    enableButton(R.id.button3, isRecording);
	    enableButton(R.id.button4, !isRecording);
	    
	 }

	 private void setFormatButtonCaption() {
	    ((Button) findViewById(R.id.button3)).setText("audio_format" + " (" + file_exts[currentFormat] + ")");
	 }
	 String path;
	 private String getFilename() {
	    String filepath = Environment.getExternalStorageDirectory().getPath();
	    File file = new File(filepath, AUDIO_RECORDER_FOLDER);
	    if (!file.exists()) {
	        file.mkdirs();
	    }
	     path=file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";
	  
	    return path;
	 }

	 private void startRecording() {
	    recorder = new MediaRecorder();
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(output_formats[currentFormat]);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    recorder.setOutputFile(getFilename());
	    recorder.setOnErrorListener(errorListener);
	    recorder.setOnInfoListener(infoListener);
	    try {
	        recorder.prepare();
	        recorder.start();
	    } catch (IllegalStateException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	 }

	 private void stopRecording() {
	    if (null != recorder) {
	        recorder.stop();
	        recorder.reset();
	        recorder.release();
	        recorder = null;
	    }
	 }

	 private void displayFormatDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    String formats[] = { "MPEG 4", "3GPP" };
	    builder.setTitle("choose_format_title").setSingleChoiceItems(formats, currentFormat, new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	             currentFormat = which;
	             setFormatButtonCaption();
	             dialog.dismiss();
	         }
	     }).show();
	 }

	 private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
	    @Override
	    public void onError(MediaRecorder mr, int what, int extra) {
	      //  Toast.makeText(this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
	     }
	 };

	 private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
	    @Override
	    public void onInfo(MediaRecorder mr, int what, int extra) {
	      //  Toast.makeText(AudioRecordingActivity.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
	    }
	 };

	 
	 File mediaFile=null;
	 private View.OnClickListener btnClick = new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	         switch (v.getId()) {
	            case R.id.button1: {
	            	
	            	  mediaFile = 
	            			   new File(Environment.getExternalStorageDirectory().getAbsolutePath() 
	            			       + "/myvideo.mp4");
	            							
	            			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	            					
	            			Uri videoUri = Uri.fromFile(mediaFile);
	            					
	            			intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
	            			startActivityForResult(intent, 1);

	            	//	                Toast.makeText(KidsMindNoticeActivity.this, "Start Recording", Toast.LENGTH_SHORT).show();
//	                 enableButtons(true);
//	                startRecording();
	                break;
	            }
	            case R.id.button2: {
	                Toast.makeText(KidsMindNoticeActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
	                enableButtons(false);
	                stopRecording();
	            	loading(KidsMindNoticeActivity.this);
					//bg.setVisibility(View.GONE);
					//list.setVisibility(View.VISIBLE);
					SharedPreferences pref = getSharedPreferences("pref",
							MODE_PRIVATE);
					SharedPreferences.Editor editor3=pref.edit();
					String message = "안녕하세요";
				
						
					
					
							asyncImageUploadJson(message);

					
	                break;
	            }
	            case R.id.button3: {
	                displayFormatDialog();
	                break;
	            }
	            case R.id.button4:
	            	

	                 mc.setAnchorView(vi);
	                 Uri video = Uri.parse(Const.VIDEO_LOADING+"mdadvice24.mp4");
	                 vi.setMediaController(mc);
	                 vi.setVideoURI(video);
	                 vi.start();

	            	 
//	            	 vi.setVideoPath(path);
//	            	 vi.start();
	            	break;
	        }
	     }
	 };
	 Uri uri2=null;
		@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			 uri2=data.getData();
			   vi.setMediaController(mc);
               vi.setVideoURI(uri);
               vi.start();
               loading(KidsMindNoticeActivity.this);
				//bg.setVisibility(View.GONE);
				//list.setVisibility(View.VISIBLE);
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				SharedPreferences.Editor editor3=pref.edit();
				String message = "안녕하세요";
			
					
				
				
						asyncImageUploadJson(message);


		}
	}
		Dialog dialog2=null;
		void loading(Activity context)
		{
			// Create dialog
			 dialog2 = new Dialog(context);
			dialog2.getWindow().setBackgroundDrawable

			(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog2.setCancelable(false);
			dialog2.setCanceledOnTouchOutside(false);
			dialog2.setContentView(R.layout.progress);
			dialog2.findViewById(R.id.imageView1);
			dialog2.findViewById(R.id.imageView1).setVisibility(ImageView.VISIBLE);
			dialog2.findViewById(R.id.imageView1).setBackgroundResource(R.anim.progress);

			AnimationDrawable frameAnimation = (AnimationDrawable) dialog2.findViewById(R.id.imageView1).getBackground();
			frameAnimation.start();
			//라디오 버튼 
			dialog2.show();
		}
	 private File uploadFile;
	 private void asyncImageUploadJson(String msg) {

			// uploadFileExecute(uploadFile);
			SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();

			int userId = pref.getInt("user_id", 0);
			String questionid = pref.getString("qposition", "");
			Map<String, Object> params = new HashMap<String, Object>();
			editor.commit();
			String ch = pref.getString("whereintent", "");
		
				uploadFile = new File(uri2.getPath());

			
			Log.v(TAG, "파일 용량 =>" + String.valueOf(uploadFile.length()));
			params.put("audio", uploadFile);
			params.put("message", msg);
			
			aquery.ajax(Const.AUDIOURL + "/" + userId + "/" + questionid,params, JSONObject.class, this, "jsonImageUploadCallback");
		}

		public void jsonImageUploadCallback(String url, JSONObject json,
				AjaxStatus status) {
			if (json != null) {
				try {
					aquery.ajaxCancel();
					dialog2.dismiss();
					boolean isSuccess = json.getString("result").equals(
							Const.SUCCESS);

					if (isSuccess) {
						
						int advice_id = json.getInt("advice_id");
						SharedPreferences pref = getSharedPreferences("pref",
								MODE_PRIVATE);
						SharedPreferences.Editor editor = pref.edit();
						editor.putInt("advice_id", advice_id);
						editor.commit();
//						String url3 = Const.ADVICE_COMMENT_LIST + advice_id + "";
//
//						new JobTask().execute(url3);
						Log.v(Debugc.getTagd(), "advice_id" + advice_id);
						// 사
//						String ch = pref.getString("whereintent", "");
//						if ("direct".equals(ch)) {
//							String bpath = pref.getString("bpath", "");
//							updateRec(bpath, advice_id);
//
//						} else {
//							String savename = pref.getString("savename", "");
//							updateRec(savename, advice_id);
//
//						}
						// openInfoMessageDialogBox("상담 요청 하였습니다.");

					} else {
						String error = json.getString("error");
						// openInfoMessageDialogBox(error);
					}
				} catch (JSONException e) {
					// openErrorDialog();
					e.printStackTrace();
				}
			} else {
				// openErrorDialog();
			}
		}
}
