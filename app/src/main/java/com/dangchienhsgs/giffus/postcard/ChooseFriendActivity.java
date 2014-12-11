package com.dangchienhsgs.giffus.postcard;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;


import com.dangchienhsgs.giffus.HomeActivity;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.friend.HumanCursorListAdapter;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.utils.UserHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.ContentValuesBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChooseFriendActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private String TAG = "Choose Friend Activity";

    private boolean[] listSent;

    private List<String> listId;

    private HumanCursorListAdapter mAdapter;
    private ListView listView;

    private Cursor cursor;

    private String[] FROM_COLUMNS = new String[]{
            FriendContract.Entry.FULL_NAME

    };
    private int[] TO_FIELDS = new int[]{
            R.id.text_descriptions
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);

        initComponents();
    }

    public void initComponents() {

        listView = (ListView) findViewById(R.id.list_friend_view);

        cursor = getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.RELATIONSHIP + "=" + FriendContract.ALREADY_FRIEND,
                null,
                null
        );

        listSent = new boolean[cursor.getCount()];
        Arrays.fill(listSent, false);

        mAdapter = new HumanCursorListAdapter(
                getApplicationContext(),
                R.layout.row_friend_picker,
                cursor,
                listSent,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );

        listView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_friend, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search_friend);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(), ChooseFriendActivity.class)));

        CheckBox checkBox = (CheckBox) menu.findItem(R.id.action_select_all).getActionView();


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Arrays.fill(listSent, b);

                for (int i = 0; i < listView.getCount(); i++) {
                    View row = listView.getChildAt(i);
                    CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
                    checkBox.setChecked(b);
                }
            }
        });

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        onQueryTextChange(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        listId = mAdapter.getListId();

        cursor = getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.FULL_NAME + " LIKE '%" + s + "%'",
                null,
                null
        );

        listSent = new boolean[cursor.getCount()];

        for (int i = 0; i < listSent.length; i++) {
            cursor.moveToPosition(i);
            String id = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID));
            if (listId.contains(id)) {
                listSent[i] = true;
            } else {
                listSent[i] = false;
            }
        }

        mAdapter.changeCursor(cursor);
        mAdapter.notifyDataSetChanged();

        Log.d(TAG, listId.toString());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_send:
                actionSend();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void actionSend() {
        Postcard postcard = packPostcard();

        if (postcard == null) {
            // return home
            Log.d(TAG, "Postcard is null - can be no anyone to be chosen");
        } else {

            List<ContentValues> listContentValues = ContentValuesBuilder.giftBuilder(postcard);

            for (int i = 0; i < listContentValues.size(); i++) {
                getContentResolver().insert(PostcardContract.URI, listContentValues.get(i));
            }

            // action send
            new SendGiftTask(postcard).execute();
        }
    }

    public Postcard packPostcard() {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < cursor.getCount(); i++) {
            if (listSent[i]) {
                String friendID = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID));
                list.add(friendID);
            }
        }

        if (list.size() == 0) {
            // error
            return null;
        } else {
            // attach receivers id
            String jsonPostcard = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
            Postcard postcard = new Gson().fromJson(jsonPostcard, Postcard.class);
            postcard.setReceiverID(list);
            postcard.setSenderID(UserHandler.getUserAttribute(this, Common.USER_ID));
            postcard.setSenderFullName(UserHandler.getUserAttribute(this, Common.FULL_NAME));

            // Set sentDateTime
            Calendar calendar = Calendar.getInstance();
            postcard.setSentYear(calendar.get(calendar.YEAR));
            postcard.setSentMonth(calendar.get(calendar.MONTH));
            postcard.setSentDay(calendar.get(calendar.DAY_OF_MONTH));

            postcard.setSentHour(calendar.get(calendar.HOUR_OF_DAY));
            postcard.setSentMinute(calendar.get(calendar.MINUTE));


            return postcard;
        }
    }

    private class SendGiftTask extends AsyncTask<Void, Void, Void> {
        private Postcard postcard;

        private SendGiftTask(Postcard postcard) {
            this.postcard = postcard;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ChooseFriendActivity.this.finish();

            Intent intent=new Intent(ChooseFriendActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String jsonPostcard = new Gson().toJson(postcard, Postcard.class);

            for (int i = 0; i < postcard.getReceiverID().size(); i++) {
                Log.d(TAG, jsonPostcard);

                Map<String, String> map = new HashMap<String, String>();
                map.put(PostcardContract.Entry.SENDER_ID, postcard.getSenderID());
                map.put(PostcardContract.Entry.RECEIVER_ID, postcard.getReceiverID().get(i));
                map.put(PostcardContract.Entry.JSON_POSTCARD, jsonPostcard);
                map.put(Common.ACTION, Common.ACTION_SEND_POSTCARD);


                String response = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, map);

                Log.d(TAG, response);
            }
            return null;
        }
    }
}
