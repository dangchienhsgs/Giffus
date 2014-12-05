package com.dangchienhsgs.giffus.dialogs.songpickers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dangchienhsgs.giffus.R;


public class AddSongUrlDialogs extends DialogFragment {

    private AddSongsDialogListener mListener;
    private EditText editTitle;
    private EditText editUrl;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialogs_add_songs_url, null);

        editTitle = (EditText) view.findViewById(R.id.edit_title_song);
        editUrl = (EditText) view.findViewById(R.id.edit_url);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onAddSongsReturn(editTitle.getText().toString(), editUrl.getText().toString());
            }
        });

        builder.setView(view);
        builder.setTitle("Add Song");
        return builder.create();
    }

    public void setListener(AddSongsDialogListener mListener) {
        this.mListener = mListener;
    }

    public interface AddSongsDialogListener {
        public void onAddSongsReturn(String title, String url);
    }
}
