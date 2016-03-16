package com.colonycount.cklab;

import android.app.Application;

/**
 * Created by kyle on 2016/2/25.
 */
public class MyApplication extends Application {


    private static MyApplication mInstance;

    public static Boolean loginState = Boolean.FALSE;
    private String email;
    private String id;
    private String displayName;
    private int    gender;
    private String aboutMe;
    private String birthday;
    private String currentLocation;
    private String language;
    private String nickname;
    private String url;
    private String familyName = "";
    private String givenName  = "";
    private int ageMin = -1;
    private int ageMax = -1;
    private String imageUrl = "";
    private String orgs     = "";
    private Boolean count_lock =  Boolean.FALSE;


    public static String userName = "Hi";
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public void setEmail(String email){
        mInstance.email = email;
    }
    public void setId(String id){
        mInstance.id = id;
    }
    public void setDisplayName(String displayName){
        mInstance.displayName = displayName;
    }
    public void setGender(int gender){
        mInstance.gender = gender;
    }
    public void setAboutMe(String aboutMe){
        mInstance.aboutMe = aboutMe;
    }
    public void setBirthday(String birthday){
        mInstance.birthday = birthday;
    }
    public void setCurrentLocation(String currentLocation){
        mInstance.currentLocation = currentLocation;
    }
    public void setLanguage(String language){
        mInstance.language = language;
    }
    public void setNickname(String nickname){
        mInstance.nickname = nickname;
    }
    public void setUrl(String url){
        mInstance.url = url;
    }
    public void setName(String familyName, String givenName){
        mInstance.familyName = familyName;
        mInstance.givenName = givenName;
    }
    public void setAge(int ageMin, int ageMax){
        mInstance.ageMin  = ageMin;
        mInstance.ageMax  = ageMax;
    }
    public void setImageUrl(String imageUrl){
        mInstance.imageUrl = imageUrl;
    }
    public void addOrgs(String orgs){
        mInstance.orgs += orgs;
    }
    public String getEmail(){
        return mInstance.email;
    }
    public String getId(){
        return mInstance.id;
    }
    public String getDisplayName(){
        return mInstance.displayName;
    }
    public int getGender(){
        return mInstance.gender;
    }
    public String getAboutMe(){
        return mInstance.aboutMe;
    }
    public String getBirthday(){
        return mInstance.birthday;
    }
    public String getCurrentLocation(){
        return mInstance.currentLocation;
    }
    public String getLanguage(){
        return mInstance.language;
    }
    public String getNickname(){
        return mInstance.nickname;
    }
    public String getUrl(){
        return mInstance.url;
    }
    public String getFamilyName(){
        return mInstance.familyName;
    }
    public String getGivenName(){
        return mInstance.givenName;
    }
    public int getAgeMin(){
        return mInstance.ageMin;
    }
    public int getAgeMax(){
        return mInstance.ageMax;
    }
    public String getImageUrl(){
        return mInstance.imageUrl;
    }
    public String getOrgs(){
        return mInstance.orgs;
    }
    public Boolean getLockState(){return count_lock;}
    public void LockOn(){count_lock = Boolean.TRUE;}
    public void LockOff(){count_lock = Boolean.FALSE;}

}
