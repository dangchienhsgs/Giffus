package com.dangchienhsgs.giffus.postcard;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.utils.PostcardUtils;
import com.dangchienhsgs.giffus.utils.UserHandler;
import com.dangchienhsgs.giffus.postcard.Postcard;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

/**
 * Created by dangchienbn on 25/11/2014.
 */
public class PostcardCursorAdapter extends SimpleCursorAdapter {
    private static final String TAG = "Postcard Array Adapter";
    private Context context;

    public PostcardCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_postcard_layout, parent, false);
        }

        /* Init components of convert view */
        TextView textDescription = (TextView) convertView.findViewById(R.id.text_descriptions);
        TextView textStatus = (TextView) convertView.findViewById(R.id.text_status);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.avatar);

        /* Get data from Data Provider */
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        String userID = PreferencesHandler.getValueFromPreferences(Common.USER_ID, context);
        String senderID = cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.SENDER_ID));
        String receiverID = cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.RECEIVER_ID));
        String jsonPostcard = cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.JSON_POSTCARD));
        Postcard postcard = new Gson().fromJson(jsonPostcard, Postcard.class);

        /* Set TextView */
        String description;
        if (senderID.equals(UserHandler.getUserAttribute(context, Common.USER_ID))) {

            // We sent this present
            String receiverFullName = UserHandler.getFriendInfo(context, Common.USER_ID, receiverID, Common.FULL_NAME);
            if (receiverFullName == null) receiverFullName = "unknown person";
            description = "Sent to " + receiverFullName;

        } else {

            // Your friend sent this present
            if (postcard.isSecretSender()) {
                imageView.setImageResource(R.drawable.anonymous);
                description = "Sent by Anonymous";
            } else {
                description = "Sent by " + postcard.getSenderFullName();
            }
        }

        String status = "";
        if (PostcardUtils.checkPostcardTime(postcard)) {
            status = "Open it now !";
        } else {
            status = "Open it in " + postcard.getMinute() + ":" + postcard.getHour() + ", " + postcard.getDay() + "/" + postcard.getMonth() + "/" + postcard.getYear();
        }

        textDescription.setText(description);
        textStatus.setText(status);

        if (postcard.getSenderID().equals(UserHandler.getUserAttribute(context, Common.USER_ID))) {

            // If sender is you => get receiver avatar
            Cursor tempCursor = context.getContentResolver().query(
                    FriendContract.URI,
                    null,
                    Common.USER_ID + "=" + "'" + postcard.getReceiverID() + "'",
                    null,
                    null
            );

            if (tempCursor.getCount() > 0) {
                tempCursor.moveToPosition(0);
                imageView.setImageResource(Common.HUMAN_ICON[tempCursor.getInt(tempCursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))]);
            } else {
                imageView.setImageResource(Common.HUMAN_ICON[postcard.getInner().getAvatarID()]);
            }
        } else {

            // if sender is not you
            if (postcard.isSecretSender()) {

                // if they keep their secret name
                imageView.setImageResource(R.drawable.icon_anonymous);
            } else {

                // if they don't keep their secret
                Cursor tempCursor = context.getContentResolver().query(
                        FriendContract.URI,
                        null,
                        Common.USER_ID + "=" + "'" + postcard.getSenderID() + "'",
                        null,
                        null
                );

                if (tempCursor.getCount() > 0) {
                    tempCursor.moveToPosition(0);
                    imageView.setImageResource(Common.HUMAN_ICON[tempCursor.getInt(tempCursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))]);
                } else {
                    imageView.setImageResource(Common.HUMAN_ICON[postcard.getInner().getAvatarID()]);
                }
            }
        }
        return convertView;
    }


}
