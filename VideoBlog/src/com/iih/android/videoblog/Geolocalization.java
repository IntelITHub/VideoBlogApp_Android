package com.iih.android.videoblog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;

public class Geolocalization extends Activity {

	private Button btn_allow;
	private Button btn_dont_allow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geolocalization);

		initialization();

		btn_allow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppSharedPrefrence.getInstance(Geolocalization.this).setIsLocation(true);
				Intent intent = new Intent(Geolocalization.this,NewsFeedScreen.class);
				startActivity(intent);
				finish();
			}
		});

		btn_dont_allow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppSharedPrefrence.getInstance(Geolocalization.this).setIsLocation(false);
				Intent intent = new Intent(Geolocalization.this,NewsFeedScreen.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initialization() {
		btn_allow = (Button) findViewById(R.id.btn_Allow);
		btn_dont_allow = (Button) findViewById(R.id.btn_dntallow);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(Geolocalization.this);
		customedialog.show();
		// super.onBackPressed();
	}

}
