package com.dangchienhsgs.giffus.dialogs.alert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;

public class MyAlertDialog extends DialogFragment {

    private String textAlert;
    private String title;
    private OnMyAlertDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_alert, null);
        TextView textView = (TextView) view.findViewById(R.id.text_alert);

        textView.setText(textAlert);

        builder.setView(view);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
                mListener.onMyDialogClose();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
                mListener.onMyDialogAccept();
            }
        });

        return builder.create();
    }

    public void setTextAlert(String textAlert) {
        this.textAlert = textAlert;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setListener(OnMyAlertDialogListener mListener) {
        this.mListener = mListener;
    }

    public interface OnMyAlertDialogListener {
        public void onMyDialogClose();

        public void onMyDialogAccept();
    }
}
