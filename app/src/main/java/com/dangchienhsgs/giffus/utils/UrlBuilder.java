package com.dangchienhsgs.giffus.utils;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlBuilder {
    private String url;
    private String serverLink;

    public UrlBuilder(String serverLink) {
        url = serverLink;
        this.serverLink = serverLink;
    }

    public UrlBuilder append(String key, String value) {
        try {
            if (!key.isEmpty() && !value.isEmpty()) {
                if (url.equals(serverLink)) {
                    url = url + "?";
                } else {
                    url = url + "&";
                }
                url = url + key + "=" + URLEncoder.encode(value, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {

            url = url + key + "=" + value;
            Log.d("UrlBuilder", e.toString());

        }
        return this;
    }

    public String create() {
        return url;
    }
}
