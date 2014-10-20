package com.dangchienhsgs.giffus;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;


public class NotifyActivity extends ListActivity {
    private String[] fromColumns={
            NotificationContract.Entry.MESSAGE
    };

    private int[] toFields={
            R.id.message_notification
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(
                NotificationContract.URI,
                null,
                null,
                null,
                FriendContract.Entry._ID
        );

        SimpleCursorAdapter cursorAdapter=new SimpleCursorAdapter(
                this,
                R.layout.row_notification_layout,
                cursor,
                fromColumns,
                toFields,
                0
        );

        setListAdapter(cursorAdapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
