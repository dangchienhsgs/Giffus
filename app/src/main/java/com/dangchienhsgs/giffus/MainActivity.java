package com.dangchienhsgs.giffus;

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

import com.dangchienhsgs.giffus.provider.DataHelper;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.URLContentHandler;
import com.dangchienhsgs.giffus.utils.UrlBuilder;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private String TAG="Main Activity";

    private ProgressBar progressBar;
    private EditText edit_username;
    private EditText edit_password;
    private Button button_signIn;
    private Button button_register;

    private String username;
    private String password;


    private String jsonUser;
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

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = prefs.getString(Common.USERNAME, "");
        String password = prefs.getString(Common.PASSWORD, "");

        if (!username.isEmpty() && !password.isEmpty()){
            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
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
        private ProgressBar progressBar;
        private Context context;
        private String result_signIn;

        public SignInTask(Context context, ProgressBar progressBar) {

            this.context = context;
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
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString(Common.USERNAME, username);
                editor.putString(Common.PASSWORD, password);
                editor.commit();
                // Down Load user info
                new DownloadUserInfo(context, username, password).execute();

            } else {
                // Sign in not successful
                Log.d(TAG, "Sign in not successful");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "Invalid username or password, please try again",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class RegisterIDTask extends AsyncTask<Void, Void, String> {
        private GoogleCloudMessaging gcm;
        private Context context;
        private static final int BACKOFF_MILLI_SECONDS = 2000;
        private static final int MAX_ATTEMPTS = 5;

        private RegisterIDTask(Context context) {
            this.context = context;
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "is registering Google Cloud Messaging registration ID");
            long backoff = BACKOFF_MILLI_SECONDS;
            for (int i = 0; i < MAX_ATTEMPTS; i++) {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                try {
                    String regID = gcm.register(Common.getSenderId());
                    Log.d(TAG, "Registration IDegID is "+regID);
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
                new UpdateRegistrationId(context, regID).execute();
            }
        }
    }

    /**
     * Update registration_id to Server
     *
     */

    private class UpdateRegistrationId extends AsyncTask<Void, Void, String>{
        private String registrationID;
        private Context context;

        public UpdateRegistrationId(Context context, String registrationID) {
            this.context=context;
            this.registrationID=registrationID;
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
            Log.d(TAG, "Server sent: "+result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // Update registration id to client
            Log.d(TAG, "Save registration ID to preferences: "+Common.REGISTRATION_ID+": "+registrationID);
            SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString(Common.REGISTRATION_ID, registrationID);
            editor.commit();

            // Start Home Activity
            Log.d(TAG, "Start sync data friends ");
            new DownloadUserData(getApplicationContext()).execute();
        }
    }

    /**
     * Download user data before go to Home Activity
     */
    private class DownloadUserData extends AsyncTask<Void, Void, Void>{
        private Context context;

        private DownloadUserData(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, String> hashMap=new HashMap<String, String>();
            hashMap.put(Common.USERNAME, username);
            hashMap.put(Common.PASSWORD, password);
            hashMap.put(Common.ACTION, Common.ACTION_GET_ALL_FRIENDS_INFO);
            String friends=ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, hashMap);
            Log.d(TAG, "Friends data: "+friends);
            Log.d(TAG, "Is Updating friends");
            boolean result=new DataHelper(getContentResolver()).updateAllFriendsData(friends);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "Start Home Activity");
            Intent intent=new Intent(context, HomeActivity.class);
            startActivity(intent);
        }
    }

    /* Download response from server and start Home Activity
     */
    private class DownloadUserInfo extends AsyncTask<Void, Void, String> {
        private Context context;
        private String username;
        private String password;

        private DownloadUserInfo(Context context, String username, String password) {
            this.context = context;
            this.username=username;
            this.password=password;
        }

        @Override

        protected String doInBackground(Void... strings) {
            String url = new UrlBuilder(Common.SERVER_LINK).append(Common.ACTION, Common.ACTION_GET_INFO)
                    .append(Common.USERNAME, username).append(Common.PASSWORD, password).toString();
            String result=new URLContentHandler().getURLFirstLine(url);
            Log.d(TAG, "Successful get user Info: "+result);
            return result ;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update to Preferences;
            SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString(Common.JSON_USER_INFO, result);
            editor.commit();

            // Start to registration ID to server
            new RegisterIDTask(context).execute();

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
    }


}
