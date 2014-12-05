package com.dangchienhsgs.giffus.server;

import android.os.AsyncTask;
import android.util.Log;

import com.dangchienhsgs.giffus.utils.Common;

import java.util.HashMap;

public class AcceptFriendTask extends AsyncTask<Void, Void, String> {
    private String acceptUsername;
    private String acceptPassword;
    private String requireUsername;

    public AcceptFriendTask(String acceptUsername, String acceptPassword, String requireUsername) {
        this.acceptUsername = acceptUsername;
        this.acceptPassword = acceptPassword;
        this.requireUsername = requireUsername;
    }

    @Override
    protected String doInBackground(Void... voids) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Common.ACTION, Common.ACTION_ACCEPT_FRIEND_REQUEST);
        hashMap.put(Common.ACCEPT_USER, acceptUsername);
        hashMap.put(Common.ACCEPT_PASSWORD, acceptPassword);
        hashMap.put(Common.REQUIRE_USER, requireUsername);

        String result = ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, hashMap);
        return result;
    }
}
