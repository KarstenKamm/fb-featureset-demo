package com.phimobile.facebookfeatureset.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.phimobile.facebookfeatureset.FBFeatureSetMainActivity;

public class BasicMainActivityTestCase extends
		ActivityInstrumentationTestCase2<FBFeatureSetMainActivity> {

	public BasicMainActivityTestCase() {
		super(FBFeatureSetMainActivity.class);
		Log.e("KK","BasicMainActivityTestCase launched");
	}
	
	//launch activity; check existence and content of textview
	public void testBasicMainActivityFunctionality() {
		Log.e("KK","testBasicMainActivityFunctionality starts..");
		Activity activity = getActivity();
		
		assertTrue(activity != null);
		assertTrue(activity instanceof FBFeatureSetMainActivity);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.e("KK","inside runnable");
			}
		});
		
		Log.e("KK","runnable started");
		
		View v = activity.findViewById(
				com.phimobile.facebookfeatureset.R.id.fragment_container);
		
		assertTrue(v!=null);
		
		Log.e("KK","testBasicMainActivityFunctionality done..");
	}
}
