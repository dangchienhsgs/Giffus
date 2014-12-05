package com.dangchienhsgs.giffus.postcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.postcard.Postcard;

import java.util.List;


/**
 * The adapter show the list of postcards
 */
public class PostcardArrayAdapter extends ArrayAdapter<Postcard> {
    List<Postcard> list;

    public PostcardArrayAdapter(Context context, int resource, int textViewResourceId, List<Postcard> objects) {
        super(context, resource, textViewResourceId, objects);

        list = objects;
    }

    /**
     * Create a row view for each row of the listview
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @see android.view.View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_draft_postcard, parent, false);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.text_postcard_name);
        textName.setText("Postcard " + position);

        return convertView;
    }
}
