package com.iih.android.videoblog.setting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iih.android.videoblog.R;
import com.iih.android.videoblog.Parsing.PostDataAndGetData;
import com.iih.android.videoblog.Parsing.parseListner;
import com.iih.android.videoblog.commonutill.AppSharedPrefrence;
import com.iih.android.videoblog.commonutill.CommonUtility;
import com.iih.android.videoblog.commonutill.CommonVariable;
import com.iih.android.videoblog.profile.ProfileActivity;

public class GeneralSetting extends Activity implements OnClickListener,parseListner {

	private TextView action_title;
	private Button actionleft;
	private Button actionRight;
	private TextView txt_chgLanguage;
	private TextView txt_CamOnOFF;
	private Button btnSave;
	private TextView txtLangSelcted;

	private final int ResponseCodeMe = 0;

	public PostDataAndGetData psd;

	private String cameramode = "";

	private String language = "";

	Intent intent;

	private Boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		Locale locale = new Locale(AppSharedPrefrence.getInstance(GeneralSetting.this).getLanguage());
//		Locale.setDefault(locale);
//		Configuration config = new Configuration();
//		config.locale = locale;
//		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		setContentView(R.layout.generalsetting);

		//setContentView(R.layout.generalsetting);


//			Intent intent = getIntent();
//			overridePendingTransition(0, 0);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			finish();
//			overridePendingTransition(0, 0);
//			startActivity(intent);

		


		initialization();

		txt_chgLanguage.setOnClickListener(this);
		txt_CamOnOFF.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}

	private void callSwitchLang(String langCode) {
		Locale locale = new Locale(langCode);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
		//onCreate(null);
	}

	public void reload() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void CallWsSetting() {

		String MemberiD = CommonVariable.getInstance().getMember_id();
		String LanguageiD = AppSharedPrefrence.getInstance(GeneralSetting.this).getLanguageID();

		if(AppSharedPrefrence.getInstance(GeneralSetting.this).getCameraMode()){
			cameramode = "ON";
		}else{
			cameramode = "OFF";
		}

		psd = new PostDataAndGetData(GeneralSetting.this, null, null,
				"GET", CommonUtility.BaseUrl2 + "setMemberSettings" + "&iMemberId=" + MemberiD +"&iLangId=" + LanguageiD + "&eCameraMode=" + cameramode, ResponseCodeMe,0, false, true);
		psd.execute();
	}

	private void initialization() {
		// TODO Auto-generated method stub
		action_title =(TextView)findViewById(R.id.action_title);
		action_title.setText("Settings");

		actionleft =(Button)findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_menu);
		actionleft.setVisibility(View.INVISIBLE);

		actionRight =(Button)findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		txt_chgLanguage = (TextView) findViewById(R.id.txtLanguage);
		txt_CamOnOFF = (TextView) findViewById(R.id.onoff);

		btnSave = (Button)findViewById(R.id.savesettingBtn);
		txtLangSelcted = (TextView)findViewById(R.id.showlang);

		//if camera is on then it set ON/OFF button 
		if(AppSharedPrefrence.getInstance(GeneralSetting.this).getCameraMode()){
			txt_CamOnOFF.setText(getString(R.string.on));
		}else{
			txt_CamOnOFF.setText(getString(R.string.off));
		}

		if(AppSharedPrefrence.getInstance(GeneralSetting.this).getLanguageID().equals("en")){
			txtLangSelcted.setText(getString(R.string.english));
		}else{
			txtLangSelcted.setText(getString(R.string.portugal));
		}

	}
	
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	Intent intent = new Intent(GeneralSetting.this,ProfileActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
	startActivity(intent);
	finish();
}
	
//	private void callSwitchLang(String langCode) {
//		Locale locale = new Locale(langCode);
//		Locale.setDefault(locale);
//		Configuration config = new Configuration();
//		config.locale = locale;
//		getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
//		//onCreate(null);
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.txtLanguage:
//			intent = new Intent(GeneralSetting.this,SettingLanguage.class);
//			startActivity(intent);
//			finish();

			final CharSequence[] items = { getString(R.string.english),getString(R.string.portugal) };

			AlertDialog.Builder builder = new AlertDialog.Builder(GeneralSetting.this);
			builder.setTitle("Select Language");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {


					//CharSequence selectLang = items[item];
					//if(selectLang.equals(getString(R.string.english))){
					if(item == 0){
						AppSharedPrefrence.getInstance(GeneralSetting.this).setLanguage("en_US");
						//for webservice login
						AppSharedPrefrence.getInstance(GeneralSetting.this).setLanguageID("en");

						//callSwitchLang("en_US");
					}else{
						AppSharedPrefrence.getInstance(GeneralSetting.this).setLanguage("pt_rPT");
						//for webservice of login
						AppSharedPrefrence.getInstance(GeneralSetting.this).setLanguageID("pt");

						///CallWsLanguage();
						//callSwitchLang("pt_PT");
						//language = "pt_PT";
					}
					
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			break;

		case R.id.onoff:

			if(flag == false){
				txt_CamOnOFF.setText(getString(R.string.on));
				AppSharedPrefrence.getInstance(GeneralSetting.this).setCameraMode(true);
				flag = true;
			}else{
				txt_CamOnOFF.setText(getString(R.string.off));
				flag = false;
				AppSharedPrefrence.getInstance(GeneralSetting.this).setCameraMode(false);
			}
			break;

		case R.id.savesettingBtn:

			CallWsSetting();
			

			break;

		default:
			break;
		}
	}

	/**
	 * Web-service Response
	 */

	LinkedHashMap<String, Object> temp;
	ArrayList<LinkedHashMap<String, Object>> jsonData;
	ArrayList<Object> arrayList;
	ArrayList<Object> tempData;
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

					switch (responce) {
					case ResponseCodeMe:

						data= (LinkedHashMap<String, Object>) temp.get("data");

						callSwitchLang(AppSharedPrefrence.getInstance(GeneralSetting.this).getLanguage());

						reload();

//						if(AppSharedPrefrence.getInstance(GeneralSetting.this).getSelectedLanguage().equals(getString(R.string.english))){
//							txtLangSelcted.setText(AppSharedPrefrence.getInstance(GeneralSetting.this).getSelectedLanguage());
//						}else{
//							txtLangSelcted.setText(AppSharedPrefrence.getInstance(GeneralSetting.this).getSelectedLanguage());
//						}

						break;

					default:
						break;
					}
				} else {
					//CommonUtility.showAlert(data.get("msg").toString(),GeneralSetting.this);
				}
			} else {
				CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), GeneralSetting.this);
			}
		} catch (Exception e) {
			CommonUtility.showAlert(getString(R.string.there_is_some_problem_in_getting_data_), GeneralSetting.this);
		}
	}

}
