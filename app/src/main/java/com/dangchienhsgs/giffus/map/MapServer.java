package com.dangchienhsgs.giffus.map;

import android.util.Log;

import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapServer {
    private static final String TAG = "Map Server Utils";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";


    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";


    //"https://maps.googleapis.com/maps/api/place/details/json?sensor=false&key=API_KEY&reference=REFERENCE_KEY";

    /**
     * Get array names of places in Google Map defined by a string inputted by user
     *
     * @param input : The input string user type in edit text     *
     * @return: List of places 's name
     */
    public static Map<String, String> autoComplete(String input) {
        HashMap<String, String> result = new HashMap<String, String>();

        HttpURLConnection conn = null;

        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + Common.GOOGLE_MAP_API_KEY);
            //sb.append("&components=country:");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return result;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return result;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            result = new HashMap<String, String>();
            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject placeObject = predsJsonArray.getJSONObject(i);
                result.put(placeObject.getString("description"), placeObject.getString("reference"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return result;
    }


    public static String getPlaceDetailUrl(String refs) {
        UrlBuilder builder = new UrlBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        builder.append("reference", refs);
        builder.append("key", Common.GOOGLE_MAP_API_KEY);
        builder.append("sensor", "false");
        return builder.create();
    }
}
