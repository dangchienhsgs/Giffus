package com.dangchienhsgs.giffus.friend;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dangchienhsgs.giffus.HomeActivity;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.ContentValuesBuilder;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SyncFriendsActivity extends ActionBarActivity {
    private String TAG = "Sync Friends";
    private ListView listView;
    private TextView textView;

    private List<String> listPhoneNumber;
    private List<Human> listHuman;

    private ProgressBar progressBar;
    private Button buttonSend;

    private Boolean[] listCheck;
    private HumanArrayListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_friends);

        listView = (ListView) findViewById(R.id.list_friend_to_add);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonSend = (Button) findViewById(R.id.button_send);
        textView = (TextView) findViewById(R.id.text_descriptions);


        // Set progressBar on and start sync
        // listView and Button come to be invisible
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        buttonSend.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        // Start to sync
        new FindFriendsTask().execute();
    }

    /**
     * Get the list of mobile phones in user's contacts
     * Use it to query in server to find friends
     *
     * @return
     */
    public List<String> getListMobilePhone() {
        List<String> list = new ArrayList<String>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (phoneNumber.contains("+84")) {
                    phoneNumber.replace("+84", "0");
                }
                list.add(phoneNumber);
            }
        }
        return list;
    }

    public List<Human> getHumanList(List<String> listNumber) {
        List<Human> list = new ArrayList<Human>();
        for (String number : listNumber) {
            String userInfo = ServerUtilities.getHumanInfoByAttribute(Common.MOBILE_PHONE, number);
            if (userInfo.trim().equals(ServerUtilities.NOT_FOUND)) {
                Log.d(TAG, number + "fail");
            } else {
                try {
                    Human human = new Human(userInfo);
                    list.add(human);
                } catch (JSONException e) {
                    Log.d(TAG, "Json string from server is error" + userInfo);
                }
            }
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, SearchFriendsActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sync_friends, menu);
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

    public void onClickSendFriendRequest(View view) {
        progressBar.setVisibility(View.VISIBLE);

        listView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        buttonSend.setVisibility(View.INVISIBLE);
        new SendRequestTask().execute();
    }

    private class FindFriendsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "is connecting to phoneBook to get list phone number");
            listPhoneNumber = getListMobilePhone();

            Log.d(TAG, "is connecting to server to get friends");
            listHuman = getHumanList(listPhoneNumber);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // set List View adapter
            Log.d(TAG, "is setting list view adapter");
            listCheck = new Boolean[listHuman.size()];
            Arrays.fill(listCheck, true);
            mAdapter = new HumanArrayListAdapter(
                    getApplicationContext(),
                    R.layout.row_friend_picker,
                    listHuman,
                    listCheck,
                    true
            );
            listView.setAdapter(mAdapter);

            // Set some components visible
            progressBar.setVisibility(View.INVISIBLE);
            buttonSend.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private class SendRequestTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < listCheck.length; i++) {
                if (listCheck[i]) {
                    Human human = listHuman.get(i);
                    Log.d(TAG, "Send friend request to " + human.getUsername());
                    ServerUtilities.sendRequestFriend(
                            PreferencesHandler.getValueFromPreferences(Common.USERNAME, getApplicationContext()),
                            PreferencesHandler.getValueFromPreferences(Common.PASSWORD, getApplicationContext()),
                            listHuman.get(i).getUsername()
                    );

                    // update to database
                    ContentValues contentValues = ContentValuesBuilder.friendBuilder(human);
                    contentValues.put(FriendContract.Entry.RELATIONSHIP, FriendContract.IS_REQUESTING);
                    ContentResolver contentResolver = getContentResolver();
                    contentResolver.insert(FriendContract.URI, contentValues);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
