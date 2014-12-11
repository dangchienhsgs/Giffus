package com.dangchienhsgs.giffus;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.EmailValidator;
import com.dangchienhsgs.giffus.utils.UrlBuilder;

import java.util.HashMap;


public class RestorePasswordActivity extends ActionBarActivity implements Button.OnClickListener{
    private EditText editEmail;
    private Button buttonSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        initComponents();
    }

    public void initComponents(){
        editEmail=(EditText) findViewById(R.id.edit_email);
        buttonSend=(Button) findViewById(R.id.button_send);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);
        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String email=editEmail.getText().toString();

        if (email==null){

            // If not input
            Toast.makeText(getApplicationContext(), "Your email address is empty, please input your email", Toast.LENGTH_SHORT).show();
        } else {
            email=email.trim();
            if (!new EmailValidator().validate(email)){

                // If email invalid
                Toast.makeText(getApplicationContext(), "Your email address is wrong, please re-input your email", Toast.LENGTH_SHORT).show();

            } else {

                // If email valid
                new RestoreEmailTask(email).execute();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restore_password, menu);
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

    private class RestoreEmailTask extends AsyncTask<Void, String,String>{
        private String email;

        private RestoreEmailTask(String email) {
            this.email = email;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap map=new HashMap();
            map.put(Common.ACTION, Common.ACTION_RESTORE_PASSWORD);
            map.put(Common.EMAIL, email);

            String response=ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, map);
            return response;
        }

        @Override
        protected void onPreExecute() {
            buttonSend.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            buttonSend.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            if (s.trim().contains("email not found")){
                Toast.makeText(getApplicationContext(), "This email has not been existed, please check again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Email was sent ! It will deliver to your mail after a few hours !", Toast.LENGTH_SHORT).show();
                RestorePasswordActivity.this.finish();
            }
        }
    }
}
