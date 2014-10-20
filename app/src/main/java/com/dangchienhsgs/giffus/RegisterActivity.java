package com.dangchienhsgs.giffus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.server.ServerUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity {
    private Button registerButton;
    private EditText editFullName;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPhoneNumber;
    private ProgressBar progressBar;

    private String fullname, username, email, phoneNumber, password, user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.button_register);
        editFullName = (EditText) findViewById(R.id.edit_fullname);
        editUsername = (EditText) findViewById(R.id.edit_username);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone);
        editPassword = (EditText) findViewById(R.id.edit_password);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View view) {

        // Check value
        fullname = editFullName.getText().toString().trim();
        username = editUsername.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        phoneNumber = editPhoneNumber.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        boolean check=true;

        // Check empty
        if (fullname.isEmpty() | username.isEmpty() |
                email.isEmpty() | phoneNumber.isEmpty() | password.isEmpty()) {
            check=false;
            Toast.makeText(getApplicationContext(), "Something is empty", Toast.LENGTH_SHORT).show();
        }

        // Check email
        if (!new EmailValidator().validate(email)){
            check=false;
            Toast.makeText(getApplicationContext(), "Email fail", Toast.LENGTH_SHORT).show();
        }

        if (check){
            // Generate user_id
            user_id=UUID.randomUUID().toString();

            Map<String, String> data=new HashMap<String, String>();
            data.put(Common.USERNAME, username);
            data.put(Common.PASSWORD, password);
            data.put(Common.MOBILE_PHONE, phoneNumber);
            data.put(Common.EMAIL, email);
            data.put(Common.FULL_NAME, fullname);
            data.put(Common.USER_ID, user_id);
            data.put(Common.ACTION, Common.ACTION_SIGN_UP);

            progressBar.setVisibility(View.VISIBLE);
            new RegisterBackgroundTask(data, progressBar, getApplicationContext()).execute();
        }

    }


    class RegisterBackgroundTask extends AsyncTask<Void, Void, Void>{
        Map<String, String> data;
        ProgressBar progressBar;
        Context context;

        RegisterBackgroundTask(Map<String, String> data, ProgressBar progressBar, Context context) {
            this.data = data;
            this.progressBar=progressBar;
            this.context=context;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            new ServerUtilities().postToServer(Common.getServerLink(), data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);

            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor=prefs.edit();

            editor.putString(Common.EMAIL, email);
            editor.putString(Common.USERNAME, username);
            editor.putString(Common.PASSWORD, password);
            editor.putString(Common.FULL_NAME, fullname);
            editor.putString(Common.MOBILE_PHONE, phoneNumber);
            editor.commit();

            startActivity(intent);
        }
    }

    /**
     * Check email validation by Regular Expression
     */
    public class EmailValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        /**
         * Validate hex with regular expression
         *
         * @param hex
         *            hex for validation
         * @return true valid hex, false invalid hex
         */
        public boolean validate(final String hex) {

            matcher = pattern.matcher(hex);
            return matcher.matches();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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
}
