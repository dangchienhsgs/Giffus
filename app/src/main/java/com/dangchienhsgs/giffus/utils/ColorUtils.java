package com.dangchienhsgs.giffus.utils;

import android.graphics.Color;

public class ColorUtils {
    public static String convertColorToHex(int color) {
        String hexColor = "#" + Integer.toHexString(color).substring(2);
        return hexColor;
    }

    public static int convertHexToColor(String hex) {
        int color = Color.parseColor(hex);
        return color;
    }
}
