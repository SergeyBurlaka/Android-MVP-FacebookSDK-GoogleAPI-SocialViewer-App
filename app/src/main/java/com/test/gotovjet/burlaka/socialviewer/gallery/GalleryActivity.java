package com.test.gotovjet.burlaka.socialviewer.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.test.gotovjet.burlaka.socialviewer.LoginActivity;
import com.test.gotovjet.burlaka.socialviewer.R;
import com.test.gotovjet.burlaka.socialviewer.model.Constants;
import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ArrayList<String> Photo_list_id = new ArrayList<String>();
    private ViewPager viewPager;
    private ImageViewAdapter imageViewAdapter;
    private UserInfo userModelSingleton;
    private PictureCash pictureCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        getPicture ();
        userModelSingleton = UserInfo.getInstance();
        pictureCash = new PictureCash(this);
    }

    public  void getPicture (){
        /*make API call*/
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("TAG", "Facebook Albums: " + response.toString());
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                   // alFBAlbum = new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                        GetFacebookImages(joAlbum.optString("id")); //find Album ID and get All Images from album
                                        //GetFacebookImages(joAlbum.getString("id"));
                                    }
                                }
                            } else {
                                Log.d("Test", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();}

    public void GetFacebookImages(final String albumId) {
      //  String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        parameters.putString("limit", "100");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);

                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();
                                JSONObject joAlbum = new JSONObject();

                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");

                                    for (int a = 0; a < jaData.length(); a++){//Get no. of images {
                                        joAlbum = jaData.getJSONObject(a);
                                        JSONArray jaImages=joAlbum.getJSONArray("images"); //get images Array in JSONArray format

                                        String tempSourceImage = jaImages.getJSONObject(0).getString("source");
                                        Photo_list_id.add(tempSourceImage);
                                        if (imageViewAdapter!= null)
                                        imageViewAdapter.notifyDataSetChanged();

                                    }
                                }

                                //set your adapter here
                                setAdapter ();

                            } else {
                            Log.v("TAG", response.getError().toString());
                        }
                    } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    private void setAdapter() {
                        if (imageViewAdapter == null){
                            imageViewAdapter = new ImageViewAdapter(
                                    GalleryActivity.this, Photo_list_id);
                            viewPager.setAdapter(imageViewAdapter);
                        }
                    }
                }).executeAsync();
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
            userModelSingleton.setLogin_status(Constants.LOGIN_OUT);
            userModelSingleton.saveCash(this);
        LoginManager.getInstance().logOut();
        Intent goToEditSign = new Intent( GalleryActivity.this, LoginActivity.class);
        startActivity(goToEditSign);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModelSingleton.saveCash(this);
    }
}





