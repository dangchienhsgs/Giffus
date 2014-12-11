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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerBuilder;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.picturepickers.PicturesPickerDialogs;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.EmailValidator;
import com.dangchienhsgs.giffus.utils.UrlBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity implements DatePickerDialogFragment.DatePickerDialogHandler,
        PicturesPickerDialogs.OnSelectedPicturesListener {
    private String TAG = "Register Acitivy";

    private Button registerButton;
    private EditText editFullName;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPhoneNumber;
    private EditText editBirthday;
    private ProgressBar progressBar;

    private ImageView avatar;

    private String fullname, username, email, phoneNumber, password, user_id, birthday;
    private int avatar_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();
    }

    public void initComponents() {
        registerButton = (Button) findViewById(R.id.button_register);
        editFullName = (EditText) findViewById(R.id.edit_fullname);
        editUsername = (EditText) findViewById(R.id.edit_username);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editBirthday = (EditText) findViewById(R.id.edit_birth);
        editBirthday.setText("19/11/1994");

        birthday = "19/11/1994";

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageResource(Common.HUMAN_ICON[0]);
        avatar_id = 0;

        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String birthday = editBirthday.getText().toString();
                String values[] = birthday.replace("/", " ").trim().split(" ");

                DatePickerBuilder builder = new DatePickerBuilder();
                builder.setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setFragmentManager(getSupportFragmentManager());
                builder.show();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PicturesPickerDialogs dialogs = new PicturesPickerDialogs();
                dialogs.setListener(RegisterActivity.this);
                dialogs.setListImages(Common.HUMAN_ICON);
                dialogs.show(getSupportFragmentManager(), "Picture Dialog");
            }
        });
    }

    public void onClick(View view) {

        // Check value
        fullname = editFullName.getText().toString().trim();
        username = editUsername.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        phoneNumber = editPhoneNumber.getText().toString().trim();
        password = editPassword.getText().toString().trim();
        birthday = editBirthday.getText().toString();

        boolean check = true;

        // Check empty
        if (fullname.isEmpty() | username.isEmpty() |
                email.isEmpty() | phoneNumber.isEmpty() | password.isEmpty() | birthday.isEmpty()) {
            check = false;
            Toast.makeText(getApplicationContext(), "Something is empty", Toast.LENGTH_SHORT).show();
        }

        // Check email
        if (!new EmailValidator().validate(email)) {
            check = false;
            Toast.makeText(getApplicationContext(), "Email fail", Toast.LENGTH_SHORT).show();
        }

        if (check) {
            // Generate user_id
            user_id = UUID.randomUUID().toString();

            Map<String, String> data = new HashMap<String, String>();
            data.put(Common.USERNAME, username);
            data.put(Common.PASSWORD, password);
            data.put(Common.MOBILE_PHONE, phoneNumber);
            data.put(Common.EMAIL, email);
            data.put(Common.FULL_NAME, fullname);
            data.put(Common.USER_ID, user_id);
            data.put(Common.ACTION, Common.ACTION_SIGN_UP);
            data.put(Common.AVATAR_ID, String.valueOf(avatar_id));
            data.put(Common.BIRTHDAY, birthday);

            //new RegisterBackgroundTask(data, progressBar, getApplicationContext()).execute();

            new CheckUsernameTask(data).execute();
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

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        birthday = dayOfMonth + "/" + monthOfYear + "/" + year;
        editBirthday.setText(birthday);
    }

    @Override
    public void onSelectedPictures(PicturesPickerDialogs dialogs, int position) {
        avatar_id = position;
        avatar.setImageResource(Common.HUMAN_ICON[avatar_id]);
    }

    class RegisterBackgroundTask extends AsyncTask<Void, Void, Void> {
        Map<String, String> data;
        ProgressBar progressBar;
        Context context;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        RegisterBackgroundTask(Map<String, String> data, ProgressBar progressBar, Context context) {
            this.data = data;
            this.progressBar = progressBar;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            new ServerUtilities().postToServer(Common.getServerLink(), data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Register Successful !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            // Finish this activity
            finish();
        }
    }


    private class CheckUsernameTask extends AsyncTask<Void, String, String> {
        private Map<String, String> data;
        private String username;
        private String email;

        private CheckUsernameTask(Map<String, String> data) {
            username=data.get(Common.USERNAME);
            email=data.get(Common.EMAIL);
            this.data=data;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> map=new HashMap<String, String>();
            map.put(Common.USERNAME, username);
            map.put(Common.EMAIL, email);
            map.put(Common.ACTION, Common.ACTION_CHECK_USERNAME_AND_EMAIL);


            String response=ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, map);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] temp=s.trim().split(" ");

            String usernameResult=temp[0].trim();
            String emailResult=temp[1].trim();

            if (usernameResult.equals("false") && emailResult.equals("false")){
                // continue
                new RegisterBackgroundTask(data, progressBar, RegisterActivity.this).execute();
            } else {

                registerButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                if (usernameResult.equals("true") && emailResult.equals("true")){
                    Toast.makeText(getApplicationContext(), "Both username and email has been existed", Toast.LENGTH_SHORT).show();
                } else {
                    if (usernameResult.equals("true")){
                        Toast.makeText(getApplicationContext(), "This username has been existed", Toast.LENGTH_SHORT).show();
                    }

                    if (emailResult.equals("true")){
                        Toast.makeText(getApplicationContext(), "This email has been existed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
}
