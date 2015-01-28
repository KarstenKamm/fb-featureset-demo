package com.phimobile.facebookfeatureset.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.phimobile.facebookfeatureset.MainActivity;

public class BasicMainActivityTestCase extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public BasicMainActivityTestCase() {
		super(MainActivity.class);
		Log.e("KK","BasicMainActivityTestCase launched");
	}
	
	//launch activity; check existence and content of textview
	public void testBasicMainActivityFunctionality() {
		Log.e("KK","testBasicMainActivityFunctionality starts..");
		Activity activity = getActivity();
		
		assertTrue(activity != null);
		assertTrue(activity instanceof MainActivity);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.e("KK","inside runnable");
			}
		});
		
		Log.e("KK","runnable started");
		
		TextView v = (TextView) activity.findViewById(
				com.phimobile.facebookfeatureset.R.id.main_helloworld);
		
		assertTrue(v!=null);
		assertTrue(v.getText().toString().equals(activity.getString(
				com.phimobile.facebookfeatureset.R.string.hello_world)));
		
		Log.e("KK","testBasicMainActivityFunctionality done..");
	}
}
