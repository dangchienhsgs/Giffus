package com.dangchienhsgs.giffus.dialogs.picturepickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dangchienhsgs.giffus.R;

public class PicturesPickerDialogs extends DialogFragment {

    private OnSelectedPicturesListener mListener;
    private int[] images;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialogs_pictures_picker, null);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.list_pictures);

        for (int i = 0; i < images.length; i++) {
            View item = (View) layoutInflater.inflate(R.layout.layout_dialogs_pictures_items, null);
            ImageView imageView = (ImageView) item.findViewById(R.id.item_pictures);
            imageView.setImageResource(images[i]);

            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    mListener.onSelectedPictures(PicturesPickerDialogs.this, position);
                    getDialog().cancel();
                }
            });
            linearLayout.addView(item);
        }

        builder.setView(view);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }

    public void setListImages(int list[]) {
        this.images = list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSelectedPicturesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The activity doesn't implements the OnSelectedPicturesListener");
        }
    }

    public interface OnSelectedPicturesListener {
        public void onSelectedPictures(PicturesPickerDialogs dialogs, int position);
    }
}
