package com.phimobile.facebookfeatureset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import bolts.AppLinks;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;


public class FBFeatureSetMainActivity extends FragmentActivity {
	private static final String TAG = "FB SDK Tester Activity";
	
	private FrameLayout container;
	
	private UiLifecycleHelper uiHelper;
	private FBStateChangeListener listener;
	private SessionState state;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
        Settings.sdkInitialize(this); 
        Uri targetUrl =
          AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
            Toast.makeText(this, "linked from fb post: targetUrl.toString()", Toast.LENGTH_SHORT).show();
            
        }
        
        //TODO KK 3 show api level, fb sdk version
        //TODO KK 0 try this: authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
        container = (FrameLayout) findViewById(R.id.fragment_container);
        
        switchToLoginFragment();
//        switchToUserSettingsFragment();
    }
    
    public void switchToShare1Fragment() {
    	switchToFragment(new Share1Fragment());
    }
    
    public void switchToUserSettingsFragment() {
		switchToFragment(new LoginUserSettingsFragment());
		
	}

	public void switchToLoginFragment() {
    	switchToFragment(new LoginButtonFragment());
    }
    
    
    
    private void switchToFragment(Fragment frag) {
    	getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        invalidateOptionsMenu();
        return true;
    }

    

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Session session = Session.getActiveSession();
		for (int i=0;i<menu.size();i++) {
			if (menu.getItem(i).getItemId()== R.id.menu_share1) {
				if (session!=null && session.isOpened()) {
					menu.getItem(i).setVisible(true);
				} else {
					menu.getItem(i).setVisible(false);
				}
			}
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId()== R.id.menu_loginbutton){
			switchToLoginFragment();
			return true;
		}
		if (item.getItemId()== R.id.menu_usersettingsfragment){
			switchToUserSettingsFragment();
			return true;
		}
		if (item.getItemId()== R.id.menu_share1){
			switchToShare1Fragment();
			return true;
		}
		
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
		uiHelper.onPause();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	
	// Called when session changes
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (FBFeatureSetMainActivity.this.state!=null) {
				Log.w("KK", "fb state change: was " + 
					FBFeatureSetMainActivity.this.state.name() + 
					", is: " + state.name());
			} else {
				Log.w("KK", "fb state established: " + state.name());
			}
			
			
			FBFeatureSetMainActivity.this.state = state;   
			
			if (listener!=null) {
				listener.onStatusChange(session, state);
			}

			invalidateOptionsMenu();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e(TAG, String.format("Error: %s", error.toString()));
	            
	            logFBActivityResultData(data);
	        }

			private void logFBActivityResultData(Bundle data) {
				boolean didCancel = FacebookDialog.getNativeDialogDidComplete(data);
	            String completionGesture = FacebookDialog.getNativeDialogCompletionGesture(data);
	            String postId = FacebookDialog.getNativeDialogPostId(data);
	            Log.w(TAG,"post " + postId + " canceled? " + didCancel + "; completion: " + completionGesture);
			}

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i(TAG, "Success!");
	            logFBActivityResultData(data);
	        }});
		Log.i(TAG, "OnActivityResult...");
	}

	public void setOnFBStateChangeListener(FBStateChangeListener listener) {
		this.listener = listener; 
	}

	public SessionState getFBState() {
		return state;
	}
	
	
	public void shareWithShareDialog(final String url, boolean useAppLinkingWithoutWebPage) {
		Log.e("KK","sharing with ShareDialog: " + url);
		if ( FacebookDialog.canPresentShareDialog(getApplicationContext(), 
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG) ) {
			if (useAppLinkingWithoutWebPage) {
				//TODO KK 1 null check Session
				new Request(Session.getActiveSession(),"/app/app_link_hosts",null,HttpMethod.GET,new Request.Callback() {
					//TODO KK 1 check this branch: app link without web page
					@Override
					public void onCompleted(Response response) {
						FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(FBFeatureSetMainActivity.this)
				        .setLink(url)
				        .build();
				uiHelper.trackPendingDialogCall(shareDialog.present());
					}
				}).executeAsync();
				
			} else {
				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
			        .setLink(url)
			        .build();
				
				uiHelper.trackPendingDialogCall(shareDialog.present());
			}
		} else {
			Toast.makeText(this, "Cant show Share dialog", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void sharePhotoWithShareDialog() {
		List<Bitmap>photos = new ArrayList<Bitmap>();
		photos.add(BitmapFactory.decodeResource(getResources(), R.drawable.client_launcher_icon));
		
		//TODO KK 2 check that photos has no null elements
//		photos.add(BitmapFactory.decodeResource(getResources(), R.drawable.com_facebook_button_like_icon));
		if (FacebookDialog.canPresentShareDialog (getApplicationContext(),
			    FacebookDialog.ShareDialogFeature.PHOTOS)) {
			
			//note: dont use setApplicationName(), images will not upload if you do
			  // Publish the post using the Photo Share Dialog
			  FacebookDialog shareDialog = new FacebookDialog.PhotoShareDialogBuilder(this)
			    .addPhotos(photos)
//			    .setRequestCode(requestCode)
			    .build();
			  uiHelper.trackPendingDialogCall(shareDialog.present());
			} else {
				Toast.makeText(this, "Cant show Photo Share dialog", Toast.LENGTH_SHORT).show();
			}
	}
	
	public void shareStoryWithShareDialog(String objectTitle) {
		
		
		OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
		action.setProperty("object", "https://example.com/book/Snow-Crash.html");
		action.setType("fbfeaturesettester:find");
		//TODO KK 1 test if can present
		FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(this, action, "object")
		        .build();
		uiHelper.trackPendingDialogCall(shareDialog.present());
	}
	
	public void shareWithFeedDialog(String url) {
	    Bundle params = new Bundle();
	    params.putString("name", "Facebook SDK Tester: Feed Dialog");
	    params.putString("caption", "link text goes here..");
	    params.putString("description", "the feed dialog text content an image are customizable!");
	    params.putString("link", "http://de.wikipedia.org");
//	    params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(FBFeatureSetMainActivity.this,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }

	        })
	        .build();
	    feedDialog.show();
	}


	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	public void shareWithApiCall(String url) {
		Session session = Session.getActiveSession();

	    if (session != null){
	    	
//TODO KK 0 implement reauth process

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK Tester: API call");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", url);

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getApplicationContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }
		
	}
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
}
