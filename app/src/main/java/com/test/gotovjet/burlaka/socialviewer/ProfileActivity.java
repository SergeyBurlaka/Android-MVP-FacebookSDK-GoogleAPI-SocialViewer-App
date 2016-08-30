package com.test.gotovjet.burlaka.socialviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.test.gotovjet.burlaka.socialviewer.model.Constants;
import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;
import com.test.gotovjet.burlaka.socialviewer.presenter.facebook.FBGetInfo;
import com.test.gotovjet.burlaka.socialviewer.presenter.facebook.FBGetInfoPresenter;
import com.test.gotovjet.burlaka.socialviewer.view.ProfileView;

public class ProfileActivity extends AppCompatActivity implements ProfileView, PopupMenu.OnMenuItemClickListener {

    private FBGetInfoPresenter fbGetInfoPresenter;
    public TextView userName;
    public TextView userEmail;
    public TextView userbday;;
    private UserInfo userDataModel;
    private ImageView userAvatar;
    private ImageLoader imageLoader;
    private ImageView photoBackground;
    private ImageLoaderConfiguration config;
    private UserInfo userModelSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        //user_profile_name
        userName = (TextView)findViewById(R.id.user_profile_name);
        userEmail = (TextView)findViewById(R.id.user_profile_short_bio);
        userbday = (TextView) findViewById(R.id.user_bday);
        userAvatar = (ImageView) findViewById(R.id.user_profile_photo);
        photoBackground = (ImageView) findViewById(R.id.header_cover_image);

        //get FB profile
       // showToast("hello_world");
        userModelSingleton = UserInfo.getInstance();
        if(userModelSingleton.getService() == Constants.FB_SERVICE){
            fbGetInfoPresenter = new FBGetInfo(this);
            fbGetInfoPresenter.getMyProfileRequest();
        }else{

            setInfoToView ( );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.edit_profile:
                Toast.makeText(getBaseContext(), "You selected Video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.out:
                Toast.makeText(getBaseContext(), "You selected EMail", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void setInfoToView ( ) {
            userDataModel = UserInfo.getInstance();
            userName.setText(userDataModel.getUser_name());
            userEmail.setText(userDataModel.getUser_email());
            userbday.setText("Birthday: ");
            userbday.append(userDataModel.getUser_bday());

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        imageLoader.init(config.build());

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
            imageLoader.displayImage(userDataModel.getAvatarURL(), userAvatar, options);
            imageLoader.displayImage(userDataModel.getAvatarURL(), photoBackground, options);

    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.items, popup.getMenu());
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModelSingleton.saveCash(this);
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
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.out:
                userModelSingleton.setLogin_status(Constants.LOGIN_OUT);
                userModelSingleton.saveCash(this);
                LoginManager.getInstance().logOut();
                Intent goToEditSign = new Intent( ProfileActivity.this, LoginActivity.class);
                startActivity(goToEditSign);
                finish();
                return true;
            case R.id.edit_profile:
                Intent goToEditProfile = new Intent( ProfileActivity.this, EditActivity.class);
                startActivity(goToEditProfile);
                return true;
            default:
                return false;
    }}
}
