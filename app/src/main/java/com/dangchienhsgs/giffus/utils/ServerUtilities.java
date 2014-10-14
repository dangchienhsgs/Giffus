package com.dangchienhsgs.giffus.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerUtilities {
    public static final String SERVER_NAME="http://staticsurvey.herobo.com//service.php";


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

    //
    public static void main(String args[]){
        Map<String, String> map=new HashMap<String, String>();
        map.put(Common.USERNAME, "dangchienhsgs");
        map.put(Common.PASSWORD, "chien1994");
        map.put(Common.ACTION, Common.ACTION_UPDATE_USER_INFO);
        String result=new ServerUtilities().postToServer(ServerUtilities.SERVER_NAME, map);
        System.out.println (result);
    }


}
