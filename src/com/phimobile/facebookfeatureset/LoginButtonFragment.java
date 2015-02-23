package com.phimobile.facebookfeatureset;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginButtonFragment extends Fragment implements FBStateChangeListener {

	private final static String TAG = "LoginButtonFragment";
	private LoginButton login;
	private View loggedInContent;
	private Button ownRequestButton;
	
	private TextView name;
	private TextView gender;
	private TextView location;
	
	private FBFeatureSetMainActivity activity;
	
	private boolean isRunning;

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (FBFeatureSetMainActivity) activity;
		this.activity.setOnFBStateChangeListener(this);
	}
	

	@Override
	public void onDetach() {
		super.onDetach();
		this.activity.setOnFBStateChangeListener(null);
	}


	//TODO KK 1 new feature: show permissions
	//TODO KK 2 choose permissions, add to login button
	//TODO KK 2 display ShareDialog.canpresentxx infos
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container,false);
		
		login = (LoginButton) v.findViewById(R.id.login_fbloginbutton);
		//login.setReadPermissions(Arrays.asList("user_likes", "user_status","email","user_birthday"));
		
		loggedInContent = v.findViewById(R.id.login_loggedincontent);
		
		Session session = Session.getActiveSession();
		if (session!=null && session.isOpened()) {
			loggedInContent.setVisibility(View.VISIBLE);
		}else {
			loggedInContent.setVisibility(View.GONE);
		}
		
		name = (TextView) v.findViewById(R.id.login_name);
		gender = (TextView) v.findViewById(R.id.login_gender);
		location = (TextView) v.findViewById(R.id.login_location);
		ownRequestButton = (Button) v.findViewById(R.id.login_ownrequestbutton);
		ownRequestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Session session = Session.getActiveSession();
				if (session != null) {
					// make request to the /me API to get Graph user
					Request.newMeRequest(session, new Request.GraphUserCallback() {
	
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							Log.e(TAG,"own request completed");
							if (user != null) {
								
								// Set User name 
								name.setText("Hello " + user.getName());
								// Set Gender
								gender.setText("Your Gender: "
										+ user.getProperty("gender").toString());
								location.setText("Your Current Location: "
										+ (user.getLocation()==null?"unbekannt":
											user.getLocation().getProperty("name").toString()));
							} else {
								Log.e(TAG,"user is null");
							}
						}
					}).executeAsync();
				} else {
					Log.wtf("KK","session is " + (session==null?"null":"not opened"));
				}
			}
		});
		
		
		return v;
	}
	

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To maintain FB Login session
		
	}

	@Override
	public void onStatusChange(Session session, SessionState state) {

		if (isRunning) {
			if (state.isOpened()) {
				loggedInContent.setVisibility(View.VISIBLE);
			} else if (state.isClosed()) {
				loggedInContent.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onPause() {
		isRunning = false;
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		isRunning = true;
	}
	





}
