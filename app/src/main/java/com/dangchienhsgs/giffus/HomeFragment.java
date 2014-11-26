package com.dangchienhsgs.giffus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dangchienhsgs.giffus.adapter.PostcardArrayAdapter;
import com.dangchienhsgs.giffus.postcard.Postcard;
import com.dangchienhsgs.giffus.postcard.PreviewCoverActivity;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

/**
 * Created by dangchienhsgs on 9/13/14.
 */
public class HomeFragment extends Fragment implements ListView.OnItemClickListener {

    private ListView listView;
    private PostcardArrayAdapter mAdapter;
    private Cursor cursor;

    private String[] FROM_COLUMNS = new String[]{
            PostcardContract.Entry.SENDER_ID

    };
    private int[] TO_FIELDS = new int[]{
            R.id.text_descriptions
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_home, container, false);

        listView = (ListView) view.findViewById(R.id.list_postcard);

        cursor = getActivity().getContentResolver().query(
                PostcardContract.URI,
                null,
                null,
                null,
                null
        );

        mAdapter = new PostcardArrayAdapter(
                getActivity(),
                R.layout.row_postcard_layout,
                cursor,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        cursor.moveToPosition(i);

        // check time
        Intent intent = new Intent(getActivity(), PreviewCoverActivity.class);

        intent.putExtra(
                Common.JSON_POSTCARD_STRING,
                cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.JSON_POSTCARD))
        );

        Log.d("Home Fragment", cursor.getString(cursor.getColumnIndex(PostcardContract.Entry.JSON_POSTCARD)));

        intent.putExtra(Common.FLAG, Common.FLAG_OPEN_POSTCARD);
        startActivity(intent);
        // Open gift

    }
}
