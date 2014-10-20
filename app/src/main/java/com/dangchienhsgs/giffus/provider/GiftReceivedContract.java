package com.dangchienhsgs.giffus.provider;

import android.net.Uri;

/**
 * Created by dangchienbn on 30/09/2014.
 */
public class GiftReceivedContract {
    public static final String TABLE_NAME = "gift_receiver";

    public static final Uri URI = Uri.parse("content://"+DataProvider.CONTENT_AUTHORITY+"/"+TABLE_NAME);

    public class Entry{
        public static final String _ID="_id";
        public static final String MESSAGE = "message";
        public static final String DATETIME = "datatime";
        public static final String LOCATION = "location";
        public static final String IS_FINISHED = "is_finished";
        public static final String SENDER_ID = "senderID";
        public static final String GIFT_ID = "giftID";
    }


}
