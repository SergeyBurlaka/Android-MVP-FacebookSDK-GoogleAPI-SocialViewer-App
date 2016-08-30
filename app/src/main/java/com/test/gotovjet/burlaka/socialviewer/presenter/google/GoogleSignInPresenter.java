package com.test.gotovjet.burlaka.socialviewer.presenter.google;

import android.content.Intent;

import com.test.gotovjet.burlaka.socialviewer.LoginActivity;

/**
 * Created by Operator on 26.08.2016.
 */
public interface GoogleSignInPresenter {
    void createGoogleClient (LoginActivity loginView);
    void onStart();
    void signIn(LoginActivity loginView);
    void onActivityResult (LoginActivity loginView,int requestCode, int resultCode, Intent data);
    void onStop ();
    void onDestroy();
}
