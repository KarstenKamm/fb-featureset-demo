package com.phimobile.facebookfeatureset;

import android.util.Log;

import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;

public class LoginUserSettingsFragment extends UserSettingsFragment {


	@Override
	protected void onSessionStateChange(SessionState state, Exception exception) {
		Log.e("KK","onSessionStateChange " + state.name());
		super.onSessionStateChange(state, exception);
	}

	
}
