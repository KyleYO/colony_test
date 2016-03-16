package com.colonycount.cklab.activity;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.colonycount.cklab.MyApplication;
import com.colonycount.cklab.activity.base.GPlusClientActivity;
import com.colonycount.cklab.asynctask.LoginAsyncTask;
import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.AgeRange;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.android.gms.plus.model.people.Person.Name;
import com.google.android.gms.plus.model.people.Person.Organizations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends GPlusClientActivity implements View.OnClickListener, AsyncTaskCompleteListener<Boolean> {
	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    
    private Bitmap[] colonyInfos = new Bitmap[NUM_PAGES];
    
    private LoginActivity activity;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.layout_main);
		setViews();
		
		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		for(int i = 0; i < colonyInfos.length; i++){
			Bitmap mBitmap = colonyInfos[i];
			if(isFinishing() && mBitmap != null && !mBitmap.isRecycled()){
				mBitmap.recycle();
				mBitmap = null;
			}
		}
	}
	
	
	private void setViews(){
//		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		// Instantiate a ViewPager and a PagerAdapter.
		activity = this;
        mPager = (ViewPager) findViewById(R.id.pager);
        colonyInfos[0] = BitmapFactory.decodeResource(getResources(), R.drawable.colonyinfo_1);
        colonyInfos[1] = BitmapFactory.decodeResource(getResources(), R.drawable.colonyinfo_2);
        colonyInfos[2] = BitmapFactory.decodeResource(getResources(), R.drawable.colonyinfo_3);
        colonyInfos[3] = BitmapFactory.decodeResource(getResources(), R.drawable.colonyinfo_4);
        
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
	}
	
	/**
     * A simple pager adapter that represents 4 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment(colonyInfos[position]);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    
    class ScreenSlidePageFragment extends Fragment {
    	private Bitmap image;
    	
    	public ScreenSlidePageFragment(Bitmap image){
    		this.image = image;
    	}
    		
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
            ImageView colonyInfo = (ImageView) rootView.findViewById(R.id.colony_info);
            SignInButton mSignInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
            colonyInfo.setImageBitmap(image);
            mSignInButton.setOnClickListener(activity);

            return rootView;
        }
    }
	
	
	@Override
	public void onClick(View v) {
		if (!mGoogleApiClient.isConnecting()) {
			// We only process button clicks when GoogleApiClient is not transitioning between connected and not connected.
			switch (v.getId()) {
			case R.id.sign_in_button:
				mGoogleApiClient.connect();
				MyApplication.getInstance().loginState = Boolean.TRUE;
				mSignInProgress = STATE_SIGN_IN;
				break;
			}
		}
	}

	/*
	 * onConnected is called when our Activity successfully connects to Google
	 * Play services. onConnected indicates that an account was selected on the
	 * device, that the selected account has granted any requested permissions
	 * to our app and that we were able to establish a service connection to
	 * Google Play services.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		// Reaching onConnected means we consider the user signed in.
		// Log.d(TAG, "onConnected");
		
		// Retrieve some profile information to personalize our app for the user.
		Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		
		Name name = currentUser.getName();
		AgeRange ageRange = currentUser.getAgeRange();
		Image image = currentUser.getImage();
		List<Organizations> organizations = currentUser.getOrganizations();
		
		// Retrive person info
		MyApplication.getInstance().setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));
		MyApplication.getInstance().setId(currentUser.getId());
		MyApplication.getInstance().setDisplayName(currentUser.getDisplayName());
		MyApplication.getInstance().setGender(currentUser.getGender());
		MyApplication.getInstance().setAboutMe(currentUser.getAboutMe());
		MyApplication.getInstance().setBirthday(currentUser.getBirthday());
		MyApplication.getInstance().setCurrentLocation(currentUser.getCurrentLocation());
		MyApplication.getInstance().setLanguage(currentUser.getLanguage());
		MyApplication.getInstance().setNickname(currentUser.getNickname());
		MyApplication.getInstance().setUrl(currentUser.getUrl());

		if(name != null){
			MyApplication.getInstance().setName(name.getFamilyName(),name.getGivenName());
		}

		if(ageRange != null){
			MyApplication.getInstance().setAge(ageRange.getMin(),ageRange.getMax());
		}

		if(image != null){
			MyApplication.getInstance().setImageUrl(image.getUrl());
		}

		if(organizations != null){
			for(int i = 0; i < organizations.size(); i++){
				if(i == organizations.size()-1)
					MyApplication.getInstance().addOrgs(organizations.get(i).toString());
				else
					MyApplication.getInstance().addOrgs(organizations.get(i).toString() + ",, ");
			}
		}
		
		Map<String, String> request = new HashMap<String, String>();
		request.put("email", MyApplication.getInstance().getEmail());
		request.put("id", MyApplication.getInstance().getId());
		request.put("displayName", MyApplication.getInstance().getDisplayName());
		request.put("gender", MyApplication.getInstance().getGender()+"");
		request.put("aboutMe", MyApplication.getInstance().getAboutMe());
		request.put("birthday", MyApplication.getInstance().getBirthday());
		request.put("currentLocation", MyApplication.getInstance().getCurrentLocation());
		request.put("language", MyApplication.getInstance().getLanguage());
		request.put("nickname", MyApplication.getInstance().getNickname());
		request.put("url", MyApplication.getInstance().getUrl());
		request.put("familyName", MyApplication.getInstance().getFamilyName());
		request.put("givenName", MyApplication.getInstance().getGivenName());
		request.put("ageMin", MyApplication.getInstance().getAgeMin()+"");
		request.put("ageMax", MyApplication.getInstance().getAgeMax()+"");
		request.put("imageUrl", MyApplication.getInstance().getImageUrl());
		request.put("orgs", MyApplication.getInstance().getOrgs());
		
		// Indicate that the sign in process is complete.
		mSignInProgress = STATE_DEFAULT;
		
//		// Log.d("test", "user account = " + loadPrefStringData(USER_ACCOUNT));
//    	// Log.d("test", "user id = " + loadPrefStringData(USER_ID));
		
		// save logged in information
		savePrefData(LOGGED_IN, true);
		savePrefData(USER_ACCOUNT, MyApplication.getInstance().getEmail());
		savePrefData(USER_ID, MyApplication.getInstance().getId());
		
		LoginAsyncTask dbTask = new LoginAsyncTask(this, "系統訊息", "登入中，請稍後...", this, LoginAsyncTask.class);
		request.put("check_user", MyApplication.getInstance().getEmail());
		dbTask.setRequest(request);
		dbTask.execute();
	}

	
	/*
	 * onConnectionFailed is called when our Activity could not connect to
	 * Google Play services. onConnectionFailed indicates that the user needs to
	 * select an account, grant permissions or resolve an error in order to sign
	 * in.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes
		// might be returned in onConnectionFailed.
		// Log.d(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
		
		if (mSignInProgress != STATE_IN_PROGRESS) {
			// We do not have an intent in progress so we should store the
			// latest error resolution intent for use when the sign in button is clicked.
			mSignInIntent = result.getResolution();
			mSignInError = result.getErrorCode();
			
			if (mSignInProgress == STATE_SIGN_IN) {
				// Log.d(TAG, "onConnectionFailed: STATE_SIGN_IN");
				// STATE_SIGN_IN indicates the user already clicked the sign in
				// button so we should continue processing errors until the user is
				// signed in or they click cancel.
				resolveSignInError();
			}
		}
	}

	
	/*
	 * Starts an appropriate intent or dialog for user interaction to resolve
	 * the current error preventing the user from being signed in. This could be
	 * a dialog allowing the user to select an account, an activity allowing the
	 * user to consent to the permissions being requested by your app, a setting
	 * to enable device networking, etc.
	 */
	private void resolveSignInError() {
		// Log.d(TAG, "resolveSignInError");
		
		if (mSignInIntent != null) {
			// We have an intent which will allow our user to sign in or
			// resolve an error. For example if the user needs to
			// select an account to sign in with, or if they need to consent
			// to the permissions your app is requesting.

			try {
				// Send the pending intent that we stored on the most recent
				// OnConnectionFailed callback. This will allow the user to
				// resolve the error currently preventing our connection to
				// Google Play services.
				mSignInProgress = STATE_IN_PROGRESS;
				startIntentSenderForResult(mSignInIntent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// Log.d(TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());
				// The intent was canceled before it was sent. Attempt to
				// connect to get an updated ConnectionResult.
				mSignInProgress = STATE_SIGN_IN;
				mGoogleApiClient.connect();
			}
		} else {
			// Google Play services wasn't able to provide an intent for some
			// error types, so we show the default Google Play services error
			// dialog which may still start an intent on our behalf if the
			// user can resolve the issue.
			Resources resources = getResources();
			new FragmentDialog(null, 
					           resources.getString(R.string.google_login_error_msg), 
					           0, 
					           resources.getString(R.string.google_dialog_close), 
					           null, 
					           null, 
					           null).show(getSupportFragmentManager(), "dialog");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RC_SIGN_IN:
			if (resultCode == RESULT_OK) {
				// Log.d(TAG, "onActivityResult: RESULT_OK");
				// If the error resolution was successful we should continue
				// processing errors.
				mSignInProgress = STATE_SIGN_IN;
				
				// Update the user interface to reflect that the user is signed in.
//				mSignInButton.setEnabled(false);
			} else {
				// If the error resolution was not successful or the user
				// canceled, we should stop processing errors.
				mSignInProgress = STATE_DEFAULT;
			}

			if (!mGoogleApiClient.isConnecting()) {
				// If Google Play services resolved the issue with a dialog then
				// onStart is not called so we need to re-attempt connection
				// here.
				mGoogleApiClient.connect();
			}
			break;
		}
	}


	@Override
	public void onConnectionSuspended(int cause) {
		// Log.d(TAG, "onConnectionSuspended");
		// The connection to Google Play services was lost for some reason.
		// We call connect() to attempt to re-establish the connection or get a
		// ConnectionResult that we can attempt to resolve.
		mGoogleApiClient.connect();
	}

	@Override
	public void onTaskComplete(AsyncTaskPayload result, String taskName) {
		String status = result.getValue("status");
		String msg = result.getValue("msg");
		
		if(status.equals("success")){
			if(taskName.equals("LoginAsyncTask") && msg.equals("success")){
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		} else {
			Toast.makeText(this, "error: " + msg, Toast.LENGTH_LONG).show();
		}
	}
}