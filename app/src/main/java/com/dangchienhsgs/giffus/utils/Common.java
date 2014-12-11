package com.dangchienhsgs.giffus.utils;

import com.dangchienhsgs.giffus.R;

/* This class saved all the special parameter of our software
 * such as SERVER_LINK, PROJECT_ID of Giffus
 */
public class Common {
    public static final String SERVER_LINK = "http://staticsurvey.herobo.com//service.php";

    public static final String GOOGLE_PROJECT_ID = "1054670780291";
    public static final String GOOGLE_MAP_API_KEY = "AIzaSyAN-hB_Q-PJhzgTwqhg9tZamjz0Hl4XlqQ";

    public static final String REGISTER_ACITIVY = " my register";
    public static final String EXTRA_STATUS = "status";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String BIRTHDAY = "birthday";
    public static final String MOBILE_PHONE = "mobile_phone";
    public static final String GENDER = "genre";
    public static final String JOB = "job";
    public static final String IS_WANNA_RECEIVE = "isWannaReceive";
    public static final String FULL_NAME = "fullname";
    public static final String REGISTRATION_ID = "registrations_id";
    public static final String USER_ID = "user_id";
    public static final String AVATAR_ID = "avatar_id";

    public static final String FRIEND_ID = "friend_id";

    public static final String ACTION = "action";
    public static final String ACTION_UPDATE_USER_INFO = "update_user_info";
    public static final String ACTION_GET_ALL_FRIENDS_INFO = "get_all_friends";
    public static final String ACTION_GET_INFO_BY_USERNAME = "get_user_info_by_username";
    public static final String ACTION_GET_USER_INFO_BY_ID = "get_user_info_by_id";
    public static final String ACTION_ACCEPT_FRIEND_REQUEST = "accept_friendship";
    public static final String ACTION_GET_USER_INFO_BY_ATT = "get_user_info_by_att";
    public static final String ACTION_SEARCH_FRIEND_BY_NAME = "search_friend_by_name";
    public static final String ACTION_SEND_POSTCARD = "send_postcard";
    public static final String ACTION_ACCEPTED_FRIENDSHIP = "accepted_friendship";
    public static final String ACTION_DECLINE_FRIENDSHIP = "decline_friendship";
    public static final String ACTION_SIGN_UP = "sign_up";
    public static final String ACTION_REMOVE_FRIENDSHIP = "remove_friendship";
    public static final String ACTION_CHECK_USERNAME_AND_EMAIL="check_username_and_email";
    public static final String ACTION_RESTORE_PASSWORD="restore_password";


    public static final String NAME_SEARCH = "name";

    public static final String ACCEPT_USER = "accept_user";
    public static final String ACCEPT_PASSWORD = "accept_password";

    public static final String REQUIRE_USER = "require_user";

    public static final String REQUEST_USER = "request_user";
    public static final String REQUEST_PASSWORD = "request_password";

    public static final String REMOVED_USER = "removed_user";


    public static final String MESSAGE_ACTION = "action";

    public static final String FRIEND_STATE = "state";
    public static final String JSON_GIFFUS_LOCATION = "json_giffus_location";

    public static final String ACTION_MESSAGE_INVITED = "invited";

    public static final String MESSAGE_REQUIRE_USER_ID = "require_user_id";
    public static final String MESSAGE_SEND_POSTCARD = "message_send_postcard";
    public static final String MESSAGE_INVITED_USER_ID = "invited_user_id";
    public static final String MESSAGE_ACCEPTED_USER_ID = "accept_user_id";
    public static final String MESSAGE_DECLINED_USER_ID = "decline_user_id";
    public static final String MESSAGE_REMOVED_USER_ID = "removed_user_id";
    public static final String MESSAGE_REQUEST_USER_ID = "request_user_id";


    public static final String DECLINE_USER = "decline_user";
    public static final String DECLINE_PASSWORD = "decline_password";


    public static final String ATTRIBUTE = "attribute";
    public static final String VALUE = "value";

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 0;

    public static final String ROBOTO_LIGHT_FONT_PATH = "/system/fonts/Roboto-Light.ttf";

    public static final String JSON_POSTCARD_STRING = "json_postcard_string";
    public static final String JSON_COVER_STRING = "json_cover_string";

    public static final String FLAG = "flag";
    public static final String FLAG_OPEN_DRAFT = "flag_open_draft";
    public static final String FLAG_NEW_COVER = "flag_new_cover";
    public static final String FLAG_OPEN_POSTCARD = "flag_open_postcard";
    public static final String FLAG_PREVIEW_POSTCARD = "flag_preview_postcard";
    public static final String FLAG_BACK_TO_COVER = "flag_back_to_cover";

    // This is the order of Drawer Navigation List
    public static final int DRAWER_NOTIFICATIONS_ID = 0;
    public static final int DRAWER_ACCOUNT_ID = 1;
    public static final int DRAWER_SIGN_OUT_ID = 2;

    public static final int INDEX_SYNC_FRIEND = 0;
    public static final int INDEX_SEARCH_FRIEND_BY_NAME = 1;


    public static final String ARRAY_POSTCARD_DRAFT = "array_postcard_draft";

    public static final int[] HUMAN_ICON = {
            R.drawable.icon_baby, R.drawable.icon_nurse, R.drawable.icon_old,
            R.drawable.icon_professor, R.drawable.icon_spy,
            R.drawable.icon_thief, R.drawable.icon_anonymous
    };

    public static final int[] BUBBLE_TEXT_BACKGROUND = {
            R.drawable.text_background_blue_gray,
            R.drawable.bubble, R.drawable.bubble4, R.drawable.bubble_3, R.drawable.bubble_green,
            R.drawable.blue_bubble, R.drawable.speech_bubble_hi, R.drawable.speech_bubble_md
    };

    public static final int[] COVER_BACKGROUND = {
            R.drawable.background4,
            R.drawable.background_beach,
            R.drawable.background_autumn,
            R.drawable.background_sun,
            R.drawable.background_tailor,
            R.drawable.background3,
            R.drawable.background5,
            R.drawable.background6,
            R.drawable.background7,
            R.drawable.background8,
            R.drawable.background9,
            R.drawable.background10,
            R.drawable.background11,
            R.drawable.background12,
            R.drawable.background13,
            R.drawable.background14,
            R.drawable.background15,
    };

    public static final String[] SONGS_TITLE = {
            "Song from secret garden",
            "Ban tinh ca dau tien",
            "Another"
    };
    public static final String[] SONGS_URL = {
            "http://dl2.org.mp3.zdn.vn/fsdd1131lwwjA/b981d2f62e20f68592b540cc5a79e827/54772050/2014/08/27/1/f/1f1520f809b15cbe73c9f823b8a79cde.mp3?filename=Gat%20Di%20Nuoc%20Mat%20-%20Noo%20Phuoc%20Thinh%20Tonny%20Viet.mp3",
            "http://dl2.org.mp3.zdn.vn/fsdd1131lwwjA/14be05c04ae42cfb2a0569d51b3507f6/54813bd0/2011/01/14/0/a/0a169a83ae67390020b9540229feb553.mp3?filename=Ban%20Tinh%20Ca%20Dau%20Tien%20-%20Duy%20Khoa.mp3"
    };
    public static String getSenderId() {
        return GOOGLE_PROJECT_ID;
    }

    public static String getServerLink() {
        return SERVER_LINK;
    }

    public static String getPreferenceEmail() {
        return null;
    }
}
