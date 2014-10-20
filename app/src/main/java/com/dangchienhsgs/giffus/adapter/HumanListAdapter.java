package com.dangchienhsgs.giffus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.account.Human;

import java.util.List;

public class HumanListAdapter extends ArrayAdapter<Human> {
    private String TAG = "Human List Adapter";

    private Boolean[] listCheck;

    private Boolean checkAll;

    public HumanListAdapter(Context context, int resource, List<Human> objects, Boolean[] listCheck, boolean checkAll) {
        super(context, resource, objects);
        this.listCheck = listCheck;
        this.checkAll=checkAll;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Human human = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_add_friends, parent, false);
        }
        TextView textFullName = (TextView) convertView.findViewById(R.id.text_full_name);
        TextView textEmail = (TextView) convertView.findViewById(R.id.text_email);

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        if (checkAll){
            checkBox.setChecked(true);
        }

        checkBox.setTag(position);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Integer position = (Integer) compoundButton.getTag();
                Log.d(TAG, "Bi Click " + position);
                listCheck[position] = b;
            }

        });
        textFullName.setText(human.getFullName());
        textEmail.setText(human.getEmail());
        return convertView;
    }
}
