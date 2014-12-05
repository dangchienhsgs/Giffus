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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.map.Song;

import java.util.List;

public class SongsPickerDialogs extends DialogFragment implements AddSongUrlDialogs.AddSongsDialogListener {
    List<Song> listSongs;
    URLArrayListAdapter mAdapter;
    int temp = 0;
    private ImageButton buttonAdd;
    private ListView listView;
    private Spinner spinner;
    private OnReturnListener mListener;
    private List<String> listSpinnerTitle;
    private List<String> listSpinnerUrl;

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


        listView = (ListView) view.findViewById(R.id.list_url);
        buttonAdd = (ImageButton) view.findViewById(R.id.add_button);
        spinner = (Spinner) view.findViewById(R.id.spinner_songs);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.row_spinner_songs,
                R.id.text_title,
                listSpinnerTitle
        );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == listSpinnerTitle.size() - 1) {
                    AddSongUrlDialogs dialogs = new AddSongUrlDialogs();
                    dialogs.setListener(SongsPickerDialogs.this);
                    dialogs.show(getFragmentManager(), "SongURLDialog");
                    spinner.setSelection(temp);
                } else {
                    temp = spinner.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = spinner.getSelectedItemPosition();

                if (position < listSpinnerTitle.size() - 1) {
                    mAdapter.add(new Song(listSpinnerTitle.get(position), listSpinnerUrl.get(position)));
                    mAdapter.notifyDataSetChanged();
                }
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnReturnListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity don't implements the override method of OnReturnListener");
        }
    }

    public List<String> getListSpinnerTitle() {
        return listSpinnerTitle;
    }

    public void setListSpinnerTitle(List<String> listSpinnerTitle) {
        this.listSpinnerTitle = listSpinnerTitle;
    }

    public List<String> getListSpinnerUrl() {
        return listSpinnerUrl;
    }

    public void setListSpinnerUrl(List<String> listSpinnerUrl) {
        this.listSpinnerUrl = listSpinnerUrl;
    }

    @Override
    public void onAddSongsReturn(String title, String url) {
        mAdapter.add(new Song(title, url));
        mAdapter.notifyDataSetChanged();
    }

    public List<Song> getListSongs() {
        return listSongs;
    }

    public void setListSongs(List<Song> listSongs) {
        this.listSongs = listSongs;
        //mAdapter.notifyDataSetChanged();
    }

    /**
     * This listener will tell the activity about the dialogs close
     */
    public interface OnReturnListener {
        public void onReturn(SongsPickerDialogs songsPickerDialogs);
    }

}
