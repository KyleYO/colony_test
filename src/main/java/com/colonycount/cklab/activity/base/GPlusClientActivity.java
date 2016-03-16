package com.colonycount.cklab.activity.base;

import android.app.PendingIntent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class GPlusClientActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	/* GoogleApiClient wraps our service connection to Google Play services and
	   provides access to the users sign in state and Google's APIs. */
	protected GoogleApiClient mGoogleApiClient;
	/* Sign in state */
	protected static final int STATE_DEFAULT = 0;
	protected static final int STATE_SIGN_IN = 1;
	protected static final int STATE_IN_PROGRESS = 2;
	
	protected static final int RC_SIGN_IN = 0;
	
	protected static final int DIALOG_PLAY_SERVICES_ERROR = 0;

	protected static final String SAVED_PROGRESS = "sign_in_progress";
	
	// We use mSignInProgress to track whether user has clicked sign in.
	// mSignInProgress can be one of three values:
	//
	// STATE_DEFAULT: The default state of the application before the user
	// has clicked 'sign in', or after they have clicked
	// 'sign out'. In this state we will not attempt to
	// resolve sign in errors and so will display our
	// Activity in a signed out state.
	// STATE_SIGN_IN: This state indicates that the user has clicked 'sign
	// in', so resolve successive errors preventing sign in
	// until the user has successfully authorized an account
	// for our app.
	// STATE_IN_PROGRESS: This state indicates that we have started an intent to
	// resolve an error, and so we should not start further
	// intents until the current intent completes.
	protected int mSignInProgress;

	// Used to store the PendingIntent most recently returned by Google Play
	// services until the user clicks 'sign in'.
	protected PendingIntent mSignInIntent;

	// Used to store the error code most recently returned by Google Play
	// services
	// until the user clicks 'sign in'.
	protected int mSignInError;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
		}
		
		mGoogleApiClient = buildGoogleApiClient();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		// Log.d(TAG, "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Log.d(TAG, "onStop");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Log.d(TAG, "onSaveInstanceState");
		outState.putInt(SAVED_PROGRESS, mSignInProgress);
	}

	
	protected GoogleApiClient buildGoogleApiClient() {
		// When we build the GoogleApiClient we specify where connected and
		// connection failed callbacks should be returned, which Google APIs our
		// app uses and which OAuth 2.0 scopes our app requests.
		// Log.d(TAG, "buildGoogleApiClient()");

		return new GoogleApiClient.Builder(this)
			    .addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}	
}
