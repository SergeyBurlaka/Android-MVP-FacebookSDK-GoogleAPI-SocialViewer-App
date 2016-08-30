package com.test.gotovjet.burlaka.socialviewer.presenter.facebook;

import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.gotovjet.burlaka.socialviewer.model.Constants;
import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;
import com.test.gotovjet.burlaka.socialviewer.view.ProfileView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Operator on 28.08.2016.
 */
public class FBGetInfo implements FBGetInfoPresenter {
    ProfileView profileView;

    public FBGetInfo(ProfileView profileView) {
        //UserInfo.InnerUserModel.getInstance().addChangeListener(this);
        this.profileView = profileView;
    }

    @Override
    public void getMyProfileRequest (){

        Bundle params = new Bundle();
        params.putString("fields", "email,name,first_name,last_name,birthday,picture.type(large)");

       new GraphRequest(AccessToken.getCurrentAccessToken(),  "/me/", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public ImageLoader imageLoader;
                    public ImageView mImageView;
                    public UserInfo userModel;

                    @Override
                    public void onCompleted( GraphResponse response) {
                            saveDataInSingletone(response);
                            profileView.setInfoToView();
                    }
                    private void saveDataInSingletone(GraphResponse response)  {
                        JSONObject data = response.getJSONObject();
                        userModel = UserInfo.getInstance();
                        String lastName, firstName;
                        String profilePicUrl;

                        if (response.getError() != null){ onOfflineWork(); return; }

                        if (data.has("picture")) {
                            try {
                                profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                userModel.setAvatarURL(profilePicUrl);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // set profile image to imageview using Picasso or Native methods
                        }

                        try {
                            userModel.setEmail( data.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            userModel.setEmail("");
                        }

                        try {
                            userModel.setBday(data.getString("birthday"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            userModel.setBday("");
                        }

                        try {
                            lastName = response.getJSONObject().getString("last_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            lastName = "";
                        }

                        try {
                            firstName = data.getString("first_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            firstName = "";
                        }

                        String userName = new StringBuilder().append(lastName).append(" ").append(firstName).toString();
                            userModel.setUser_name(userName);
                           // userModel.saveCash(profileView.getContext());
                    }


                    private void onOfflineWork() {
                    //userModel.readCash(profileView.getContext());
                    userModel.setInternet_status(Constants.OFFLINE);
                    profileView.setInfoToView();
                    }

                }).executeAsync();
    }


    @Override
    public void onDestroy() {
        profileView = null;
    }

}
