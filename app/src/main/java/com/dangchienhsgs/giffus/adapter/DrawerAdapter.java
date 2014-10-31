package com.dangchienhsgs.giffus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.Common;

public class DrawerAdapter extends ArrayAdapter<String> {
    private String TAG = "Drawer Adapter";
    private String[] title;

    public DrawerAdapter(Context context, int resource, String title[]) {
        super(context, resource, title);
        this.title = title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.drawer_list_item, parent, false);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon_list_drawer);
        TextView textView = (TextView) convertView.findViewById(R.id.text_list_drawer);

        textView.setText(getItem(position));
        Log.d(TAG, textView.getText().toString());

        // Set icon
        icon.setImageResource(Common.HUMAN_ICON[0]);

        return convertView;
    }
}
