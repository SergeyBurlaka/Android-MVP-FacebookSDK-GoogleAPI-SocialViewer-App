package com.test.gotovjet.burlaka.socialviewer.view;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by Operator on 26.08.2016.
 */
public interface LoginView {
    void specifyGoogleSignIn(GoogleSignInOptions gso);
    void startProfileActivity();
    Context getContext();
     void goToGallery ();
    void showToast(String mssg);
    void callFromVKSignIn(int requestCode, int resultCode, Intent data);
}
