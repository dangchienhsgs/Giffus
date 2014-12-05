package com.dangchienhsgs.giffus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.postcard.PostcardArrayAdapter;
import com.dangchienhsgs.giffus.postcard.CreateCoverActivity;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.postcard.Postcard;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangchienhsgs on 9/13/14.
 */
public class LibraryFragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<Postcard> listPostcard;
    private PostcardArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.library_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_draft_postcard);

        String arrayPostcard = PreferencesHandler.getValueFromPreferences(Common.ARRAY_POSTCARD_DRAFT, getActivity());
        listPostcard = new ArrayList<Postcard>();

        if (arrayPostcard != null) {

            try {
                JSONArray jsonArray = new JSONArray(arrayPostcard);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String jsonPostcard = (String) jsonArray.get(i);
                    Postcard postcard = new Gson().fromJson(jsonPostcard, Postcard.class);
                    listPostcard.add(postcard);
                }

                mAdapter = new PostcardArrayAdapter(
                        getActivity(),
                        R.layout.row_draft_postcard,
                        R.id.text_postcard_name,
                        listPostcard
                );

                listView.setAdapter(mAdapter);

                listView.setOnItemClickListener(this);
            } catch (JSONException e) {

            }
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), CreateCoverActivity.class);

        intent.putExtra(Common.FLAG, Common.FLAG_OPEN_DRAFT);
        intent.putExtra(Common.JSON_POSTCARD_STRING, new Gson().toJson(mAdapter.getItem(i), Postcard.class));
        startActivity(intent);
    }
}
