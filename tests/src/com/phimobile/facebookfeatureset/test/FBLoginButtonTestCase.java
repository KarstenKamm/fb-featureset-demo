package com.phimobile.facebookfeatureset.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;

import com.facebook.Session;
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


	//launch activity; 
	//click login button
	
	boolean isSessionOpen;
	
	public void testBasicLoginFunctionality() {
		Log.e("KK","FBLoginButtonTestCase starts..");
		Log.e("KK","package: " + getActivity().getPackageName());
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
				
				final LoginButton loginButton = (LoginButton) 
						activity.findViewById(com.phimobile.facebookfeatureset.R.id.login_fbloginbutton);
				
				assertTrue (loginButton!=null);
				
				Session session = Session.getActiveSession();
				assertTrue(session!=null);
				
				Log.e("KK","session found! isOpened?" + session.isOpened());
				isSessionOpen = session.isOpened();
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.e("KK","clicking");
						loginButton.performClick();
						
					}
				});
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("KK","session found! isOpened?" + session.isOpened());
				
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
