package com.test.gotovjet.burlaka.socialviewer.presenter.facebook;

//import com.facebook.FacebookSdk;

import android.app.Activity;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.test.gotovjet.burlaka.socialviewer.model.Constants;
import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;
import com.test.gotovjet.burlaka.socialviewer.view.LoginView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Operator on 26.08.2016.
 */
public class FBSignIn implements FBSignInPresenter {

    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN_FB = 1916;
    private LoginView fieldLoginView;
    private List<String> permissionNeeds = Arrays.asList("public_profile",
            "email",  "user_photos", "user_birthday");

    public  GO intent = null;

    public FBSignIn(LoginView loginView) {
        this.fieldLoginView = loginView;
    }

    @Override
    public void initSignInFB() {
        FacebookSdk.sdkInitialize(fieldLoginView.getContext(), RC_SIGN_IN_FB);
    }

    @Override
    public void logIn (){
        callbackManager = CallbackManager.Factory.create();
        FacebookCallback<LoginResult> callback = new Callback();
        LoginManager.getInstance().logInWithReadPermissions((Activity) fieldLoginView,
                permissionNeeds);
        LoginManager.getInstance().registerCallback(callbackManager,callback);
    }

    @Override
    public void logIn (GO go){
        this.intent = go;
        logIn ();
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        fieldLoginView = null;
    }
    private class Callback implements FacebookCallback<LoginResult> {
        private UserInfo userModel;

        public Callback ( ){
        }

        @Override
        public void onSuccess(LoginResult loginResult) {
            userModel = UserInfo.getInstance();

            if (intent == GO.GALLERY){
                userModel.setLogin_status(Constants.IN_GALLERY);
                userModel.saveCash(fieldLoginView.getContext());
                fieldLoginView.goToGallery();
                intent = null;
                return;}

            if(isLoggedIn()) {
                userModel.setService(Constants.FB_SERVICE);
                userModel.setLogin_status(Constants.LOGIN_IN);
                fieldLoginView.startProfileActivity();
            }
        }

        public boolean isLoggedIn() {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            return accessToken != null;
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {

        }
    }

    public static  enum GO {
        GALLERY
    }
}
