package com.dangchienhsgs.giffus;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.support.v4.widget.SimpleCursorAdapter;

import com.dangchienhsgs.giffus.adapter.FriendCursorAdapter;
import com.dangchienhsgs.giffus.provider.FriendContract;

public class FriendFragment extends ListFragment {

    Cursor cursor;
    private String TAG = "Friend Fragment";
    private FriendCursorAdapter mAdapter;
    private String[] FROM_COLUMNS = new String[]{
            FriendContract.Entry.FULL_NAME
    };
    private int[] TO_FIELDS = new int[]{
            R.id.friend_full_name
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(
                FriendContract.URI,
                null,
                FriendContract.Entry.RELATIONSHIP + "=" + FriendContract.ALREADY_FRIEND,
                null,
                null
        );

        Log.d(TAG, "Create Friend Cursor Adapter " + cursor.getCount());

        mAdapter = new FriendCursorAdapter(getActivity(),
                R.layout.row_friend_layout, // layout to for the list items
                cursor,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );

        setListAdapter(mAdapter);
        setEmptyText(getString(R.string.loading));
    }
}
