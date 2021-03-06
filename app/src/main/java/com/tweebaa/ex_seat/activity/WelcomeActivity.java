package com.tweebaa.ex_seat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tweebaa.ex_seat.R;
import com.tweebaa.ex_seat.model.DataUtil;

import org.json.JSONObject;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity  {

	private LoginButton mFacebookLoginButton;
	private CallbackManager mFacebookCallbackManager;
	private AccessTokenTracker mFacebookAccessTokenTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_welcome);

		mFacebookLoginButton = (LoginButton)findViewById(R.id.login_with_facebook);
		mFacebookLoginButton.setReadPermissions(Arrays.asList("user_friends", "email", "user_photos", "user_location", "user_birthday", "public_profile"));
		mFacebookCallbackManager = CallbackManager.Factory.create();

		/*mFacebookAccessTokenTracker = new AccessTokenTracker() {
	        @Override
	        protected void onCurrentAccessTokenChanged(
	            AccessToken oldAccessToken,
	            AccessToken currentAccessToken) {
	                // Set the access token using
	                // currentAccessToken when it's loaded or set.
	        }
	    };*/
	    // If the access token is available already assign it.
	    DataUtil.accessToken = AccessToken.getCurrentAccessToken();
		if(DataUtil.accessToken != null){
			//String sLogonName = DataUtil.accessToken.getUserId();
			//Toast.makeText(getApplicationContext(),sLogonName,Toast.LENGTH_LONG).show();
			DataUtil.profile = Profile.getCurrentProfile();
		}


	 // Callback registration
		mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				//super.onSucess(loginResult);

				DataUtil.accessToken = loginResult.getAccessToken();
				DataUtil.profile = Profile.getCurrentProfile();
				//String sLogonName = DataUtil.accessToken.getUserId();
				//Toast.makeText(getApplicationContext(),sLogonName,Toast.LENGTH_LONG).show();*/

				GraphRequest request = GraphRequest.newMeRequest(
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
				request.executeAsync();

				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);  //从IntentActivity跳转到SubActivity
				intent.putExtra("name", "raymond");  //放入数据
				startActivity(intent);  //开始跳转
				LoginManager.getInstance().logOut();
				finish();
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

				Button m_Signinbtn = (Button) findViewById(R.id.button1);
				m_Signinbtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Toast.makeText(getApplicationContext(),"Clicked", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						//intent.setClass(WelcomeActivity.this, MainActivity.class);  //从IntentActivity跳转到SubActivity
						intent.setClass(WelcomeActivity.this, LoginActivity.class);
						intent.putExtra("name", "raymond");  //放入数据
						startActivity(intent);  //开始跳转
					}
				});

			}



			@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
				mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
			}

			@Override
			public void onDestroy() {
				super.onDestroy();
				//mFacebookAccessTokenTracker.stopTracking();
			}

		}
