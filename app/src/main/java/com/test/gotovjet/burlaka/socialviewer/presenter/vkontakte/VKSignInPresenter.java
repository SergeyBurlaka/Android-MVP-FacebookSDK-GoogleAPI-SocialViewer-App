package com.test.gotovjet.burlaka.socialviewer.presenter.vkontakte;

import android.content.Intent;

import com.test.gotovjet.burlaka.socialviewer.LoginActivity;
import com.test.gotovjet.burlaka.socialviewer.view.LoginView;

/**
 * Created by Operator on 30.08.2016.
 */
public interface VKSignInPresenter {
    void getCertificateFingerprint ();
    void onStart (LoginView fieldLoginView);
    void loginVK(LoginActivity loginActivity);
     void onActivityResult (int requestCode, int resultCode, Intent data);
}
