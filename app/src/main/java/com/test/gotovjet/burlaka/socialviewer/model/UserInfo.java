package com.test.gotovjet.burlaka.socialviewer.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Operator on 28.08.2016.
 */
public class UserInfo {

   // private static  UserInfo userInstance = null;
    private String user_email;
    private String  user_name;
    private String user_bday;
    private int internet_status ;
    private int login_status;
    private int service;
    private String id;
    private String avatar;
    SharedPreferences.Editor save_cash;
    SharedPreferences read_cash;
    private UserInfo user;


    private static UserInfo mInstance = null;

    public void setAvatarURL(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarURL() {
        return avatar;
    }

   private UserInfo() {
        internet_status = Constants.ONLINE;
    }

    public static UserInfo getInstance(){
        if(mInstance == null)
        {
            mInstance = new UserInfo();
        }
        return mInstance;
    }

    public void saveCash (Context context){
        save_cash = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE).edit();
        save_cash.putString( Constants.USER_NAME,  user_name);
        save_cash.putString(Constants.USER_EMAIL,  user_email);
        save_cash.putString(Constants.USER_BDAY,  user_bday);
        save_cash.putInt(Constants.SERVICE,  service);
        save_cash.putInt(Constants.LOGIN_STATUS,  login_status);
        save_cash.commit();
    }

    public void readCash (Context context){
        read_cash = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
        user_name =read_cash.getString( Constants.USER_NAME, "NONE");
        user_email = read_cash.getString(Constants.USER_EMAIL, "NONE");
        user_bday = read_cash.getString(Constants.USER_BDAY, "NONE");
        service = read_cash.getInt(Constants.SERVICE, -1);
        login_status = read_cash.getInt(Constants.LOGIN_STATUS, -1);

    }

    //Setters
    public void setEmail(String email) {
        this.user_email = email;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setBday(String bday) {
        this.user_bday = bday;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public void setService(int service) {
        this.service = service;
    }


    public void setId(String id) {this.id = id;}

    //Getters
    public String getUser_email() {
        return user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_bday() {
        return user_bday;
    }

    public int getLogin_status() {
        return login_status;
    }

    public int getService() {
        return service;
    }

    public String getId() {return id;}

    public int getInternet_status() {
        return internet_status;
    }

    public void setInternet_status(int internet_status) {
        this.internet_status = internet_status;
    }
}
