package com.dangchienhsgs.giffus.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.UserHandler;
import com.dangchienhsgs.giffus.postcard.Postcard;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

/**
 * Created by dangchienbn on 25/11/2014.
 */
public class PostcardArrayAdapter extends SimpleCursorAdapter {
    private static final String TAG = "Postcard Array Adapter";
    private Context context;

    public PostcardArrayAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_postcard_layout, parent, false);
        }

        TextView textDescription = (TextView) convertView.findViewById(R.id.text_descriptions);
        TextView textStatus = (TextView) convertView.findViewById(R.id.text_status);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.avatar);

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        String senderID = cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.SENDER_ID));
        String receiverID = cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.RECEIVER_ID));
        String jsonPostcard = cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.JSON_POSTCARD));
        Postcard postcard = new Gson().fromJson(jsonPostcard, Postcard.class);

        String description;
        if (senderID == UserHandler.getUserAttribute(context, Common.USER_ID)) {
            // Background different
            String receiverFullName = UserHandler.getFriendInfo(context, Common.USER_ID, receiverID, Common.FULL_NAME);
            description = "Gift send to " + receiverFullName;
        } else {
            if (postcard.isSecretSender()) {
                description = "Gift sent by Anonymous";
            } else {
                description = "Gift sent by " + postcard.getSenderFullName();
            }
        }
        String status = "Time: " + postcard.getSentDay() + "/" + postcard.getSentMonth() + "/" + postcard.getSentYear()
                + ", Location: ";
        if (postcard.getLocation() == null) {
            status = status + " None";
        } else {
            status = status + postcard.getLocation().getMapTitle();
        }

        textDescription.setText(description);
        textStatus.setText(status);

        if (postcard.getSenderID() == UserHandler.getUserAttribute(context, Common.USER_ID)) {
            Cursor tempCursor = context.getContentResolver().query(
                    FriendContract.URI,
                    null,
                    Common.USER_ID + "=" + postcard.getReceiverID(),
                    null,
                    null
            );

            if (tempCursor.getCount() > 0) tempCursor.moveToPosition(0);
            imageView.setImageResource(Common.HUMAN_ICON[tempCursor.getInt(tempCursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))]);
        } else {
            if (postcard.isSecretSender()) {
                imageView.setImageResource(R.drawable.icon_anonymous);
            } else {
                Cursor tempCursor = context.getContentResolver().query(
                        FriendContract.URI,
                        null,
                        Common.USER_ID + "=" + postcard.getSenderID(),
                        null,
                        null
                );

                if (tempCursor.getCount() > 0) tempCursor.moveToPosition(0);
                imageView.setImageResource(Common.HUMAN_ICON[tempCursor.getInt(tempCursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))]);
            }
        }
        return convertView;
    }
}
