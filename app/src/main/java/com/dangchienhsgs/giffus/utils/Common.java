package com.dangchienhsgs.giffus.utils;

/* This class saved all the special parameter of our software
 * such as SERVER_LINK, PROJECT_ID of Giffus
 */
public class Common {
    public static final String SERVER_LINK="http://staticsurvey.herobo.com//service.php";
    public static final String GOOGLE_PROJECT_ID="1054670780291";
    public static final String REGISTER_ACITIVY=" my register";
    public static final String EXTRA_STATUS="status";
    public static final String USERNAME="username";
    public static final String PASSWORD="password";
    public static final String EMAIL="email";
    public static final String PHONE_NUMBER="phone_number";
    public static final String FULL_NAME="fullname";
    public static final String JSON_USER_INFO="JSONUser";
    public static final String REGISTRATION_ID="registrations_id";
    public static final String USER_ID="user_id";

    public static final String ACTION="action";
    public static final String ACTION_UPDATE_USER_INFO="update_user_info";
    public static final String ACTION_GET_ALL_FRIENDS_INFO="get_all_friend";
    public static final String ACTION_GET_INFO="get_info";
    public static final String ACTION_SIGN_UP="sign_up";

    public static final String ATTRIBUTE="attribute";
    public static final String VALUE="value";

    public static final String ACCOUNT="Giffus";
    public static final String ACCOUNT_TYPE="com.dangchienhsgs.giffus";

    public static final int STATUS_SUCCESS=1;
    public static final int STATUS_FAIL=0;



    public static String getSenderId(){
        return GOOGLE_PROJECT_ID;
    }
    public static String getServerLink(){
        return SERVER_LINK;
    }
    public static String getPreferenceEmail(){
        return null;
    }
}
