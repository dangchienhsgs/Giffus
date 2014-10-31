package com.dangchienhsgs.giffus.dialogs.songpickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.media.Song;

import java.util.ArrayList;
import java.util.List;

public class LyricsPickerDialogs extends DialogFragment {
    private static final String TAG = "Lyrics Pinker Dialogs";

    private List<Song> listSongs;
    private List<String> listTitle = new ArrayList<String>();

    private int currentSpinnerPosition;
    private Spinner spinner;
    private EditText editLyrics;

    private OnFinishedDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFinishedDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity don't implements OnFinishDialogs Listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.layout_dialogs_lyrics, null);
        spinner = (Spinner) view.findViewById(R.id.spinner_songs_lyrics_dialog);
        editLyrics = (EditText) view.findViewById(R.id.edit_lyric);

        currentSpinnerPosition = 0;
        editLyrics.setText(listSongs.get(currentSpinnerPosition).getLyrics());


        listTitle = new ArrayList<String>();
        for (Song song : listSongs) {
            listTitle.add(song.getTitle());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listTitle
        );
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Previous " + currentSpinnerPosition + " After " + spinner.getSelectedItemPosition());
                Log.d(TAG, "Size " + listSongs.size());

                // Update lyrics
                Song temp = listSongs.get(currentSpinnerPosition);
                temp.setLyrics(editLyrics.getText().toString());
                listSongs.set(currentSpinnerPosition, temp);

                // navigation to current item
                currentSpinnerPosition = spinner.getSelectedItemPosition();
                editLyrics.setText(listSongs.get(currentSpinnerPosition).getLyrics());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        builder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Update lyrics
                Song temp = listSongs.get(currentSpinnerPosition);
                temp.setLyrics(editLyrics.getText().toString());
                listSongs.set(currentSpinnerPosition, temp);

                // cancel
                mListener.onFinished(LyricsPickerDialogs.this, listSongs);
                getDialog().cancel();
            }
        });

        builder.setView(view);
        builder.setTitle("Set Lyrics");

        return builder.create();

    }

    public List<Song> getListSongs() {
        return listSongs;
    }

    public void setListSongs(List<Song> listSongs) {
        this.listSongs = listSongs;
    }

    public interface OnFinishedDialogListener {
        public void onFinished(LyricsPickerDialogs dialogs, List<Song> listSongs);
    }
}