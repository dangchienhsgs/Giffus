package com.dangchienhsgs.giffus.provider;

import android.net.Uri;

/**
 * Created by dangchienbn on 30/09/2014.
 */
public class GiftSentContract implements DataContract {
    public static final String TABLE_NAME = "gift_sent";

    public static final Uri URI = Uri.parse("content://" + DataProvider.CONTENT_AUTHORITY + "/" + TABLE_NAME);

    public class Entry {
        public static final String _ID = "_id";
        public static final String GIFT_ID = "gift_id";

        public static final String COVER_BACKGROUND_ID = "cover_image_id";

        public static final String COVER_SMALL_TEXT = "cover_small_text";
        public static final String COVER_COLOR_SMALL_TEXT = "cover_color_small_text";
        public static final String COVER_SIZE_SMALL_TEXT = "cover_size_small_text";
        public static final String COVER_SMALL_TEXT_FONT = "cover_small_text_font";

        public static final String COVER_LARGE_TEXT = "cover_large_text";
        public static final String COVER_COLOR_LARGE_TEXT = "cover_color_large_text";
        public static final String COVER_SIZE_LARGE_TEXT = "cover_size_large_text";
        public static final String COVER_LARGE_TEXT_FONT = "cover_large_text_font";


        public static final String INNER_BACKGROUND_ID = "background";
        public static final String INNER_AVATAR_ID = "avatar_id";

        public static final String INNER_SMALL_TEXT = "inner_small_text";
        public static final String INNER_COLOR_SMALL_TEXT = "inner_color_small_text";
        public static final String INNER_SIZE_SMALL_TEXT = "inner_size_small_text";
        public static final String INNER_SMALL_TEXT_FONT = "inner_small_text_font";

        public static final String INNER_LARGE_TEXT = "inner_large_text";
        public static final String INNER_COLOR_LARGE_TEXT = "inner_color_large_text";
        public static final String INNER_SIZE_LARGE_TEXT = "inner_size_large_text";
        public static final String INNER_LARGE_TEXT_FONT = "inner_large_text_font";

        public static final String INNER_SONG_URLS = "song_urls";
        public static final String INNER_SONG_LYRICS = "song_lyrics";
        public static final String INNER_SONG_TITLE = "song_titles";

    }
}
