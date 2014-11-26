package com.dangchienhsgs.giffus.provider;

import android.net.Uri;


public class FriendContract implements DataContract {
    public static final String TABLE_NAME = "friends";

    public static final Uri URI = Uri.parse("content://" + DataProvider.CONTENT_AUTHORITY + "/" + TABLE_NAME);


    public final static int ALREADY_FRIEND = 0;

    public final static int IS_REQUESTING = 1;
    // this friend wait we is waiting us to accept
    public final static int WAIT_ACCEPTING = 2;

    public final static int NO_RELATION = 3;

    public class Entry {
        public static final String _ID = "_id";
        public static final String USERNAME = "username";
        public static final String USER_ID = "user_id";
        public static final String EMAIL = "email";
        public static final String FULL_NAME = "fullname";
        public static final String RELATIONSHIP = "relationship";
        public static final String AVATAR_ID = "avatar_id";
        public static final String GENRE = "genre";
        public static final String BIRTHDAY = "birthday";
        public static final String MOBILE_PHONE = "mobile_phone";
    }
}
