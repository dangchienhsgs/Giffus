package com.dangchienhsgs.giffus.friend;

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

import java.util.ArrayList;
import java.util.List;


public class HumanCursorListAdapter extends SimpleCursorAdapter {

    boolean[] listCheck;

    List<String> listId;

    public HumanCursorListAdapter(Context context, int layout, Cursor c, boolean[] listCheck, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.listCheck = listCheck;
        this.listId = new ArrayList<String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_friend_picker, parent, false);
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

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setTag(Integer.valueOf(position));
        checkBox.setChecked(listCheck[position]);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (Integer) compoundButton.getTag();
                listCheck[position] = b;

                Cursor cursor = getCursor();
                String id = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID));
                if (b) {
                    listId.add(id);
                } else {
                    listId.remove(id);
                }
            }
        });

        return convertView;
    }

    public void setChosen(int position, boolean value) {
        listCheck[position] = value;
    }

    public boolean getChosen(int position) {
        return listCheck[position];
    }

    public List<String> getListId() {
        return listId;
    }

    public void setListId(List<String> listId) {
        this.listId = listId;
    }
}

