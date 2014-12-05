package com.dangchienhsgs.giffus.friend;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dangchienhsgs.giffus.FriendInfoActivity;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.server.AcceptFriendTask;
import com.dangchienhsgs.giffus.utils.Common;

public class FriendCursorAdapter extends SimpleCursorAdapter {

    private String TAG = "Friend Cursor Adapter";
    private ListView listView;

    public FriendCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ListView listView) {
        super(context, layout, c, from, to, flags);
        this.listView = listView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);


        int relationship = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.RELATIONSHIP)));
        switch (relationship) {
            case FriendContract.IS_REQUESTING:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_friend_layout, parent, false);

                break;
            case FriendContract.ALREADY_FRIEND:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_friend_layout, parent, false);
                break;
            case FriendContract.WAIT_ACCEPTING:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_friend_layout_in_accept, parent, false);
        }

        TextView textFullName = (TextView) convertView.findViewById(R.id.text_descriptions);
        TextView textStatus = (TextView) convertView.findViewById(R.id.text_status);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);


        textFullName.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.FULL_NAME)));
        avatar.setImageResource(Common.HUMAN_ICON[cursor.getInt(cursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))]);

        switch (relationship) {
            case FriendContract.ALREADY_FRIEND:
                textStatus.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.EMAIL)));
                break;
            case FriendContract.IS_REQUESTING:
                textStatus.setText("in requesting");
                break;
            case FriendContract.WAIT_ACCEPTING:
                textStatus.setText("wait to be accepted");
                avatar.setTag(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID)));
                avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String friendID = (String) view.getTag();
                        Intent intent = new Intent(mContext, FriendInfoActivity.class);
                        intent.putExtra(Common.FRIEND_ID, friendID);
                        mContext.startActivity(intent);

                    }
                });

                Button button = (Button) convertView.findViewById(R.id.button_accept_friend);
                button.setTag(position);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the cursor
                        Cursor cursor = getCursor();

                        // Move cursor to the position
                        // get the Index
                        Integer position = (Integer) view.getTag();
                        cursor.moveToPosition(position);
                        Log.d(TAG, "Vi tri " + position);

                        // get userID of friend;
                        String friendID = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID));
                        String friendName = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USERNAME));

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
                                FriendContract.Entry.USER_ID + "='" + friendID + "'",
                                null
                        );

                        // Remove notification
                        contentResolver.delete(
                                NotificationContract.URI,
                                NotificationContract.Entry.FRIEND_ID + "=" + "'" + friendID + "'",
                                null);

                        // Get new Cursor
                        Cursor newCursor = contentResolver.query(
                                FriendContract.URI,
                                null,
                                null,
                                null,
                                FriendContract.Entry._ID
                        );

                        // Remove the row
                        //listView.removeViewInLayout(view);

                        // Update listView
                        FriendCursorAdapter.this.swapCursor(newCursor);
                        FriendCursorAdapter.this.notifyDataSetChanged();

                        // Get user information
                        String username = PreferencesHandler.getValueFromPreferences(Common.USERNAME, mContext);
                        String password = PreferencesHandler.getValueFromPreferences(Common.PASSWORD, mContext);

                        // Send accept to server
                        Log.d(TAG, "Send response to server");
                        new AcceptFriendTask(username, password, friendName).execute();

                    }
                });
                break;

        }


        return convertView;
    }
}
