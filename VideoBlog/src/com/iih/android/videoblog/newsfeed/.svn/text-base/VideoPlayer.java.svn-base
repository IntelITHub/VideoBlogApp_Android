package com.iih.android.videoblog.newsfeed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.RegisterActivity;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;

public class VideoPlayer extends Activity {

	ProgressBar progressBar = null;

	VideoView videoView = null;

	Context context = null;

	private Button actionleft;
	private Button actionRight;
	private TextView action_title;

	
	@Override
	public void onCreate(Bundle iclic) {
		super.onCreate(iclic);
		context = null;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.videoplay);

		actionleft = (Button) findViewById(R.id.action_Left);
		actionleft.setVisibility(View.GONE);

		actionRight = (Button) findViewById(R.id.action_right);
		actionRight.setVisibility(View.GONE);

		videoView = (VideoView) findViewById(R.id.surface_view);

		progressBar = (ProgressBar) findViewById(R.id.progressbar);

		String videopath = AppSharedPrefrence.getInstance(VideoPlayer.this).GetVideoPath();

		Uri videoUri = Uri.parse(videopath);

		videoView.setVideoURI(videoUri);
		videoView.setMediaController(new MediaController(this));
		videoView.requestFocus();
		videoView.setBackgroundColor(Color.DKGRAY);
		videoView.start();

		progressBar.setVisibility(View.VISIBLE);

		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.GONE);
				videoView.setBackgroundColor(Color.TRANSPARENT);
				//mp.start();
				videoView.start();
//				mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
//					@Override
//					public void onVideoSizeChanged(MediaPlayer mp, int arg1,int arg2) {
//						// TODO Auto-generated method stub
//						progressBar.setVisibility(View.GONE);
//						videoView.setBackgroundColor(Color.TRANSPARENT);
//						mp.start();
//					}
//				});

			}
		});

		videoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				// "The video cant be played" pop up dialog appeared, video wont
				// start
				videoView.start();
				return false;
			}
		});

//		videoView.setVideoURI(Uri.parse(videopath));
//		videoView.setMediaController(new MediaController(this));
//		videoView.requestFocus();
//		videoView.setBackgroundColor(Color.BLUE);
//		videoView.start();

	}

}
