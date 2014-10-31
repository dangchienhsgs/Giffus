package com.dangchienhsgs.giffus.dialogs.songpickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.media.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsPickerDialogs extends DialogFragment {
    List<Song> listSongs;
    URLArrayListAdapter mAdapter;

    private Button buttonAdd;

    private EditText editUrl;
    private EditText editTitle;

    private OnReturnListener mListener;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_add_musics, null);
        ListView listView = (ListView) view.findViewById(R.id.list_url);

        buttonAdd = (Button) view.findViewById(R.id.add_button);
        editUrl = (EditText) view.findViewById(R.id.edit_url);
        editTitle = (EditText) view.findViewById(R.id.edit_title);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editUrl.getText().toString();
                String title = editTitle.getText().toString();

                mAdapter.add(new Song(title, url));
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter = new URLArrayListAdapter(getActivity(), R.layout.row_list_url, listSongs);
        listView.setAdapter(mAdapter);

        builder.setView(view);
        builder.setNegativeButton(
                R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onReturn(SongsPickerDialogs.this);
                        getDialog().cancel();
                    }
                }
        );
        builder.setTitle("Set Musics Links");

        return builder.create();
    }

    public List<Song> getListSongs() {
        return listSongs;
    }

    public void setListSongs(List<Song> listSongs) {
        this.listSongs = listSongs;
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnReturnListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity don't implements the override medthod of OnReturnListener");
        }
    }

    /**
     * This listener will tell the activity about the dialogs close
     */
    public interface OnReturnListener {
        public void onReturn(SongsPickerDialogs songsPickerDialogs);
    }

}
