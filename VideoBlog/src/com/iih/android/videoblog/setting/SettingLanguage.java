package com.iih.android.videoblog.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iih.android.videoblog.LoginActivity;
import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.newsfeed.NewsFeedScreen;

public class SettingLanguage extends Activity implements parseListner{

	private Button btnEng;
	private Button btnPor;

	public PostDataAndGetData psd;
	private final int langageRcode = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.language);

		

		initialization();

		btnEng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppSharedPrefrence.getInstance(SettingLanguage.this).setLanguage("en_US");
				//For Webservice
				AppSharedPrefrence.getInstance(SettingLanguage.this).setLanguageID("en");
				CallWsLanguage();
				callSwitchLang("en_US");
			}
		});

		btnPor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppSharedPrefrence.getInstance(SettingLanguage.this).setLanguage("pt_rPT");
				AppSharedPrefrence.getInstance(SettingLanguage.this).setLanguageID("pt");
				CallWsLanguage();
				callSwitchLang("pt_PT");
				reload();
			}
		});
	}
	private void initialization() {
		btnEng = (Button) findViewById(R.id.btn_eng);
		btnPor =(Button)findViewById(R.id.btn_por);
	}

	public void CallWsLanguage() {

		String MemberiD = CommonVariable.getInstance().getMember_id();
		String LanguageiD = AppSharedPrefrence.getInstance(SettingLanguage.this).getLanguageID();
		
		psd = new PostDataAndGetData(SettingLanguage.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "setLanguage" + "&iMemberId=" + MemberiD +"&iLangId="+ LanguageiD,langageRcode,0, false, false);
		try {
			psd.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void callSwitchLang(String langCode) {
		Locale locale = new Locale(langCode);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		onCreate(null);
	}

	public void reload() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(SettingLanguage.this,GeneralSetting.class);
		startActivity(intent);
		finish();
	}
	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;

	LinkedHashMap<String, Object> data;

	@SuppressWarnings("unchecked")
	@Override
	public void GetResult(Object jsonDataO, int responce) {
		try {
			arrayList = (ArrayList<Object>) jsonDataO;
			ArrayList<LinkedHashMap<String, Object>> jsonDataTemp = (ArrayList<LinkedHashMap<String, Object>>) jsonDataO;
			int size = arrayList.size();
			if (size != 0) {
				temp = (LinkedHashMap<String, Object>) arrayList.get(0);

				data= (LinkedHashMap<String, Object>) temp.get("message");
				if (data.get("success").toString().equalsIgnoreCase("1")) {
					jsonData = jsonDataTemp;
//					tempData=(ArrayList<Object>) temp.get("data");
					data=(LinkedHashMap<String, Object>) temp.get("data");
					switch (responce) {
					case langageRcode:

//						Intent intent = new Intent(SettingLanguage.this,NewsFeedScreen.class);
//						startActivity(intent);
//						finish();
						break;

					default:
						break;
					}
				} else {
					CommonUtility.showAlert(data.get("msg").toString(),SettingLanguage.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), SettingLanguage.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), SettingLanguage.this);
		}

	}

}
