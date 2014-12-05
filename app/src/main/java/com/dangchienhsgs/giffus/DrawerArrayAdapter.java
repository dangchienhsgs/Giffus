package com.dangchienhsgs.giffus;

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


public class DrawerArrayAdapter extends ArrayAdapter<String> {
    private String TAG = "Drawer Adapter";
    private String[] title;

    public DrawerArrayAdapter(Context context, int resource, String title[]) {
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
        switch (position) {
            case Common.DRAWER_NOTIFICATIONS_ID:
                icon.setImageResource(R.drawable.notifications);
                break;
            case Common.DRAWER_SIGN_OUT_ID:
                icon.setImageResource(R.drawable.sign_out);
                break;
            case Common.DRAWER_ACCOUNT_ID:
                icon.setImageResource(R.drawable.profle);
                break;
        }

        return convertView;
    }
}
