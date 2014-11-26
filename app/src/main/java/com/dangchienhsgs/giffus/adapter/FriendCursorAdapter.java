package com.dangchienhsgs.giffus.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.utils.Common;

import java.util.zip.Inflater;

/**
 * Created by dangchienbn on 22/10/2014.
 */
public class FriendCursorAdapter extends SimpleCursorAdapter {

    private String TAG = "Friend Cursor Adapter";

    public FriendCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.row_friend_layout, parent, false);
        } else {
            view = convertView;
        }

        // Get avatar id
        Cursor cursor = getCursor();
        Log.d(TAG, "So luong: " + cursor.getCount());
        //Get full name and avatar id
        String avatarID = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.AVATAR_ID));
        String fullName = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.FULL_NAME));

        Log.d(TAG, avatarID + " " + fullName);

        // Set avatar
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        avatar.setImageResource(Common.HUMAN_ICON[Integer.parseInt(avatarID)]);

        // Set full name
        TextView textView = (TextView) view.findViewById(R.id.friend_full_name);
        textView.setText(fullName);

        return view;
    }
}
