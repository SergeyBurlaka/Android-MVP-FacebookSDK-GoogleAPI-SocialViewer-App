package com.test.gotovjet.burlaka.socialviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.test.gotovjet.burlaka.socialviewer.gallery.GalleryActivity;
import com.test.gotovjet.burlaka.socialviewer.model.Constants;
import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;
import com.test.gotovjet.burlaka.socialviewer.presenter.facebook.FBSignIn;
import com.test.gotovjet.burlaka.socialviewer.presenter.facebook.FBSignInPresenter;
import com.test.gotovjet.burlaka.socialviewer.presenter.google.GoogleSignIn;
import com.test.gotovjet.burlaka.socialviewer.presenter.google.GoogleSignInPresenter;
import com.test.gotovjet.burlaka.socialviewer.presenter.vkontakte.VKSignIn;
import com.test.gotovjet.burlaka.socialviewer.presenter.vkontakte.VKSignInPresenter;
import com.test.gotovjet.burlaka.socialviewer.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private GoogleSignInPresenter signInGooglePresenter;
    private  SignInButton signInButton;
    private FBSignInPresenter signInFBPresenter;
    private Button nextButton;
    private Button goToProfile;
    private UserInfo userModelSingelton;
    private VKSignInPresenter signInVKPresenter;
    private Button vkButton;
    //private String tokenToFaceBook;
    //public static boolean activityLive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FB
        signInFBPresenter = new FBSignIn(this);
        signInFBPresenter.initSignInFB();

        userModelSingelton = UserInfo.getInstance();
        userModelSingelton.readCash(this);
         switch (userModelSingelton.getLogin_status()){
             case Constants.LOGIN_IN:
                 startProfileActivity();
                 break;
             case Constants.IN_GALLERY:
                 goToGallery ();
         }
        setContentView(R.layout.activity_login);

        LoginButton buttonFB = (LoginButton) findViewById(R.id.facebook_button);
        buttonFB.setReadPermissions("email", "public_profile","user_birthday", "user_photos");
       // buttonFB.sr
        buttonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFBPresenter.logIn ();
            }
        });

        //Google+
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGooglePresenter.signIn(LoginActivity.this);
            }
        });
        signInGooglePresenter = new GoogleSignIn(this);
        signInGooglePresenter.createGoogleClient(this);
        //VK
        signInVKPresenter = new VKSignIn();
        signInVKPresenter.getCertificateFingerprint();
        //signInVKPresenter.initVKSDK(this);
        //VKSdk.initialize(getContext());
        vkButton = (Button)findViewById(R.id.sign_in_vk_button);
        vkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInVKPresenter.loginVK(LoginActivity.this);
            }
        });
        //FB photo gallery
        nextButton = (Button) findViewById( R.id.nextButton );
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFBPresenter.logIn (FBSignIn.GO.GALLERY);
            }
        });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    protected void onStart() {
        super.onStart();
        signInGooglePresenter.onStart();
        signInVKPresenter.onStart(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        signInGooglePresenter.onActivityResult(LoginActivity.this, requestCode, resultCode, data);
        signInFBPresenter.onActivityResult( requestCode, resultCode, data);
        signInVKPresenter.onActivityResult(requestCode, resultCode, data);
    }


    //For pulling LoginActivity by presenter
    @Override
    public void specifyGoogleSignIn(GoogleSignInOptions gso) {
        signInButton.setScopes(gso.getScopeArray());
    }


    @Override
    public void startProfileActivity() {
        Intent goToProfile = new Intent(LoginActivity.this, ProfileActivity.class);
               /* if (tokenToFaceBook != null)
                 goToGallery.putExtra("token_face_book", tokenToFaceBook);*/
        startActivity(goToProfile);
        finish();
    }


    @Override
    public void goToGallery (){
        Intent goToGallery = new Intent(LoginActivity.this, GalleryActivity.class);
               /* if (tokenToFaceBook != null)
                 goToGallery.putExtra("token_face_book", tokenToFaceBook);*/
        startActivity(goToGallery);
        finish();
    }


    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }


    @Override
    public void showToast(String mssg) {
        Toast.makeText(this, mssg ,
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void callFromVKSignIn(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed(){
       // moveTaskToBack(true);
       // super.onBackPressed();
       // finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activityLive = false;
        signInGooglePresenter.onDestroy();
        signInFBPresenter.onDestroy();
    }
}
