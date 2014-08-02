package com.iih.android.videoblog.commonutill;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.iih.android.videoblog.R;

public class CustomeAlertYesNo extends Dialog implements android.view.View.OnClickListener {

	public Activity activity;
	public Dialog dialog;
	public Button yes, no;

	public CustomeAlertYesNo(Activity act) {
		super(act);
		this.activity = act;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.customedialogyesno);
		yes = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			activity.finish();
			break;
		case R.id.btn_no:
			dismiss();
			break;
		default:
			break;
		}
		dismiss();
	}

}
