package com.dangchienhsgs.giffus.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.utils.Common;


public class HumanCursorListAdapter extends SimpleCursorAdapter {

    boolean[] listCheck;


    public HumanCursorListAdapter(Context context, int layout, Cursor c, boolean[] listCheck, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.listCheck = listCheck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_add_friends, parent, false);
        }

        // Get Cursor
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        // set avatar
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        avatar.setImageResource(Common.HUMAN_ICON[Integer.parseInt(
                cursor.getString(cursor.getColumnIndex(FriendContract.Entry.AVATAR_ID))
        )]);

        // Set Full name
        TextView fullname = (TextView) convertView.findViewById(R.id.text_full_name);
        fullname.setText(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.FULL_NAME)));

        // set Check Box listener
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setTag(position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listCheck[(Integer) compoundButton.getTag()] = b;
            }
        });

        return convertView;
    }
}
