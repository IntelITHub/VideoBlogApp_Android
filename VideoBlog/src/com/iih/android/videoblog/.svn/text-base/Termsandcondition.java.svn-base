package com.iih.android.videoblog;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Termsandcondition extends Activity {

	private Button DontAccept;
	private Button Accept;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.termscondition);

		initialization();

		Accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Termsandcondition.this,Geolocalization.class);
				startActivity(intent);
				finish();
			}
		});

		DontAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private void initialization() {
		// TODO Auto-generated method stub
		DontAccept = (Button)findViewById(R.id.btn_dntaccept);
		Accept = (Button)findViewById(R.id.btn_Accept);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(Termsandcondition.this);
		customedialog.show();
		//super.onBackPressed();
	}
}
