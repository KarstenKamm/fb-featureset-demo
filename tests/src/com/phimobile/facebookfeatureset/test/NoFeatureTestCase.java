package com.phimobile.facebookfeatureset.test;

import com.phimobile.facebookfeatureset.NoFeature;

import android.test.AndroidTestCase;
import android.util.Log;

public class NoFeatureTestCase extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		Log.e("KK","setting up..");
		super.setUp();
		Log.e("KK","setUp() done");
		
	}

	@Override
	protected void tearDown() throws Exception {
		Log.e("KK","tearing down..");
		super.tearDown();
		Log.e("KK","tearDown() done");
	}
	
	public void testNoFeature() {
		Log.e("KK","testing NoFeature class..");
		
		NoFeature f = new NoFeature();
		assertEquals(false, f.inverBoolean(true));
		assertEquals(true, f.inverBoolean(false));
		
		Log.e("KK","testNoFeature() done");
	}

	
}
