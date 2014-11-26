package com.dangchienhsgs.giffus.provider;

import android.net.Uri;

public class NotificationContract implements DataContract {
    public static final String TABLE_NAME = "notifications";
    public static final Uri URI = Uri.parse("content://" + DataProvider.CONTENT_AUTHORITY + "/" + TABLE_NAME);

    public static final int TYPE_FRIEND_REQUEST = 0;
    public static final int TYPE_RECEIVE_POST_CARD = 1;

    public class Entry {
        public static final String _ID = "_id";
        public static final String TYPE = "type";
        public static final String ENABLE = "enable";
        public static final String FRIEND_ID = "friend_id";
        public static final String MESSAGE = "message";
        public static final String DAY = "day";
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";
        public static final String AVATAR_ID = "avatar_id";
        public static final String SECOND = "second";
    }

}
