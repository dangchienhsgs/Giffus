package com.dangchienhsgs.giffus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerBuilder;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.picturepickers.PicturesPickerDialogs;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.EmailValidator;
import com.dangchienhsgs.giffus.utils.UserHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AccountActivity extends ActionBarActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        PicturesPickerDialogs.OnSelectedPicturesListener,
        Button.OnClickListener {

    private EditText editUsername;
    private EditText editFullName;
    private EditText editPassword;
    private EditText editBirthday;
    private EditText editPhoneNumber;
    private EditText editEmail;
    private ImageView avatar;
    private Button updateButton;

    private ProgressBar progressBar;
    private int avatarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initComponents();
    }

    /**
     * Initiation all child view of the parent activity
     */
    public void initComponents() {
        avatar = (ImageView) findViewById(R.id.avatar);
        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editBirthday = (EditText) findViewById(R.id.edit_birth);
        editFullName = (EditText) findViewById(R.id.edit_fullname);
        editPhoneNumber = (EditText) findViewById(R.id.edit_mobile_phone);

        updateButton = (Button) findViewById(R.id.button_update);
        avatarID = Integer.parseInt(UserHandler.getUserAttribute(getApplicationContext(), Common.AVATAR_ID));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        avatar.setImageResource(Common.HUMAN_ICON[avatarID]);
        editUsername.setText(UserHandler.getUserAttribute(this, Common.USERNAME));
        editFullName.setText(UserHandler.getUserAttribute(this, Common.FULL_NAME));
        editPassword.setText(UserHandler.getUserAttribute(this, Common.PASSWORD));
        editEmail.setText(UserHandler.getUserAttribute(this, Common.EMAIL));
        editPhoneNumber.setText(UserHandler.getUserAttribute(this, Common.MOBILE_PHONE));
        editBirthday.setText(UserHandler.getUserAttribute(this, Common.BIRTHDAY));

        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                dialogs.setListImages(Common.HUMAN_ICON);
                dialogs.setListener(AccountActivity.this);
                dialogs.show(getSupportFragmentManager(), "Picture Dialog");
            }
        });

        updateButton.setOnClickListener(this);
    }

    @Override
    public void onSelectedPictures(PicturesPickerDialogs dialogs, int position) {
        avatar.setImageResource(Common.HUMAN_ICON[position]);
        avatarID = position;
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        editBirthday.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        // Check value
        String fullName = editFullName.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String birthday = editBirthday.getText().toString();

        boolean check = true;

        // Check empty
        if (fullName.isEmpty() | username.isEmpty() |
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

            Map<String, String> data = new HashMap<String, String>();
            data.put(Common.USERNAME, username);
            data.put(Common.PASSWORD, password);
            data.put(Common.MOBILE_PHONE, phoneNumber);
            data.put(Common.EMAIL, email);
            data.put(Common.FULL_NAME, fullName);
            data.put(Common.AVATAR_ID, String.valueOf(avatarID));
            data.put(Common.BIRTHDAY, birthday);

            // Save to preferences
            PreferencesHandler.saveValueToPreferences(Common.USERNAME, username, this);
            PreferencesHandler.saveValueToPreferences(Common.PASSWORD, password, this);
            PreferencesHandler.saveValueToPreferences(Common.MOBILE_PHONE, phoneNumber, this);
            PreferencesHandler.saveValueToPreferences(Common.FULL_NAME, fullName, this);
            PreferencesHandler.saveValueToPreferences(Common.EMAIL, email, this);
            PreferencesHandler.saveValueToPreferences(Common.AVATAR_ID, String.valueOf(avatarID), this);
            PreferencesHandler.saveValueToPreferences(Common.BIRTHDAY, birthday, this);


            progressBar.setVisibility(View.VISIBLE);
            new UpdateBackgroundTask(this, data, progressBar).execute();
        }
    }

    private class UpdateBackgroundTask extends AsyncTask<Void, Void, Void> {
        private static final String GAP = ("(gap)");
        private Context context;
        private Map<String, String> data;
        private ProgressBar progressBar;

        private UpdateBackgroundTask(Context context, Map<String, String> data, ProgressBar progressBar) {
            this.context = context;
            this.data = data;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);

            finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String attributes = "";
            String values = "";

            for (int i = 0; i < data.keySet().size(); i++) {
                String key = new ArrayList<String>(data.keySet()).get(i);
                attributes = attributes + key;
                values = values + data.get(key);

                if (i < data.keySet().size() - 1) {
                    attributes = attributes + GAP;
                    values = values + GAP;
                }
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Common.ACTION, Common.ACTION_UPDATE_USER_INFO);
            map.put(Common.USERNAME, UserHandler.getUserAttribute(context, Common.USERNAME));
            map.put(Common.PASSWORD, UserHandler.getUserAttribute(context, Common.PASSWORD));
            map.put(Common.ATTRIBUTE, attributes);
            map.put(Common.VALUE, values);

            String response = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, map);
            Log.d("Update User Info", response);

            return null;
        }


    }
}
