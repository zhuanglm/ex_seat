package com.example.ex_seat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class WelcomeActivity extends Activity {
	
	private LoginButton		loginButton;
	private CallbackManager callbackManager;
	//private AccessTokenTracker	accessTokenTracker;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_welcome);

		ActionBar mActionBar=getActionBar();
		mActionBar.hide();
		
		loginButton = (LoginButton)findViewById(R.id.login_button);
	    loginButton.setReadPermissions(Arrays.asList("user_friends","email", "user_photos","user_location","user_birthday", "public_profile"));
	    callbackManager = CallbackManager.Factory.create();

	    /*accessTokenTracker = new AccessTokenTracker() {
	        @Override
	        protected void onCurrentAccessTokenChanged(
	            AccessToken oldAccessToken,
	            AccessToken currentAccessToken) {
	                // Set the access token using
	                // currentAccessToken when it's loaded or set.
	        }
	    };*/
	    // If the access token is available already assign it.
	    DataDef.accessToken = AccessToken.getCurrentAccessToken();
		if(DataDef.accessToken != null){
			//String sLogonName = DataDef.accessToken.getUserId();
			//Toast.makeText(getApplicationContext(),sLogonName,Toast.LENGTH_LONG).show();
			DataDef.profile = Profile.getCurrentProfile();
		}


	 // Callback registration
	    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
	        @Override
	        public void onSuccess(LoginResult loginResult) {
	        	//super.onSucess(loginResult);
	            // App code
				DataDef.accessToken = loginResult.getAccessToken();
				DataDef.profile = Profile.getCurrentProfile();
				//String sLogonName = DataDef.accessToken.getUserId();
				//Toast.makeText(getApplicationContext(),sLogonName,Toast.LENGTH_LONG).show();

				/*GraphRequest request = GraphRequest.newMeRequest(
						loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
							@Override
							public void onCompleted(JSONObject me, GraphResponse response) {
								if (response.getError() != null) {
									// handle error
								} else {
									String email = me.optString("email");
									String id = me.optString("id");
									// send email and id to your web server
								}
							}
						});
				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,email,location,gender");
				request.setParameters(parameters);
				request.executeAsync();*/
	        }

	        @Override
	        public void onCancel() {
	            // App code
	        }

	        @Override
	        public void onError(FacebookException exception) {
	            // App code
	        }
	    });

		Button m_Startbtn = (Button)findViewById(R.id.button1);
		m_Startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);  //从IntentActivity跳转到SubActivity
				intent.putExtra("name", "raymond");  //放入数据
				startActivity(intent);  //开始跳转
				finish();
            }
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    //accessTokenTracker.stopTracking();
	}

}
