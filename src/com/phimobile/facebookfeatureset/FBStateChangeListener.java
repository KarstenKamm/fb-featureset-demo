package com.phimobile.facebookfeatureset;

import com.facebook.Session;
import com.facebook.SessionState;

public interface FBStateChangeListener {
	public void onStatusChange(Session session, SessionState state);
}
