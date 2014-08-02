package com.iih.android.videoblog;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CustomeAlertYesNo;

public class Language extends Activity {

	private Button btnEng;
	private Button btnPor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.language);

		initialization();

		btnEng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppSharedPrefrence.getInstance(Language.this).setLanguage("en_US");
				//for webservice
				AppSharedPrefrence.getInstance(Language.this).setLanguageID("en");
				callSwitchLang("en_US");
				Intent intent = new Intent(Language.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

		btnPor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppSharedPrefrence.getInstance(Language.this).setLanguage("pt_rPT");
				//for webservice
				AppSharedPrefrence.getInstance(Language.this).setLanguageID("pt");
				callSwitchLang("pt_PT");
				Intent intent = new Intent(Language.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void initialization() {
		btnEng = (Button) findViewById(R.id.btn_eng);
		btnPor =(Button)findViewById(R.id.btn_por);
	}

	private void callSwitchLang(String langCode) {
		Locale locale = new Locale(langCode);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		onCreate(null);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		CustomeAlertYesNo customedialog = new CustomeAlertYesNo(Language.this);
		customedialog.show();
		//super.onBackPressed();
	}
	
}
