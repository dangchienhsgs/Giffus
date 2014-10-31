package com.dangchienhsgs.giffus.dialogs.songpickers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.media.Song;

import java.util.List;

public class URLArrayListAdapter extends ArrayAdapter<Song> {

    public URLArrayListAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_list_url, parent, false);
        }

        TextView textUrl = (TextView) convertView.findViewById(R.id.text_url);
        TextView textTitle = (TextView) convertView.findViewById(R.id.text_title);

        textUrl.setText(getItem(position).getUrl());
        textTitle.setText(getItem(position).getTitle());

        Button buttonRemove = (Button) convertView.findViewById(R.id.remove_button);
        buttonRemove.setTag(position);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = (Integer) view.getTag();
                remove(getItem(index));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
