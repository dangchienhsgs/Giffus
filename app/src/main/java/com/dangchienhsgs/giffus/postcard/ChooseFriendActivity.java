package com.dangchienhsgs.giffus.postcard;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.adapter.HumanArrayListAdapter;
import com.dangchienhsgs.giffus.adapter.HumanCursorListAdapter;
import com.dangchienhsgs.giffus.provider.FriendContract;

import java.util.Arrays;


public class ChooseFriendActivity extends ListActivity {

    boolean[] listSent;
    boolean checkAll;
    HumanCursorListAdapter mAdapter;
    private String[] FROM_COLUMNS = new String[]{
            FriendContract.Entry.FULL_NAME
    };
    private int[] TO_FIELDS = new int[]{
            R.id.friend_full_name
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAll = false;

        Cursor cursor = getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.RELATIONSHIP + "=" + FriendContract.ALREADY_FRIEND,
                null,
                null
        );

        listSent = new boolean[cursor.getCount()];
        Arrays.fill(listSent, false);

        mAdapter = new HumanCursorListAdapter(
                getApplicationContext(),
                R.layout.row_add_friends,
                cursor,
                listSent,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );

        setListAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_select_all:
                // mark check All
                checkAll = !checkAll;

                ListView listView = getListView();
                for (int i = 0; i < listView.getCount(); i++) {
                    View row = listView.getChildAt(i);
                    CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
                    checkBox.setChecked(checkAll);
                    listSent[i] = checkAll;
                }
            case R.id.action_send:

        }
        return super.onOptionsItemSelected(item);
    }
}
