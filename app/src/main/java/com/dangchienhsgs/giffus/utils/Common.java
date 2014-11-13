package com.dangchienhsgs.giffus.utils;

import com.dangchienhsgs.giffus.R;

/* This class saved all the special parameter of our software
 * such as SERVER_LINK, PROJECT_ID of Giffus
 */
public class Common {
    public static final String SERVER_LINK = "http://staticsurvey.herobo.com//service.php";

    public static final String GOOGLE_PROJECT_ID = "1054670780291";
    public static final String GOOGLE_MAP_API_KEY = "AIzaSyBG2yUFfe5nxHjTrNg3iO3GsL3YDkY-AQ8";

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
    public static final String JSON_USER_INFO = "JSONUser";
    public static final String REGISTRATION_ID = "registrations_id";
    public static final String USER_ID = "user_id";
    public static final String AVATAR_ID = "avatar_id";

    public static final String ACTION = "action";
    public static final String ACTION_UPDATE_USER_INFO = "update_user_info";
    public static final String ACTION_GET_ALL_FRIENDS_INFO = "get_all_friends";
    public static final String ACTION_GET_INFO_BY_USERNAME = "get_user_info_by_username";
    public static final String ACTION_GET_USER_INFO_BY_ID = "get_user_info_by_id";
    public static final String ACTION_ACCEPT_FRIEND_REQUEST = "accept_friendship";
    public static final String ACTION_GET_USER_INFO_BY_ATT = "get_user_info_by_att";
    public static final String ACTION_SEARCH_FRIEND_BY_NAME = "search_friend_by_name";
    public static final String NAME_SEARCH = "name";

    public static final String ACCEPT_USER = "accept_user";
    public static final String ACCEPT_PASSWORD = "accept_password";
    public static final String REQUIRE_USER = "require_user";
    public static final String ACTION_SIGN_UP = "sign_up";

    public static final String MESSAGE_ACTION = "action";
    public static final String ACTION_MESSAGE_ACCEPTED = "accepted_friendship";

    public static final String ACTION_MESSAGE_INVITED = "invited";
    public static final String MESSAGE_REQUIRE_USER_ID = "require_user_id";
    public static final String MESSAGE_INVITED_USER_ID = "invited_user_id";
    public static final String MESSAGE_ACCEPTED_USER_ID = "accept_user_id";

    public static final String ATTRIBUTE = "attribute";
    public static final String VALUE = "value";

    public static final String ACCOUNT = "Giffus";
    public static final String ACCOUNT_TYPE = "com.dangchienhsgs.giffus";

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 0;

    public static final String ROBOTO_LIGHT_FONT_PATH = "/system/fonts/Roboto-Light.ttf";

    public static final String JSON_GIFFUS_LOCATION = "json_giffus_location";

    // This is the order of Drawer Navigation List
    public static final int DRAWER_NOTIFICATIONS_ID = 0;
    public static final int DRAWER_NOTIFICATIONS_FRIEND_REQUESTS_ID = 1;
    public static final int DRAWER_HOME_ID = 2;
    public static final int DRAWER_ACCOUNT_ID = 3;
    public static final int DRAWER_SIGN_OUT_ID = 4;


    public static final int[] HUMAN_ICON = {
            R.drawable.icon_baby, R.drawable.icon_nurse, R.drawable.icon_old,
            R.drawable.icon_professor, R.drawable.icon_spy,
            R.drawable.icon_thief
    };

    public static final int[] COVER_BACKGROUND = {
            R.drawable.background, R.drawable.background2,
    };

    public static final int[] TEXT_BACKGROUND = {
            R.drawable.text_background_blue_gray

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
