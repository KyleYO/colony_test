package com.colonycount.cklab.activity.base;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class  BaseActivity extends FragmentActivity {
	/**
	 * Debug name for logcat
	 */
	protected String TAG = getClass().getSimpleName();
	
	protected static final String PREF = "my_sharedpreference";
	/**
	 * Save user login state
	 * Value: boolean
	 */
	protected static final String LOGGED_IN = "logged_in";
	/**
	 * If logged in, save user account
	 * Value: String
	 */
	protected static final String USER_ACCOUNT = "user_account";
	/**
	 * If logged in, save user id
	 * Value: String
	 */
	protected static final String USER_ID = "user_id";
	
	protected static final String COLONY_TYPE_LIST = "colony_type_list";
	
	protected static final String COLONY_EXP_PARAM_LIST = "colony_exp_param_list";
	
	protected static final String COLONY_TYPE = "colony_type";
	
	protected static final String COLONY_EXP_PARAM = "colony_exp_param";
	
	private static SharedPreferences settings;
	private static Editor editor;
	
	/**
	 * �N����x�s�bSharedPreferences
	 * @param key
	 * @param value
	 */
	protected void savePrefData(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * �N����x�s�bSharedPreferences
	 * @param key
	 * @param value
	 */
	protected void savePrefData(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	
	protected void savePrefData(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}
	
	protected void savePrefStringSetData(String type, String value){
		if(type.equals(COLONY_TYPE)){
			Set<String> colonyTypeList = settings.getStringSet(COLONY_TYPE_LIST, new HashSet<String>());
			colonyTypeList.add(value);
			editor.putStringSet(COLONY_TYPE_LIST, colonyTypeList);
		} else if(type.equals(COLONY_EXP_PARAM)){
			Set<String> colonyExpParamList = settings.getStringSet(COLONY_EXP_PARAM_LIST, new HashSet<String>());
			colonyExpParamList.add(value);
			editor.putStringSet(COLONY_EXP_PARAM_LIST, colonyExpParamList);
		}
		
		editor.commit();
	}
	
	/**
	 * �qSharedPreferencesŪ����
	 * @param key
	 * @return String �Y��Ƭ��šA�h�^��null
	 */
	protected String loadPrefStringData(String key) {
		return settings.getString(key, null);		
	}
	
	/**
	 * �qSharedPreferencesŪ����
	 * @param key
	 * @return boolean �Y��Ƭ��šA�h�^��false
	 */
	protected boolean loadPrefBooleanData(String key) {
		return settings.getBoolean(key, false);		
	}
	
	/**
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	protected int loadPrefIntData(String key, int defValue){
		return settings.getInt(key, defValue);
	}
	
	/**
	 * 
	 * @param key
	 * @return string set if exist, else return null
	 */
	protected Set<String> loadPrefStringSetData(String key){
		return settings.getStringSet(key, null);
	}
	
	protected void removePrefData(String key){
		editor.remove(key);
		editor.commit();
	}
	
	/**
	 * 
	 * @param key
	 * @return 
	 */
	// TODO: remove bug
	protected void removePrefStringSetData(String type, String value){
		if(type.equals(COLONY_TYPE)){
			Set<String> colonyTypeList = settings.getStringSet(COLONY_TYPE_LIST, null);
			if(colonyTypeList != null){
				colonyTypeList.remove(value);
				editor.putStringSet(COLONY_TYPE_LIST, colonyTypeList);
			} 
		} else if(type.equals(COLONY_EXP_PARAM)){
			Set<String> colonyExpParamList = settings.getStringSet(COLONY_EXP_PARAM_LIST, null);
			if(colonyExpParamList != null){
				colonyExpParamList.remove(value);
				editor.putStringSet(COLONY_EXP_PARAM_LIST, colonyExpParamList);
			} 
		}
		
		
		editor.commit();
	}
	
	/**
	 * set activity to full screen
	 */
	protected void setFullScreen(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Fabric.with(this, new Crashlytics());

		settings = getSharedPreferences(PREF, Context.MODE_PRIVATE);
		editor = settings.edit();
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
