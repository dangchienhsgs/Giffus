package com.dangchienhsgs.giffus.friend;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dangchienhsgs.giffus.FriendInfoActivity;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.friend.FriendCursorAdapter;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.utils.Common;

public class FriendFragment extends ListFragment implements ListView.OnItemClickListener {

    Cursor cursor;
    private String TAG = "Friend Fragment";
    private FriendCursorAdapter mAdapter;
    private String[] FROM_COLUMNS = new String[]{
            FriendContract.Entry.FULL_NAME
    };
    private int[] TO_FIELDS = new int[]{
            R.id.text_descriptions
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(
                FriendContract.URI,
                null,
                null,
                null,
                null
        );

        Log.d(TAG, "Create Friend Cursor Adapter " + cursor.getCount());

        mAdapter = new FriendCursorAdapter(getActivity(),
                R.layout.row_friend_layout, // layout to for the list items
                cursor,
                FROM_COLUMNS,
                TO_FIELDS,
                0,
                getListView()
        );

        setListAdapter(mAdapter);

        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        cursor.moveToPosition(i);

        String friendID = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USER_ID));
        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
        intent.putExtra(Common.FRIEND_ID, friendID);
        startActivity(intent);

        getActivity().finish();
        Log.d(TAG, i + "");
    }
}
