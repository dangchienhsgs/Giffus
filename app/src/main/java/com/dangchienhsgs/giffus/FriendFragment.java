package com.dangchienhsgs.giffus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dangchienhsgs.giffus.R;

/**
 * Created by dangchienhsgs on 9/13/14.
 */
public class FriendFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_tab, container, false);
    }


}
