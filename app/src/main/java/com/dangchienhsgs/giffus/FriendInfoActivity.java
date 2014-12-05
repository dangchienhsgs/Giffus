package com.dangchienhsgs.giffus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.server.AcceptFriendTask;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;

import java.util.HashMap;
import java.util.Map;


public class FriendInfoActivity extends ActionBarActivity {
    int relationShip;
    String friendID;
    String username;

    Menu menu;
    MenuItem removeItem, declineItem, acceptItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        initComponents();
    }

    public void initComponents() {
        Intent intent = getIntent();
        friendID = intent.getStringExtra(Common.FRIEND_ID);

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        TextView textUsername = (TextView) findViewById(R.id.text_username);
        TextView textEmail = (TextView) findViewById(R.id.text_email);
        TextView textBirthday = (TextView) findViewById(R.id.text_birth);
        TextView textFullName = (TextView) findViewById(R.id.text_fullname);
        TextView textPhoneNumber = (TextView) findViewById(R.id.text_mobile_phone);


        Cursor cursor = getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.USER_ID + "=" + "'" + friendID + "'",
                null,
                null
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No friend have id=" + friendID, Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToPosition(0);
            avatar.setImageResource(Common.HUMAN_ICON[cursor.getInt(cursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))]);

            username = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USERNAME));
            textUsername.setText(username);

            textFullName.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.FULL_NAME)));
            textEmail.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.EMAIL)));
            textPhoneNumber.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.MOBILE_PHONE)));
            textBirthday.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.BIRTHDAY)));

            relationShip = cursor.getInt(cursor.getColumnIndex(FriendContract.Entry.RELATIONSHIP));

        }
    }

    public void initMenu() {
        removeItem = menu.findItem(R.id.action_remove);
        declineItem = menu.findItem(R.id.action_decline_friend);
        acceptItem = menu.findItem(R.id.action_accept_friend);

        switch (relationShip) {
            case FriendContract.ALREADY_FRIEND:
                declineItem.setVisible(false);
                acceptItem.setVisible(false);
                break;

            case FriendContract.IS_REQUESTING:
                declineItem.setVisible(false);
                removeItem.setVisible(false);
                acceptItem.setVisible(false);
                break;
            case FriendContract.WAIT_ACCEPTING:
                declineItem.setVisible(true);
                removeItem.setVisible(false);
                acceptItem.setVisible(true);
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_info, menu);
        this.menu = menu;

        initMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_accept_friend:
                actionAcceptRequestFriend();
                break;
            case R.id.action_decline_friend:
                actionDeclineRequestFriend();
                break;
            case R.id.action_remove:
                actionRemoveFriend();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void actionAcceptRequestFriend() {
        // get userID of friend;
        Cursor cursor = getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.USER_ID + "=" + "'" + friendID + "'",
                null,
                null
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Something was error ! This human not in your friend list", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToPosition(0);

            // get Content values
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

            // change relationship
            contentValues.put(FriendContract.Entry.RELATIONSHIP, FriendContract.ALREADY_FRIEND);
            contentValues.remove(FriendContract.Entry._ID);

            Log.d("New friend", contentValues.toString());

            // Update friend
            ContentResolver contentResolver = getContentResolver();
            contentResolver.update(
                    FriendContract.URI,
                    contentValues,
                    FriendContract.Entry.USER_ID + "='" + friendID + "'",
                    null
            );

            // Remove notification
            contentResolver.delete(
                    NotificationContract.URI,
                    NotificationContract.Entry.FRIEND_ID + "=" + "'" + friendID + "'",
                    null);

            // Get user information
            String acceptUser = PreferencesHandler.getValueFromPreferences(Common.USERNAME, this);
            String acceptPassword = PreferencesHandler.getValueFromPreferences(Common.PASSWORD, this);
            String requireUser = username;

            // Send accept to server
            new AcceptFriendTask(acceptUser, acceptPassword, requireUser).execute();

        }

    }


    public void actionDeclineRequestFriend() {
        new DeclineFriendTask(
                PreferencesHandler.getValueFromPreferences(Common.USERNAME, this),
                PreferencesHandler.getValueFromPreferences(Common.PASSWORD, this),
                username,
                this
        ).execute();
    }


    public void actionRemoveFriend() {
        new RemoveFriendTask(
                PreferencesHandler.getValueFromPreferences(Common.USERNAME, this),
                PreferencesHandler.getValueFromPreferences(Common.PASSWORD, this),
                username,
                this
        ).execute();
    }


    private class DeclineFriendTask extends AsyncTask<Void, Void, Void> {
        private String declineUser;
        private String requireUser;
        private String declinePassword;

        private Context context;

        private DeclineFriendTask(String declineUser, String declinePassword, String requireUser, Context context) {
            this.declineUser = declineUser;
            this.requireUser = requireUser;
            this.declinePassword = declinePassword;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, String> map = new HashMap<String, String>();

            map.put(Common.DECLINE_USER, PreferencesHandler.getValueFromPreferences(Common.USERNAME, context));
            map.put(Common.DECLINE_PASSWORD, PreferencesHandler.getValueFromPreferences(Common.PASSWORD, context));
            map.put(Common.REQUIRE_USER, username);
            map.put(Common.ACTION, Common.ACTION_DECLINE_FRIENDSHIP);

            String response = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, map);

            Log.d("Decline Friend Task", response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FriendInfoActivity.this.finish();
        }
    }

    private class RemoveFriendTask extends AsyncTask<Void, Void, Void> {
        private String requestUser;
        private String requestPassword;
        private String removedUser;

        private Context context;

        private RemoveFriendTask(String requestUser, String requestPassword, String removedUser, Context context) {
            this.requestUser = requestUser;
            this.requestPassword = requestPassword;
            this.removedUser = removedUser;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Delete friendship on client
            ContentResolver contentResolver = context.getContentResolver();

            contentResolver.delete(
                    FriendContract.URI,
                    FriendContract.Entry.USERNAME + "='" + removedUser + "'",
                    null
            );

            // Delete on the server
            Map<String, String> map = new HashMap<String, String>();
            map.put(Common.REQUEST_USER, requestUser);
            map.put(Common.REQUEST_PASSWORD, requestPassword);
            map.put(Common.REMOVED_USER, removedUser);
            map.put(Common.ACTION, Common.ACTION_REMOVE_FRIENDSHIP);

            String response = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, map);

            Log.d("Remove Friend Task", response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FriendInfoActivity.this.finish();
            Intent intent = new Intent(FriendInfoActivity.this, HomeActivity.class);
            startActivity(intent);
        }


    }

}
