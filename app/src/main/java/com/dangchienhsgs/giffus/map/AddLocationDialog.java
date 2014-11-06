package com.dangchienhsgs.giffus.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.dangchienhsgs.giffus.R;

/**
 * Created by dangchienhsgs on 06/11/2014.
 */
public class AddLocationDialog extends DialogFragment {
    private CheckBox checkBoxSecret;
    private EditText editHint;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_add_location_info, null);

        // Set some init values for components of dialogs
        checkBoxSecret = (CheckBox) view.findViewById(R.id.check_box_keep_secret);
        editHint = (EditText) view.findViewById(R.id.edit_location_hint);
        editHint.setVisibility(View.INVISIBLE);

        checkBoxSecret.setChecked(false);
        checkBoxSecret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editHint.setVisibility(View.VISIBLE);
                } else {
                    editHint.setVisibility(View.INVISIBLE);
                }
            }
        });


        // Set Negative Button
        builder.setNegativeButton(
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().cancel();
                    }
                }
        );

        // Set Positive Button
        builder.setPositiveButton(
                "Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do something here
                    }
                }
        );

        // Finishing create dialog
        builder.setTitle("Location Options");
        builder.setView(view);
        return builder.create();
    }

    public interface onAddLocationListener {
        public void onLocationAdded();
    }
}
