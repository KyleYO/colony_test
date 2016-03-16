package com.colonycount.cklab.activity;

import android.content.Intent;
import android.util.Log;

import com.colonycount.cklab.MyApplication;
import com.colonycount.cklab.activity.base.BaseActivity;

import java.util.Date;

public class MainActivity extends BaseActivity {

	public static ItemDAO DB;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		DB = new ItemDAO(getApplicationContext());

		Log.d("BGService", "Main_BGServiceStart : " + new Date().toString());
		Intent ServiceIntent = new Intent(MainActivity.this,MyService.class);
		startService(ServiceIntent);

		// Log.d(TAG, "onStart");
		// Log.d(TAG, "LOGGED_IN:" + loadPrefBooleanData(LOGGED_IN));
		
		Intent intent = new Intent();
		// not logged in
		if(!MyApplication.getInstance().loginState){
			intent.setClass(MainActivity.this, LoginActivity.class);
		// logged in
		} else {
			intent.setClass(MainActivity.this, HomeActivity.class);
		}
		startActivity(intent);
		finish();
	}
}