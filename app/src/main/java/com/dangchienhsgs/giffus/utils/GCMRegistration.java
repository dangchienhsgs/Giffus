package com.dangchienhsgs.giffus.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.Random;

/**
 * Created by dangchienhsgs on 23/09/2014.
 */
public class GCMRegistration {
    private static final String TAG="GcmUtil";

    public static final String PROPERTY_REG_ID="registration_id";
    private static final String PROPERTY_APP_VERSION="appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME="onServerExpirationTimeMs";

    public static final long REGISTRATION_EXPIRY_TIME_MS=1000*3600*24*7;

    // Default lifespan 7days

    // Number of times try to connect register on gcm
    private static final int MAX_ATTEMPTS=5;
    private static final int BACKOFF_MILLI_SECONDS=2000;
    private static final Random random=new Random();

    private Context ctx;
    private SharedPreferences prefs;
    private GoogleCloudMessaging gcm;
    private AsyncTask registrationTask;

    public GCMRegistration(Context ctx) {
        super();
        this.ctx = ctx;
        prefs= PreferenceManager.getDefaultSharedPreferences(ctx);

        gcm = GoogleCloudMessaging.getInstance(ctx);
    }

    /**
     * Get the current registration if for application on GCM Service
     * If result is empty, the registration has failed
     *
     * @return registrationId, or empty string if registration is not complete
     */
    private String getRegistrationId(){
        // Check registration if it exists
        String registrationId=prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()){
            return "";
        }

        // Check app version if they equal
        int registeredVersion=prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion=getAppVersion();

        if (registeredVersion!=currentVersion){
            return "";
        }
        // return result if it valid
        return registrationId;
    }

    private void setRegistrationId(String regId){
        int appVersion=getAppVersion();
        SharedPreferences.Editor editor=prefs.edit();

        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        long expirationTime=System.currentTimeMillis()+REGISTRATION_EXPIRY_TIME_MS;
        editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);

        editor.commit();
    }
    private int getAppVersion(){
        try{
            PackageInfo packageInfo=ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            throw new RuntimeException("Could not get package name: "+e);
        }
    }

    /**
     * Check if the registration has expired
     * <p> To avoid the situation that the device send registrations but server
     * lose it and the app developer may choose to re-register after REGISTRATION_EXPIRY_MS
     * @return true if the registration has expired
     */
    private boolean isRegistrationExpired(){
        long expirationTime=prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
        return System.currentTimeMillis()>expirationTime;
    }

    private void broadcastStatus(boolean status){
        Intent intent=new Intent(Common.REGISTER_ACITIVY);
        intent.putExtra(Common.EXTRA_STATUS, status ? Common.STATUS_SUCCESS : Common.STATUS_FAIL);
        ctx.sendBroadcast(intent);
    }

    public void cleanup(){
        if (registrationTask!=null){
            registrationTask.cancel(true);
        }
        if (gcm!=null){
            gcm.close();
        }
    }
}

