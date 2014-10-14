package com.dangchienhsgs.giffus;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SimpleCursorAdapter;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.provider.FriendContract;

public class HomeFragment extends ListFragment {

    private SimpleCursorAdapter mAdapter;

    private String[] FROM_COLUMNS = new String[]{
            FriendContract.Entry.FULL_NAME
    };

    private int[] TO_FIELDS = new int[]{
            R.id.friend_full_name
    };


    Cursor cursor;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ContentResolver contentResolver=getActivity().getContentResolver();
        cursor=contentResolver.query(FriendContract.FRIEND_CONTENT_URI, null, null, null, null);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.friend_row_layout, // layout to for the list items
                cursor,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );

        setListAdapter(mAdapter);
        setEmptyText(getString(R.string.loading));
    }
}
