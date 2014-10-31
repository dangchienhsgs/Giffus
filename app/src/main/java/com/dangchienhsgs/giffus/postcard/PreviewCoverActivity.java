package com.dangchienhsgs.giffus.postcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.client.PreferencesHandler;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

public class PreviewCoverActivity extends Activity {
    private String TAG = "Preview Cover Activity";

    private TextView textLarge;
    private TextView textSmall;
    private Cover cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cover_preview);

        Intent intent = getIntent();
        String jsonCover = intent.getStringExtra(Cover.JSON_NAME);
        cover = new Gson().fromJson(jsonCover, Cover.class);


        //set Text Large
        textLarge = (TextView) findViewById(R.id.text_large_preview);
        textLarge.setText(cover.getTextLarge());
        textLarge.setTextColor(cover.getColorLargeText());
        textLarge.setTextSize(cover.getSizeLargeText());
        textLarge.setTypeface(Typeface.createFromFile(cover.getFontTextLarge()));

        // set Text Small
        textSmall = (TextView) findViewById(R.id.text_small_preview);
        textSmall.setText(cover.getTextSmall());
        textSmall.setTypeface(Typeface.createFromFile(cover.getFontTextSmall()));
        textSmall.setTextColor(cover.getColorSmallText());
        textSmall.setTextSize(cover.getSizeSmallText());

        // setBackground
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_cover_preview);
        if (relativeLayout == null) Log.d(TAG, "NULL");
        Log.d(TAG, "Background " + cover.getBackgroundID());
        relativeLayout.setBackgroundResource(Common.COVER_BACKGROUND[cover.getBackgroundID()]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cover_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
