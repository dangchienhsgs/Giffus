package com.dangchienhsgs.giffus.postcard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.dialogs.alert.MyAlertDialog;
import com.dangchienhsgs.giffus.dialogs.mapdialogs.LocationDialog;
import com.dangchienhsgs.giffus.map.GiftLocation;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;


public class PreviewCoverActivity extends ActionBarActivity implements LocationDialog.OnReturnLocationDialogListener {
    private String TAG = "Preview Cover Activity";

    private TextView textLarge;
    private TextView textSmall;

    private Postcard postcard;
    private Cover cover;
    private String flag;

    private boolean isChecked = false;

    private String jsonPostcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar from action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_cover_preview);

        initComponents();
    }

    public void initComponents() {

        Intent intent = getIntent();
        jsonPostcard = intent.getStringExtra(Common.JSON_POSTCARD_STRING);

        Log.d(TAG, jsonPostcard);
        flag = intent.getStringExtra(Common.FLAG);

        postcard = new Gson().fromJson(jsonPostcard, Postcard.class);

        cover = postcard.getCover();

        //set Text Large
        textLarge = (TextView) findViewById(R.id.text_large_preview);
        textLarge.setText(cover.getTextLarge());
        textLarge.setTextColor(cover.getColorLargeText());
        textLarge.setTextSize(TypedValue.COMPLEX_UNIT_PX, cover.getSizeLargeText());
        textLarge.setTypeface(Typeface.createFromFile(cover.getFontTextLarge()));

        // set Text Small
        textSmall = (TextView) findViewById(R.id.text_small_preview);
        textSmall.setText(cover.getTextSmall());
        textSmall.setTypeface(Typeface.createFromFile(cover.getFontTextSmall()));
        textSmall.setTextColor(cover.getColorSmallText());
        textSmall.setTextSize(TypedValue.COMPLEX_UNIT_PX, cover.getSizeSmallText());

        // setBackground
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_cover_preview);
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

        switch (id) {
            case R.id.action_show_location:
                actionShowLocation();
                break;
            case R.id.action_next:
                actionNext();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void actionNext() {
        try {
            Inner inner = postcard.getInner();
            if (inner == null) {
                Toast.makeText(this, "The inner have not been set", Toast.LENGTH_SHORT).show();
            } else {
                if (isChecked || postcard.getLocation() == null) {
                    Intent intent = new Intent(this, PreviewInnerActivity.class);
                    intent.putExtra(Common.JSON_POSTCARD_STRING, jsonPostcard);
                    intent.putExtra(Common.FLAG, Common.FLAG_OPEN_POSTCARD);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "You have not come to this, please confirm by press map icon", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "The inner have not been set", Toast.LENGTH_SHORT).show();
        }
    }

    public void actionShowLocation() {
        GiftLocation location = null;
        boolean check = true;
        try {
            location = postcard.getLocation();
            if (location == null) {
                check = false;
            }
        } catch (Exception e) {
            check = false;
        }
        if (check) {
            LocationDialog locationDialog = new LocationDialog();
            locationDialog.setLocation(location);
            locationDialog.setListener(this);
            locationDialog.show(getFragmentManager(), "Map Dialog");

        } else {
            // Not contains any location
            MyAlertDialog dialog = new MyAlertDialog();
            dialog.setTextAlert(
                    "This gift not contains any location"
            );
            dialog.setTitle("Alert");
            dialog.setListener(new MyAlertDialog.OnMyAlertDialogListener() {
                @Override
                public void onMyDialogClose() {
                    // Do nothing
                }

                @Override
                public void onMyDialogAccept() {
                    // do nothing
                }
            });

            dialog.show(getFragmentManager(), "Alert Dialog");
        }

    }

    @Override
    public void onReturnLocation(boolean isChecked) {
        if (isChecked == true) {
            this.isChecked = true;
        }
    }
}
