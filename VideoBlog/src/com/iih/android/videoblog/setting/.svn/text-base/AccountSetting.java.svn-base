package com.iih.android.videoblog.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iih.android.videoblog.R;

public class AccountSetting extends Activity {

	private TextView action_title;
	private Button actionleft;
	private Button actionRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountsetting);

		action_title =(TextView)findViewById(R.id.action_title);
		action_title.setText(R.string.account_settings);

		actionleft =(Button)findViewById(R.id.action_Left);
		actionleft.setBackgroundResource(R.drawable.actionbar_back);

		actionRight =(Button)findViewById(R.id.action_right);
		actionRight.setVisibility(View.INVISIBLE);

		actionleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
