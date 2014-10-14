package com.dangchienhsgs.giffus.provider;

import android.net.Uri;

/**
 * Created by dangchienbn on 30/09/2014.
 */
public class FriendContract {
    public static final String TABLE_NAME = "friends";

    public static final Uri FRIEND_CONTENT_URI= Uri.parse("content://"+DataProvider.CONTENT_AUTHORITY+"/"+TABLE_NAME);


    public final static int ALREADY_FRIEND = 0;
    public final static int IS_REQUESTING = 1;
    public final static int IN_ACCEPTING = 2;

    public class Entry{
        public static final String _ID="_id";
        public static final String USERNAME="username";
        public static final String USER_ID = "user_id";
        public static final String EMAIL = "email";
        public static final String FULL_NAME = "fullname";
        public static final String RELATIONSHIP = "relationship";
        public static final String AVATAR_ID = "avatar_id";
        public static final String GENRE="genre";
        public static final String BIRTHDAY="birthday";
        public static final String MOBILE_PHONE="mobile_phone";

        public static final int COL_FULL_NAME_INDEX=4;
    }
}
