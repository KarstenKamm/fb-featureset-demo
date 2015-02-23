package com.phimobile.facebookfeatureset;

import com.facebook.Session;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Share1Fragment extends Fragment {

	private FBFeatureSetMainActivity activity;
	private boolean isRunning = false;

	
	//TODO KK 1 customizable url  
	//TODO KK 1 share with metainfo
	//TODO KK 1 share local image with feed / api call
	
	private Button shareWithShareDialogBtn;
	private Button shareWithFeedDialogBtn;
	private Button shareWithApiCallBtn;
	private Button sharePhotoBtn;
	private Button shareStoryBtn;
	
	private static final String defaultTestUrl = "https://karstenkamm.github.io/hello-world/fbtester-target.html";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_share1, container, false);
		shareWithApiCallBtn = (Button) v.findViewById(R.id.share1_sharetext_apicall_btn);
		shareWithFeedDialogBtn = (Button) v.findViewById(R.id.share1_sharetext_feeddialog_btn);
		shareWithShareDialogBtn = (Button) v.findViewById(R.id.share1_sharetext_sharedialog_btn);
		sharePhotoBtn = (Button) v.findViewById(R.id.share1_sharephoto_btn);
		shareStoryBtn = (Button) v.findViewById(R.id.share1_sharestory_btn);
		
		shareStoryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkIfLoggedIntoFb()) {
					activity.shareStoryWithShareDialog("title");
				}
			}
		});
		
		sharePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkIfLoggedIntoFb()) {
					activity.sharePhotoWithShareDialog();
				}
			}
		});
		shareWithShareDialogBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkIfLoggedIntoFb()) {
					activity.shareWithShareDialog(defaultTestUrl, false);
				}
			}
		});
		shareWithFeedDialogBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkIfLoggedIntoFb()) {
					activity.shareWithFeedDialog(defaultTestUrl);
				}
				
			}
		});
		shareWithApiCallBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkIfLoggedIntoFb()) {
					activity.shareWithApiCall(defaultTestUrl);
				}
			}
		});
		return v;
	}

	protected boolean checkIfLoggedIntoFb() {
		Session session = Session.getActiveSession();
		return (activity!=null && session !=null && session.isOpened());
	}

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (FBFeatureSetMainActivity) activity;
	}
	@Override
	public void onDetach() {
		super.onDetach();
		this.activity.setOnFBStateChangeListener(null);
	}

	@Override
	public void onResume() {
		super.onResume();
		isRunning = true;
	}

	@Override
	public void onPause() {
		isRunning = false;
		super.onPause();
	}

}
