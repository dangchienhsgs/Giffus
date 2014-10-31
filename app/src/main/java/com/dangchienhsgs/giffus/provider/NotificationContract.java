package com.dangchienhsgs.giffus.provider;

import android.net.Uri;

public class NotificationContract implements DataContract {
    public static final String TABLE_NAME = "notifications";
    public static final Uri URI = Uri.parse("content://" + DataProvider.CONTENT_AUTHORITY + "/" + TABLE_NAME);
    public static final int TYPE_FRIEND_REQUEST = 0;

    public class Entry {
        public static final String _ID = "_id";
        public static final String TYPE = "type";
        public static final String ENABLE = "enable";
        public static final String FRIEND_ID = "friend_id";
        public static final String MESSAGE = "message";
    }

}
