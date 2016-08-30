package com.test.gotovjet.burlaka.socialviewer.presenter.vkontakte;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.test.gotovjet.burlaka.socialviewer.LoginActivity;
import com.test.gotovjet.burlaka.socialviewer.model.Constants;
import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;
import com.test.gotovjet.burlaka.socialviewer.view.LoginView;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Operator on 30.08.2016.
 */
public class VKSignIn extends Application implements VKSignInPresenter {

    private LoginView fieldLoginView;
    public static final String TAG = "getCertificate";
    VKCallback<VKAccessToken> vkAccessTokenVKCallback;
    private String[] scope = {VKScope.EMAIL};
    public UserInfo userModelSingeltone;

    @Override
    public void getCertificateFingerprint (){
        String[] fingerprints = VKUtil.getCertificateFingerprint(this,this.getPackageName());
        Log.v(TAG, "index=" + Arrays.toString(fingerprints));
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        VKSdk.initialize(this);
    }

    @Override
    public void onStart (LoginView fieldLoginView){
        this.fieldLoginView = fieldLoginView;
       // vkAccessTokenVKCallback = getvkAccessTokenVKCallback();
    }


    @Override
    public void loginVK (LoginActivity loginActivity){
        VKSdk.login(loginActivity, scope);
    }


    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                final String email = res.email;
                userInfoReq(email);
                userPicture ();

            }

            private void userPicture() {
                VKParameters params = new VKParameters();
                params.put(VKApiConst.FIELDS, "photo_max_orig");

                VKRequest request = new VKRequest("users.get",params);
                request.executeWithListener(new VKRequest.VKRequestListener() {

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        JSONArray resp = null;
                        try {
                            resp = response.json.getJSONArray("response");

                        JSONObject user = resp.getJSONObject(0);
                        String photo_max_orig_url = user.getString("photo_max_orig");
                            userModelSingeltone = UserInfo.getInstance();
                            userModelSingeltone.setAvatarURL(photo_max_orig_url);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }
                });
            }

            private void userInfoReq(final String useremail) {
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name,last_name,sex"));
                request.executeWithListener(new VKRequest.VKRequestListener() {


                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList usersList = (VKList) response.parsedModel;
                        // get user
                        VKApiUserFull user = (VKApiUserFull) usersList.get(0);
                        String fn = user.first_name;
                        String ln = user.last_name;
                        String userName = new StringBuilder().append(ln).append(" ").append(fn).toString();
                        userModelSingeltone = UserInfo.getInstance();
                        userModelSingeltone.setUser_name(userName);
                        userModelSingeltone.setEmail(useremail);
                        userModelSingeltone.setLogin_status(Constants.LOGIN_IN);
                        fieldLoginView.startProfileActivity();
                    }
                });

            }

            @Override
            public void onError(VKError error) {
            }
        })) {
           fieldLoginView.callFromVKSignIn(requestCode, resultCode, data);   //super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
