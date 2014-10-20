package com.dangchienhsgs.giffus.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.account.UserHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;

import org.w3c.dom.Text;

import java.util.HashMap;


public class FriendRequestCursorAdapter extends SimpleCursorAdapter {
    private ListView listView;
    private String TAG="Simple Cursor Adapter";
    public FriendRequestCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ListView listView) {
        super(context, layout, c, from, to, flags);
        this.listView=listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null){
            view=newView(mContext, mCursor, parent);
        } else {
            view=convertView;
        }

        TextView textView=(TextView) view.findViewById(R.id.request_human_full_name);
        Cursor cursor=getCursor();
        cursor.moveToPosition(position);
        textView.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.FULL_NAME)));

        Button button=(Button) view.findViewById(R.id.button_accept_friend);
        button.setTag(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the cursor
                Cursor cursor = getCursor();

                // Move cursor to the position
                // get the Index
                Integer position=(Integer) view.getTag();
                cursor.moveToPosition(position);
                Log.d(TAG, "Vi tri "+position);

                // get userID of friend;
                String friendID = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID));
                String friendName=cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USERNAME));

                // get Content values
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

                // change relationship
                contentValues.put(FriendContract.Entry.RELATIONSHIP, FriendContract.ALREADY_FRIEND);
                contentValues.remove(FriendContract.Entry._ID);
                Log.d(TAG, contentValues.toString());


                // Update friend
                ContentResolver contentResolver = mContext.getContentResolver();
                contentResolver.update(
                        FriendContract.URI,
                        contentValues,
                        FriendContract.Entry.USER_ID+"="+friendID,
                        null
                );

                // Remove notification
                contentResolver.delete(
                        NotificationContract.URI,
                        NotificationContract.Entry.FRIEND_ID+"="+friendID,
                        null);
                // Get new Cursor
                Cursor newCursor=contentResolver.query(
                        FriendContract.URI,
                        null,
                        FriendContract.Entry.RELATIONSHIP+"="+FriendContract.WAIT_ACCEPTING,
                        null,
                        FriendContract.Entry._ID
                );

                // Remove the row
                listView.removeViewInLayout(view);

                // Update listView
                FriendRequestCursorAdapter.this.swapCursor(newCursor);
                FriendRequestCursorAdapter.this.notifyDataSetChanged();

                // Get user information
                String username= UserHandler.getValueFromPreferences(Common.USERNAME, mContext);
                String password= UserHandler.getValueFromPreferences(Common.PASSWORD, mContext);

                // Send accept to server
                Log.d(TAG, "Send response to server");
                new FriendResponseSender(username, password, friendName).execute();

            }
        });
        return view;
    }

    private class FriendResponseSender extends AsyncTask<Void, Void, String>{
        private String acceptUsername;
        private String acceptPassword;
        private String requireUsername;

        FriendResponseSender(String acceptUsername, String acceptPassword, String requireUsername) {
            this.acceptUsername = acceptUsername;
            this.acceptPassword = acceptPassword;
            this.requireUsername = requireUsername;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> hashMap=new HashMap<String, String>();
            hashMap.put(Common.ACTION, Common.ACTION_ACCEPT_FRIEND_REQUEST);
            hashMap.put(Common.ACCEPT_USER, acceptUsername);
            hashMap.put(Common.ACCEPT_PASSWORD, acceptPassword);
            hashMap.put(Common.REQUIRE_USER, requireUsername);

            String result=ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, hashMap);
            Log.d(TAG, result);
            return result;
        }
    }
}
