package com.dangchienhsgs.giffus.server;


import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.URLContentHandler;
import com.dangchienhsgs.giffus.utils.UrlBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerUtilities {
    public static final String SERVER_NAME="http://staticsurvey.herobo.com//service.php";

    public static String ACTION="action";
    public static String ACTION_REQUEST_FRIENDSHIP="require_friendship";
    public static String REQUEST_USER="require_user";
    public static String REQUEST_USER_PASSWORD="require_password";
    public static String INVITED_USER="invited_user";
    // Send data to server
    public static String postToServer(String endpoint, Map<String, String> params) {
        UrlBuilder urlBuilder=new UrlBuilder(endpoint);
        Iterator<Map.Entry<String, String>> iterator=params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> param=iterator.next();
            urlBuilder.append(param.getKey(), param.getValue());
        }
        String url=urlBuilder.toString();
        System.out.println (url);
        String result=new URLContentHandler().getURLFirstLine(url);
        return result;

    }

    public static String sendRequestFriend(String requestUser, String requestUserPassword, String invitedUser){
        HashMap<String, String> hashMap=new HashMap<String, String>();
        hashMap.put(REQUEST_USER, requestUser);
        hashMap.put(REQUEST_USER_PASSWORD, requestUserPassword);
        hashMap.put(INVITED_USER, invitedUser);
        hashMap.put(ACTION, ACTION_REQUEST_FRIENDSHIP);

        String result=postToServer(SERVER_NAME, hashMap);
        return result;
    }

}
