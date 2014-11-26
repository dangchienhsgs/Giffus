package com.dangchienhsgs.giffus.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.dangchienhsgs.giffus.R;

public class AddLocationDialog extends DialogFragment {
    private final String TAG = "Add Location Dialog";


    private CheckBox checkBoxSecret;

    private EditText editHint;
    private EditText editAlternativeTitle;

    private GiftLocation location;

    private AddLocationListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_add_location_info, null);

        // Set some init values for components of dialogs
        checkBoxSecret = (CheckBox) view.findViewById(R.id.check_box_keep_secret);
        editHint = (EditText) view.findViewById(R.id.edit_location_hint);
        editAlternativeTitle = (EditText) view.findViewById(R.id.edit_alternative_title);

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
                        if (location == null) {
                            Toast.makeText(getActivity(), "You didn't choose any location", Toast.LENGTH_SHORT).show();
                        } else {

                            location.setAlternativeTitle(editAlternativeTitle.getText().toString());
                            if (checkBoxSecret.isChecked()) {
                                location.setSecret(true);
                                if (!editHint.getText().toString().isEmpty()) {
                                    location.setHint(editHint.getText().toString());
                                }
                            }
                            mListener.onLocationAdded(location);
                        }
                    }
                }
        );

        // Finishing create dialog
        builder.setTitle("Location Options");
        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddLocationListener) activity;
        } catch (ClassCastException e) {
            Log.d(TAG, "Your activity did not implement the AddLocationListener");
        }
    }

    public void setLocation(GiftLocation location) {
        this.location = location;
    }

    public interface AddLocationListener {
        public void onLocationAdded(GiftLocation location);
    }


}
