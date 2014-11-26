package com.dangchienhsgs.giffus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.human.Human;
import com.dangchienhsgs.giffus.adapter.HumanArrayListAdapter;
import com.dangchienhsgs.giffus.client.PreferencesHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.ContentValuesBuilder;
import com.dangchienhsgs.giffus.utils.UserHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SearchByNameActivity extends ActionBarActivity {
    private String TAG = "Search By Name Activity";

    private EditText editName;
    private Button buttonSearch;
    private Button buttonContinue;
    private ListView listView;
    private ProgressBar progressBar;

    private List<Human> listHuman;
    private HumanArrayListAdapter mAdapter;
    private Boolean checkList[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);

        // Init components
        editName = (EditText) findViewById(R.id.edit_name);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonContinue = (Button) findViewById(R.id.button_continue);

        // Set visible
        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        buttonContinue.setVisibility(View.INVISIBLE);
    }

    public void onClickSearch(View view) {
        String name = editName.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please input a valid name !", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            new SearchFriendTask().execute(name);
        }
    }

    public void onClickAdd(View view) {
        new SendFriendInviteTask().execute();
    }

    public List<Human> analyzeResults(String result) {
        try {
            Log.d(TAG, "is analyzing the array result");
            Log.d(TAG, result);

            JSONArray jsonArray = new JSONArray(result);

            List<Human> humanList = new ArrayList<Human>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Human human = new Human(jsonArray.getJSONObject(i));
                Log.d(TAG, i + " " + human);
                humanList.add(human);
            }

            return humanList;

        } catch (JSONException e) {
            Log.d(TAG, "Server send JSON Array to be error");
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_by_name, menu);
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

    private class SendFriendInviteTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < listHuman.size(); i++) {
                if (checkList[i]) {
                    Human human = listHuman.get(i);
                    Log.d(TAG, "Send friend request to " + human.getUsername());
                    ServerUtilities.sendRequestFriend(
                            PreferencesHandler.getValueFromPreferences(Common.USERNAME, getApplicationContext()),
                            PreferencesHandler.getValueFromPreferences(Common.PASSWORD, getApplicationContext()),
                            listHuman.get(i).getUsername()
                    );

                    // Create content values
                    ContentValues contentValues = ContentValuesBuilder.friendBuilder(human);
                    contentValues.put(FriendContract.Entry.RELATIONSHIP, FriendContract.IS_REQUESTING);

                    // Update to database
                    ContentResolver contentResolver = getContentResolver();
                    contentResolver.insert(FriendContract.URI, contentValues);

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            List<Boolean> listCheck = new ArrayList<Boolean>();
            for (int i = 0; i < checkList.length; i++) {
                listCheck.add(checkList[i]);
            }


            for (int i = 0; i < listHuman.size(); i++) {
                if (listCheck.get(i)) {
                    listHuman.remove(i);
                    listCheck.remove(i);
                }
            }

            mAdapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Friend requests sent !", Toast.LENGTH_SHORT).show();
        }
    }

    private class SearchFriendTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... names) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(Common.ACTION, Common.ACTION_SEARCH_FRIEND_BY_NAME);
            hashMap.put(
                    Common.USERNAME,
                    PreferencesHandler.getValueFromPreferences(Common.USERNAME, getApplicationContext())
            );
            hashMap.put(Common.NAME_SEARCH, names[0]);
            String result = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, hashMap);
            return result;
        }

        @Override
        protected void onPostExecute(String arrayJSON) {
            progressBar.setVisibility(View.INVISIBLE);

            listHuman = analyzeResults(arrayJSON);
            if (listHuman == null) {
                Toast.makeText(getApplicationContext(), "Found no one !", Toast.LENGTH_SHORT).show();
            } else {

                // Remove friend we already friend

                for (int i = 0; i < listHuman.size(); i++) {
                    if (UserHandler.checkFriend(getApplicationContext(), listHuman.get(i).getUserID()) != FriendContract.NO_RELATION) {
                        listHuman.remove(i);
                    }
                }
                // Init some adapter and components
                checkList = new Boolean[listHuman.size()];

                mAdapter = new HumanArrayListAdapter(
                        getApplicationContext(),
                        R.layout.row_friend_picker,
                        listHuman,
                        checkList,
                        false
                );

                // Set Adapter
                listView.setAdapter(mAdapter);

                if (listHuman.size() == 0) {
                    Toast.makeText(
                            getApplicationContext(),
                            "No one who have not been your friend named like " + editName.getText(),
                            Toast.LENGTH_SHORT
                    ).show();
                }


                // Visible some Views
                progressBar.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                buttonContinue.setVisibility(View.VISIBLE);

            }
        }
    }
}
