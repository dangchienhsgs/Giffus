package com.dangchienhsgs.giffus;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dangchienhsgs.giffus.adapter.FriendRequestCursorAdapter;
import com.dangchienhsgs.giffus.provider.FriendContract;


public class FriendRequestsActivity extends ListActivity {
    private String[] fromColumns={
            FriendContract.Entry.FULL_NAME
    };

    private int[] toFields={
            R.id.request_human_full_name
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(
                FriendContract.URI,
                null,
                FriendContract.Entry.RELATIONSHIP+"="+FriendContract.WAIT_ACCEPTING,
                null,
                FriendContract.Entry._ID
        );

        FriendRequestCursorAdapter cursorAdapter=new FriendRequestCursorAdapter(
                this,
                R.layout.row_suggest_friend_layout,
                cursor,
                fromColumns,
                toFields,
                0,
                getListView()
        );

        setListAdapter(cursorAdapter);
    }

}
