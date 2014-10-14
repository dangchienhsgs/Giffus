package com.dangchienhsgs.giffus.utils;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by dangchienhsgs on 9/8/14.
 */
public class URLContentHandler {
    public String getURLContent(String... urls) {
        try {
            String result = "";
            for (int i = 0; i < urls.length; i++) {
                URL url = new URL(urls[i]);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                String str = "";
                while (scanner.hasNextLine()) {
                    str = str + scanner.nextLine();
                }
                result = result + str;
                scanner.close();
            }
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getURLFirstLine(String link) {
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            String result=null;

            if (scanner.hasNextLine()) result=scanner.nextLine();

            scanner.close();
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String args[]){
        System.out.println (new URLContentHandler().
                getURLFirstLine("http://staticsurvey.herobo.com//service.php?data={username:chien,password:hello}&username=dangchienhsgs&password=chien1994&action=update_user_info"));
    }
}