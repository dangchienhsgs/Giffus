package com.dangchienhsgs.giffus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.account.UserHandler;
import com.dangchienhsgs.giffus.provider.DataHelper;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.URLContentHandler;
import com.dangchienhsgs.giffus.utils.UrlBuilder;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private String TAG = "Main Activity";

    private ProgressBar progressBar;
    private EditText edit_username;
    private EditText edit_password;
    private Button button_signIn;
    private Button button_register;

    private String username;
    private String password;


    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
        button_signIn = (Button) findViewById(R.id.button_signIn);
        button_register = (Button) findViewById(R.id.button_register);

        String username = UserHandler.getValueFromPreferences(Common.USERNAME, getApplicationContext());
        String password = UserHandler.getValueFromPreferences(Common.PASSWORD, getApplicationContext());

        if (!username.isEmpty() && !password.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Task check Username and Password when user log in
     * If it match, continue to create a URLContent Download Task to download the resposne
     */
    private class SignInTask extends AsyncTask<String, Void, String> {
        private Activity activity;
        private ProgressBar progressBar;
        private String result_signIn;

        public SignInTask(Activity activity, ProgressBar progressBar) {
            this.activity=activity;
            this.progressBar = progressBar;

        }

        @Override
        protected String doInBackground(String... strings) {
            result_signIn = new URLContentHandler().getURLFirstLine(strings[0]);
            return result_signIn;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result_signIn.contains("true")) {
                // Successful sign in
                // Save username and password to preferences
                Log.d(TAG, "Sign in is successful");
                UserHandler.saveValueToPreferences(Common.USERNAME, username, getApplicationContext());
                UserHandler.saveValueToPreferences(Common.PASSWORD, password, getApplicationContext());
                // Down Load user info
                new DownloadUserInfo(activity, username, password).execute();

            } else {
                // Sign in not successful
                Log.d(TAG, "Sign in not successful");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(activity, "Invalid username or password, please try again",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class RegisterIDTask extends AsyncTask<Void, Void, String> {
        private GoogleCloudMessaging gcm;
        private Activity activity;
        private static final int BACKOFF_MILLI_SECONDS = 2000;
        private static final int MAX_ATTEMPTS = 5;

        private RegisterIDTask(Activity activity) {
            this.activity = activity;
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "is registering Google Cloud Messaging registration ID");
            long backoff = BACKOFF_MILLI_SECONDS;
            for (int i = 0; i < MAX_ATTEMPTS; i++) {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(activity);
                }
                try {
                    String regID = gcm.register(Common.getSenderId());
                    Log.d(TAG, "Registration IDegID is " + regID);
                    return regID;
                } catch (IOException e) {
                    Log.d(TAG, "Registration Google Cloud Messaging was fail");
                    if (i == MAX_ATTEMPTS) {
                        break;
                    }
                    try {
                        Log.d(TAG, "We try to register again");
                        Thread.sleep(backoff);
                    } catch (InterruptedException e2) {
                        Thread.currentThread().interrupt();
                    }
                    backoff = backoff * 2;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String regID) {
            if (regID != null) {
                Log.d(TAG, "Registration is successful, continue to update id to server");
                new UpdateRegistrationId(activity, regID).execute();
            }
        }
    }

    /**
     * Update registration_id to Server
     */

    private class UpdateRegistrationId extends AsyncTask<Void, Void, String> {
        private String registrationID;
        private Activity activity;

        public UpdateRegistrationId(Activity activity, String registrationID) {
            this.activity = activity;
            this.registrationID = registrationID;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Update registration id to Server
            Log.d(TAG, "Update registration ID to server");
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Common.USERNAME, username);
            params.put(Common.PASSWORD, password);
            params.put(Common.ACTION, Common.ACTION_UPDATE_USER_INFO);
            params.put(Common.ATTRIBUTE, Common.REGISTRATION_ID);
            params.put(Common.VALUE, registrationID);

            String result = ServerUtilities.postToServer(Common.SERVER_LINK, params);
            Log.d(TAG, "Server sent: " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // Update registration id to client
            Log.d(TAG, "Save registration ID to preferences: " + Common.REGISTRATION_ID + ": " + registrationID);
            UserHandler.saveValueToPreferences(Common.REGISTRATION_ID, registrationID, getApplicationContext());

            // Start Home Activity
            Log.d(TAG, "Start sync data friends ");
            new DownloadUserData(activity).execute();
        }
    }

    /**
     * Download user data before go to Home Activity
     */
    private class DownloadUserData extends AsyncTask<Void, Void, Void> {
        private Activity activity;

        private DownloadUserData(Activity activity) {

            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(Common.USERNAME, username);
            hashMap.put(Common.PASSWORD, password);
            hashMap.put(Common.ACTION, Common.ACTION_GET_ALL_FRIENDS_INFO);
            String friends = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, hashMap);
            Log.d(TAG, "Friends data: " + friends);
            Log.d(TAG, "Is Updating friends");
            boolean result = new DataHelper(getContentResolver()).updateAllFriendsData(friends);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "Start Home Activity");
            Intent intent = new Intent(activity, HomeActivity.class);
            startActivity(intent);
            activity.finish();
        }
    }

    /* Download response from server and start Home Activity
     */
    private class DownloadUserInfo extends AsyncTask<Void, Void, String> {
        private Activity activity;
        private String username;
        private String password;

        private DownloadUserInfo(Activity activity, String username, String password) {
            this.activity = activity;
            this.username = username;
            this.password = password;
        }

        @Override

        protected String doInBackground(Void... strings) {
            HashMap<String, String> hashMap=new HashMap<String, String>();
            hashMap.put(Common.USERNAME, username);
            hashMap.put(Common.PASSWORD, password);
            hashMap.put(Common.ACTION, Common.ACTION_GET_INFO_BY_USERNAME);
            String result = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, hashMap);
            Log.d(TAG, "Successful get user Info: " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update to Preferences;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            Log.d(TAG, "Save user's information to preferences");
            UserHandler.savedUserInfoToPreferences(preferences, result);

            Log.d(TAG, "Test "+preferences.getString(Common.USER_ID, ""));
            // Start to registration ID to server
            new RegisterIDTask(activity).execute();

        }
    }

    // onClick function to Button Sign in
    public void onClickSignIn(View v) {
        username = edit_username.getText().toString().trim();
        password = edit_password.getText().toString().trim();

        if (username.length() == 0 | password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please complete your username and password !", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            String url = Common.SERVER_LINK + "?username=" +
                    username + "&password=" + password + "&action=login";
            new SignInTask(this, progressBar).execute(url);
        }
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }


}
