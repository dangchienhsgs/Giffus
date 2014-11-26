package com.dangchienhsgs.giffus.provider;

import android.net.Uri;

/**
 * Created by dangchienbn on 30/09/2014.
 */
public class PostcardContract implements DataContract {
    public static final String TABLE_NAME = "postcard";

    public static final Uri URI = Uri.parse("content://" + DataProvider.CONTENT_AUTHORITY + "/" + TABLE_NAME);

    public class Entry {
        public static final String _ID = "_id";
        public static final String POSTCARD_ID = "gift_id";
        public static final String SENDER_ID = "sender_id";
        public static final String RECEIVER_ID = "receiver_id";
        public static final String JSON_POSTCARD = "json_postcard";
    }


}
