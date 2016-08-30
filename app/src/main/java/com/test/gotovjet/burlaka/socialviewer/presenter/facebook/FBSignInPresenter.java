package com.test.gotovjet.burlaka.socialviewer.presenter.facebook;

import android.content.Intent;

/**
 * Created by Operator on 26.08.2016.
 */
public interface FBSignInPresenter {
    void initSignInFB();
    void onActivityResult (int requestCode, int resultCode, Intent data);
    void onDestroy();
    void logIn();
    void logIn (FBSignIn.GO go);
}
