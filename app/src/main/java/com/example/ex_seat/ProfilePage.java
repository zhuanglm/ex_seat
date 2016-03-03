package com.example.ex_seat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zhuang on 2016-03-03.
 */
public class ProfilePage extends Fragment {

    private Activity mAct;

    private TextView text_username;
    private ProfilePictureView profilePictureView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAct = getActivity();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle bundle = getArguments();
        //bundle.getString("EVENT");
        //setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_record, container, false);

        text_username = (TextView) view.findViewById(R.id.text_username);
        final TextView text_email = (TextView) view.findViewById(R.id.textEmail);
        final TextView text_city = (TextView) view.findViewById(R.id.textLocation);

        if(DataDef.accessToken != null) {
            //text_username.setText(DataDef.profile.getName());
            profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePicture);
            //profilePictureView.setProfileId(DataDef.profile.getId());

            GraphRequest request = GraphRequest.newMeRequest(
                    DataDef.accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {
                                // handle error
                            } else {
                                String email = me.optString("email");
                                String name = me.optString("name");
                                String id = me.optString("id");
                                String birthday = me.optString("birthday");

                                try{
                                    String location = me.getJSONObject("location").optString("name");
                                    text_city.setText(location);
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                text_username.setText(name);
                                text_email.setText(email);
                                profilePictureView.setProfileId(id);
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,location,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
