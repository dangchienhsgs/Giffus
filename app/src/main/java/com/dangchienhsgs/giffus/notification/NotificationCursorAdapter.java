package com.dangchienhsgs.giffus.notification;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dangchienhsgs.giffus.HomeActivity;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.utils.Common;

/**
 * Created by dangchienbn on 26/11/2014.
 */
public class NotificationCursorAdapter extends SimpleCursorAdapter {
    private final String TAG = "Notification Cursor Adapter";
    private Context context;
    private Cursor cursor;

    public NotificationCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_notification_layout, parent, false);
        }

        cursor = getCursor();
        cursor.moveToPosition(position);

        TextView textTitle = (TextView) convertView.findViewById(R.id.text_notification_message);
        TextView textStatus = (TextView) convertView.findViewById(R.id.text_notification_time);

        textTitle.setText(cursor.getString(cursor.getColumnIndex(NotificationContract.Entry.MESSAGE)));
        textStatus.setText(
                cursor.getString(cursor.getColumnIndex(NotificationContract.Entry.DAY)) + "/"
                        + cursor.getString(cursor.getColumnIndex(NotificationContract.Entry.MONTH)) + "/"
                        + cursor.getString(cursor.getColumnIndex(NotificationContract.Entry.YEAR))

        );

        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        imageView.setImageResource(Common.HUMAN_ICON[cursor.getInt(cursor.getColumnIndex(NotificationContract.Entry.AVATAR_ID))]);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
            }
        });


        return convertView;
    }
}
