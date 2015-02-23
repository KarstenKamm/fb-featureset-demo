package com.phimobile.facebookfeatureset.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;

import com.facebook.android.Facebook;
import com.facebook.widget.LoginButton;
import com.phimobile.facebookfeatureset.FBFeatureSetMainActivity;
import com.phimobile.facebookfeatureset.LoginButtonFragment;

public class FBLoginButtonTestCase extends
		ActivityInstrumentationTestCase2<FBFeatureSetMainActivity> {
	
	private Instrumentation mInstrumentation;
	private FBFeatureSetMainActivity activity;

	public FBLoginButtonTestCase() {
		super(FBFeatureSetMainActivity.class);
		Log.e("KK","FBLoginButtonTestCase launched");
	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mInstrumentation = getInstrumentation();
		activity = (FBFeatureSetMainActivity) getActivity();
	}


	//launch activity; check existence and content of textview
	public void testBasicLoginFunctionality() {
		Log.e("KK","FBLoginButtonTestCase starts..");
		
		assertTrue(activity != null);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.e("KK","inside runnable");
				
				activity.switchToLoginFragment();
				
				//assert currentFragment is LoginButtonFragment
				LoginButtonFragment frag = (LoginButtonFragment) activity.getSupportFragmentManager().findFragmentById(
						com.phimobile.facebookfeatureset.R.id.fragment_container);
				
				assertTrue(frag!=null);
				
				LoginButton loginButton = (LoginButton) 
						activity.findViewById(com.phimobile.facebookfeatureset.R.id.login_fbloginbutton);
				
				assertTrue (loginButton!=null);
				
				
				Log.e("KK","runnable done");
			}
		});
		
		Log.e("KK","runnable started");
		
		mInstrumentation.waitForIdleSync();
		
		View v = activity.findViewById(
				com.phimobile.facebookfeatureset.R.id.fragment_container);
		
		assertTrue(v!=null);
		
		Log.e("KK","FBLoginButtonTestCase done..");
	}
}
